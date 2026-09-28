package com.cvesters.moneyjars.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import lombok.Getter;

import com.cvesters.moneyjars.account.TestAccount;
import com.cvesters.moneyjars.jar.TestJar;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionDirection;

@Getter
public enum TestPaymentTransaction {

	RENT(1L, LocalDate.of(2026, Month.JANUARY, 10), 2,
			new BigDecimal("1200.00"), "Landlord", "January rent",
			TestJar.HOUSEHOLD, TestAccount.CHECKING,
			PaymentTransactionDirection.OUTGOING),
	GROCERY(2L, LocalDate.of(2026, Month.JANUARY, 10), 1,
			new BigDecimal("134.50"), "Supermarket", "Weekly groceries",
			TestJar.HOUSEHOLD, TestAccount.CHECKING,
			PaymentTransactionDirection.OUTGOING),
	CAFE(3L, LocalDate.of(2026, Month.JANUARY, 15), 1, new BigDecimal("15.00"),
			"Cafe", "Coffee", TestJar.HOUSEHOLD, TestAccount.WALLET,
			PaymentTransactionDirection.OUTGOING);

	private final long id;
	private final LocalDate date;
	private final int sequence;
	private final BigDecimal amount;
	private final String beneficiary;
	private final String description;
	private final TestJar jar;
	private final TestAccount account;
	private final PaymentTransactionDirection direction;

	TestPaymentTransaction(final long id, final LocalDate date,
			final int sequence, final BigDecimal amount,
			final String beneficiary, final String description,
			final TestJar jar, final TestAccount account,
			final PaymentTransactionDirection direction) {
		this.id = id;
		this.date = date;
		this.sequence = sequence;
		this.amount = amount;
		this.beneficiary = beneficiary;
		this.description = description;
		this.jar = jar;
		this.account = account;
		this.direction = direction;
	}

	public PaymentTransaction bdo() {
		return new PaymentTransaction(id, date, sequence, amount, beneficiary,
				description, jar.getId(), account.getId(), direction);
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
