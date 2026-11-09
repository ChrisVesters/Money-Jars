package com.cvesters.moneyjars.transaction.bdo;

import java.math.BigDecimal;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

public record PaymentTransactionCategoryTotal(Long categoryId,
		BigDecimal incoming, BigDecimal outgoing) {

	public PaymentTransactionCategoryTotal {
		Objects.requireNonNull(incoming);
		Validate.isTrue(incoming.signum() != -1);
		Objects.requireNonNull(outgoing);
		Validate.isTrue(outgoing.signum() != -1);
	}

	public static PaymentTransactionCategoryTotal of(final Long categoryId,
			final PaymentTransactionDirection direction,
			final BigDecimal amount) {
		Objects.requireNonNull(direction);

		return switch (direction) {
			case INCOMING -> new PaymentTransactionCategoryTotal(categoryId,
					amount, BigDecimal.ZERO);
			case OUTGOING -> new PaymentTransactionCategoryTotal(categoryId,
					BigDecimal.ZERO, amount);
		};
	}

	public PaymentTransactionCategoryTotal add(
			final PaymentTransactionCategoryTotal other) {
		Objects.requireNonNull(other);
		Validate.isTrue(Objects.equals(categoryId, other.categoryId));

		return new PaymentTransactionCategoryTotal(categoryId,
				incoming.add(other.incoming), outgoing.add(other.outgoing));
	}
}
