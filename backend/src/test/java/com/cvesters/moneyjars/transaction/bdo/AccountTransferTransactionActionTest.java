package com.cvesters.moneyjars.transaction.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.transaction.TestAccountTransferTransaction;

class AccountTransferTransactionActionTest {

	private static final TestAccountTransferTransaction TRANSACTION = TestAccountTransferTransaction.CASH_WITHDRAWAL;

	@Nested
	class Create {

		@Test
		void success() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = TRANSACTION.getDescription();
			final long sourceAccountId = TRANSACTION.getSourceAccount().getId();
			final long targetAccountId = TRANSACTION.getTargetAccount().getId();

			final var action = new AccountTransferTransactionAction.Create(date,
					amount, description, sourceAccountId, targetAccountId);

			assertThat(action.date()).isEqualTo(TRANSACTION.getDate());
			assertThat(action.amount())
					.isEqualByComparingTo(TRANSACTION.getAmount());
			assertThat(action.description())
					.isEqualTo(TRANSACTION.getDescription());
			assertThat(action.sourceAccountId())
					.isEqualTo(TRANSACTION.getSourceAccount().getId());
			assertThat(action.targetAccountId())
					.isEqualTo(TRANSACTION.getTargetAccount().getId());
		}

		@Test
		void dateNull() {
			final LocalDate date = null;
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = TRANSACTION.getDescription();
			final long sourceAccountId = TRANSACTION.getSourceAccount().getId();
			final long targetAccountId = TRANSACTION.getTargetAccount().getId();

			assertThatThrownBy(
					() -> new AccountTransferTransactionAction.Create(date,
							amount, description, sourceAccountId,
							targetAccountId))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void amountNull() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = null;
			final String description = TRANSACTION.getDescription();
			final long sourceAccountId = TRANSACTION.getSourceAccount().getId();
			final long targetAccountId = TRANSACTION.getTargetAccount().getId();

			assertThatThrownBy(
					() -> new AccountTransferTransactionAction.Create(date,
							amount, description, sourceAccountId,
							targetAccountId))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void amountNegative() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount().negate();
			final String description = TRANSACTION.getDescription();
			final long sourceAccountId = TRANSACTION.getSourceAccount().getId();
			final long targetAccountId = TRANSACTION.getTargetAccount().getId();

			assertThatThrownBy(
					() -> new AccountTransferTransactionAction.Create(date,
							amount, description, sourceAccountId,
							targetAccountId))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = null;
			final long sourceAccountId = TRANSACTION.getSourceAccount().getId();
			final long targetAccountId = TRANSACTION.getTargetAccount().getId();

			assertThatThrownBy(
					() -> new AccountTransferTransactionAction.Create(date,
							amount, description, sourceAccountId,
							targetAccountId))
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
			final long sourceAccountId = TRANSACTION.getSourceAccount().getId();
			final long targetAccountId = TRANSACTION.getTargetAccount().getId();

			final var action = new AccountTransferTransactionAction.Update(date,
					amount, description, sourceAccountId, targetAccountId);

			assertThat(action.date()).isEqualTo(TRANSACTION.getDate());
			assertThat(action.amount())
					.isEqualByComparingTo(TRANSACTION.getAmount());
			assertThat(action.description())
					.isEqualTo(TRANSACTION.getDescription());
			assertThat(action.sourceAccountId())
					.isEqualTo(TRANSACTION.getSourceAccount().getId());
			assertThat(action.targetAccountId())
					.isEqualTo(TRANSACTION.getTargetAccount().getId());
		}

		@Test
		void dateNull() {
			final LocalDate date = null;
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = TRANSACTION.getDescription();
			final long sourceAccountId = TRANSACTION.getSourceAccount().getId();
			final long targetAccountId = TRANSACTION.getTargetAccount().getId();

			assertThatThrownBy(
					() -> new AccountTransferTransactionAction.Update(date,
							amount, description, sourceAccountId,
							targetAccountId))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void amountNull() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = null;
			final String description = TRANSACTION.getDescription();
			final long sourceAccountId = TRANSACTION.getSourceAccount().getId();
			final long targetAccountId = TRANSACTION.getTargetAccount().getId();

			assertThatThrownBy(
					() -> new AccountTransferTransactionAction.Update(date,
							amount, description, sourceAccountId,
							targetAccountId))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void amountNegative() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount().negate();
			final String description = TRANSACTION.getDescription();
			final long sourceAccountId = TRANSACTION.getSourceAccount().getId();
			final long targetAccountId = TRANSACTION.getTargetAccount().getId();

			assertThatThrownBy(
					() -> new AccountTransferTransactionAction.Update(date,
							amount, description, sourceAccountId,
							targetAccountId))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = null;
			final long sourceAccountId = TRANSACTION.getSourceAccount().getId();
			final long targetAccountId = TRANSACTION.getTargetAccount().getId();

			assertThatThrownBy(
					() -> new AccountTransferTransactionAction.Update(date,
							amount, description, sourceAccountId,
							targetAccountId))
					.isInstanceOf(NullPointerException.class);
		}
	}

}
