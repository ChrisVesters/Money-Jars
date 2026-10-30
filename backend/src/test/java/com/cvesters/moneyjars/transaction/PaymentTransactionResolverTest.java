package com.cvesters.moneyjars.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
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

import com.cvesters.moneyjars.account.AccountService;
import com.cvesters.moneyjars.account.TestAccount;
import com.cvesters.moneyjars.account.bdo.Account;
import com.cvesters.moneyjars.category.CategoryService;
import com.cvesters.moneyjars.category.TestCategory;
import com.cvesters.moneyjars.category.bdo.Category;
import com.cvesters.moneyjars.common.exceptions.MissingEntityException;
import com.cvesters.moneyjars.config.GraphqlExceptionResolver;
import com.cvesters.moneyjars.jar.JarService;
import com.cvesters.moneyjars.jar.TestJar;
import com.cvesters.moneyjars.jar.bdo.Jar;
import com.cvesters.moneyjars.transaction.bdo.Transaction;

@GraphQlTest({ TransactionController.class, PaymentTransactionResolver.class })
@Import({ TransactionGraphqlConfig.class, GraphqlExceptionResolver.class })
class PaymentTransactionResolverTest {

	@Autowired
	private GraphQlTester graphQlTester;

	@MockitoBean
	private JarService jarService;

	@MockitoBean
	private AccountService accountService;

	@MockitoBean
	private CategoryService categoryService;

	@MockitoBean
	private TransactionService transactionService;

	@Nested
	class ResolveJar {

		private static final TestPaymentTransaction TRANSACTION = TestPaymentTransaction.RENT;
		private static final TestJar JAR = TRANSACTION.getJar();

		@Test
		void success() {
			final Transaction transaction = TRANSACTION.bdo();
			when(transactionService.get(TRANSACTION.getId()))
					.thenReturn(transaction);

			final Jar jar = JAR.bdo();
			when(jarService.get(JAR.getId())).thenReturn(jar);

			final String document = """
					query {
						getTransaction(id: %d) {
							... on PaymentTransaction {
								jar {
									id
									name
									description
								}
							}
						}
					}
					""".formatted(TRANSACTION.getId());

			final Response response = graphQlTester.document(document)
					.execute();

			final String prefix = "getTransaction.jar";
			response.path(prefix + ".id")
					.entity(Long.class)
					.isEqualTo(JAR.getId())
					.path(prefix + ".name")
					.entity(String.class)
					.isEqualTo(JAR.getName())
					.path(prefix + ".description")
					.entity(String.class)
					.isEqualTo(JAR.getDescription());
		}

		@Test
		void notFound() {
			final Transaction transaction = TRANSACTION.bdo();
			when(transactionService.get(TRANSACTION.getId()))
					.thenReturn(transaction);

			when(jarService.get(JAR.getId()))
					.thenThrow(MissingEntityException.class);

			final String document = """
					query {
						getTransaction(id: %d) {
							... on PaymentTransaction {
								jar {
									id
									name
									description
								}
							}
						}
					}
					""".formatted(TRANSACTION.getId());

			graphQlTester.document(document)
					.execute()
					.errors()
					.expect(error -> {
						assertThat(error.getErrorType())
								.isEqualTo(ErrorType.NOT_FOUND);
						assertThat(error.getPath())
								.isEqualTo("getTransaction.jar");
						return true;
					});
		}
	}

	@Nested
	class ResolveAccount {

		private static final TestPaymentTransaction TRANSACTION = TestPaymentTransaction.RENT;
		private static final TestAccount ACCOUNT = TRANSACTION.getAccount();

		@Test
		void success() {
			final Transaction transaction = TRANSACTION.bdo();
			when(transactionService.get(TRANSACTION.getId()))
					.thenReturn(transaction);

			final Account account = ACCOUNT.bdo();
			when(accountService.get(ACCOUNT.getId())).thenReturn(account);

			final String document = """
					query {
						getTransaction(id: %d) {
							... on PaymentTransaction {
								account {
									id
									name
									description
								}
							}
						}
					}
					""".formatted(TRANSACTION.getId());

			final Response response = graphQlTester.document(document)
					.execute();

			final String prefix = "getTransaction.account";
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

			final String document = """
					query {
						getTransaction(id: %d) {
							... on PaymentTransaction {
								account {
									id
									name
									description
								}
							}
						}
					}
					""".formatted(TRANSACTION.getId());

			graphQlTester.document(document)
					.execute()
					.errors()
					.expect(error -> {
						assertThat(error.getErrorType())
								.isEqualTo(ErrorType.NOT_FOUND);
						assertThat(error.getPath())
								.isEqualTo("getTransaction.account");
						return true;
					});
		}
	}

	@Nested
	class ResolveCategory {

		private static final String DOCUMENT = """
				query {
					getTransaction(id: %d) {
						... on PaymentTransaction {
							category {
								id
								name
								description
							}
						}
					}
				}
				""";

		@Test
		void success() {
			final TestPaymentTransaction expectedTransaction = TestPaymentTransaction.RENT;
			final TestCategory expectedCategory = expectedTransaction
					.getCategory();

			final Transaction transaction = expectedTransaction.bdo();
			when(transactionService.get(expectedTransaction.getId()))
					.thenReturn(transaction);

			final Category category = expectedCategory.bdo();
			when(categoryService.get(expectedCategory.getId()))
					.thenReturn(category);

			final Response response = graphQlTester
					.document(DOCUMENT.formatted(expectedTransaction.getId()))
					.execute();

			final String prefix = "getTransaction.category";
			response.path(prefix + ".id")
					.entity(Long.class)
					.isEqualTo(expectedCategory.getId())
					.path(prefix + ".name")
					.entity(String.class)
					.isEqualTo(expectedCategory.getName())
					.path(prefix + ".description")
					.entity(String.class)
					.isEqualTo(expectedCategory.getDescription());
		}

		@Test
		void noCategory() {
			final TestPaymentTransaction expectedTransaction = TestPaymentTransaction.CAFE;

			final Transaction transaction = expectedTransaction.bdo();
			when(transactionService.get(expectedTransaction.getId()))
					.thenReturn(transaction);

			graphQlTester
					.document(DOCUMENT.formatted(expectedTransaction.getId()))
					.execute()
					.path("getTransaction.category")
					.valueIsNull();
			verifyNoInteractions(categoryService);
		}

		@Test
		void notFound() {
			final TestPaymentTransaction expectedTransaction = TestPaymentTransaction.RENT;
			final TestCategory expectedCategory = expectedTransaction
					.getCategory();

			final Transaction transaction = expectedTransaction.bdo();
			when(transactionService.get(expectedTransaction.getId()))
					.thenReturn(transaction);

			when(categoryService.get(expectedCategory.getId()))
					.thenThrow(MissingEntityException.class);

			graphQlTester
					.document(DOCUMENT.formatted(expectedTransaction.getId()))
					.execute()
					.errors()
					.expect(error -> {
						assertThat(error.getErrorType())
								.isEqualTo(ErrorType.NOT_FOUND);
						assertThat(error.getPath())
								.isEqualTo("getTransaction.category");
						return true;
					});
		}
	}

	@Nested
	class ResolveDirection {

		private static final TestPaymentTransaction TRANSACTION = TestPaymentTransaction.RENT;
		private static final TestAccount ACCOUNT = TRANSACTION.getAccount();

		@Test
		void success() {
			final Transaction transaction = TRANSACTION.bdo();
			when(transactionService.get(TRANSACTION.getId()))
					.thenReturn(transaction);

			final Account account = ACCOUNT.bdo();
			when(accountService.get(ACCOUNT.getId())).thenReturn(account);

			final String document = """
					query {
						getTransaction(id: %d) {
							... on PaymentTransaction {
								direction
							}
						}
					}
					""".formatted(TRANSACTION.getId());

			final Response response = graphQlTester.document(document)
					.execute();

			response.path("getTransaction.direction")
					.entity(String.class)
					.isEqualTo(TRANSACTION.getDirection().name());
		}
	}
}