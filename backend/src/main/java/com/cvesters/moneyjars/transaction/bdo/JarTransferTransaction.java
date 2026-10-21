package com.cvesters.moneyjars.transaction.bdo;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.apache.commons.lang3.Validate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class JarTransferTransaction extends Transaction {

	private long sourceJarId;
	private long targetJarId;

	public JarTransferTransaction(final LocalDate date, final int sequence,
			final BigDecimal amount, final String description,
			final long sourceJarId, final long targetJarId) {
		this(null, date, sequence, amount, description, sourceJarId,
				targetJarId);
	}

	public JarTransferTransaction(final Long id, final LocalDate date,
			final int sequence, final BigDecimal amount,
			final String description, final long sourceJarId,
			final long targetJarId) {
		super(id, date, sequence, amount, description);

		this.sourceJarId = sourceJarId;
		this.targetJarId = targetJarId;
	}

	public BigDecimal getSignedAmount(final long jarId) {
		Validate.isTrue(involves(jarId));

		final BigDecimal amount = getAmount();
		return jarId == sourceJarId ? amount.negate() : amount;
	}

	public boolean involves(final long jarId) {
		return jarId == sourceJarId || jarId == targetJarId;
	}
}
