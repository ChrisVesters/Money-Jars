package com.cvesters.moneyjars.transaction.bdo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

import lombok.Getter;

@Getter
public abstract sealed class Transaction permits PaymentTransaction {

	private final Long id;
	private LocalDate date;
	private BigDecimal amount;
	private String beneficiary;
	private String description;

	protected Transaction(final LocalDate date, final BigDecimal amount,
			final String beneficiary, final String description) {
		this(null, date, amount, beneficiary, description);
	}

	protected Transaction(final Long id, final LocalDate date,
			final BigDecimal amount, final String beneficiary,
			final String description) {
		Objects.requireNonNull(date);
		Objects.requireNonNull(amount);
		Validate.notBlank(beneficiary);
		Objects.requireNonNull(description);

		this.id = id;
		this.date = date;
		this.amount = amount;
		this.beneficiary = beneficiary;
		this.description = description;
	}

	public void setDate(final LocalDate date) {
		Objects.requireNonNull(date);

		this.date = date;
	}

	public void setAmount(final BigDecimal amount) {
		Objects.requireNonNull(amount);

		this.amount = amount;
	}

	public void setBeneficiary(final String beneficiary) {
		Validate.notBlank(beneficiary);

		this.beneficiary = beneficiary;
	}

	public void setDescription(final String description) {
		Objects.requireNonNull(description);

		this.description = description;
	}
}
