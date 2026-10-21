package com.cvesters.moneyjars.transaction.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.jar.TestJar;
import com.cvesters.moneyjars.transaction.TestJarTransferTransaction;
import com.cvesters.moneyjars.transaction.bdo.JarTransferTransaction;

class JarTransferTransactionDaoTest {

	private static final TestJarTransferTransaction TRANSACTION = TestJarTransferTransaction.CAR_SAVINGS;

	@Nested
	class Constructor {

		@Test
		void success() {
			final var dao = new JarTransferTransactionDao(TRANSACTION.bdo());

			assertThat(dao.getId()).isNull();
			assertThat(dao.getDate()).isEqualTo(TRANSACTION.getDate());
			assertThat(dao.getSequence()).isEqualTo(TRANSACTION.getSequence());
			assertThat(dao.getAmount()).isEqualTo(TRANSACTION.getAmount());
			assertThat(dao.getDescription())
					.isEqualTo(TRANSACTION.getDescription());
			assertThat(dao.getSourceJarId())
					.isEqualTo(TRANSACTION.getSourceJar().getId());
			assertThat(dao.getTargetJarId())
					.isEqualTo(TRANSACTION.getTargetJar().getId());
		}

		@Test
		void transactionNull() {
			assertThatThrownBy(() -> new JarTransferTransactionDao(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class UpdateWith {

		@Test
		void success() {
			final var updatedDate = TRANSACTION.getDate().plusDays(1);
			final var updatedSequence = 1;
			final var updatedAmount = TRANSACTION.getAmount()
					.add(BigDecimal.ONE);
			final var updatedDescription = "Updated description";
			final var updatedSourceJar = TestJar.HOLIDAY.getId();
			final var updatedTargetJar = TestJar.HOUSEHOLD.getId();

			final var updatedTransaction = new JarTransferTransaction(
					updatedDate, updatedSequence, updatedAmount,
					updatedDescription, updatedSourceJar, updatedTargetJar);

			final var dao = new JarTransferTransactionDao(TRANSACTION.bdo());
			dao.updateWith(updatedTransaction);

			assertThat(dao.getId()).isNull();
			assertThat(dao.getDate()).isEqualTo(updatedDate);
			assertThat(dao.getSequence()).isEqualTo(updatedSequence);
			assertThat(dao.getAmount()).isEqualTo(updatedAmount);
			assertThat(dao.getDescription()).isEqualTo(updatedDescription);
			assertThat(dao.getSourceJarId()).isEqualTo(updatedSourceJar);
			assertThat(dao.getTargetJarId()).isEqualTo(updatedTargetJar);
		}

		@Test
		void transactionNull() {
			final var dao = new JarTransferTransactionDao(TRANSACTION.bdo());

			assertThatThrownBy(() -> dao.updateWith(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class ToBdo {

		@Test
		void success() {
			final var dao = new JarTransferTransactionDao(TRANSACTION.bdo());
			final var bdo = dao.toBdo();

			assertThat(bdo.getId()).isNull();
			assertThat(bdo.getDate()).isEqualTo(TRANSACTION.getDate());
			assertThat(bdo.getSequence()).isEqualTo(TRANSACTION.getSequence());
			assertThat(bdo.getAmount()).isEqualTo(TRANSACTION.getAmount());
			assertThat(bdo.getDescription())
					.isEqualTo(TRANSACTION.getDescription());
			assertThat(bdo.getSourceJarId())
					.isEqualTo(TRANSACTION.getSourceJar().getId());
			assertThat(bdo.getTargetJarId())
					.isEqualTo(TRANSACTION.getTargetJar().getId());
		}
	}
}
