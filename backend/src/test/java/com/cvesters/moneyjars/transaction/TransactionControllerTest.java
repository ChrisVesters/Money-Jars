package com.cvesters.moneyjars.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.ResponseError;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.GraphQlTester.Response;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cvesters.moneyjars.common.bdo.PagedResult;
import com.cvesters.moneyjars.common.bdo.Pagination;
import com.cvesters.moneyjars.common.exceptions.MissingEntityException;
import com.cvesters.moneyjars.transaction.bdo.Transaction;
import com.cvesters.moneyjars.transaction.bdo.TransactionFilter;
import com.cvesters.moneyjars.transaction.bdo.TransactionQuery;

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
	class QueryTransactions {

		@Test
		void success() {
			final var query = new TransactionQuery(
					new TransactionFilter(LocalDate.of(2026, 1, 1),
							LocalDate.of(2026, 1, 31)),
					new Pagination(1, 2));
			final List<Transaction> transactions = Stream
					.of(TestPaymentTransaction.RENT,
							TestPaymentTransaction.GROCERY)
					.map(TestPaymentTransaction::bdo)
					.map(Transaction.class::cast)
					.toList();

			when(transactionService.query(query))
					.thenReturn(new PagedResult<>(transactions, 1, 2, 5));

			final String document = """
					query {
						queryTransactions(query: {
							filter: { from: "2026-01-01", to: "2026-01-31" }
							page: 1
							size: 2
						}) {
							content {
								id
								date
								amount
								description
								... on PaymentTransaction {
									counterparty
								}
							}
							page
							size
							totalElements
						}
					}
					""";

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, TestPaymentTransaction.RENT,
					"queryTransactions.content[0]");
			assertEquals(response, TestPaymentTransaction.GROCERY,
					"queryTransactions.content[1]");
			response.path("queryTransactions.content")
					.entityList(Object.class)
					.hasSize(2)
					.path("queryTransactions.page")
					.entity(Integer.class)
					.isEqualTo(1)
					.path("queryTransactions.size")
					.entity(Integer.class)
					.isEqualTo(2)
					.path("queryTransactions.totalElements")
					.entity(Long.class)
					.isEqualTo(5L);
		}

		@Test
		void defaults() {
			final var query = new TransactionQuery(
					new TransactionFilter(null, null), new Pagination(0, 100));

			when(transactionService.query(query)).thenReturn(
					new PagedResult<>(Collections.emptyList(), 0, 100, 0));

			final String document = """
					query {
						queryTransactions(query: { filter: {} }) {
							content {
								id
							}
							page
							size
							totalElements
						}
					}
					""";

			graphQlTester.document(document)
					.execute()
					.path("queryTransactions.content")
					.entityList(Object.class)
					.hasSize(0)
					.path("queryTransactions.page")
					.entity(Integer.class)
					.isEqualTo(0)
					.path("queryTransactions.size")
					.entity(Integer.class)
					.isEqualTo(100)
					.path("queryTransactions.totalElements")
					.entity(Long.class)
					.isEqualTo(0L);
		}

		@Test
		void filterMissing() {
			final String document = """
					query {
						queryTransactions(query: { page: 0, size: 10 }) {
							content {
								id
							}
							page
							size
							totalElements
						}
					}
					""";

			graphQlTester.document(document)
					.execute()
					.errors()
					.satisfy(errors -> assertThat(errors).singleElement()
							.extracting(ResponseError::getErrorType)
							.isEqualTo(graphql.ErrorType.ValidationError));

			verifyNoInteractions(transactionService);
		}
	}

	@Nested
	class GetTransaction {

		private static final TestPaymentTransaction TRANSACTION = TestPaymentTransaction.RENT;

		@Test
		void success() {
			final Transaction transaction = TRANSACTION.bdo();

			when(transactionService.get(TRANSACTION.getId()))
					.thenReturn(transaction);

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
			when(transactionService.get(TRANSACTION.getId()))
					.thenThrow(MissingEntityException.class);

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
					.errors()
					.expect(error -> {
						assertThat(error.getErrorType())
								.isEqualTo(ErrorType.NOT_FOUND);
						assertThat(error.getPath()).isEqualTo("getTransaction");
						return true;
					});
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
