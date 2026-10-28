package com.cvesters.moneyjars.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionAction;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionDirection;

public final class PaymentTransactionActionDto {

	private PaymentTransactionActionDto() {
	}

	public static record Create(LocalDate date, String amount,
			String description, long jarId, long accountId, Long categoryId,
			String counterparty, String direction) {

		public PaymentTransactionAction.Create toBdo() {
			return new PaymentTransactionAction.Create(date,
					new BigDecimal(amount), description, jarId, accountId,
					categoryId, counterparty,
					PaymentTransactionDirection.valueOf(direction));
		}
	}

	public static record Update(LocalDate date, String amount,
			String description, long jarId, long accountId, Long categoryId,
			String counterparty, String direction) {

		public PaymentTransactionAction.Update toBdo() {
			return new PaymentTransactionAction.Update(date,
					new BigDecimal(amount), description, jarId, accountId,
					categoryId, counterparty,
					PaymentTransactionDirection.valueOf(direction));
		}
	}
}
