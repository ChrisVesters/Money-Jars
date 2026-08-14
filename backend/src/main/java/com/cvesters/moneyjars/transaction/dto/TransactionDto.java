package com.cvesters.moneyjars.transaction.dto;

import java.math.BigDecimal;
import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cvesters.moneyjars.transaction.bdo.Transaction;

@Getter
@NoArgsConstructor
public abstract sealed class TransactionDto permits PaymentTransactionDto {

	private Long id;
	private String date;
	private BigDecimal amount;
	private String beneficiary;
	private String description;

	protected TransactionDto(final Transaction bdo) {
		Objects.requireNonNull(bdo);

		this.id = bdo.getId();
		this.date = bdo.getDate().toString();
		this.amount = bdo.getAmount();
		this.beneficiary = bdo.getBeneficiary();
		this.description = bdo.getDescription();
	}
}
