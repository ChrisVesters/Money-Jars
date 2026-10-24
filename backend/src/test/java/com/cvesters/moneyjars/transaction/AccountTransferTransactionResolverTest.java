package com.cvesters.moneyjars.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.GraphQlTester.Response;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cvesters.moneyjars.common.exceptions.MissingEntityException;
import com.cvesters.moneyjars.config.GraphqlExceptionResolver;
import com.cvesters.moneyjars.account.AccountService;
import com.cvesters.moneyjars.account.TestAccount;
import com.cvesters.moneyjars.account.bdo.Account;
import com.cvesters.moneyjars.transaction.bdo.Transaction;

@GraphQlTest({ TransactionController.class,
		AccountTransferTransactionResolver.class })
@Import({ TransactionGraphqlConfig.class, GraphqlExceptionResolver.class })
class AccountTransferTransactionResolverTest {

	@Autowired
	private GraphQlTester graphQlTester;

	@MockitoBean
	private AccountService accountService;

	@MockitoBean
	private TransactionService transactionService;

	@Nested
	class ResolveSourceAccount {

		private static final TestAccountTransferTransaction TRANSACTION = TestAccountTransferTransaction.CASH_WITHDRAWAL;
		private static final TestAccount ACCOUNT = TRANSACTION
				.getSourceAccount();

		private static final String DOCUMENT = """
				query {
					getTransaction(id: %d) {
						... on AccountTransferTransaction {
							sourceAccount {
								id
								name
								description
							}
						}
					}
				}
				""".formatted(TRANSACTION.getId());

		@Test
		void success() {
			final Transaction transaction = TRANSACTION.bdo();
			when(transactionService.get(TRANSACTION.getId()))
					.thenReturn(transaction);

			final Account account = ACCOUNT.bdo();
			when(accountService.get(ACCOUNT.getId())).thenReturn(account);

			final Response response = graphQlTester.document(DOCUMENT)
					.execute();

			final String prefix = "getTransaction.sourceAccount";
			response.path(prefix + ".id")
					.entity(Long.class)
					.isEqualTo(ACCOUNT.getId())
					.path(prefix + ".name")
					.entity(String.class)
					.isEqualTo(ACCOUNT.getName())
					.path(prefix + ".description")
					.entity(String.class)
					.isEqualTo(ACCOUNT.getDescription());
		}

		@Test
		void notFound() {
			final Transaction transaction = TRANSACTION.bdo();
			when(transactionService.get(TRANSACTION.getId()))
					.thenReturn(transaction);

			when(accountService.get(ACCOUNT.getId()))
					.thenThrow(MissingEntityException.class);

			graphQlTester.document(DOCUMENT)
					.execute()
					.errors()
					.expect(error -> {
						assertThat(error.getErrorType())
								.isEqualTo(ErrorType.NOT_FOUND);
						assertThat(error.getPath())
								.isEqualTo("getTransaction.sourceAccount");
						return true;
					});
		}
	}

	@Nested
	class ResolveTargetAccount {

		private static final TestAccountTransferTransaction TRANSACTION = TestAccountTransferTransaction.CASH_WITHDRAWAL;
		private static final TestAccount ACCOUNT = TRANSACTION
				.getTargetAccount();

		private static final String DOCUMENT = """
				query {
					getTransaction(id: %d) {
						... on AccountTransferTransaction {
							targetAccount {
								id
								name
								description
							}
						}
					}
				}
				""".formatted(TRANSACTION.getId());

		@Test
		void success() {
			final Transaction transaction = TRANSACTION.bdo();
			when(transactionService.get(TRANSACTION.getId()))
					.thenReturn(transaction);

			final Account account = ACCOUNT.bdo();
			when(accountService.get(ACCOUNT.getId())).thenReturn(account);

			final Response response = graphQlTester.document(DOCUMENT)
					.execute();

			final String prefix = "getTransaction.targetAccount";
			response.path(prefix + ".id")
					.entity(Long.class)
					.isEqualTo(ACCOUNT.getId())
					.path(prefix + ".name")
					.entity(String.class)
					.isEqualTo(ACCOUNT.getName())
					.path(prefix + ".description")
					.entity(String.class)
					.isEqualTo(ACCOUNT.getDescription());
		}

		@Test
		void notFound() {
			final Transaction transaction = TRANSACTION.bdo();
			when(transactionService.get(TRANSACTION.getId()))
					.thenReturn(transaction);

			when(accountService.get(ACCOUNT.getId()))
					.thenThrow(MissingEntityException.class);

			graphQlTester.document(DOCUMENT)
					.execute()
					.errors()
					.expect(error -> {
						assertThat(error.getErrorType())
								.isEqualTo(ErrorType.NOT_FOUND);
						assertThat(error.getPath())
								.isEqualTo("getTransaction.targetAccount");
						return true;
					});
		}
	}
}
