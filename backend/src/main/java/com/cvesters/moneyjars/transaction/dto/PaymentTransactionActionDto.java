package com.cvesters.moneyjars.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionDirection;
import com.cvesters.moneyjars.transaction.bdo.TransactionAction;

public final class PaymentTransactionActionDto {

	private PaymentTransactionActionDto() {
	}

	public static record CreatePayment(LocalDate date, BigDecimal amount,
			String beneficiary, String description, long jarId, long accountId,
			String direction) {

		public TransactionAction.CreatePayment toBdo() {
			return new TransactionAction.CreatePayment(date, amount,
					beneficiary, description, jarId, accountId,
					PaymentTransactionDirection.valueOf(direction));
		}
	}

	public static record UpdatePayment(LocalDate date, BigDecimal amount,
			String beneficiary, String description, long jarId, long accountId,
			String direction) {

		public TransactionAction.UpdatePayment toBdo() {
			return new TransactionAction.UpdatePayment(date, amount,
					beneficiary, description, jarId, accountId,
					PaymentTransactionDirection.valueOf(direction));
		}
	}
}
