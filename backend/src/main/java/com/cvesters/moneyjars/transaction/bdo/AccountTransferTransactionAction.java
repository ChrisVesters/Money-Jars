package com.cvesters.moneyjars.transaction.bdo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

public final class AccountTransferTransactionAction {

	private AccountTransferTransactionAction() {
	}

	public static record Create(LocalDate date, BigDecimal amount,
			String description, long sourceAccountId, long targetAccountId) {

		public Create {
			Objects.requireNonNull(date);
			Objects.requireNonNull(amount);
			Validate.isTrue(amount.signum() != -1);
			Objects.requireNonNull(description);
		}
	}

	public static record Update(LocalDate date, BigDecimal amount,
			String description, long sourceAccountId, long targetAccountId) {

		public Update {
			Objects.requireNonNull(date);
			Objects.requireNonNull(amount);
			Validate.isTrue(amount.signum() != -1);
			Objects.requireNonNull(description);
		}
	}
}
