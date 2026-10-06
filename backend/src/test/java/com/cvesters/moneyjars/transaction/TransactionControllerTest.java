package com.cvesters.moneyjars.transaction;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.GraphQlTester.Response;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cvesters.moneyjars.transaction.bdo.Transaction;

@GraphQlTest(TransactionController.class)
@Import(TransactionGraphqlConfig.class)
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
					.of(TestPaymentTransaction.RENT,
							TestPaymentTransaction.GROCERY)
					.map(TestPaymentTransaction::bdo)
					.map(Transaction.class::cast)
					.toList();

			when(transactionService.getAll()).thenReturn(transactions);

			final String document = """
					query {
						getTransactions {
							id
							date
							amount
							description
							... on PaymentTransaction {
								counterparty
							}
						}
					}
					""";

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, TestPaymentTransaction.RENT,
					"getTransactions[0]");
			assertEquals(response, TestPaymentTransaction.GROCERY,
					"getTransactions[1]");
		}
	}

	@Nested
	class GetTransaction {

		private static final TestPaymentTransaction TRANSACTION = TestPaymentTransaction.RENT;

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
							description
							... on PaymentTransaction {
								counterparty
							}
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
							description
							... on PaymentTransaction {
								counterparty
							}
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
