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

import com.cvesters.moneyjars.transaction.bdo.AccountTransferTransaction;
import com.cvesters.moneyjars.transaction.bdo.AccountTransferTransactionAction;

@GraphQlTest(AccountTransferTransactionController.class)
class AccountTransferTransactionControllerTest {

	@Autowired
	private GraphQlTester graphQlTester;

	@MockitoBean
	private AccountTransferTransactionService transactionService;

	@Nested
	class CreateAccountTransferTransaction {

		private static final TestAccountTransferTransaction TRANSACTION = TestAccountTransferTransaction.CASH_WITHDRAWAL;

		@Test
		void success() {
			final var expectedAction = new AccountTransferTransactionAction.Create(
					TRANSACTION.getDate(), TRANSACTION.getAmount(),
					TRANSACTION.getDescription(),
					TRANSACTION.getSourceAccount().getId(),
					TRANSACTION.getTargetAccount().getId());
			final AccountTransferTransaction createdTransaction = TRANSACTION
					.bdo();
			when(transactionService.create(expectedAction))
					.thenReturn(createdTransaction);

			final String document = """
					mutation {
						createAccountTransferTransaction(req: {
							date: "%s"
							amount: "%s"
							description: "%s"
							sourceAccountId: %d
							targetAccountId: %d
						}) {
							id
							date
							amount
							description
						}
					}
					""".formatted(TRANSACTION.getDate().toString(),
					TRANSACTION.getAmountString(), TRANSACTION.getDescription(),
					TRANSACTION.getSourceAccount().getId(),
					TRANSACTION.getTargetAccount().getId());

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, TRANSACTION,
					"createAccountTransferTransaction");
		}
	}

	@Nested
	class UpdateAccountTransferTransaction {

		private static final TestAccountTransferTransaction TRANSACTION = TestAccountTransferTransaction.CASH_WITHDRAWAL;

		@Test
		void success() {
			final var expectedAction = new AccountTransferTransactionAction.Update(
					TRANSACTION.getDate(), TRANSACTION.getAmount(),
					TRANSACTION.getDescription(),
					TRANSACTION.getSourceAccount().getId(),
					TRANSACTION.getTargetAccount().getId());
			final AccountTransferTransaction updatedTransaction = TRANSACTION
					.bdo();
			when(transactionService.update(eq(TRANSACTION.getId()),
					eq(expectedAction))).thenReturn(updatedTransaction);

			final String document = """
					mutation {
						updateAccountTransferTransaction(id: %d, req: {
							date: "%s"
							amount: "%s"
							description: "%s"
							sourceAccountId: %d
							targetAccountId: %d
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
					TRANSACTION.getSourceAccount().getId(),
					TRANSACTION.getTargetAccount().getId());

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, TRANSACTION,
					"updateAccountTransferTransaction");
		}
	}

	@Nested
	class DeleteAccountTransferTransaction {

		@Test
		void success() {
			final String document = """
					mutation {
						deleteAccountTransferTransaction(id: 1)
					}
					""";

			final Response response = graphQlTester.document(document)
					.execute();

			response.path("deleteAccountTransferTransaction")
					.entity(Boolean.class)
					.isEqualTo(true);

			verify(transactionService).delete(1L);
		}
	}

	void assertEquals(final Response response,
			final TestAccountTransferTransaction expected,
			final String prefix) {
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
