package com.cvesters.moneyjars.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;

import com.cvesters.moneyjars.jar.TestJar;
import com.cvesters.moneyjars.transaction.bdo.Transaction;

@Getter
public enum TestTransaction {

	RENT(1L, LocalDate.of(2026, 1, 1), new BigDecimal("-1200.00"),
			TestJar.HOUSEHOLD, "Landlord", "January rent"),
	GROCERY(2L, LocalDate.of(2026, 1, 3), new BigDecimal("-134.50"),
			TestJar.HOUSEHOLD, "Supermarket", "Weekly groceries");

	private final long id;
	private final LocalDate date;
	private final BigDecimal amount;
	private final TestJar jar;
	private final String beneficiary;
	private final String description;

	TestTransaction(final long id, final LocalDate date,
			final BigDecimal amount, final TestJar jar,
			final String beneficiary, final String description) {
		this.id = id;
		this.date = date;
		this.amount = amount;
		this.jar = jar;
		this.beneficiary = beneficiary;
		this.description = description;
	}

	public Transaction bdo() {
		return new Transaction(id, date, amount, jar.getId(), beneficiary,
				description);
	}
}
