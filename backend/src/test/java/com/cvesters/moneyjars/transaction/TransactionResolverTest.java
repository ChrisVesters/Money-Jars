package com.cvesters.moneyjars.transaction;

import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.GraphQlTester.Response;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cvesters.moneyjars.jar.JarService;
import com.cvesters.moneyjars.jar.TestJar;
import com.cvesters.moneyjars.jar.bdo.Jar;
import com.cvesters.moneyjars.transaction.bdo.Transaction;

@GraphQlTest({ TransactionController.class, TransactionResolver.class })
class TransactionResolverTest {

	@Autowired
	private GraphQlTester graphQlTester;

	@MockitoBean
	private JarService jarService;

	@MockitoBean
	private TransactionService transactionService;

	@Nested
	class ResolveJar {

		private static final TestTransaction TRANSACTION = TestTransaction.RENT;
		private static final TestJar JAR = TRANSACTION.getJar();

		@Test
		void success() {
			final Transaction transaction = TRANSACTION.bdo();
			when(transactionService.find(TRANSACTION.getId()))
					.thenReturn(Optional.of(transaction));

			final Jar jar = JAR.bdo();
			when(jarService.find(JAR.getId())).thenReturn(Optional.of(jar));

			final String document = """
					query {
						getTransaction(id: %d) {
							jar {
								id
								name
								description
								balance
							}
						}
					}
					""".formatted(TRANSACTION.getId());

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, JAR, "getTransaction.jar");
		}

		@Test
		@Disabled("Currently not possible!")
		void notFound() {
			final Transaction transaction = TRANSACTION.bdo();
			when(transactionService.find(TRANSACTION.getId()))
					.thenReturn(Optional.of(transaction));

			when(jarService.find(JAR.getId())).thenReturn(Optional.empty());

			final String document = """
					query {
						getTransaction(id: %d) {
							jar {
								id
								name
								description
								balance
							}
						}
					}
					""".formatted(TRANSACTION.getId());

			graphQlTester.document(document)
					.execute()
					.path("getTransaction.jar")
					.valueIsNull();
		}
	}

	void assertEquals(final Response response, final TestJar expected,
			final String prefix) {
		response.path(prefix + ".id")
				.entity(Long.class)
				.isEqualTo(expected.getId())
				.path(prefix + ".name")
				.entity(String.class)
				.isEqualTo(expected.getName())
				.path(prefix + ".description")
				.entity(String.class)
				.isEqualTo(expected.getDescription())
				.path(prefix + ".balance")
				.entity(Float.class)
				.isEqualTo(expected.getBalance().floatValue());
	}
}