package com.cvesters.moneyjars.accountentry;

import java.math.BigDecimal;

import lombok.Getter;

import com.cvesters.moneyjars.account.TestAccount;
import com.cvesters.moneyjars.accountentry.bdo.AccountEntry;
import com.cvesters.moneyjars.transaction.TestPaymentTransaction;

@Getter
public enum TestAccountEntry {

	CHECKING_RENT(1L, TestPaymentTransaction.RENT, TestAccount.CHECKING,
			new BigDecimal("2548.05"), new BigDecimal("1348.05")),
	CHECKING_GROCERY(2L, TestPaymentTransaction.GROCERY, TestAccount.CHECKING,
			new BigDecimal("2682.55"), new BigDecimal("2548.05")),
	WALLET_CAFE(3L, TestPaymentTransaction.CAFE, TestAccount.WALLET,
			new BigDecimal("0.00"), new BigDecimal("-15.00")),
	CHECKING_SALARY(4L, TestPaymentTransaction.SALARY, TestAccount.CHECKING,
			new BigDecimal("0.00"), new BigDecimal("2182.55")),
	SAVINGS_BONUS(5L, TestPaymentTransaction.BONUS, TestAccount.SAVINGS,
			new BigDecimal("0.00"), new BigDecimal("2500.00")),
	CHECKING_CAR_ALLOWANCE(6L, TestPaymentTransaction.CAR_ALLOWANCE,
			TestAccount.CHECKING, new BigDecimal("2182.55"),
			new BigDecimal("2682.55")),
	CREDIT_CARD_FLIGHTS(7L, TestPaymentTransaction.FLIGHTS,
			TestAccount.CREDIT_CARD, new BigDecimal("0.00"),
			new BigDecimal("-200.00")),
	CREDIT_CARD_FUEL(8L, TestPaymentTransaction.FUEL, TestAccount.CREDIT_CARD,
			new BigDecimal("-200.00"), new BigDecimal("-265.40")),
	WALLET_MARKET(9L, TestPaymentTransaction.MARKET, TestAccount.WALLET,
			new BigDecimal("-15.00"), new BigDecimal("-113.20"));

	private final long id;
	private final TestPaymentTransaction transaction;
	private final TestAccount account;
	private final BigDecimal balanceBefore;
	private final BigDecimal balanceAfter;

	TestAccountEntry(final long id, final TestPaymentTransaction transaction,
			final TestAccount account, final BigDecimal balanceBefore,
			final BigDecimal balanceAfter) {
		this.id = id;
		this.transaction = transaction;
		this.account = account;
		this.balanceBefore = balanceBefore;
		this.balanceAfter = balanceAfter;
	}

	public AccountEntry bdo() {
		return new AccountEntry(id, transaction.getId(), account.getId(),
				balanceBefore, balanceAfter);
	}
}
