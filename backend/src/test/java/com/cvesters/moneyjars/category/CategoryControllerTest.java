package com.cvesters.moneyjars.category;

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

import com.cvesters.moneyjars.category.bdo.Category;
import com.cvesters.moneyjars.category.bdo.CategoryAction;
import com.cvesters.moneyjars.common.exceptions.MissingEntityException;
import com.cvesters.moneyjars.config.GraphqlExceptionResolver;

@GraphQlTest(CategoryController.class)
@Import(GraphqlExceptionResolver.class)
class CategoryControllerTest {

	@Autowired
	private GraphQlTester graphQlTester;

	@MockitoBean
	private CategoryService categoryService;

	@Nested
	class GetCategories {

		private static final TestCategory GROCERIES = TestCategory.GROCERIES;
		private static final TestCategory RENT = TestCategory.RENT;

		@Test
		void success() {
			final List<Category> categories = Stream.of(GROCERIES, RENT)
					.map(TestCategory::bdo)
					.toList();

			when(categoryService.getAll()).thenReturn(categories);

			final String document = """
					query {
						getCategorys {
							id
							name
							description
						}
					}
					""";

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, GROCERIES, "getCategorys[0]");
			assertEquals(response, RENT, "getCategorys[1]");
		}

	}

	@Nested
	class GetCategory {

		private static final TestCategory GROCERIES = TestCategory.GROCERIES;

		@Test
		void success() {
			final Category category = GROCERIES.bdo();

			when(categoryService.get(GROCERIES.getId())).thenReturn(category);

			final String document = """
					query {
						getCategory(id: 1) {
							id
							name
							description
						}
					}
					""";

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, GROCERIES, "getCategory");
		}

		@Test
		void notFound() {
			when(categoryService.get(GROCERIES.getId()))
					.thenThrow(MissingEntityException.class);

			final String document = """
					query {
						getCategory(id: 1) {
							id
							name
							description
						}
					}
					""";

			graphQlTester.document(document)
					.execute()
					.errors()
					.expect(error -> {
						assertThat(error.getErrorType())
								.isEqualTo(ErrorType.NOT_FOUND);
						assertThat(error.getPath()).isEqualTo("getCategory");
						return true;
					});
		}

	}

	@Nested
	class CreateCategory {

		private static final TestCategory RENT = TestCategory.RENT;

		@Test
		void success() {
			final var action = new CategoryAction.Create(RENT.getName(),
					RENT.getDescription());
			final Category createdCategory = RENT.bdo();
			when(categoryService.create(action)).thenReturn(createdCategory);

			final String document = """
					mutation {
						createCategory(req: {
							name: "%s",
							description: "%s"
						}) {
							id
							name
							description
						}
					}
					""".formatted(RENT.getName(), RENT.getDescription());

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, RENT, "createCategory");
		}

	}

	@Nested
	class UpdateCategory {

		private static final TestCategory RENT = TestCategory.RENT;

		@Test
		void success() {
			final var action = new CategoryAction.Update(RENT.getName(),
					RENT.getDescription());
			final Category updatedCategory = RENT.bdo();
			when(categoryService.update(eq(RENT.getId()), eq(action)))
					.thenReturn(updatedCategory);

			final String document = """
					mutation {
						updateCategory(id: %d, req: {
							name: "%s",
							description: "%s"
						}) {
							id
							name
							description
						}
					}
					""".formatted(RENT.getId(), RENT.getName(),
					RENT.getDescription());

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, RENT, "updateCategory");
		}
	}

	@Nested
	class DeleteCategory {

		@Test
		void success() {
			final String document = """
					mutation {
						deleteCategory(id: 1)
					}
					""";

			final Response response = graphQlTester.document(document)
					.execute();

			response.path("deleteCategory")
					.entity(Boolean.class)
					.isEqualTo(true);

			verify(categoryService).delete(1L);
		}
	}

	void assertEquals(final Response response, final TestCategory expected,
			final String prefix) {
		response.path(prefix + ".id")
				.entity(Long.class)
				.isEqualTo(expected.getId())
				.path(prefix + ".name")
				.entity(String.class)
				.isEqualTo(expected.getName())
				.path(prefix + ".description")
				.entity(String.class)
				.isEqualTo(expected.getDescription());
	}

}
