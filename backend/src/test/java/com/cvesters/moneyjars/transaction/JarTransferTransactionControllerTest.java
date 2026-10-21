package com.cvesters.moneyjars.transaction;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.GraphQlTester.Response;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cvesters.moneyjars.transaction.bdo.JarTransferTransaction;
import com.cvesters.moneyjars.transaction.bdo.JarTransferTransactionAction;

@GraphQlTest(JarTransferTransactionController.class)
class JarTransferTransactionControllerTest {

	@Autowired
	private GraphQlTester graphQlTester;

	@MockitoBean
	private JarTransferTransactionService transactionService;

	@Nested
	class CreateJarTransferTransaction {

		private static final TestJarTransferTransaction TRANSACTION = TestJarTransferTransaction.CAR_SAVINGS;

		@Test
		void success() {
			final var expectedAction = new JarTransferTransactionAction.Create(
					TRANSACTION.getDate(), TRANSACTION.getAmount(),
					TRANSACTION.getDescription(),
					TRANSACTION.getSourceJar().getId(),
					TRANSACTION.getTargetJar().getId());
			final JarTransferTransaction createdTransaction = TRANSACTION.bdo();
			when(transactionService.create(expectedAction))
					.thenReturn(createdTransaction);

			final String document = """
					mutation {
						createJarTransferTransaction(req: {
							date: "%s"
							amount: "%s"
							description: "%s"
							sourceJarId: %d
							targetJarId: %d
						}) {
							id
							date
							amount
							description
						}
					}
					""".formatted(TRANSACTION.getDate().toString(),
					TRANSACTION.getAmountString(), TRANSACTION.getDescription(),
					TRANSACTION.getSourceJar().getId(),
					TRANSACTION.getTargetJar().getId());

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, TRANSACTION, "createJarTransferTransaction");
		}
	}

	@Nested
	class UpdateJarTransferTransaction {

		private static final TestJarTransferTransaction TRANSACTION = TestJarTransferTransaction.CAR_SAVINGS;

		@Test
		void success() {
			final var expectedAction = new JarTransferTransactionAction.Update(
					TRANSACTION.getDate(), TRANSACTION.getAmount(),
					TRANSACTION.getDescription(),
					TRANSACTION.getSourceJar().getId(),
					TRANSACTION.getTargetJar().getId());
			final JarTransferTransaction updatedTransaction = TRANSACTION.bdo();
			when(transactionService.update(eq(TRANSACTION.getId()),
					eq(expectedAction))).thenReturn(updatedTransaction);

			final String document = """
					mutation {
						updateJarTransferTransaction(id: %d, req: {
							date: "%s"
							amount: "%s"
							description: "%s"
							sourceJarId: %d
							targetJarId: %d
						}) {
							id
							date
							amount
							description
						}
					}
					""".formatted(TRANSACTION.getId(),
					TRANSACTION.getDate().toString(),
					TRANSACTION.getAmountString(), TRANSACTION.getDescription(),
					TRANSACTION.getSourceJar().getId(),
					TRANSACTION.getTargetJar().getId());

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, TRANSACTION, "updateJarTransferTransaction");
		}
	}

	@Nested
	class DeleteJarTransferTransaction {

		@Test
		void success() {
			final String document = """
					mutation {
						deleteJarTransferTransaction(id: 1)
					}
					""";

			final Response response = graphQlTester.document(document)
					.execute();

			response.path("deleteJarTransferTransaction")
					.entity(Boolean.class)
					.isEqualTo(true);

			verify(transactionService).delete(1L);
		}
	}

	void assertEquals(final Response response,
			final TestJarTransferTransaction expected, final String prefix) {
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
