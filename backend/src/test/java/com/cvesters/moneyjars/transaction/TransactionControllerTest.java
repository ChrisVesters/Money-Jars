package com.cvesters.moneyjars.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.GraphQlTester.Response;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cvesters.moneyjars.transaction.bdo.Transaction;

@GraphQlTest(TransactionController.class)
class TransactionControllerTest {

	@Autowired
	private GraphQlTester graphQlTester;

	@MockitoBean
	private TransactionService transactionService;

	@Nested
	class GetTransactions {

		@Test
		void success() {
			final List<Transaction> transactions = Stream
					.of(TestTransaction.RENT, TestTransaction.GROCERY)
					.map(TestTransaction::bdo)
					.toList();

			when(transactionService.getAll()).thenReturn(transactions);

			final String document = """
					query {
						getTransactions {
							id
							date
							amount
							beneficiary
							description
						}
					}
					""";

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, TestTransaction.RENT, "getTransactions[0]");
			assertEquals(response, TestTransaction.GROCERY,
					"getTransactions[1]");
		}
	}

	@Nested
	class GetTransaction {

		private static final TestTransaction TRANSACTION = TestTransaction.RENT;

		@Test
		void success() {
			final Transaction transaction = TRANSACTION.bdo();

			when(transactionService.find(TRANSACTION.getId()))
					.thenReturn(Optional.of(transaction));

			final String document = """
					query {
						getTransaction(id: 1) {
							id
							date
							amount
							beneficiary
							description
						}
					}
					""";

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, TRANSACTION, "getTransaction");
		}

		@Test
		void notFound() {
			when(transactionService.find(TRANSACTION.getId()))
					.thenReturn(Optional.empty());

			final String document = """
					query {
						getTransaction(id: 1) {
							id
							date
							amount
							beneficiary
							description
						}
					}
					""";

			graphQlTester.document(document)
					.execute()
					.path("getTransaction")
					.valueIsNull();
		}
	}

	@Nested
	class CreateTransaction {

		private static final TestTransaction TRANSACTION = TestTransaction.RENT;

		@Test
		void success() {
			final Transaction createdTransaction = TRANSACTION.bdo();
			when(transactionService.create(argThat(action -> {
				assertThat(action.date()).isEqualTo(TRANSACTION.getDate());
				assertThat(action.amount())
						.isEqualByComparingTo(TRANSACTION.getAmount());
				assertThat(action.jarId())
						.isEqualTo(TRANSACTION.getJar().getId());
				assertThat(action.beneficiary())
						.isEqualTo(TRANSACTION.getBeneficiary());
				assertThat(action.description())
						.isEqualTo(TRANSACTION.getDescription());
				return true;
			}))).thenReturn(createdTransaction);

			final String document = """
					mutation {
						createTransaction(req: {
							date: "%s"
							amount: %s
							jarId: %d
							beneficiary: "%s"
							description: "%s"
						}) {
							id
							date
							amount
							beneficiary
							description
						}
					}
					""".formatted(TRANSACTION.getDate().toString(),
					TRANSACTION.getAmount(), TRANSACTION.getJar().getId(),
					TRANSACTION.getBeneficiary(), TRANSACTION.getDescription());

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, TRANSACTION, "createTransaction");
		}
	}

	@Nested
	class UpdateTransaction {

		private static final TestTransaction TRANSACTION = TestTransaction.RENT;

		@Test
		void success() {
			final Transaction updatedTransaction = TRANSACTION.bdo();
			when(transactionService.update(eq(TRANSACTION.getId()),
					argThat(transaction -> {
						assertThat(transaction.date())
								.isEqualTo(TRANSACTION.getDate());
						assertThat(transaction.amount())
								.isEqualByComparingTo(TRANSACTION.getAmount());
						assertThat(transaction.jarId())
								.isEqualTo(TRANSACTION.getJar().getId());
						assertThat(transaction.beneficiary())
								.isEqualTo(TRANSACTION.getBeneficiary());
						assertThat(transaction.description())
								.isEqualTo(TRANSACTION.getDescription());
						return true;
					}))).thenReturn(updatedTransaction);

			final String document = """
					mutation {
						updateTransaction(id: %d, req: {
							date: "%s"
							amount: %s
							jarId: %d
							beneficiary: "%s"
							description: "%s"
						}) {
							id
							date
							amount
							beneficiary
							description
						}
					}
					""".formatted(TRANSACTION.getId(),
					TRANSACTION.getDate().toString(), TRANSACTION.getAmount(),
					TRANSACTION.getJar().getId(), TRANSACTION.getBeneficiary(),
					TRANSACTION.getDescription());

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, TRANSACTION, "updateTransaction");
		}
	}

	@Nested
	class DeleteTransaction {

		@Test
		void success() {
			final String document = """
					mutation {
						deleteTransaction(id: 1)
					}
					""";

			graphQlTester.document(document).execute();

			verify(transactionService).delete(1L);
		}
	}

	void assertEquals(final Response response, final TestTransaction expected,
			final String prefix) {
		response.path(prefix + ".id")
				.entity(Long.class)
				.isEqualTo(expected.getId())
				.path(prefix + ".date")
				.entity(String.class)
				.isEqualTo(expected.getDate().toString())
				.path(prefix + ".amount")
				.entity(Float.class)
				.isEqualTo(expected.getAmount().floatValue())
				.path(prefix + ".beneficiary")
				.entity(String.class)
				.isEqualTo(expected.getBeneficiary())
				.path(prefix + ".description")
				.entity(String.class)
				.isEqualTo(expected.getDescription());

	}

}
