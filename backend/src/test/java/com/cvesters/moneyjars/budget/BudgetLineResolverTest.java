package com.cvesters.moneyjars.budget;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.GraphQlTester.Response;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cvesters.moneyjars.budget.bdo.BudgetLine;
import com.cvesters.moneyjars.budget.bdo.BudgetLineAction;
import com.cvesters.moneyjars.category.CategoryService;
import com.cvesters.moneyjars.category.TestCategory;
import com.cvesters.moneyjars.category.bdo.Category;
import com.cvesters.moneyjars.common.exceptions.MissingEntityException;
import com.cvesters.moneyjars.config.GraphqlExceptionResolver;

@GraphQlTest({ BudgetLineController.class, BudgetLineResolver.class })
@Import(GraphqlExceptionResolver.class)
class BudgetLineResolverTest {

	private static final TestBudgetLine LINE = TestBudgetLine.NOVEMBER_RENT;

	@Autowired
	private GraphQlTester graphQlTester;

	@MockitoBean
	private BudgetLineService budgetLineService;

	@MockitoBean
	private CategoryService categoryService;

	@BeforeEach
	void setUp() {
		final var action = new BudgetLineAction.Update(LINE.getAmount(),
				LINE.getDirection());
		final BudgetLine line = LINE.bdo();
		when(budgetLineService.update(LINE.getId(), action)).thenReturn(line);
	}

	private String document(final String selection) {
		return """
				mutation {
					updateBudgetLine(id: %d, req: {
						amount: "%s",
						direction: %s
					}) {
						%s
					}
				}
				""".formatted(LINE.getId(), LINE.getAmount().toPlainString(),
				LINE.getDirection().name(), selection);
	}

	@Nested
	class ResolveCategory {

		private static final TestCategory CATEGORY = LINE.getCategory();

		private static final String SELECTION = """
				category {
					id
					name
					description
				}
				""";

		@Test
		void success() {
			final Category category = CATEGORY.bdo();
			when(categoryService.get(CATEGORY.getId())).thenReturn(category);

			final Response response = graphQlTester
					.document(document(SELECTION))
					.execute();

			final String prefix = "updateBudgetLine.category";
			response.path(prefix + ".id")
					.entity(Long.class)
					.isEqualTo(CATEGORY.getId())
					.path(prefix + ".name")
					.entity(String.class)
					.isEqualTo(CATEGORY.getName())
					.path(prefix + ".description")
					.entity(String.class)
					.isEqualTo(CATEGORY.getDescription());
		}

		@Test
		void notFound() {
			when(categoryService.get(CATEGORY.getId()))
					.thenThrow(MissingEntityException.class);

			graphQlTester.document(document(SELECTION))
					.execute()
					.errors()
					.expect(error -> {
						assertThat(error.getErrorType())
								.isEqualTo(ErrorType.NOT_FOUND);
						assertThat(error.getPath())
								.isEqualTo("updateBudgetLine.category");
						return true;
					});
		}
	}
}
