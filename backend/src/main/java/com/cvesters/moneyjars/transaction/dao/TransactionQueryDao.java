package com.cvesters.moneyjars.transaction.dao;

import java.time.LocalDate;

import org.springframework.data.core.TypedPropertyPath;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.data.jpa.domain.Specification;

import com.cvesters.moneyjars.common.bdo.Pagination;
import com.cvesters.moneyjars.transaction.bdo.TransactionFilter;

public final class TransactionQueryDao {

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

	public static Pageable toPageable(final Pagination pagination) {
		final Sort sort = Sort.by(Sort.Order.desc(DATE),
				Sort.Order.desc(SEQUENCE));
		return PageRequest.of(pagination.page(), pagination.size(), sort);
	}
}
