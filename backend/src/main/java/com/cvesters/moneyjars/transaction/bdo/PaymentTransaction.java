package com.cvesters.moneyjars.transaction.bdo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class PaymentTransaction extends Transaction {

	private long jarId;
	private long accountId;
	private String counterparty;
	private PaymentTransactionDirection direction;

	public PaymentTransaction(final LocalDate date, final int sequence,
			final BigDecimal amount, final String description, final long jarId,
			final long accountId, final String counterparty,
			final PaymentTransactionDirection direction) {
		this(null, date, sequence, amount, description, jarId, accountId,
				counterparty, direction);
	}

	public PaymentTransaction(final Long id, final LocalDate date,
			final int sequence, final BigDecimal amount,
			final String description, final long jarId, final long accountId,
			final String counterparty,
			final PaymentTransactionDirection direction) {
		Validate.notBlank(counterparty);
		Objects.requireNonNull(direction);

		super(id, date, sequence, amount, description);

		this.jarId = jarId;
		this.accountId = accountId;
		this.counterparty = counterparty;
		this.direction = direction;
	}

	public void setCounterparty(final String counterparty) {
		Validate.notBlank(counterparty);

		this.counterparty = counterparty;
	}

	public void setDirection(final PaymentTransactionDirection direction) {
		Objects.requireNonNull(direction);

		this.direction = direction;
	}
}
