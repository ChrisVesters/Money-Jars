package com.cvesters.moneyjars.transaction.dao;

import org.springframework.data.core.TypedPropertyPath;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.data.jpa.domain.Specification;

import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionFilter;

// TODO: extends TransactionQueryDao?
public final class PaymentTransactionQueryDao {

	private static final TypedPropertyPath<PaymentTransactionDao, Long> CATEGORY = TypedPropertyPath
			.of(PaymentTransactionDao::getCategoryId);

	private PaymentTransactionQueryDao() {
	}

	public static Specification<PaymentTransactionDao> toSpecification(
			final PaymentTransactionFilter filter) {
		return Specification.where(hasCategory(filter.categoryId()));
	}

	private static PredicateSpecification<PaymentTransactionDao> hasCategory(
			final Long categoryId) {
		if (categoryId == null) {
			return PredicateSpecification.unrestricted();
		}
		return (root, builder) -> builder.equal(
				root.get(CATEGORY.toDotPath()), categoryId);
	}
}
