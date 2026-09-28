package com.cvesters.moneyjars.transaction.bdo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class PaymentTransaction extends Transaction {

	private long jarId;
	private long accountId;
	private PaymentTransactionDirection direction;

	public PaymentTransaction(final LocalDate date, final int sequence,
			final BigDecimal amount, final String beneficiary,
			final String description, final long jarId, final long accountId,
			final PaymentTransactionDirection direction) {
		this(null, date, sequence, amount, beneficiary, description, jarId,
				accountId, direction);
	}

	public PaymentTransaction(final Long id, final LocalDate date,
			final int sequence, final BigDecimal amount,
			final String beneficiary, final String description,
			final long jarId, final long accountId,
			final PaymentTransactionDirection direction) {
		Objects.requireNonNull(direction);

		super(id, date, sequence, amount, beneficiary, description);

		this.jarId = jarId;
		this.accountId = accountId;
		this.direction = direction;
	}

	public void setDirection(final PaymentTransactionDirection direction) {
		Objects.requireNonNull(direction);

		this.direction = direction;
	}
}
