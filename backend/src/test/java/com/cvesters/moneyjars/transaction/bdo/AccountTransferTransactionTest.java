package com.cvesters.moneyjars.transaction.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.account.TestAccount;
import com.cvesters.moneyjars.transaction.TestAccountTransferTransaction;

class AccountTransferTransactionTest extends TransactionTest {

	private static final TestAccountTransferTransaction TRANSACTION = TestAccountTransferTransaction.CASH_WITHDRAWAL;

	@Nested
	class Constructor {

		@Test
		void withoutId() {
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = TRANSACTION.getDescription();
			final long sourceAccountId = TRANSACTION.getSourceAccount().getId();
			final long targetAccountId = TRANSACTION.getTargetAccount().getId();

			final var transaction = new AccountTransferTransaction(date,
					sequence, amount, description, sourceAccountId,
					targetAccountId);

			assertThat(transaction.getId()).isNull();
			assertThat(transaction.getDate()).isEqualTo(date);
			assertThat(transaction.getSequence()).isEqualTo(sequence);
			assertThat(transaction.getAmount()).isEqualByComparingTo(amount);
			assertThat(transaction.getDescription()).isEqualTo(description);
			assertThat(transaction.getSourceAccountId())
					.isEqualTo(sourceAccountId);
			assertThat(transaction.getTargetAccountId())
					.isEqualTo(targetAccountId);
		}

		@Test
		void withId() {
			final long id = TRANSACTION.getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = TRANSACTION.getDescription();
			final long sourceAccountId = TRANSACTION.getSourceAccount().getId();
			final long targetAccountId = TRANSACTION.getTargetAccount().getId();

			final var transaction = new AccountTransferTransaction(id, date,
					sequence, amount, description, sourceAccountId,
					targetAccountId);

			assertThat(transaction.getId()).isEqualTo(id);
			assertThat(transaction.getDate()).isEqualTo(date);
			assertThat(transaction.getSequence()).isEqualTo(sequence);
			assertThat(transaction.getAmount()).isEqualByComparingTo(amount);
			assertThat(transaction.getDescription()).isEqualTo(description);
			assertThat(transaction.getSourceAccountId())
					.isEqualTo(sourceAccountId);
			assertThat(transaction.getTargetAccountId())
					.isEqualTo(targetAccountId);
		}

		@Test
		void dateNull() {
			final LocalDate date = null;
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = TRANSACTION.getDescription();
			final long sourceAccountId = TRANSACTION.getSourceAccount().getId();
			final long targetAccountId = TRANSACTION.getTargetAccount().getId();

			assertThatThrownBy(
					() -> new AccountTransferTransaction(date, sequence, amount,
							description, sourceAccountId, targetAccountId))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void amountNull() {
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = null;
			final String description = TRANSACTION.getDescription();
			final long sourceAccountId = TRANSACTION.getSourceAccount().getId();
			final long targetAccountId = TRANSACTION.getTargetAccount().getId();

			assertThatThrownBy(
					() -> new AccountTransferTransaction(date, sequence, amount,
							description, sourceAccountId, targetAccountId))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void amountNegative() {
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount().negate();
			final String description = TRANSACTION.getDescription();
			final long sourceAccountId = TRANSACTION.getSourceAccount().getId();
			final long targetAccountId = TRANSACTION.getTargetAccount().getId();

			assertThatThrownBy(
					() -> new AccountTransferTransaction(date, sequence, amount,
							description, sourceAccountId, targetAccountId))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = null;
			final long sourceAccountId = TRANSACTION.getSourceAccount().getId();
			final long targetAccountId = TRANSACTION.getTargetAccount().getId();

			assertThatThrownBy(
					() -> new AccountTransferTransaction(date, sequence, amount,
							description, sourceAccountId, targetAccountId))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class GetSignedAmount {

		private final AccountTransferTransaction transaction = getTransaction();

		@Test
		void sourceAccount() {
			final long accountId = TRANSACTION.getSourceAccount().getId();

			final BigDecimal expected = TRANSACTION.getAmount().negate();

			assertThat(transaction.getSignedAmount(accountId)).isNegative()
					.isEqualByComparingTo(expected);
		}

		@Test
		void targetAccount() {
			final long accountId = TRANSACTION.getTargetAccount().getId();

			final BigDecimal expected = TRANSACTION.getAmount();

			assertThat(transaction.getSignedAmount(accountId)).isPositive()
					.isEqualByComparingTo(expected);
		}

		@Test
		void otherAccount() {
			final long accountId = TestAccount.SAVINGS.getId();

			assertThatThrownBy(() -> transaction.getSignedAmount(accountId))
					.isInstanceOf(IllegalArgumentException.class);
		}
	}

	@Nested
	class Involves {

		private final AccountTransferTransaction transaction = getTransaction();

		@Test
		void sourceAccount() {
			final long accountId = TRANSACTION.getSourceAccount().getId();

			assertThat(transaction.involves(accountId)).isTrue();
		}

		@Test
		void targetAccount() {
			final long accountId = TRANSACTION.getTargetAccount().getId();

			assertThat(transaction.involves(accountId)).isTrue();
		}

		@Test
		void otherAccount() {
			final long accountId = TestAccount.SAVINGS.getId();

			assertThat(transaction.involves(accountId)).isFalse();
		}
	}

	@Override
	public AccountTransferTransaction getTransaction() {
		return TRANSACTION.bdo();
	}
}
