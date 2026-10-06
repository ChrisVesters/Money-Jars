package com.cvesters.moneyjars.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

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
							amount: %s
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
					TRANSACTION.getAmount(), TRANSACTION.getDescription(),
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
							amount: %s
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
					TRANSACTION.getDate().toString(), TRANSACTION.getAmount(),
					TRANSACTION.getDescription(), TRANSACTION.getJar().getId(),
					TRANSACTION.getAccount().getId(),
					TRANSACTION.getCounterparty(),
					TRANSACTION.getDirection().name());

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, TRANSACTION, "updatePaymentTransaction");
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
				.entity(Float.class)
				.isEqualTo(expected.getAmount().floatValue())
				.path(prefix + ".description")
				.entity(String.class)
				.isEqualTo(expected.getDescription())
				.path(prefix + ".counterparty")
				.entity(String.class)
				.isEqualTo(expected.getCounterparty());
	}

}
