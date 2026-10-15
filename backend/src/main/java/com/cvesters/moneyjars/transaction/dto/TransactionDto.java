package com.cvesters.moneyjars.transaction.dto;

import java.util.Objects;

import lombok.Getter;

import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;
import com.cvesters.moneyjars.transaction.bdo.Transaction;

@Getter
public abstract sealed class TransactionDto permits PaymentTransactionDto {

	private Long id;
	private String date;
	private String amount;
	private String description;

	public static TransactionDto fromBdo(final Transaction transaction) {
		Objects.requireNonNull(transaction);

		return switch (transaction) {
			case PaymentTransaction pt -> new PaymentTransactionDto(pt);
		};
	}

	protected TransactionDto(final Transaction bdo) {
		Objects.requireNonNull(bdo);

		this.id = bdo.getId();
		this.date = bdo.getDate().toString();
		this.amount = bdo.getAmount().toPlainString();
		this.description = bdo.getDescription();
	}
}
