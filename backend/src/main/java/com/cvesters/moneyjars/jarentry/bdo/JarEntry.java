package com.cvesters.moneyjars.jarentry.bdo;

import java.math.BigDecimal;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

import lombok.Getter;

import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;

@Getter
public class JarEntry {

	private final Long id;
	private final long transactionId;
	private final long jarId;
	private BigDecimal balanceBefore;
	private BigDecimal balanceAfter;

	public static JarEntry first(final PaymentTransaction transaction) {
		Objects.requireNonNull(transaction);

		final long transactionId = transaction.getId();
		final long jarId = transaction.getJarId();
		final BigDecimal amount = transaction.getSignedAmount();
		final BigDecimal balanceBefore = BigDecimal.ZERO;
		final BigDecimal balanceAfter = amount;

		return new JarEntry(null, transactionId, jarId, balanceBefore,
				balanceAfter);
	}

	public JarEntry(final Long id, final long transactionId, final long jarId,
			final BigDecimal balanceBefore, final BigDecimal balanceAfter) {
		Objects.requireNonNull(balanceBefore);
		Objects.requireNonNull(balanceAfter);

		this.id = id;
		this.transactionId = transactionId;
		this.jarId = jarId;
		this.balanceBefore = balanceBefore;
		this.balanceAfter = balanceAfter;
	}

	public JarEntry next(final PaymentTransaction transaction) {
		Objects.requireNonNull(transaction);
		Validate.isTrue(transaction.getJarId() == this.jarId);

		final long transactionId = transaction.getId();
		final long jarId = this.jarId;
		final BigDecimal amount = transaction.getSignedAmount();
		final BigDecimal balanceBefore = this.getBalanceAfter();
		final BigDecimal balanceAfter = balanceBefore.add(amount);

		return new JarEntry(null, transactionId, jarId, balanceBefore,
				balanceAfter);
	}

	public void shift(final BigDecimal amount) {
		Objects.requireNonNull(amount);

		this.balanceAfter = this.balanceAfter.add(amount);
		this.balanceBefore = this.balanceBefore.add(amount);
	}

}
