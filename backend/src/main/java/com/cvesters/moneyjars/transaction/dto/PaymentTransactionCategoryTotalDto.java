package com.cvesters.moneyjars.transaction.dto;

import java.util.Objects;

import lombok.Getter;

import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionCategoryTotal;

@Getter
public class PaymentTransactionCategoryTotalDto {

	private Long categoryId;
	private String incoming;
	private String outgoing;

	public PaymentTransactionCategoryTotalDto(
			final PaymentTransactionCategoryTotal total) {
		Objects.requireNonNull(total);

		this.categoryId = total.categoryId();
		this.incoming = total.incoming().toPlainString();
		this.outgoing = total.outgoing().toPlainString();
	}
}
