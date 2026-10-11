package com.cvesters.moneyjars.transaction.bdo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

public final class PaymentTransactionAction {

	private PaymentTransactionAction() {
	}

	public static record Create(LocalDate date, BigDecimal amount,
			String description, long jarId, long accountId, String counterparty,
			PaymentTransactionDirection direction) {

		public Create {
			Objects.requireNonNull(date);
			Objects.requireNonNull(amount);
			Validate.isTrue(amount.signum() != -1);
			Objects.requireNonNull(description);
			Validate.notBlank(counterparty);
			Objects.requireNonNull(direction);
		}
	}

	public static record Update(LocalDate date, BigDecimal amount,
			String description, long jarId, long accountId, String counterparty,
			PaymentTransactionDirection direction) {

		public Update {
			Objects.requireNonNull(date);
			Objects.requireNonNull(amount);
			Validate.isTrue(amount.signum() != -1);
			Objects.requireNonNull(description);
			Validate.notBlank(counterparty);
			Objects.requireNonNull(direction);
		}
	}
}
