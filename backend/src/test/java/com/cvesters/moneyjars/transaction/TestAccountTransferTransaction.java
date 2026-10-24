package com.cvesters.moneyjars.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import lombok.Getter;

import com.cvesters.moneyjars.account.TestAccount;
import com.cvesters.moneyjars.transaction.bdo.AccountTransferTransaction;

@Getter
public enum TestAccountTransferTransaction implements TestTransaction {

	CASH_WITHDRAWAL(14L, LocalDate.of(2026, Month.FEBRUARY, 3), 3,
			new BigDecimal("100.00"), "Cash withdrawal", TestAccount.CHECKING,
			TestAccount.WALLET),
	SAVINGS_DEPOSIT(15L, LocalDate.of(2026, Month.FEBRUARY, 10), 2,
			new BigDecimal("500.00"), "Monthly savings", TestAccount.CHECKING,
			TestAccount.SAVINGS),
	CREDIT_CARD_SETTLEMENT(16L, LocalDate.of(2026, Month.FEBRUARY, 10), 3,
			new BigDecimal("615.40"), "Credit card settlement",
			TestAccount.SAVINGS, TestAccount.CREDIT_CARD);

	private final long id;
	private final LocalDate date;
	private final int sequence;
	private final BigDecimal amount;
	private final String description;
	private final TestAccount sourceAccount;
	private final TestAccount targetAccount;

	TestAccountTransferTransaction(final long id, final LocalDate date,
			final int sequence, final BigDecimal amount,
			final String description, final TestAccount sourceAccount,
			final TestAccount targetAccount) {
		this.id = id;
		this.date = date;
		this.sequence = sequence;
		this.amount = amount;
		this.description = description;
		this.sourceAccount = sourceAccount;
		this.targetAccount = targetAccount;
	}

	@Override
	public AccountTransferTransaction bdo() {
		return new AccountTransferTransaction(id, date, sequence, amount,
				description, sourceAccount.getId(), targetAccount.getId());
	}

	public String getAmountString() {
		return amount.toPlainString();
	}
}
