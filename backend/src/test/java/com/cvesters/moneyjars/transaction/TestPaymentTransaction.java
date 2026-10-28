package com.cvesters.moneyjars.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import lombok.Getter;

import com.cvesters.moneyjars.account.TestAccount;
import com.cvesters.moneyjars.category.TestCategory;
import com.cvesters.moneyjars.jar.TestJar;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionDirection;

@Getter
public enum TestPaymentTransaction implements TestTransaction {

	RENT(1L, LocalDate.of(2026, Month.JANUARY, 10), 1,
			new BigDecimal("1200.00"), "January rent", TestJar.HOUSEHOLD,
			TestAccount.CHECKING, TestCategory.RENT, "Landlord",
			PaymentTransactionDirection.OUTGOING),
	GROCERY(2L, LocalDate.of(2026, Month.JANUARY, 10), 0,
			new BigDecimal("134.50"), "Weekly groceries", TestJar.HOUSEHOLD,
			TestAccount.CHECKING, TestCategory.GROCERIES, "Supermarket",
			PaymentTransactionDirection.OUTGOING),
	CAFE(3L, LocalDate.of(2026, Month.JANUARY, 15), 0, new BigDecimal("15.00"),
			"Coffee", TestJar.HOUSEHOLD, TestAccount.WALLET, null, "Cafe",
			PaymentTransactionDirection.OUTGOING),
	SALARY(4L, LocalDate.of(2026, Month.JANUARY, 1), 0,
			new BigDecimal("2182.55"), "January salary", TestJar.HOUSEHOLD,
			TestAccount.CHECKING, null, "Employer",
			PaymentTransactionDirection.INCOMING),
	BONUS(5L, LocalDate.of(2026, Month.JANUARY, 1), 1,
			new BigDecimal("2500.00"), "Year-end bonus", TestJar.HOLIDAY,
			TestAccount.SAVINGS, null, "Employer",
			PaymentTransactionDirection.INCOMING),
	CAR_ALLOWANCE(6L, LocalDate.of(2026, Month.JANUARY, 1), 2,
			new BigDecimal("500.00"), "Car allowance", TestJar.CAR,
			TestAccount.CHECKING, null, "Employer",
			PaymentTransactionDirection.INCOMING),
	FLIGHTS(7L, LocalDate.of(2026, Month.JANUARY, 20), 0,
			new BigDecimal("200.00"), "Flight tickets", TestJar.HOLIDAY,
			TestAccount.CREDIT_CARD, null, "Airline",
			PaymentTransactionDirection.OUTGOING),
	FUEL(8L, LocalDate.of(2026, Month.FEBRUARY, 3), 0, new BigDecimal("65.40"),
			"Fuel", TestJar.CAR, TestAccount.CREDIT_CARD, TestCategory.FUEL,
			"Gas station", PaymentTransactionDirection.OUTGOING),
	MARKET(9L, LocalDate.of(2026, Month.FEBRUARY, 3), 1,
			new BigDecimal("98.20"), "Fresh produce", TestJar.HOUSEHOLD,
			TestAccount.WALLET, TestCategory.GROCERIES, "Farmers market",
			PaymentTransactionDirection.OUTGOING),
	HOTEL(10L, LocalDate.of(2026, Month.JANUARY, 20), 1,
			new BigDecimal("350.00"), "Hotel stay", TestJar.HOLIDAY,
			TestAccount.CREDIT_CARD, null, "Hotel",
			PaymentTransactionDirection.OUTGOING);

	private final long id;
	private final LocalDate date;
	private final int sequence;
	private final BigDecimal amount;
	private final String description;
	private final TestJar jar;
	private final TestAccount account;
	private final TestCategory category;
	private final String counterparty;
	private final PaymentTransactionDirection direction;

	TestPaymentTransaction(final long id, final LocalDate date,
			final int sequence, final BigDecimal amount,
			final String description, final TestJar jar,
			final TestAccount account, final TestCategory category,
			final String counterparty,
			final PaymentTransactionDirection direction) {
		this.id = id;
		this.date = date;
		this.sequence = sequence;
		this.amount = amount;
		this.description = description;
		this.jar = jar;
		this.account = account;
		this.category = category;
		this.counterparty = counterparty;
		this.direction = direction;
	}

	@Override
	public PaymentTransaction bdo() {
		return new PaymentTransaction(id, date, sequence, amount, description,
				jar.getId(), account.getId(), getCategoryId(), counterparty,
				direction);
	}

	public Long getCategoryId() {
		return Optional.ofNullable(category)
				.map(TestCategory::getId)
				.orElse(null);
	}

	public short getDirectionId() {
		return switch (direction) {
			case INCOMING -> 0;
			case OUTGOING -> 1;
		};
	}

	public String getDirectionString() {
		return switch (direction) {
			case INCOMING -> "INCOMING";
			case OUTGOING -> "OUTGOING";
		};
	}
}
