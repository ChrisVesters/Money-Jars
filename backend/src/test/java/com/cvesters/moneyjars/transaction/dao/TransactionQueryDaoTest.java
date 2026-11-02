package com.cvesters.moneyjars.transaction.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.core.TypedPropertyPath;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.PredicateSpecification;
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
	class ReferencedBy {

		private final Root<TransactionDao> root = mock();
		private final CriteriaQuery<?> query = mock();
		private final CriteriaBuilder builder = mock();
		private final Subquery<Long> subquery = mock();
		private final Root<ReferencingDao> reference = mock();
		private final TypedPropertyPath<ReferencingDao, Long> transactionId = mock();
		private final PredicateSpecification<ReferencingDao> filter = mock();
		private final Path<Long> idPath = mock();
		private final Path<Long> referenceTransactionIdPath = mock();
		private final Predicate referencesTransaction = mock();
		private final Predicate exists = mock();

		@BeforeEach
		void setUp() {
			when(query.subquery(Long.class)).thenReturn(subquery);
			when(subquery.from(ReferencingDao.class)).thenReturn(reference);
			when(transactionId.toDotPath()).thenReturn("transactionId");
			when(root.<Long>get("id")).thenReturn(idPath);
			when(reference.<Long>get("transactionId"))
					.thenReturn(referenceTransactionIdPath);
			when(builder.equal(referenceTransactionIdPath, idPath))
					.thenReturn(referencesTransaction);
			when(builder.exists(subquery)).thenReturn(exists);
		}

		@Test
		void filtered() {
			final Predicate filterPredicate = mock();
			when(filter.toPredicate(reference, builder))
					.thenReturn(filterPredicate);
			final Predicate combined = mock();
			when(builder.and(referencesTransaction, filterPredicate))
					.thenReturn(combined);

			final Specification<TransactionDao> result = TransactionQueryDao
					.referencedBy(ReferencingDao.class, transactionId, filter);

			final Predicate predicate = result.toPredicate(root, query,
					builder);
			assertThat(predicate).isSameAs(exists);
			verify(subquery).select(referenceTransactionIdPath);
			verify(subquery).where(combined);
		}

		@Test
		void unrestricted() {
			when(filter.toPredicate(reference, builder)).thenReturn(null);

			final Specification<TransactionDao> result = TransactionQueryDao
					.referencedBy(ReferencingDao.class, transactionId, filter);

			final Predicate predicate = result.toPredicate(root, query,
					builder);
			assertThat(predicate).isSameAs(exists);
			verify(subquery).select(referenceTransactionIdPath);
			verify(subquery).where(referencesTransaction);
		}

		@Test
		void filterNull() {
			final Specification<TransactionDao> result = TransactionQueryDao
					.referencedBy(ReferencingDao.class, transactionId, null);

			final Predicate predicate = result.toPredicate(root, query,
					builder);
			assertThat(predicate).isSameAs(exists);
			verify(subquery).select(referenceTransactionIdPath);
			verify(subquery).where(referencesTransaction);
		}

		@Test
		void typeNull() {
			assertThatThrownBy(() -> TransactionQueryDao
					.referencedBy(null, transactionId, filter))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void transactionIdNull() {
			assertThatThrownBy(() -> TransactionQueryDao
					.referencedBy(ReferencingDao.class, null, filter))
					.isInstanceOf(NullPointerException.class);
		}

		private static final class ReferencingDao {
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
