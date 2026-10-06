package com.cvesters.moneyjars.transaction.bdo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import lombok.Getter;

// TODO: Amount should always be positive!
@Getter
public abstract sealed class Transaction permits PaymentTransaction {

	private final Long id;
	private LocalDate date;
	private int sequence;
	private BigDecimal amount;
	private String description;

	protected Transaction(final Long id, final LocalDate date,
			final int sequence, final BigDecimal amount,
			final String description) {
		Objects.requireNonNull(date);
		Objects.requireNonNull(amount);
		Objects.requireNonNull(description);

		this.id = id;
		this.date = date;
		this.sequence = sequence;
		this.amount = amount;
		this.description = description;
	}

	public void setDate(final LocalDate date) {
		Objects.requireNonNull(date);

		this.date = date;
	}

	public void setSequence(final int sequence) {
		this.sequence = sequence;
	}

	public void setAmount(final BigDecimal amount) {
		Objects.requireNonNull(amount);

		this.amount = amount;
	}

	public void setDescription(final String description) {
		Objects.requireNonNull(description);

		this.description = description;
	}
}
