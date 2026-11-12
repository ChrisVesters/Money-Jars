package com.cvesters.moneyjars.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.GraphQlTester.Response;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cvesters.moneyjars.common.bdo.PagedResult;
import com.cvesters.moneyjars.common.bdo.Pagination;
import com.cvesters.moneyjars.transaction.TestAccountTransferTransaction;
import com.cvesters.moneyjars.transaction.TestPaymentTransaction;
import com.cvesters.moneyjars.transaction.TestTransaction;
import com.cvesters.moneyjars.transaction.TransactionGraphqlConfig;
import com.cvesters.moneyjars.transaction.bdo.TransactionFilter;
import com.cvesters.moneyjars.transaction.bdo.TransactionSearch;

@GraphQlTest({ AccountController.class, AccountResolver.class })
@Import(TransactionGraphqlConfig.class)
class AccountResolverTest {

	@Autowired
	private GraphQlTester graphQlTester;

	@MockitoBean
	private AccountService accountService;

	@MockitoBean
	private AccountEntryService entryService;

	@Nested
	class ResolveBalance {

		private static final TestAccount CHECKING = TestAccount.CHECKING;
		private static final TestAccountEntry ENTRY = TestAccountEntry.CHECKING_RENT;

		private static final String DOCUMENT = """
				query {
					getAccount(id: %d) {
						balance
					}
				}
				""".formatted(CHECKING.getId());

		@Test
		void lastEntry() {
			when(accountService.get(CHECKING.getId()))
					.thenReturn(CHECKING.bdo());
			when(entryService.findLast(CHECKING.getId()))
					.thenReturn(Optional.of(ENTRY.bdo()));

			graphQlTester.document(DOCUMENT)
					.execute()
					.path("getAccount.balance")
					.entity(String.class)
					.satisfies(balance -> assertThat(balance).isEqualTo(
							ENTRY.getBalanceAfter().toPlainString()));
		}

		@Test
		void noEntries() {
			when(accountService.get(CHECKING.getId()))
					.thenReturn(CHECKING.bdo());
			when(entryService.findLast(CHECKING.getId()))
					.thenReturn(Optional.empty());

			graphQlTester.document(DOCUMENT)
					.execute()
					.path("getAccount.balance")
					.entity(String.class)
					.satisfies(balance -> assertThat(balance)
							.isEqualTo(BigDecimal.ZERO.toPlainString()));
		}
	}

	@Nested
	class ResolveInUse {

		private static final TestAccount CHECKING = TestAccount.CHECKING;

		private static final String DOCUMENT = """
				query {
					getAccount(id: %d) {
						inUse
					}
				}
				""".formatted(CHECKING.getId());

		@Test
		void used() {
			when(accountService.get(CHECKING.getId()))
					.thenReturn(CHECKING.bdo());
			when(entryService.exists(CHECKING.getId())).thenReturn(true);

			graphQlTester.document(DOCUMENT)
					.execute()
					.path("getAccount.inUse")
					.entity(Boolean.class)
					.isEqualTo(true);
		}

		@Test
		void unused() {
			when(accountService.get(CHECKING.getId()))
					.thenReturn(CHECKING.bdo());
			when(entryService.exists(CHECKING.getId())).thenReturn(false);

			graphQlTester.document(DOCUMENT)
					.execute()
					.path("getAccount.inUse")
					.entity(Boolean.class)
					.isEqualTo(false);
		}
	}

	@Nested
	class ResolveTransactions {

		private static final TestAccount CHECKING = TestAccount.CHECKING;
		private static final TestAccountEntry ENTRY_1 = TestAccountEntry.CHECKING_RENT;
		private static final TestAccountEntry ENTRY_2 = TestAccountEntry.CHECKING_GROCERY;
		private static final TestAccountEntry ENTRY_3 = TestAccountEntry.CHECKING_CASH_WITHDRAWAL;

		private static final String DOCUMENT = """
				query {
					getAccount(id: %d) {
						transactions%s {
							content {
								__typename
								id
								date
								amount
								description
								... on PaymentTransaction {
									counterparty
								}
							}
							page
							size
							totalElements
						}
					}
				}
				""";

		@Test
		void defaultPagination() {
			final TestPaymentTransaction transaction1 = ENTRY_1
					.getPaymentTransaction();
			final TestPaymentTransaction transaction2 = ENTRY_2
					.getPaymentTransaction();
			final TestAccountTransferTransaction transaction3 = ENTRY_3
					.getAccountTransferTransaction();

			when(accountService.get(CHECKING.getId()))
					.thenReturn(CHECKING.bdo());
			when(entryService.searchTransactions(CHECKING.getId(),
					new TransactionSearch(new TransactionFilter(null, null),
							new Pagination(0, 100))))
					.thenReturn(new PagedResult<>(
							List.of(transaction1.bdo(), transaction2.bdo(),
									transaction3.bdo()),
							0, 100, 3));

			final Response response = graphQlTester
					.document(DOCUMENT.formatted(CHECKING.getId(), ""))
					.execute();

			assertPaymentTransaction(response, transaction1,
					"getAccount.transactions.content[0]");
			assertPaymentTransaction(response, transaction2,
					"getAccount.transactions.content[1]");
			assertAccountTransferTransaction(response, transaction3,
					"getAccount.transactions.content[2]");
			assertPage(response, 0, 100, 3);
		}

		@Test
		void customQuery() {
			final TestPaymentTransaction transaction = ENTRY_2
					.getPaymentTransaction();

			when(accountService.get(CHECKING.getId()))
					.thenReturn(CHECKING.bdo());
			when(entryService.searchTransactions(CHECKING.getId(),
					new TransactionSearch(
							new TransactionFilter(LocalDate.of(2026, 1, 1),
									LocalDate.of(2026, 1, 31)),
							new Pagination(1, 1))))
					.thenReturn(new PagedResult<>(List.of(transaction.bdo()),
							1, 1, 3));

			final Response response = graphQlTester
					.document(DOCUMENT.formatted(CHECKING.getId(),
							"""
							(query: {
								filter: { from: "2026-01-01", to: "2026-01-31" }
								page: 1
								size: 1
							})"""))
					.execute();

			assertPaymentTransaction(response, transaction,
					"getAccount.transactions.content[0]");
			response.path("getAccount.transactions.content")
					.entityList(Object.class)
					.hasSize(1);
			assertPage(response, 1, 1, 3);
		}

		@Test
		void noEntries() {
			when(accountService.get(CHECKING.getId()))
					.thenReturn(CHECKING.bdo());
			when(entryService.searchTransactions(CHECKING.getId(),
					new TransactionSearch(new TransactionFilter(null, null),
							new Pagination(0, 100))))
					.thenReturn(new PagedResult<>(Collections.emptyList(), 0,
							100, 0));

			final Response response = graphQlTester
					.document(DOCUMENT.formatted(CHECKING.getId(), ""))
					.execute();

			response.path("getAccount.transactions.content")
					.entityList(Object.class)
					.hasSize(0);
			assertPage(response, 0, 100, 0);
		}

		@Test
		void invalidSize() {
			when(accountService.get(CHECKING.getId()))
					.thenReturn(CHECKING.bdo());

			graphQlTester
					.document(DOCUMENT.formatted(CHECKING.getId(),
							"(query: { filter: {}, size: 0 })"))
					.execute()
					.errors()
					.satisfy(errors -> assertThat(errors).hasSize(1));
		}

		private void assertPage(final Response response, final int page,
				final int size, final long totalElements) {
			response.path("getAccount.transactions.page")
					.entity(Integer.class)
					.isEqualTo(page)
					.path("getAccount.transactions.size")
					.entity(Integer.class)
					.isEqualTo(size)
					.path("getAccount.transactions.totalElements")
					.entity(Long.class)
					.isEqualTo(totalElements);
		}

		private void assertPaymentTransaction(final Response response,
				final TestPaymentTransaction expected, final String prefix) {
			assertTransaction(response, expected, prefix);
			response.path(prefix + ".__typename")
					.entity(String.class)
					.isEqualTo("PaymentTransaction")
					.path(prefix + ".counterparty")
					.entity(String.class)
					.isEqualTo(expected.getCounterparty());
		}

		private void assertAccountTransferTransaction(final Response response,
				final TestAccountTransferTransaction expected,
				final String prefix) {
			assertTransaction(response, expected, prefix);
			response.path(prefix + ".__typename")
					.entity(String.class)
					.isEqualTo("AccountTransferTransaction");
		}

		private void assertTransaction(final Response response,
				final TestTransaction expected, final String prefix) {
			response.path(prefix + ".id")
					.entity(Long.class)
					.isEqualTo(expected.getId())
					.path(prefix + ".date")
					.entity(String.class)
					.isEqualTo(expected.getDate().toString())
					.path(prefix + ".amount")
					.entity(String.class)
					.isEqualTo(expected.getAmountString())
					.path(prefix + ".description")
					.entity(String.class)
					.isEqualTo(expected.getDescription());
		}
	}
}
