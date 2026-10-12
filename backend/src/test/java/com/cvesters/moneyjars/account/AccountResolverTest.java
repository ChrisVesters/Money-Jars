package com.cvesters.moneyjars.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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

import com.cvesters.moneyjars.transaction.TestPaymentTransaction;
import com.cvesters.moneyjars.transaction.TransactionGraphqlConfig;
import com.cvesters.moneyjars.transaction.TransactionService;

@GraphQlTest({ AccountController.class, AccountResolver.class })
@Import(TransactionGraphqlConfig.class)
class AccountResolverTest {

	@Autowired
	private GraphQlTester graphQlTester;

	@MockitoBean
	private AccountService accountService;

	@MockitoBean
	private AccountEntryService entryService;

	@MockitoBean
	private TransactionService transactionService;

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
			when(accountService.find(CHECKING.getId()))
					.thenReturn(Optional.of(CHECKING.bdo()));
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
			when(accountService.find(CHECKING.getId()))
					.thenReturn(Optional.of(CHECKING.bdo()));
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
	class ResolveTransactions {

		private static final TestAccount CHECKING = TestAccount.CHECKING;
		private static final TestAccountEntry ENTRY_1 = TestAccountEntry.CHECKING_RENT;
		private static final TestAccountEntry ENTRY_2 = TestAccountEntry.CHECKING_GROCERY;

		private static final String DOCUMENT = """
				query {
					getAccount(id: %d) {
						transactions {
							id
							date
							amount
							description
							... on PaymentTransaction {
								counterparty
							}
						}
					}
				}
				""".formatted(CHECKING.getId());

		@Test
		void multiple() {
			final TestPaymentTransaction transaction1 = ENTRY_1.getTransaction();
			final TestPaymentTransaction transaction2 = ENTRY_2.getTransaction();

			when(accountService.find(CHECKING.getId()))
					.thenReturn(Optional.of(CHECKING.bdo()));
			when(entryService.getAll(CHECKING.getId()))
					.thenReturn(List.of(ENTRY_1.bdo(), ENTRY_2.bdo()));
			when(transactionService.get(transaction1.getId()))
					.thenReturn(transaction1.bdo());
			when(transactionService.get(transaction2.getId()))
					.thenReturn(transaction2.bdo());

			final Response response = graphQlTester.document(DOCUMENT)
					.execute();

			assertTransaction(response, transaction1,
					"getAccount.transactions[0]");
			assertTransaction(response, transaction2,
					"getAccount.transactions[1]");
		}

		@Test
		void noEntries() {
			when(accountService.find(CHECKING.getId()))
					.thenReturn(Optional.of(CHECKING.bdo()));
			when(entryService.getAll(CHECKING.getId()))
					.thenReturn(Collections.emptyList());

			graphQlTester.document(DOCUMENT)
					.execute()
					.path("getAccount.transactions")
					.entityList(Object.class)
					.hasSize(0);
		}

		private void assertTransaction(final Response response,
				final TestPaymentTransaction expected, final String prefix) {
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
					.isEqualTo(expected.getDescription())
					.path(prefix + ".counterparty")
					.entity(String.class)
					.isEqualTo(expected.getCounterparty());
		}
	}
}
