package com.cvesters.moneyjars.jar;

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

import com.cvesters.moneyjars.transaction.TestJarTransferTransaction;
import com.cvesters.moneyjars.transaction.TestPaymentTransaction;
import com.cvesters.moneyjars.transaction.TestTransaction;
import com.cvesters.moneyjars.transaction.TransactionGraphqlConfig;
import com.cvesters.moneyjars.transaction.TransactionService;

@GraphQlTest({ JarController.class, JarResolver.class })
@Import(TransactionGraphqlConfig.class)
class JarResolverTest {

	@Autowired
	private GraphQlTester graphQlTester;

	@MockitoBean
	private JarService jarService;

	@MockitoBean
	private JarEntryService entryService;

	@MockitoBean
	private TransactionService transactionService;

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
	class ResolveTransactions {

		private static final TestJar HOUSEHOLD = TestJar.HOUSEHOLD;
		private static final TestJarEntry ENTRY_1 = TestJarEntry.HOUSEHOLD_RENT;
		private static final TestJarEntry ENTRY_2 = TestJarEntry.HOUSEHOLD_GROCERY;
		private static final TestJarEntry ENTRY_3 = TestJarEntry.HOUSEHOLD_CAR_SAVINGS;

		private static final String DOCUMENT = """
				query {
					getJar(id: %d) {
						transactions {
							__typename
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
				""".formatted(HOUSEHOLD.getId());

		@Test
		void multiple() {
			final TestPaymentTransaction transaction1 = ENTRY_1
					.getPaymentTransaction();
			final TestPaymentTransaction transaction2 = ENTRY_2
					.getPaymentTransaction();
			final TestJarTransferTransaction transaction3 = ENTRY_3
					.getJarTransferTransaction();

			when(jarService.get(HOUSEHOLD.getId())).thenReturn(HOUSEHOLD.bdo());
			when(entryService.getAll(HOUSEHOLD.getId())).thenReturn(
					List.of(ENTRY_1.bdo(), ENTRY_2.bdo(), ENTRY_3.bdo()));
			when(transactionService.get(transaction1.getId()))
					.thenReturn(transaction1.bdo());
			when(transactionService.get(transaction2.getId()))
					.thenReturn(transaction2.bdo());
			when(transactionService.get(transaction3.getId()))
					.thenReturn(transaction3.bdo());

			final Response response = graphQlTester.document(DOCUMENT)
					.execute();

			assertPaymentTransaction(response, transaction1,
					"getJar.transactions[0]");
			assertPaymentTransaction(response, transaction2,
					"getJar.transactions[1]");
			assertJarTransferTransaction(response, transaction3,
					"getJar.transactions[2]");
		}

		@Test
		void noEntries() {
			when(jarService.get(HOUSEHOLD.getId())).thenReturn(HOUSEHOLD.bdo());
			when(entryService.getAll(HOUSEHOLD.getId()))
					.thenReturn(Collections.emptyList());

			graphQlTester.document(DOCUMENT)
					.execute()
					.path("getJar.transactions")
					.entityList(Object.class)
					.hasSize(0);
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
