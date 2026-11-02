package com.cvesters.moneyjars.transaction.dao;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

import org.springframework.data.core.TypedPropertyPath;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.data.jpa.domain.Specification;

import com.cvesters.moneyjars.common.bdo.Pagination;
import com.cvesters.moneyjars.transaction.bdo.TransactionFilter;

public final class TransactionQueryDao {

	private static final TypedPropertyPath<TransactionDao, Long> ID = TypedPropertyPath
			.of(TransactionDao::getId);
	private static final TypedPropertyPath<TransactionDao, LocalDate> DATE = TypedPropertyPath
			.of(TransactionDao::getDate);
	private static final TypedPropertyPath<TransactionDao, Integer> SEQUENCE = TypedPropertyPath
			.of(TransactionDao::getSequence);

	private TransactionQueryDao() {
	}

	public static Specification<TransactionDao> toSpecification(
			final TransactionFilter filter) {
		return Specification.where(hasDateFrom(filter.from()))
				.and(hasDateTo(filter.to()));
	}

	private static PredicateSpecification<TransactionDao> hasDateFrom(
			final LocalDate from) {
		if (from == null) {
			return PredicateSpecification.unrestricted();
		}
		return (root, builder) -> builder
				.greaterThanOrEqualTo(root.get(DATE.toDotPath()), from);
	}

	private static PredicateSpecification<TransactionDao> hasDateTo(
			final LocalDate to) {
		if (to == null) {
			return PredicateSpecification.unrestricted();
		}
		return (root, builder) -> builder
				.lessThanOrEqualTo(root.get(DATE.toDotPath()), to);
	}

	public static <E> Specification<TransactionDao> referencedBy(
			final Class<E> type, final TypedPropertyPath<E, Long> transactionId,
			final PredicateSpecification<E> filter) {
		Objects.requireNonNull(type);
		Objects.requireNonNull(transactionId);

		return (root, query, builder) -> {
			final Subquery<Long> subquery = query.subquery(Long.class);
			final Root<E> reference = subquery.from(type);
			final Path<Long> transactionIdPath = reference
					.get(transactionId.toDotPath());

			final Predicate referencesTransaction = builder
					.equal(transactionIdPath, root.get(ID.toDotPath()));
			final Predicate restriction = Optional.ofNullable(filter)
					.map(f -> f.toPredicate(reference, builder))
					.map(p -> builder.and(referencesTransaction, p))
					.orElse(referencesTransaction);

			subquery.select(transactionIdPath);
			subquery.where(restriction);
			return builder.exists(subquery);
		};
	}

	public static Pageable toPageable(final Pagination pagination) {
		final Sort sort = Sort.by(Sort.Order.desc(DATE),
				Sort.Order.desc(SEQUENCE));
		return PageRequest.of(pagination.page(), pagination.size(), sort);
	}
}
