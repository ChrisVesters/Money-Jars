package com.cvesters.moneyjars.jar;

import java.math.BigDecimal;

import lombok.Getter;

import com.cvesters.moneyjars.jar.bdo.JarEntry;
import com.cvesters.moneyjars.transaction.TestJarTransferTransaction;
import com.cvesters.moneyjars.transaction.TestPaymentTransaction;
import com.cvesters.moneyjars.transaction.TestTransaction;

@Getter
public enum TestJarEntry {

	HOUSEHOLD_RENT(1L, TestPaymentTransaction.RENT, TestJar.HOUSEHOLD,
			new BigDecimal("2048.05"), new BigDecimal("848.05")),
	HOUSEHOLD_GROCERY(2L, TestPaymentTransaction.GROCERY, TestJar.HOUSEHOLD,
			new BigDecimal("2182.55"), new BigDecimal("2048.05")),
	HOUSEHOLD_CAFE(3L, TestPaymentTransaction.CAFE, TestJar.HOUSEHOLD,
			new BigDecimal("848.05"), new BigDecimal("833.05")),
	HOUSEHOLD_SALARY(4L, TestPaymentTransaction.SALARY, TestJar.HOUSEHOLD,
			new BigDecimal("0.00"), new BigDecimal("2182.55")),
	HOLIDAY_BONUS(5L, TestPaymentTransaction.BONUS, TestJar.HOLIDAY,
			new BigDecimal("0.00"), new BigDecimal("2500.00")),
	CAR_ALLOWANCE(6L, TestPaymentTransaction.CAR_ALLOWANCE, TestJar.CAR,
			new BigDecimal("0.00"), new BigDecimal("500.00")),
	HOLIDAY_FLIGHTS(7L, TestPaymentTransaction.FLIGHTS, TestJar.HOLIDAY,
			new BigDecimal("2500.00"), new BigDecimal("2300.00")),
	CAR_FUEL(8L, TestPaymentTransaction.FUEL, TestJar.CAR,
			new BigDecimal("500.00"), new BigDecimal("434.60")),
	HOUSEHOLD_MARKET(9L, TestPaymentTransaction.MARKET, TestJar.HOUSEHOLD,
			new BigDecimal("833.05"), new BigDecimal("734.85")),
	HOLIDAY_HOTEL(10L, TestPaymentTransaction.HOTEL, TestJar.HOLIDAY,
			new BigDecimal("2300.00"), new BigDecimal("1950.00")),
	HOUSEHOLD_CAR_SAVINGS(11L, TestJarTransferTransaction.CAR_SAVINGS,
			TestJar.HOUSEHOLD, new BigDecimal("734.85"),
			new BigDecimal("634.85")),
	CAR_CAR_SAVINGS(12L, TestJarTransferTransaction.CAR_SAVINGS, TestJar.CAR,
			new BigDecimal("434.60"), new BigDecimal("534.60")),
	HOUSEHOLD_HOLIDAY_SAVINGS(13L, TestJarTransferTransaction.HOLIDAY_SAVINGS,
			TestJar.HOUSEHOLD, new BigDecimal("634.85"),
			new BigDecimal("384.85")),
	HOLIDAY_HOLIDAY_SAVINGS(14L, TestJarTransferTransaction.HOLIDAY_SAVINGS,
			TestJar.HOLIDAY, new BigDecimal("1950.00"),
			new BigDecimal("2200.00")),
	HOLIDAY_ROAD_TRIP(15L, TestJarTransferTransaction.ROAD_TRIP,
			TestJar.HOLIDAY, new BigDecimal("2200.00"),
			new BigDecimal("2150.00")),
	CAR_ROAD_TRIP(16L, TestJarTransferTransaction.ROAD_TRIP, TestJar.CAR,
			new BigDecimal("534.60"), new BigDecimal("584.60"));

	private final long id;
	private final TestTransaction transaction;
	private final TestJar jar;
	private final BigDecimal balanceBefore;
	private final BigDecimal balanceAfter;

	TestJarEntry(final long id, final TestTransaction transaction,
			final TestJar jar, final BigDecimal balanceBefore,
			final BigDecimal balanceAfter) {
		this.id = id;
		this.transaction = transaction;
		this.jar = jar;
		this.balanceBefore = balanceBefore;
		this.balanceAfter = balanceAfter;
	}

	public TestPaymentTransaction getPaymentTransaction() {
		return (TestPaymentTransaction) transaction;
	}

	public TestJarTransferTransaction getJarTransferTransaction() {
		return (TestJarTransferTransaction) transaction;
	}

	public JarEntry bdo() {
		return new JarEntry(id, transaction.getId(), jar.getId(), balanceBefore,
				balanceAfter);
	}
}
