package com.cvesters.moneyjars.category;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cvesters.moneyjars.transaction.PaymentTransactionService;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionFilter;

@GraphQlTest({ CategoryController.class, CategoryResolver.class })
class CategoryResolverTest {

	@Autowired
	private GraphQlTester graphQlTester;

	@MockitoBean
	private CategoryService categoryService;

	@MockitoBean
	private PaymentTransactionService paymentTransactionService;

	@Nested
	class ResolveInUse {

		private static final TestCategory GROCERIES = TestCategory.GROCERIES;
		private static final PaymentTransactionFilter FILTER = new PaymentTransactionFilter(
				GROCERIES.getId());

		private static final String DOCUMENT = """
				query {
					getCategory(id: %d) {
						inUse
					}
				}
				""".formatted(GROCERIES.getId());

		@Test
		void used() {
			when(categoryService.get(GROCERIES.getId()))
					.thenReturn(GROCERIES.bdo());
			when(paymentTransactionService.count(FILTER)).thenReturn(2L);

			graphQlTester.document(DOCUMENT)
					.execute()
					.path("getCategory.inUse")
					.entity(Boolean.class)
					.isEqualTo(true);
		}

		@Test
		void unused() {
			when(categoryService.get(GROCERIES.getId()))
					.thenReturn(GROCERIES.bdo());
			when(paymentTransactionService.count(FILTER)).thenReturn(0L);

			graphQlTester.document(DOCUMENT)
					.execute()
					.path("getCategory.inUse")
					.entity(Boolean.class)
					.isEqualTo(false);
		}
	}
}
