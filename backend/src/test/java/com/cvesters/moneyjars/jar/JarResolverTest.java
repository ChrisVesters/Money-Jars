package com.cvesters.moneyjars.jar;

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
import com.cvesters.moneyjars.transaction.TestJarTransferTransaction;
import com.cvesters.moneyjars.transaction.TestPaymentTransaction;
import com.cvesters.moneyjars.transaction.TestTransaction;
import com.cvesters.moneyjars.transaction.TransactionGraphqlConfig;
import com.cvesters.moneyjars.transaction.bdo.TransactionFilter;
import com.cvesters.moneyjars.transaction.bdo.TransactionQuery;

@GraphQlTest({ JarController.class, JarResolver.class })
@Import(TransactionGraphqlConfig.class)
class JarResolverTest {

	@Autowired
	private GraphQlTester graphQlTester;

	@MockitoBean
	private JarService jarService;

	@MockitoBean
	private JarEntryService entryService;

	@Nested
	class ResolveBalance {

		private static final TestJar HOUSEHOLD = TestJar.HOUSEHOLD;
		private static final TestJarEntry ENTRY = TestJarEntry.HOUSEHOLD_MARKET;

		private static final String DOCUMENT = """
				query {
					getJar(id: %d) {
						balance
					}
				}
				""".formatted(HOUSEHOLD.getId());

		@Test
		void lastEntry() {
			when(jarService.get(HOUSEHOLD.getId())).thenReturn(HOUSEHOLD.bdo());
			when(entryService.findLast(HOUSEHOLD.getId()))
					.thenReturn(Optional.of(ENTRY.bdo()));

			graphQlTester.document(DOCUMENT)
					.execute()
					.path("getJar.balance")
					.entity(String.class)
					.satisfies(balance -> assertThat(balance).isEqualTo(
							ENTRY.getBalanceAfter().toPlainString()));
		}

		@Test
		void noEntries() {
			when(jarService.get(HOUSEHOLD.getId())).thenReturn(HOUSEHOLD.bdo());
			when(entryService.findLast(HOUSEHOLD.getId()))
					.thenReturn(Optional.empty());

			graphQlTester.document(DOCUMENT)
					.execute()
					.path("getJar.balance")
					.entity(String.class)
					.satisfies(balance -> assertThat(balance)
							.isEqualTo(BigDecimal.ZERO.toPlainString()));
		}
	}

	@Nested
	class ResolveInUse {

		private static final TestJar HOUSEHOLD = TestJar.HOUSEHOLD;

		private static final String DOCUMENT = """
				query {
					getJar(id: %d) {
						inUse
					}
				}
				""".formatted(HOUSEHOLD.getId());

		@Test
		void used() {
			when(jarService.get(HOUSEHOLD.getId())).thenReturn(HOUSEHOLD.bdo());
			when(entryService.exists(HOUSEHOLD.getId())).thenReturn(true);

			graphQlTester.document(DOCUMENT)
					.execute()
					.path("getJar.inUse")
					.entity(Boolean.class)
					.isEqualTo(true);
		}

		@Test
		void unused() {
			when(jarService.get(HOUSEHOLD.getId())).thenReturn(HOUSEHOLD.bdo());
			when(entryService.exists(HOUSEHOLD.getId())).thenReturn(false);

			graphQlTester.document(DOCUMENT)
					.execute()
					.path("getJar.inUse")
					.entity(Boolean.class)
					.isEqualTo(false);
		}
	}

	@Nested
	class ResolveTransactions {

		private static final TestJar HOUSEHOLD = TestJar.HOUSEHOLD;
		private static final TestJarEntry ENTRY_1 = TestJarEntry.HOUSEHOLD_RENT;
		private static final TestJarEntry ENTRY_2 = TestJarEntry.HOUSEHOLD_GROCERY;
		private static final TestJarEntry ENTRY_3 = TestJarEntry.HOUSEHOLD_CAR_SAVINGS;

		private static final String DOCUMENT = """
				query {
					getJar(id: %d) {
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
			final TestJarTransferTransaction transaction3 = ENTRY_3
					.getJarTransferTransaction();

			when(jarService.get(HOUSEHOLD.getId())).thenReturn(HOUSEHOLD.bdo());
			when(entryService.queryTransactions(HOUSEHOLD.getId(),
					new TransactionQuery(new TransactionFilter(null, null),
							new Pagination(0, 100))))
					.thenReturn(new PagedResult<>(
							List.of(transaction1.bdo(), transaction2.bdo(),
									transaction3.bdo()),
							0, 100, 3));

			final Response response = graphQlTester
					.document(DOCUMENT.formatted(HOUSEHOLD.getId(), ""))
					.execute();

			assertPaymentTransaction(response, transaction1,
					"getJar.transactions.content[0]");
			assertPaymentTransaction(response, transaction2,
					"getJar.transactions.content[1]");
			assertJarTransferTransaction(response, transaction3,
					"getJar.transactions.content[2]");
			assertPage(response, 0, 100, 3);
		}

		@Test
		void customQuery() {
			final TestPaymentTransaction transaction = ENTRY_2
					.getPaymentTransaction();

			when(jarService.get(HOUSEHOLD.getId())).thenReturn(HOUSEHOLD.bdo());
			when(entryService.queryTransactions(HOUSEHOLD.getId(),
					new TransactionQuery(
							new TransactionFilter(LocalDate.of(2026, 1, 1),
									LocalDate.of(2026, 1, 31)),
							new Pagination(1, 1))))
					.thenReturn(new PagedResult<>(List.of(transaction.bdo()),
							1, 1, 3));

			final Response response = graphQlTester
					.document(DOCUMENT.formatted(HOUSEHOLD.getId(),
							"""
							(query: {
								filter: { from: "2026-01-01", to: "2026-01-31" }
								page: 1
								size: 1
							})"""))
					.execute();

			assertPaymentTransaction(response, transaction,
					"getJar.transactions.content[0]");
			response.path("getJar.transactions.content")
					.entityList(Object.class)
					.hasSize(1);
			assertPage(response, 1, 1, 3);
		}

		@Test
		void noEntries() {
			when(jarService.get(HOUSEHOLD.getId())).thenReturn(HOUSEHOLD.bdo());
			when(entryService.queryTransactions(HOUSEHOLD.getId(),
					new TransactionQuery(new TransactionFilter(null, null),
							new Pagination(0, 100))))
					.thenReturn(new PagedResult<>(Collections.emptyList(), 0,
							100, 0));

			final Response response = graphQlTester
					.document(DOCUMENT.formatted(HOUSEHOLD.getId(), ""))
					.execute();

			response.path("getJar.transactions.content")
					.entityList(Object.class)
					.hasSize(0);
			assertPage(response, 0, 100, 0);
		}

		@Test
		void invalidSize() {
			when(jarService.get(HOUSEHOLD.getId())).thenReturn(HOUSEHOLD.bdo());

			graphQlTester
					.document(DOCUMENT.formatted(HOUSEHOLD.getId(),
							"(query: { filter: {}, size: 0 })"))
					.execute()
					.errors()
					.satisfy(errors -> assertThat(errors).hasSize(1));
		}

		private void assertPage(final Response response, final int page,
				final int size, final long totalElements) {
			response.path("getJar.transactions.page")
					.entity(Integer.class)
					.isEqualTo(page)
					.path("getJar.transactions.size")
					.entity(Integer.class)
					.isEqualTo(size)
					.path("getJar.transactions.totalElements")
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

		private void assertJarTransferTransaction(final Response response,
				final TestJarTransferTransaction expected,
				final String prefix) {
			assertTransaction(response, expected, prefix);
			response.path(prefix + ".__typename")
					.entity(String.class)
					.isEqualTo("JarTransferTransaction");
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
