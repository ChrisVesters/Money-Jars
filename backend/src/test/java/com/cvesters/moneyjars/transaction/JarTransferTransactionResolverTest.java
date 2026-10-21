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
import com.cvesters.moneyjars.jar.JarService;
import com.cvesters.moneyjars.jar.TestJar;
import com.cvesters.moneyjars.jar.bdo.Jar;
import com.cvesters.moneyjars.transaction.bdo.Transaction;

@GraphQlTest({ TransactionController.class,
		JarTransferTransactionResolver.class })
@Import({ TransactionGraphqlConfig.class, GraphqlExceptionResolver.class })
class JarTransferTransactionResolverTest {

	@Autowired
	private GraphQlTester graphQlTester;

	@MockitoBean
	private JarService jarService;

	@MockitoBean
	private TransactionService transactionService;

	@Nested
	class ResolveSourceJar {

		private static final TestJarTransferTransaction TRANSACTION = TestJarTransferTransaction.CAR_SAVINGS;
		private static final TestJar JAR = TRANSACTION.getSourceJar();

		private static final String DOCUMENT = """
				query {
					getTransaction(id: %d) {
						... on JarTransferTransaction {
							sourceJar {
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

			final Jar jar = JAR.bdo();
			when(jarService.get(JAR.getId())).thenReturn(jar);

			final Response response = graphQlTester.document(DOCUMENT)
					.execute();

			final String prefix = "getTransaction.sourceJar";
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

			graphQlTester.document(DOCUMENT)
					.execute()
					.errors()
					.expect(error -> {
						assertThat(error.getErrorType())
								.isEqualTo(ErrorType.NOT_FOUND);
						assertThat(error.getPath())
								.isEqualTo("getTransaction.sourceJar");
						return true;
					});
		}
	}

	@Nested
	class ResolveTargetJar {

		private static final TestJarTransferTransaction TRANSACTION = TestJarTransferTransaction.CAR_SAVINGS;
		private static final TestJar JAR = TRANSACTION.getTargetJar();

		private static final String DOCUMENT = """
				query {
					getTransaction(id: %d) {
						... on JarTransferTransaction {
							targetJar {
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

			final Jar jar = JAR.bdo();
			when(jarService.get(JAR.getId())).thenReturn(jar);

			final Response response = graphQlTester.document(DOCUMENT)
					.execute();

			final String prefix = "getTransaction.targetJar";
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

			graphQlTester.document(DOCUMENT)
					.execute()
					.errors()
					.expect(error -> {
						assertThat(error.getErrorType())
								.isEqualTo(ErrorType.NOT_FOUND);
						assertThat(error.getPath())
								.isEqualTo("getTransaction.targetJar");
						return true;
					});
		}
	}
}
