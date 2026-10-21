package com.cvesters.moneyjars.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.cvesters.moneyjars.transaction.bdo.JarTransferTransactionAction;

public final class JarTransferTransactionActionDto {

	private JarTransferTransactionActionDto() {
	}

	public static record Create(LocalDate date, String amount,
			String description, long sourceJarId, long targetJarId) {

		public JarTransferTransactionAction.Create toBdo() {
			return new JarTransferTransactionAction.Create(date,
					new BigDecimal(amount), description, sourceJarId,
					targetJarId);
		}
	}

	public static record Update(LocalDate date, String amount,
			String description, long sourceJarId, long targetJarId) {

		public JarTransferTransactionAction.Update toBdo() {
			return new JarTransferTransactionAction.Update(date,
					new BigDecimal(amount), description, sourceJarId,
					targetJarId);
		}
	}
}
