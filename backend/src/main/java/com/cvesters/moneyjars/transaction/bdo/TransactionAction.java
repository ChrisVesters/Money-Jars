package com.cvesters.moneyjars.transaction.bdo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

public final class TransactionAction {

	private TransactionAction() {
	}

	public static record Create(LocalDate date, BigDecimal amount, long jarId,
			String beneficiary, String description) {

		public Create {
			Objects.requireNonNull(date);
			Objects.requireNonNull(amount);
			Validate.notBlank(beneficiary);
			Objects.requireNonNull(description);
		}

		public Transaction toBdo() {
			return new Transaction(date, amount, jarId, beneficiary,
					description);
		}
	}

	public static record Update(LocalDate date, BigDecimal amount,
			long jarId, String beneficiary, String description) {

		public Update {
			Objects.requireNonNull(date);
			Objects.requireNonNull(amount);
			Validate.notBlank(beneficiary);
			Objects.requireNonNull(description);
		}

		public void applyOn(final Transaction transaction) {
			Objects.requireNonNull(transaction);

			transaction.setDate(date);
			transaction.setAmount(amount);
			transaction.setJarId(jarId);
			transaction.setBeneficiary(beneficiary);
			transaction.setDescription(description);
		}
	}
}
