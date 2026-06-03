package com.cvesters.moneyjars.transaction.dto;

import java.math.BigDecimal;
import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cvesters.moneyjars.transaction.bdo.Transaction;

@Getter
@NoArgsConstructor
public class TransactionDto {

	private Long id;
	private String date;
	private BigDecimal amount;
	private long jarId;
	private String beneficiary;
	private String description;

	public TransactionDto(final Transaction bdo) {
		Objects.requireNonNull(bdo);

		this.id = bdo.getId();
		this.date = bdo.getDate().toString();
		this.amount = bdo.getAmount();
		this.jarId = bdo.getJarId();
		this.beneficiary = bdo.getBeneficiary();
		this.description = bdo.getDescription();
	}
}
