package com.cvesters.moneyjars.transaction.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import com.cvesters.moneyjars.category.TestCategory;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionFilter;

class PaymentTransactionQueryDaoTest {

	@Nested
	class ToSpecification {

		private static final long CATEGORY_ID = TestCategory.GROCERIES
				.getId();

		private final Root<PaymentTransactionDao> root = mock();
		private final CriteriaQuery<?> query = mock();
		private final CriteriaBuilder builder = mock();
		private final Path<Long> categoryPath = mock();

		@Test
		void unrestricted() {
			final var filter = new PaymentTransactionFilter(null);

			final Specification<PaymentTransactionDao> result = PaymentTransactionQueryDao
					.toSpecification(filter);

			final Predicate predicate = result.toPredicate(root, query,
					builder);
			assertThat(predicate).isNull();
			verifyNoInteractions(builder);
		}

		@Test
		void category() {
			final var filter = new PaymentTransactionFilter(CATEGORY_ID);

			final Specification<PaymentTransactionDao> result = PaymentTransactionQueryDao
					.toSpecification(filter);

			when(root.<Long>get("categoryId")).thenReturn(categoryPath);
			final Predicate categoryPredicate = mock();
			when(builder.equal(categoryPath, CATEGORY_ID))
					.thenReturn(categoryPredicate);

			final Predicate predicate = result.toPredicate(root, query,
					builder);
			assertThat(predicate).isSameAs(categoryPredicate);
		}
	}
}
