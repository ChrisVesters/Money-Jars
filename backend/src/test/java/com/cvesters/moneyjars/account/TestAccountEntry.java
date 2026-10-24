package com.cvesters.moneyjars.account;

import java.math.BigDecimal;

import lombok.Getter;

import com.cvesters.moneyjars.account.bdo.AccountEntry;
import com.cvesters.moneyjars.transaction.TestAccountTransferTransaction;
import com.cvesters.moneyjars.transaction.TestPaymentTransaction;
import com.cvesters.moneyjars.transaction.TestTransaction;

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
			new BigDecimal("-550.00"), new BigDecimal("-615.40")),
	WALLET_MARKET(9L, TestPaymentTransaction.MARKET, TestAccount.WALLET,
			new BigDecimal("-15.00"), new BigDecimal("-113.20")),
	CREDIT_CARD_HOTEL(10L, TestPaymentTransaction.HOTEL,
			TestAccount.CREDIT_CARD, new BigDecimal("-200.00"),
			new BigDecimal("-550.00")),
	CHECKING_CASH_WITHDRAWAL(11L,
			TestAccountTransferTransaction.CASH_WITHDRAWAL,
			TestAccount.CHECKING, new BigDecimal("1348.05"),
			new BigDecimal("1248.05")),
	WALLET_CASH_WITHDRAWAL(12L, TestAccountTransferTransaction.CASH_WITHDRAWAL,
			TestAccount.WALLET, new BigDecimal("-113.20"),
			new BigDecimal("-13.20")),
	CHECKING_SAVINGS_DEPOSIT(13L,
			TestAccountTransferTransaction.SAVINGS_DEPOSIT,
			TestAccount.CHECKING, new BigDecimal("1248.05"),
			new BigDecimal("748.05")),
	SAVINGS_SAVINGS_DEPOSIT(14L, TestAccountTransferTransaction.SAVINGS_DEPOSIT,
			TestAccount.SAVINGS, new BigDecimal("2500.00"),
			new BigDecimal("3000.00")),
	SAVINGS_CREDIT_CARD_SETTLEMENT(15L,
			TestAccountTransferTransaction.CREDIT_CARD_SETTLEMENT,
			TestAccount.SAVINGS, new BigDecimal("3000.00"),
			new BigDecimal("2384.60")),
	CREDIT_CARD_CREDIT_CARD_SETTLEMENT(16L,
			TestAccountTransferTransaction.CREDIT_CARD_SETTLEMENT,
			TestAccount.CREDIT_CARD, new BigDecimal("-615.40"),
			new BigDecimal("0.00"));

	private final long id;
	private final TestTransaction transaction;
	private final TestAccount account;
	private final BigDecimal balanceBefore;
	private final BigDecimal balanceAfter;

	TestAccountEntry(final long id, final TestTransaction transaction,
			final TestAccount account, final BigDecimal balanceBefore,
			final BigDecimal balanceAfter) {
		this.id = id;
		this.transaction = transaction;
		this.account = account;
		this.balanceBefore = balanceBefore;
		this.balanceAfter = balanceAfter;
	}

	public TestPaymentTransaction getPaymentTransaction() {
		return (TestPaymentTransaction) transaction;
	}

	public TestAccountTransferTransaction getAccountTransferTransaction() {
		return (TestAccountTransferTransaction) transaction;
	}

	public AccountEntry bdo() {
		return new AccountEntry(id, transaction.getId(), account.getId(),
				balanceBefore, balanceAfter);
	}
}
