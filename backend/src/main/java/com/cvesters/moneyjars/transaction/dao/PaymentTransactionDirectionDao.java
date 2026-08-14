package com.cvesters.moneyjars.transaction.dao;

import java.util.Objects;

import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionDirection;

public final class PaymentTransactionDirectionDao {

	private PaymentTransactionDirectionDao() {
	}

	public static PaymentTransactionDirection toBdo(final short type) {
		return switch (type) {
			case 0 -> PaymentTransactionDirection.INCOMING;
			case 1 -> PaymentTransactionDirection.OUTGOING;
			default -> throw new IllegalArgumentException();
		};
	}

	public static short toDao(final PaymentTransactionDirection direction) {
		Objects.requireNonNull(direction);

		return switch (direction) {
			case INCOMING -> 0;
			case OUTGOING -> 1;
		};
	}
}