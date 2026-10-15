package com.cvesters.moneyjars.transaction.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.cvesters.moneyjars.common.bdo.Pagination;
import com.cvesters.moneyjars.transaction.bdo.TransactionFilter;

class TransactionQueryDaoTest {

	@Nested
	class ToSpecification {

		private static final LocalDate FROM = LocalDate.of(2026, 1, 10);
		private static final LocalDate TO = LocalDate.of(2026, 1, 15);

		private final Root<TransactionDao> root = mock();
		private final CriteriaQuery<?> query = mock();
		private final CriteriaBuilder builder = mock();
		private final Path<LocalDate> datePath = mock();

		@Test
		void unrestricted() {
			final var filter = new TransactionFilter(null, null);

			final Specification<TransactionDao> result = TransactionQueryDao
					.toSpecification(filter);

			final Predicate predicate = result.toPredicate(root, query,
					builder);
			assertThat(predicate).isNull();
			verifyNoInteractions(builder);
		}

		@Test
		void from() {
			final var filter = new TransactionFilter(FROM, null);

			final Specification<TransactionDao> result = TransactionQueryDao
					.toSpecification(filter);

			when(root.<LocalDate>get("date")).thenReturn(datePath);
			final Predicate fromPredicate = mock();
			when(builder.greaterThanOrEqualTo(datePath, FROM))
					.thenReturn(fromPredicate);

			final Predicate predicate = result.toPredicate(root, query,
					builder);
			assertThat(predicate).isSameAs(fromPredicate);
		}

		@Test
		void to() {
			final var filter = new TransactionFilter(null, TO);

			final Specification<TransactionDao> result = TransactionQueryDao
					.toSpecification(filter);

			when(root.<LocalDate>get("date")).thenReturn(datePath);
			final Predicate toPredicate = mock();
			when(builder.lessThanOrEqualTo(datePath, TO))
					.thenReturn(toPredicate);

			final Predicate predicate = result.toPredicate(root, query,
					builder);
			assertThat(predicate).isSameAs(toPredicate);
		}

		@Test
		void fromAndTo() {
			final var filter = new TransactionFilter(FROM, TO);

			final Specification<TransactionDao> result = TransactionQueryDao
					.toSpecification(filter);

			when(root.<LocalDate>get("date")).thenReturn(datePath);
			final Predicate fromPredicate = mock();
			when(builder.greaterThanOrEqualTo(datePath, FROM))
					.thenReturn(fromPredicate);
			final Predicate toPredicate = mock();
			when(builder.lessThanOrEqualTo(datePath, TO))
					.thenReturn(toPredicate);
			final Predicate combined = mock();
			when(builder.and(fromPredicate, toPredicate)).thenReturn(combined);

			final Predicate predicate = result.toPredicate(root, query,
					builder);
			assertThat(predicate).isSameAs(combined);
		}
	}

	@Nested
	class ToPageable {

		private static final Sort SORT = Sort.by(Sort.Order.desc("date"),
				Sort.Order.desc("sequence"));

		@Test
		void firstPage() {
			final var pagination = new Pagination(0, 20);

			final Pageable result = TransactionQueryDao.toPageable(pagination);

			assertThat(result).isEqualTo(PageRequest.of(0, 20, SORT));
		}

		@Test
		void otherPage() {
			final var pagination = new Pagination(3, 5);

			final Pageable result = TransactionQueryDao.toPageable(pagination);

			assertThat(result).isEqualTo(PageRequest.of(3, 5, SORT));
		}
	}
}
