package com.cvesters.moneyjars.budget;

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

import com.cvesters.moneyjars.budget.bdo.BudgetLine;
import com.cvesters.moneyjars.budget.bdo.BudgetLineAction;

@GraphQlTest(BudgetLineController.class)
class BudgetLineControllerTest {

	@Autowired
	private GraphQlTester graphQlTester;

	@MockitoBean
	private BudgetLineService budgetLineService;

	@Nested
	class CreateBudgetLine {

		private static final TestBudgetLine LINE = TestBudgetLine.NOVEMBER_RENT;

		@Test
		void success() {
			final var action = new BudgetLineAction.Create(
					LINE.getBudget().getId(), LINE.getCategory().getId(),
					LINE.getAmount(), LINE.getDirection());
			final BudgetLine created = LINE.bdo();
			when(budgetLineService.create(action)).thenReturn(created);

			final String document = """
					mutation {
						createBudgetLine(req: {
							budgetId: %d,
							categoryId: %d,
							amount: "%s",
							direction: %s
						}) {
							id
							amount
							direction
						}
					}
					""".formatted(LINE.getBudget().getId(),
					LINE.getCategory().getId(),
					LINE.getAmount().toPlainString(),
					LINE.getDirection().name());

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, LINE, "createBudgetLine");
		}
	}

	@Nested
	class UpdateBudgetLine {

		private static final TestBudgetLine LINE = TestBudgetLine.DECEMBER_FUEL;

		@Test
		void success() {
			final var action = new BudgetLineAction.Update(LINE.getAmount(),
					LINE.getDirection());
			final BudgetLine updated = LINE.bdo();
			when(budgetLineService.update(eq(LINE.getId()), eq(action)))
					.thenReturn(updated);

			final String document = """
					mutation {
						updateBudgetLine(id: %d, req: {
							amount: "%s",
							direction: %s
						}) {
							id
							amount
							direction
						}
					}
					""".formatted(LINE.getId(),
					LINE.getAmount().toPlainString(),
					LINE.getDirection().name());

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, LINE, "updateBudgetLine");
		}
	}

	@Nested
	class DeleteBudgetLine {

		private static final TestBudgetLine LINE = TestBudgetLine.NOVEMBER_RENT;

		@Test
		void success() {
			final String document = """
					mutation {
						deleteBudgetLine(id: %d)
					}
					""".formatted(LINE.getId());

			final Response response = graphQlTester.document(document)
					.execute();

			response.path("deleteBudgetLine")
					.entity(Boolean.class)
					.isEqualTo(true);

			verify(budgetLineService).delete(LINE.getId());
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
