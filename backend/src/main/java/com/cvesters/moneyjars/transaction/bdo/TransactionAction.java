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

		// TODO This can not be done by the DTO since the sequence needs to be determined!
		public PaymentTransaction toBdo() {
			return new PaymentTransaction(date, 0, amount, beneficiary,
					description, jarId, accountId, direction);
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

		public void applyOn(final PaymentTransaction transaction) {
			Objects.requireNonNull(transaction);

			transaction.setDate(date);
			transaction.setAmount(amount);
			transaction.setBeneficiary(beneficiary);
			transaction.setDescription(description);
			transaction.setJarId(jarId);
			transaction.setAccountId(accountId);
			transaction.setDirection(direction);
		}
	}
}
