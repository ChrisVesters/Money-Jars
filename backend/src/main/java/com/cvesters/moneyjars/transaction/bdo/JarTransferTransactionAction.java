package com.cvesters.moneyjars.transaction.bdo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

public final class JarTransferTransactionAction {

	private JarTransferTransactionAction() {
	}

	public static record Create(LocalDate date, BigDecimal amount,
			String description, long sourceJarId, long targetJarId) {

		public Create {
			Objects.requireNonNull(date);
			Objects.requireNonNull(amount);
			Validate.isTrue(amount.signum() != -1);
			Objects.requireNonNull(description);
		}
	}

	public static record Update(LocalDate date, BigDecimal amount,
			String description, long sourceJarId, long targetJarId) {

		public Update {
			Objects.requireNonNull(date);
			Objects.requireNonNull(amount);
			Validate.isTrue(amount.signum() != -1);
			Objects.requireNonNull(description);
		}
	}
}
