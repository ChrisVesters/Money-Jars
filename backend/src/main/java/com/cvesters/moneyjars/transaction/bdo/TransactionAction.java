package com.cvesters.moneyjars.transaction.bdo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

public final class TransactionAction {

	private TransactionAction() {
	}

	public static record CreatePayment(LocalDate date, BigDecimal amount,
			String beneficiary, String description, long jarId, long accountId,
			PaymentTransactionDirection direction) {

		public CreatePayment {
			Objects.requireNonNull(date);
			Objects.requireNonNull(amount);
			Validate.notBlank(beneficiary);
			Objects.requireNonNull(description);
			Objects.requireNonNull(direction);
		}
	}

	public static record UpdatePayment(LocalDate date, BigDecimal amount,
			String beneficiary, String description, long jarId, long accountId,
			PaymentTransactionDirection direction) {

		public UpdatePayment {
			Objects.requireNonNull(date);
			Objects.requireNonNull(amount);
			Validate.notBlank(beneficiary);
			Objects.requireNonNull(description);
			Objects.requireNonNull(direction);
		}
	}
}
