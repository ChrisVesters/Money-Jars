package com.cvesters.moneyjars.transaction.dto;

import java.util.Objects;

import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionDirection;

public final class PaymentTransactionDirectionDto {

	private PaymentTransactionDirectionDto() {
	}

	public static PaymentTransactionDirection toBdo(final String direction) {
		Objects.requireNonNull(direction);

		return switch (direction) {
			case "INCOMING" -> PaymentTransactionDirection.INCOMING;
			case "OUTGOING" -> PaymentTransactionDirection.OUTGOING;
			default -> throw new IllegalArgumentException();
		};
	}

	public static String toDto(final PaymentTransactionDirection direction) {
		Objects.requireNonNull(direction);

		return switch (direction) {
			case INCOMING -> "INCOMING";
			case OUTGOING -> "OUTGOING";
		};
	}
}