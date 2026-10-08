package com.cvesters.moneyjars.accountentry.bdo;

import java.math.BigDecimal;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

import lombok.Getter;

import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;

@Getter
public class AccountEntry {

	private final Long id;
	private final long transactionId;
	private final long accountId;
	private BigDecimal balanceBefore;
	private BigDecimal balanceAfter;

	public static AccountEntry first(final PaymentTransaction transaction) {
		Objects.requireNonNull(transaction);

		final long transactionId = transaction.getId();
		final long accountId = transaction.getAccountId();
		final BigDecimal amount = transaction.getSignedAmount();
		final BigDecimal balanceBefore = BigDecimal.ZERO;
		final BigDecimal balanceAfter = amount;

		return new AccountEntry(null, transactionId, accountId, balanceBefore,
				balanceAfter);
	}

	public AccountEntry(final Long id, final long transactionId,
			final long accountId, final BigDecimal balanceBefore,
			final BigDecimal balanceAfter) {
		Objects.requireNonNull(balanceBefore);
		Objects.requireNonNull(balanceAfter);

		this.id = id;
		this.transactionId = transactionId;
		this.accountId = accountId;
		this.balanceBefore = balanceBefore;
		this.balanceAfter = balanceAfter;
	}

	public AccountEntry next(final PaymentTransaction transaction) {
		Objects.requireNonNull(transaction);
		Validate.isTrue(transaction.getAccountId() == this.accountId);

		final long transactionId = transaction.getId();
		final long accountId = this.accountId;
		final BigDecimal amount = transaction.getSignedAmount();
		final BigDecimal balanceBefore = this.getBalanceAfter();
		final BigDecimal balanceAfter = balanceBefore.add(amount);

		return new AccountEntry(null, transactionId, accountId, balanceBefore,
				balanceAfter);
	}

	public void shift(final BigDecimal amount) {
		Objects.requireNonNull(amount);

		this.balanceAfter = this.balanceAfter.add(amount);
		this.balanceBefore = this.balanceBefore.add(amount);
	}

}
