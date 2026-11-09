package com.cvesters.moneyjars.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.GraphQlTester.Response;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;

@GraphQlTest(PaymentTransactionController.class)
class PaymentTransactionControllerTest {

	@Autowired
	private GraphQlTester graphQlTester;

	@MockitoBean
	private PaymentTransactionService paymentTransactionService;

	@Nested
	class GetPaymentTransactionCategoryTotals {

		private static final LocalDate FROM = LocalDate.of(2026, 1, 1);
		private static final LocalDate TO = LocalDate.of(2026, 1, 31);

		private static final String DOCUMENT = """
				query {
					getPaymentTransactionCategoryTotals(from: "%s", to: "%s") {
						incoming
						outgoing
					}
				}
				""".formatted(FROM, TO);

		@Test
		void success() {
			final TestPaymentTransactionCategoryTotal first = TestPaymentTransactionCategoryTotal.JANUARY_RENT;
			final TestPaymentTransactionCategoryTotal second = TestPaymentTransactionCategoryTotal.JANUARY_UNCATEGORISED;
			when(paymentTransactionService.getCategoryTotals(FROM, TO))
					.thenReturn(List.of(first.bdo(), second.bdo()));

			final Response response = graphQlTester.document(DOCUMENT)
					.execute();

			final String prefix = "getPaymentTransactionCategoryTotals";
			assertEquals(response, first, prefix + "[0]");
			assertEquals(response, second, prefix + "[1]");
			response.path(prefix).entityList(Object.class).hasSize(2);
		}

		@Test
		void none() {
			when(paymentTransactionService.getCategoryTotals(FROM, TO))
					.thenReturn(List.of());

			graphQlTester.document(DOCUMENT)
					.execute()
					.path("getPaymentTransactionCategoryTotals")
					.entityList(Object.class)
					.hasSize(0);
		}

		private static void assertEquals(final Response response,
				final TestPaymentTransactionCategoryTotal expected,
				final String prefix) {
			response.path(prefix + ".incoming")
					.entity(String.class)
					.isEqualTo(expected.getIncoming().toPlainString())
					.path(prefix + ".outgoing")
					.entity(String.class)
					.isEqualTo(expected.getOutgoing().toPlainString());
		}
	}

	@Nested
	class CreatePaymentTransaction {

		private static final TestPaymentTransaction TRANSACTION = TestPaymentTransaction.RENT;

		@Test
		void success() {
			final PaymentTransaction createdTransaction = TRANSACTION.bdo();
			when(paymentTransactionService.create(argThat(action -> {
				assertThat(action.date()).isEqualTo(TRANSACTION.getDate());
				assertThat(action.amount())
						.isEqualByComparingTo(TRANSACTION.getAmount());
				assertThat(action.description())
						.isEqualTo(TRANSACTION.getDescription());
				assertThat(action.jarId())
						.isEqualTo(TRANSACTION.getJar().getId());
				assertThat(action.accountId())
						.isEqualTo(TRANSACTION.getAccount().getId());
				assertThat(action.counterparty())
						.isEqualTo(TRANSACTION.getCounterparty());
				assertThat(action.direction())
						.isEqualTo(TRANSACTION.getDirection());
				return true;
			}))).thenReturn(createdTransaction);

			final String document = """
					mutation {
						createPaymentTransaction(req: {
							date: "%s"
							amount: "%s"
							description: "%s"
							jarId: %d
							accountId: %d
							counterparty: "%s"
							direction: %s
						}) {
							id
							date
							amount
							description
							counterparty
						}
					}
					""".formatted(TRANSACTION.getDate().toString(),
					TRANSACTION.getAmountString(), TRANSACTION.getDescription(),
					TRANSACTION.getJar().getId(),
					TRANSACTION.getAccount().getId(),
					TRANSACTION.getCounterparty(),
					TRANSACTION.getDirection().name());

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, TRANSACTION, "createPaymentTransaction");
		}
	}

	@Nested
	class UpdatePaymentTransaction {

		private static final TestPaymentTransaction TRANSACTION = TestPaymentTransaction.RENT;

		@Test
		void success() {
			final PaymentTransaction updatedTransaction = TRANSACTION.bdo();
			when(paymentTransactionService.update(eq(TRANSACTION.getId()),
					argThat(transaction -> {
						assertThat(transaction.date())
								.isEqualTo(TRANSACTION.getDate());
						assertThat(transaction.amount())
								.isEqualByComparingTo(TRANSACTION.getAmount());
						assertThat(transaction.description())
								.isEqualTo(TRANSACTION.getDescription());
						assertThat(transaction.jarId())
								.isEqualTo(TRANSACTION.getJar().getId());
						assertThat(transaction.accountId())
								.isEqualTo(TRANSACTION.getAccount().getId());
						assertThat(transaction.counterparty())
								.isEqualTo(TRANSACTION.getCounterparty());
						assertThat(transaction.direction())
								.isEqualTo(TRANSACTION.getDirection());
						return true;
					}))).thenReturn(updatedTransaction);

			final String document = """
					mutation {
						updatePaymentTransaction(id: %d, req: {
							date: "%s"
							amount: "%s"
							description: "%s"
							jarId: %d
							accountId: %d
							counterparty: "%s"
							direction: %s
						}) {
							id
							date
							amount
							description
							counterparty
						}
					}
					""".formatted(TRANSACTION.getId(),
					TRANSACTION.getDate().toString(), TRANSACTION.getAmountString(),
					TRANSACTION.getDescription(), TRANSACTION.getJar().getId(),
					TRANSACTION.getAccount().getId(),
					TRANSACTION.getCounterparty(),
					TRANSACTION.getDirection().name());

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, TRANSACTION, "updatePaymentTransaction");
		}
	}

	@Nested
	class DeletePaymentTransaction {

		@Test
		void success() {
			final String document = """
					mutation {
						deletePaymentTransaction(id: 1)
					}
					""";

			final Response response = graphQlTester.document(document)
					.execute();

			response.path("deletePaymentTransaction")
					.entity(Boolean.class)
					.isEqualTo(true);

			verify(paymentTransactionService).delete(1L);
		}
	}

	void assertEquals(final Response response,
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
