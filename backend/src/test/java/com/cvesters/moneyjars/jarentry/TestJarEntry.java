package com.cvesters.moneyjars.jarentry;

import java.math.BigDecimal;

import lombok.Getter;

import com.cvesters.moneyjars.jar.TestJar;
import com.cvesters.moneyjars.jarentry.bdo.JarEntry;
import com.cvesters.moneyjars.transaction.TestPaymentTransaction;

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
			new BigDecimal("833.05"), new BigDecimal("734.85"));

	private final long id;
	private final TestPaymentTransaction transaction;
	private final TestJar jar;
	private final BigDecimal balanceBefore;
	private final BigDecimal balanceAfter;

	TestJarEntry(final long id, final TestPaymentTransaction transaction,
			final TestJar jar, final BigDecimal balanceBefore,
			final BigDecimal balanceAfter) {
		this.id = id;
		this.transaction = transaction;
		this.jar = jar;
		this.balanceBefore = balanceBefore;
		this.balanceAfter = balanceAfter;
	}

	public JarEntry bdo() {
		return new JarEntry(id, transaction.getId(), jar.getId(), balanceBefore,
				balanceAfter);
	}
}
