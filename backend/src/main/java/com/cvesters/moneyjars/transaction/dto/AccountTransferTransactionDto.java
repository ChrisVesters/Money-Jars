package com.cvesters.moneyjars.transaction.dto;

import lombok.Getter;

import com.cvesters.moneyjars.transaction.bdo.AccountTransferTransaction;

@Getter
public final class AccountTransferTransactionDto extends TransactionDto {

	private long sourceAccountId;
	private long targetAccountId;

	public AccountTransferTransactionDto(final AccountTransferTransaction bdo) {
		super(bdo);

		this.sourceAccountId = bdo.getSourceAccountId();
		this.targetAccountId = bdo.getTargetAccountId();
	}
}
