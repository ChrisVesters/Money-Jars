package com.cvesters.moneyjars.transaction.dto;

import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;

@Getter
@NoArgsConstructor
public final class PaymentTransactionDto extends TransactionDto {

	private long jarId;
	private long accountId;
	private String direction;

	public PaymentTransactionDto(final PaymentTransaction bdo) {
		Objects.requireNonNull(bdo);

		super(bdo);

		this.jarId = bdo.getJarId();
		this.accountId = bdo.getAccountId();
		this.direction = PaymentTransactionDirectionDto
				.toDto(bdo.getDirection());
	}
}
