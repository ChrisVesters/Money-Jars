package com.cvesters.moneyjars.budget;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.GraphQlTester.Response;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cvesters.moneyjars.budget.bdo.Budget;
import com.cvesters.moneyjars.budget.bdo.BudgetAction;
import com.cvesters.moneyjars.common.exceptions.MissingEntityException;
import com.cvesters.moneyjars.config.GraphqlExceptionResolver;

@GraphQlTest(BudgetController.class)
@Import(GraphqlExceptionResolver.class)
class BudgetControllerTest {

	@Autowired
	private GraphQlTester graphQlTester;

	@MockitoBean
	private BudgetService budgetService;

	@Nested
	class GetBudgets {

		private static final TestBudget NOVEMBER = TestBudget.NOVEMBER_2026;
		private static final TestBudget DECEMBER = TestBudget.DECEMBER_2026;

		@Test
		void success() {
			final List<Budget> budgets = Stream.of(NOVEMBER, DECEMBER)
					.map(TestBudget::bdo)
					.toList();

			when(budgetService.getAll()).thenReturn(budgets);

			final String document = """
					query {
						getBudgets {
							id
							name
							description
							startDate
							endDate
						}
					}
					""";

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, NOVEMBER, "getBudgets[0]");
			assertEquals(response, DECEMBER, "getBudgets[1]");
		}
	}

	@Nested
	class GetBudget {

		private static final TestBudget BUDGET = TestBudget.NOVEMBER_2026;

		private static final String DOCUMENT = """
				query {
					getBudget(id: %d) {
						id
						name
						description
						startDate
						endDate
					}
				}
				""";

		@Test
		void success() {
			final Budget budget = BUDGET.bdo();
			when(budgetService.get(BUDGET.getId())).thenReturn(budget);

			final Response response = graphQlTester
					.document(DOCUMENT.formatted(BUDGET.getId()))
					.execute();

			assertEquals(response, BUDGET, "getBudget");
		}

		@Test
		void notFound() {
			when(budgetService.get(BUDGET.getId()))
					.thenThrow(MissingEntityException.class);

			graphQlTester.document(DOCUMENT.formatted(BUDGET.getId()))
					.execute()
					.errors()
					.expect(error -> {
						assertThat(error.getErrorType())
								.isEqualTo(ErrorType.NOT_FOUND);
						assertThat(error.getPath()).isEqualTo("getBudget");
						return true;
					});
		}
	}

	@Nested
	class CreateBudget {

		private static final TestBudget BUDGET = TestBudget.DECEMBER_2026;

		@Test
		void success() {
			final var action = new BudgetAction.Create(BUDGET.getName(),
					BUDGET.getDescription(), BUDGET.getStartDate(),
					BUDGET.getEndDate());
			final Budget createdBudget = BUDGET.bdo();
			when(budgetService.create(action)).thenReturn(createdBudget);

			final String document = """
					mutation {
						createBudget(req: {
							name: "%s",
							description: "%s",
							startDate: "%s",
							endDate: "%s"
						}) {
							id
							name
							description
							startDate
							endDate
						}
					}
					""".formatted(BUDGET.getName(), BUDGET.getDescription(),
					BUDGET.getStartDate(), BUDGET.getEndDate());

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, BUDGET, "createBudget");
		}
	}

	@Nested
	class UpdateBudget {

		private static final TestBudget BUDGET = TestBudget.DECEMBER_2026;

		@Test
		void success() {
			final var action = new BudgetAction.Update(BUDGET.getName(),
					BUDGET.getDescription(), BUDGET.getStartDate(),
					BUDGET.getEndDate());
			final Budget updatedBudget = BUDGET.bdo();
			when(budgetService.update(eq(BUDGET.getId()), eq(action)))
					.thenReturn(updatedBudget);

			final String document = """
					mutation {
						updateBudget(id: %d, req: {
							name: "%s",
							description: "%s",
							startDate: "%s",
							endDate: "%s"
						}) {
							id
							name
							description
							startDate
							endDate
						}
					}
					""".formatted(BUDGET.getId(), BUDGET.getName(),
					BUDGET.getDescription(), BUDGET.getStartDate(),
					BUDGET.getEndDate());

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, BUDGET, "updateBudget");
		}
	}

	@Nested
	class DeleteBudget {

		private static final TestBudget BUDGET = TestBudget.NOVEMBER_2026;

		@Test
		void success() {
			final String document = """
					mutation {
						deleteBudget(id: %d)
					}
					""".formatted(BUDGET.getId());

			final Response response = graphQlTester.document(document)
					.execute();

			response.path("deleteBudget").entity(Boolean.class).isEqualTo(true);

			verify(budgetService).delete(BUDGET.getId());
		}
	}

	void assertEquals(final Response response, final TestBudget expected,
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
				.path(prefix + ".startDate")
				.entity(String.class)
				.isEqualTo(expected.getStartDate().toString())
				.path(prefix + ".endDate")
				.entity(String.class)
				.isEqualTo(expected.getEndDate().toString());
	}

}
