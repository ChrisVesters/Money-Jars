package com.cvesters.moneyjars.transaction.bdo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

import lombok.Getter;

@Getter
public class Transaction {

	private final Long id;
	private LocalDate date;
	private BigDecimal amount;
	private long jarId;
	private String beneficiary;
	private String description;

	public Transaction(final LocalDate date, final BigDecimal amount,
			final long jarId, final String beneficiary,
			final String description) {
		this(null, date, amount, jarId, beneficiary, description);
	}

	public Transaction(final Long id, final LocalDate date,
			final BigDecimal amount, final long jarId, final String beneficiary,
			final String description) {
		Objects.requireNonNull(date);
		Objects.requireNonNull(amount);
		Validate.notBlank(beneficiary);
		Objects.requireNonNull(description);

		this.id = id;
		this.date = date;
		this.amount = amount;
		this.jarId = jarId;
		this.beneficiary = beneficiary;
		this.description = description;
	}

	public void setDate(final LocalDate date) {
		Objects.requireNonNull(date);
		this.date = date;
	}

	public void setAmount(final BigDecimal amount) {
		Objects.requireNonNull(amount);
		Validate.isTrue(amount.signum() != 0);

		this.amount = amount;
	}

	public void setJarId(final long jarId) {
		this.jarId = jarId;
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
