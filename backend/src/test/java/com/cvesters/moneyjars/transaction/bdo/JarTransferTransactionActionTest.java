package com.cvesters.moneyjars.transaction.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.transaction.TestJarTransferTransaction;

class JarTransferTransactionActionTest {

	private static final TestJarTransferTransaction TRANSACTION = TestJarTransferTransaction.CAR_SAVINGS;

	@Nested
	class Create {

		@Test
		void success() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = TRANSACTION.getDescription();
			final long sourceJarId = TRANSACTION.getSourceJar().getId();
			final long targetJarId = TRANSACTION.getTargetJar().getId();

			final var action = new JarTransferTransactionAction.Create(date,
					amount, description, sourceJarId, targetJarId);

			assertThat(action.date()).isEqualTo(TRANSACTION.getDate());
			assertThat(action.amount())
					.isEqualByComparingTo(TRANSACTION.getAmount());
			assertThat(action.description())
					.isEqualTo(TRANSACTION.getDescription());
			assertThat(action.sourceJarId())
					.isEqualTo(TRANSACTION.getSourceJar().getId());
			assertThat(action.targetJarId())
					.isEqualTo(TRANSACTION.getTargetJar().getId());
		}

		@Test
		void dateNull() {
			final LocalDate date = null;
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = TRANSACTION.getDescription();
			final long sourceJarId = TRANSACTION.getSourceJar().getId();
			final long targetJarId = TRANSACTION.getTargetJar().getId();

			assertThatThrownBy(() -> new JarTransferTransactionAction.Create(
					date, amount, description, sourceJarId, targetJarId))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void amountNull() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = null;
			final String description = TRANSACTION.getDescription();
			final long sourceJarId = TRANSACTION.getSourceJar().getId();
			final long targetJarId = TRANSACTION.getTargetJar().getId();

			assertThatThrownBy(() -> new JarTransferTransactionAction.Create(
					date, amount, description, sourceJarId, targetJarId))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void amountNegative() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount().negate();
			final String description = TRANSACTION.getDescription();
			final long sourceJarId = TRANSACTION.getSourceJar().getId();
			final long targetJarId = TRANSACTION.getTargetJar().getId();

			assertThatThrownBy(() -> new JarTransferTransactionAction.Create(
					date, amount, description, sourceJarId, targetJarId))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = null;
			final long sourceJarId = TRANSACTION.getSourceJar().getId();
			final long targetJarId = TRANSACTION.getTargetJar().getId();

			assertThatThrownBy(() -> new JarTransferTransactionAction.Create(
					date, amount, description, sourceJarId, targetJarId))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Update {

		@Test
		void success() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = TRANSACTION.getDescription();
			final long sourceJarId = TRANSACTION.getSourceJar().getId();
			final long targetJarId = TRANSACTION.getTargetJar().getId();

			final var action = new JarTransferTransactionAction.Update(date,
					amount, description, sourceJarId, targetJarId);

			assertThat(action.date()).isEqualTo(TRANSACTION.getDate());
			assertThat(action.amount())
					.isEqualByComparingTo(TRANSACTION.getAmount());
			assertThat(action.description())
					.isEqualTo(TRANSACTION.getDescription());
			assertThat(action.sourceJarId())
					.isEqualTo(TRANSACTION.getSourceJar().getId());
			assertThat(action.targetJarId())
					.isEqualTo(TRANSACTION.getTargetJar().getId());
		}

		@Test
		void dateNull() {
			final LocalDate date = null;
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = TRANSACTION.getDescription();
			final long sourceJarId = TRANSACTION.getSourceJar().getId();
			final long targetJarId = TRANSACTION.getTargetJar().getId();

			assertThatThrownBy(() -> new JarTransferTransactionAction.Update(
					date, amount, description, sourceJarId, targetJarId))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void amountNull() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = null;
			final String description = TRANSACTION.getDescription();
			final long sourceJarId = TRANSACTION.getSourceJar().getId();
			final long targetJarId = TRANSACTION.getTargetJar().getId();

			assertThatThrownBy(() -> new JarTransferTransactionAction.Update(
					date, amount, description, sourceJarId, targetJarId))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void amountNegative() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount().negate();
			final String description = TRANSACTION.getDescription();
			final long sourceJarId = TRANSACTION.getSourceJar().getId();
			final long targetJarId = TRANSACTION.getTargetJar().getId();

			assertThatThrownBy(() -> new JarTransferTransactionAction.Update(
					date, amount, description, sourceJarId, targetJarId))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = null;
			final long sourceJarId = TRANSACTION.getSourceJar().getId();
			final long targetJarId = TRANSACTION.getTargetJar().getId();

			assertThatThrownBy(() -> new JarTransferTransactionAction.Update(
					date, amount, description, sourceJarId, targetJarId))
					.isInstanceOf(NullPointerException.class);
		}
	}

}
