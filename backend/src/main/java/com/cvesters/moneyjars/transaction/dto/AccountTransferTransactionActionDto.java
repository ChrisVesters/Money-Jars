package com.cvesters.moneyjars.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.cvesters.moneyjars.transaction.bdo.AccountTransferTransactionAction;

public final class AccountTransferTransactionActionDto {

	private AccountTransferTransactionActionDto() {
	}

	public static record Create(LocalDate date, String amount,
			String description, long sourceAccountId, long targetAccountId) {

		public AccountTransferTransactionAction.Create toBdo() {
			return new AccountTransferTransactionAction.Create(date,
					new BigDecimal(amount), description, sourceAccountId,
					targetAccountId);
		}
	}

	public static record Update(LocalDate date, String amount,
			String description, long sourceAccountId, long targetAccountId) {

		public AccountTransferTransactionAction.Update toBdo() {
			return new AccountTransferTransactionAction.Update(date,
					new BigDecimal(amount), description, sourceAccountId,
					targetAccountId);
		}
	}
}
