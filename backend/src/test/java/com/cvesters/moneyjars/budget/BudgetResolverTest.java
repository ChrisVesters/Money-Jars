package com.cvesters.moneyjars.budget;

import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.GraphQlTester.Response;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cvesters.moneyjars.budget.bdo.Budget;
import com.cvesters.moneyjars.budget.bdo.BudgetLine;

@GraphQlTest({ BudgetController.class, BudgetResolver.class })
class BudgetResolverTest {

	@Autowired
	private GraphQlTester graphQlTester;

	@MockitoBean
	private BudgetService budgetService;

	@MockitoBean
	private BudgetLineService budgetLineService;

	@Nested
	class ResolveLines {

		private static final TestBudget BUDGET = TestBudget.NOVEMBER_2026;
		private static final TestBudgetLine RENT = TestBudgetLine.NOVEMBER_RENT;
		private static final TestBudgetLine GROCERIES = TestBudgetLine.NOVEMBER_GROCERIES;

		private static final String DOCUMENT = """
				query {
					getBudget(id: %d) {
						lines {
							id
							amount
							direction
						}
					}
				}
				""";

		@Test
		void success() {
			final Budget budget = BUDGET.bdo();
			when(budgetService.get(BUDGET.getId())).thenReturn(budget);

			final List<BudgetLine> lines = Stream.of(RENT, GROCERIES)
					.map(TestBudgetLine::bdo)
					.toList();
			when(budgetLineService.getAllByBudget(BUDGET.getId()))
					.thenReturn(lines);

			final Response response = graphQlTester
					.document(DOCUMENT.formatted(BUDGET.getId()))
					.execute();

			assertEquals(response, RENT, "getBudget.lines[0]");
			assertEquals(response, GROCERIES, "getBudget.lines[1]");
		}

		@Test
		void empty() {
			final Budget budget = BUDGET.bdo();
			when(budgetService.get(BUDGET.getId())).thenReturn(budget);
			when(budgetLineService.getAllByBudget(BUDGET.getId()))
					.thenReturn(List.of());

			graphQlTester.document(DOCUMENT.formatted(BUDGET.getId()))
					.execute()
					.path("getBudget.lines")
					.entityList(Object.class)
					.hasSize(0);
		}
	}

	void assertEquals(final Response response, final TestBudgetLine expected,
			final String prefix) {
		response.path(prefix + ".id")
				.entity(Long.class)
				.isEqualTo(expected.getId())
				.path(prefix + ".amount")
				.entity(String.class)
				.isEqualTo(expected.getAmount().toPlainString())
				.path(prefix + ".direction")
				.entity(String.class)
				.isEqualTo(expected.getDirection().name());
	}
}
