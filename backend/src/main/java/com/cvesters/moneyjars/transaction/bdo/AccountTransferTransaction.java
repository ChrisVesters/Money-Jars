package com.cvesters.moneyjars.transaction.bdo;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.apache.commons.lang3.Validate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class AccountTransferTransaction extends Transaction {

	private long sourceAccountId;
	private long targetAccountId;

	public AccountTransferTransaction(final LocalDate date, final int sequence,
			final BigDecimal amount, final String description,
			final long sourceAccountId, final long targetAccountId) {
		this(null, date, sequence, amount, description, sourceAccountId,
				targetAccountId);
	}

	public AccountTransferTransaction(final Long id, final LocalDate date,
			final int sequence, final BigDecimal amount,
			final String description, final long sourceAccountId,
			final long targetAccountId) {
		super(id, date, sequence, amount, description);

		this.sourceAccountId = sourceAccountId;
		this.targetAccountId = targetAccountId;
	}

	public BigDecimal getSignedAmount(final long accountId) {
		Validate.isTrue(involves(accountId));

		final BigDecimal amount = getAmount();
		return accountId == sourceAccountId ? amount.negate() : amount;
	}

	public boolean involves(final long accountId) {
		return accountId == sourceAccountId || accountId == targetAccountId;
	}
}
