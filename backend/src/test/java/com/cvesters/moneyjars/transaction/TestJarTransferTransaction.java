package com.cvesters.moneyjars.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import lombok.Getter;

import com.cvesters.moneyjars.jar.TestJar;
import com.cvesters.moneyjars.transaction.bdo.JarTransferTransaction;

@Getter
public enum TestJarTransferTransaction implements TestTransaction {

	CAR_SAVINGS(11L, LocalDate.of(2026, Month.FEBRUARY, 3), 2,
			new BigDecimal("100.00"), "Car maintenance savings",
			TestJar.HOUSEHOLD, TestJar.CAR),
	HOLIDAY_SAVINGS(12L, LocalDate.of(2026, Month.FEBRUARY, 10), 0,
			new BigDecimal("250.00"), "Holiday savings", TestJar.HOUSEHOLD,
			TestJar.HOLIDAY),
	ROAD_TRIP(13L, LocalDate.of(2026, Month.FEBRUARY, 10), 1,
			new BigDecimal("50.00"), "Road trip fuel", TestJar.HOLIDAY,
			TestJar.CAR);

	private final long id;
	private final LocalDate date;
	private final int sequence;
	private final BigDecimal amount;
	private final String description;
	private final TestJar sourceJar;
	private final TestJar targetJar;

	TestJarTransferTransaction(final long id, final LocalDate date,
			final int sequence, final BigDecimal amount,
			final String description, final TestJar sourceJar,
			final TestJar targetJar) {
		this.id = id;
		this.date = date;
		this.sequence = sequence;
		this.amount = amount;
		this.description = description;
		this.sourceJar = sourceJar;
		this.targetJar = targetJar;
	}

	@Override
	public JarTransferTransaction bdo() {
		return new JarTransferTransaction(id, date, sequence, amount,
				description, sourceJar.getId(), targetJar.getId());
	}

	public String getAmountString() {
		return amount.toPlainString();
	}
}
