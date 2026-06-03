package com.cvesters.moneyjars.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.cvesters.moneyjars.transaction.bdo.TransactionAction;

public final class TransactionActionDto {

	private TransactionActionDto() {
	}

	public static record Create(LocalDate date, BigDecimal amount, long jarId,
			String beneficiary, String description) {

		public TransactionAction.Create toBdo() {
			return new TransactionAction.Create(date, amount, jarId,
					beneficiary, description);
		}
	}

	public static record Update(LocalDate date, BigDecimal amount, long jarId,
			String beneficiary, String description) {

		public TransactionAction.Update toBdo() {
			return new TransactionAction.Update(date, amount, jarId,
					beneficiary, description);
		}

	}
}
