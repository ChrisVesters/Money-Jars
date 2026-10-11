package com.cvesters.moneyjars.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionAction;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionDirection;

public final class PaymentTransactionActionDto {

	private PaymentTransactionActionDto() {
	}

	public static record CreatePayment(LocalDate date, String amount,
			String description, long jarId, long accountId, String counterparty,
			String direction) {

		public PaymentTransactionAction.Create toBdo() {
			return new PaymentTransactionAction.Create(date,
					new BigDecimal(amount), description, jarId, accountId,
					counterparty,
					PaymentTransactionDirection.valueOf(direction));
		}
	}

	public static record UpdatePayment(LocalDate date, String amount,
			String description, long jarId, long accountId, String counterparty,
			String direction) {

		public PaymentTransactionAction.Update toBdo() {
			return new PaymentTransactionAction.Update(date,
					new BigDecimal(amount), description, jarId, accountId,
					counterparty,
					PaymentTransactionDirection.valueOf(direction));
		}
	}
}
