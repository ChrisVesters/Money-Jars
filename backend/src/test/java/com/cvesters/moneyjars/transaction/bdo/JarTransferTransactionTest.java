package com.cvesters.moneyjars.transaction.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.jar.TestJar;
import com.cvesters.moneyjars.transaction.TestJarTransferTransaction;

class JarTransferTransactionTest extends TransactionTest {

	private static final TestJarTransferTransaction TRANSACTION = TestJarTransferTransaction.CAR_SAVINGS;

	@Nested
	class Constructor {

		@Test
		void withoutId() {
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = TRANSACTION.getDescription();
			final long sourceJarId = TRANSACTION.getSourceJar().getId();
			final long targetJarId = TRANSACTION.getTargetJar().getId();

			final var transaction = new JarTransferTransaction(date, sequence,
					amount, description, sourceJarId, targetJarId);

			assertThat(transaction.getId()).isNull();
			assertThat(transaction.getDate()).isEqualTo(date);
			assertThat(transaction.getSequence()).isEqualTo(sequence);
			assertThat(transaction.getAmount()).isEqualByComparingTo(amount);
			assertThat(transaction.getDescription()).isEqualTo(description);
			assertThat(transaction.getSourceJarId()).isEqualTo(sourceJarId);
			assertThat(transaction.getTargetJarId()).isEqualTo(targetJarId);
		}

		@Test
		void withId() {
			final long id = TRANSACTION.getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = TRANSACTION.getDescription();
			final long sourceJarId = TRANSACTION.getSourceJar().getId();
			final long targetJarId = TRANSACTION.getTargetJar().getId();

			final var transaction = new JarTransferTransaction(id, date,
					sequence, amount, description, sourceJarId, targetJarId);

			assertThat(transaction.getId()).isEqualTo(id);
			assertThat(transaction.getDate()).isEqualTo(date);
			assertThat(transaction.getSequence()).isEqualTo(sequence);
			assertThat(transaction.getAmount()).isEqualByComparingTo(amount);
			assertThat(transaction.getDescription()).isEqualTo(description);
			assertThat(transaction.getSourceJarId()).isEqualTo(sourceJarId);
			assertThat(transaction.getTargetJarId()).isEqualTo(targetJarId);
		}

		@Test
		void dateNull() {
			final LocalDate date = null;
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = TRANSACTION.getDescription();
			final long sourceJarId = TRANSACTION.getSourceJar().getId();
			final long targetJarId = TRANSACTION.getTargetJar().getId();

			assertThatThrownBy(() -> new JarTransferTransaction(date, sequence,
					amount, description, sourceJarId, targetJarId))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void amountNull() {
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = null;
			final String description = TRANSACTION.getDescription();
			final long sourceJarId = TRANSACTION.getSourceJar().getId();
			final long targetJarId = TRANSACTION.getTargetJar().getId();

			assertThatThrownBy(() -> new JarTransferTransaction(date, sequence,
					amount, description, sourceJarId, targetJarId))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void amountNegative() {
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount().negate();
			final String description = TRANSACTION.getDescription();
			final long sourceJarId = TRANSACTION.getSourceJar().getId();
			final long targetJarId = TRANSACTION.getTargetJar().getId();

			assertThatThrownBy(() -> new JarTransferTransaction(date, sequence,
					amount, description, sourceJarId, targetJarId))
							.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = null;
			final long sourceJarId = TRANSACTION.getSourceJar().getId();
			final long targetJarId = TRANSACTION.getTargetJar().getId();

			assertThatThrownBy(() -> new JarTransferTransaction(date, sequence,
					amount, description, sourceJarId, targetJarId))
							.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class GetSignedAmount {

		private final JarTransferTransaction transaction = getTransaction();

		@Test
		void sourceJar() {
			final long jarId = TRANSACTION.getSourceJar().getId();

			final BigDecimal expected = TRANSACTION.getAmount().negate();

			assertThat(transaction.getSignedAmount(jarId)).isNegative()
					.isEqualByComparingTo(expected);
		}

		@Test
		void targetJar() {
			final long jarId = TRANSACTION.getTargetJar().getId();

			final BigDecimal expected = TRANSACTION.getAmount();

			assertThat(transaction.getSignedAmount(jarId)).isPositive()
					.isEqualByComparingTo(expected);
		}

		@Test
		void otherJar() {
			final long jarId = TestJar.HOLIDAY.getId();

			assertThatThrownBy(() -> transaction.getSignedAmount(jarId))
					.isInstanceOf(IllegalArgumentException.class);
		}
	}

	@Nested
	class Involves {

		private final JarTransferTransaction transaction = getTransaction();

		@Test
		void sourceJar() {
			final long jarId = TRANSACTION.getSourceJar().getId();

			assertThat(transaction.involves(jarId)).isTrue();
		}

		@Test
		void targetJar() {
			final long jarId = TRANSACTION.getTargetJar().getId();

			assertThat(transaction.involves(jarId)).isTrue();
		}

		@Test
		void otherJar() {
			final long jarId = TestJar.HOLIDAY.getId();

			assertThat(transaction.involves(jarId)).isFalse();
		}
	}

	@Override
	public JarTransferTransaction getTransaction() {
		return TRANSACTION.bdo();
	}
}
