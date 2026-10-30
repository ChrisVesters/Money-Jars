package com.cvesters.moneyjars.transaction.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.cvesters.moneyjars.transaction.TestPaymentTransaction;

class PaymentTransactionTest extends TransactionTest {

	private static final TestPaymentTransaction TRANSACTION = TestPaymentTransaction.RENT;

	@Nested
	class Constructor {

		@Test
		void withoutId() {
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final Long categoryId = TRANSACTION.getCategoryId();
			final String counterparty = TRANSACTION.getCounterparty();
			final PaymentTransactionDirection direction = TRANSACTION
					.getDirection();

			final var transaction = new PaymentTransaction(date, sequence,
					amount, description, jarId, accountId, categoryId,
					counterparty, direction);

			assertThat(transaction.getId()).isNull();
			assertThat(transaction.getDate()).isEqualTo(date);
			assertThat(transaction.getSequence()).isEqualTo(sequence);
			assertThat(transaction.getAmount()).isEqualByComparingTo(amount);
			assertThat(transaction.getDescription()).isEqualTo(description);
			assertThat(transaction.getJarId()).isEqualTo(jarId);
			assertThat(transaction.getAccountId()).isEqualTo(accountId);
			assertThat(transaction.getCategoryId()).isEqualTo(categoryId);
			assertThat(transaction.getCounterparty()).isEqualTo(counterparty);
			assertThat(transaction.getDirection())
					.isEqualTo(TRANSACTION.getDirection());
		}

		@Test
		void withId() {
			final long id = TRANSACTION.getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final Long categoryId = TRANSACTION.getCategoryId();
			final String counterparty = TRANSACTION.getCounterparty();
			final PaymentTransactionDirection direction = TRANSACTION
					.getDirection();

			final var transaction = new PaymentTransaction(id, date, sequence,
					amount, description, jarId, accountId, categoryId,
					counterparty, direction);

			assertThat(transaction.getId()).isEqualTo(id);
			assertThat(transaction.getDate()).isEqualTo(date);
			assertThat(transaction.getSequence()).isEqualTo(sequence);
			assertThat(transaction.getAmount()).isEqualByComparingTo(amount);
			assertThat(transaction.getDescription()).isEqualTo(description);
			assertThat(transaction.getJarId()).isEqualTo(jarId);
			assertThat(transaction.getAccountId()).isEqualTo(accountId);
			assertThat(transaction.getCategoryId()).isEqualTo(categoryId);
			assertThat(transaction.getCounterparty()).isEqualTo(counterparty);
			assertThat(transaction.getDirection())
					.isEqualTo(TRANSACTION.getDirection());
		}

		@Test
		void categoryNull() {
			final long id = TRANSACTION.getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final Long categoryId = null;
			final String counterparty = TRANSACTION.getCounterparty();
			final PaymentTransactionDirection direction = TRANSACTION
					.getDirection();

			final var transaction = new PaymentTransaction(id, date, sequence,
					amount, description, jarId, accountId, categoryId,
					counterparty, direction);

			assertThat(transaction.getId()).isEqualTo(id);
			assertThat(transaction.getDate()).isEqualTo(date);
			assertThat(transaction.getSequence()).isEqualTo(sequence);
			assertThat(transaction.getAmount()).isEqualByComparingTo(amount);
			assertThat(transaction.getDescription()).isEqualTo(description);
			assertThat(transaction.getJarId()).isEqualTo(jarId);
			assertThat(transaction.getAccountId()).isEqualTo(accountId);
			assertThat(transaction.getCategoryId()).isNull();
			assertThat(transaction.getCounterparty()).isEqualTo(counterparty);
			assertThat(transaction.getDirection())
					.isEqualTo(TRANSACTION.getDirection());
		}

		@Test
		void dateNull() {
			final LocalDate date = null;
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final Long categoryId = TRANSACTION.getCategoryId();
			final String counterparty = TRANSACTION.getCounterparty();
			final PaymentTransactionDirection direction = TRANSACTION
					.getDirection();

			assertThatThrownBy(() -> new PaymentTransaction(date, sequence,
					amount, description, jarId, accountId, categoryId,
					counterparty, direction))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void amountNull() {
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = null;
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final Long categoryId = TRANSACTION.getCategoryId();
			final String counterparty = TRANSACTION.getCounterparty();
			final PaymentTransactionDirection direction = TRANSACTION
					.getDirection();

			assertThatThrownBy(() -> new PaymentTransaction(date, sequence,
					amount, description, jarId, accountId, categoryId,
					counterparty, direction))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void amountNegative() {
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount().negate();
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final Long categoryId = TRANSACTION.getCategoryId();
			final String counterparty = TRANSACTION.getCounterparty();
			final PaymentTransactionDirection direction = TRANSACTION
					.getDirection();

			assertThatThrownBy(() -> new PaymentTransaction(date, sequence,
					amount, description, jarId, accountId, categoryId,
					counterparty, direction))
							.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = null;
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final Long categoryId = TRANSACTION.getCategoryId();
			final String counterparty = TRANSACTION.getCounterparty();
			final PaymentTransactionDirection direction = TRANSACTION
					.getDirection();

			assertThatThrownBy(() -> new PaymentTransaction(date, sequence,
					amount, description, jarId, accountId, categoryId,
					counterparty, direction))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void directionNull() {
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final Long categoryId = TRANSACTION.getCategoryId();
			final String counterparty = TRANSACTION.getCounterparty();
			final PaymentTransactionDirection direction = null;

			assertThatThrownBy(() -> new PaymentTransaction(date, sequence,
					amount, description, jarId, accountId, categoryId,
					counterparty, direction))
							.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class SetCounterparty {

		private final PaymentTransaction transaction = getTransaction();

		@Test
		void success() {
			final var counterparty = "Bank";

			transaction.setCounterparty(counterparty);

			assertThat(transaction.getCounterparty()).isEqualTo(counterparty);
		}

		@Test
		void counterpartyNull() {
			assertThatThrownBy(() -> transaction.setCounterparty(null))
					.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void counterpartyInvalid(final String counterparty) {
			assertThatThrownBy(() -> transaction.setCounterparty(counterparty))
					.isInstanceOf(IllegalArgumentException.class);
		}
	}

	@Nested
	class setDirection {

		private final PaymentTransaction transaction = getTransaction();

		@Test
		void success() {
			final var direction = PaymentTransactionDirection.INCOMING;

			transaction.setDirection(direction);

			assertThat(transaction.getDirection()).isEqualTo(direction);
		}

		@Test
		void directionNull() {
			assertThatThrownBy(() -> transaction.setDirection(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class GetSignedAmount {

		@Test
		void incoming() {
			final var testTransaction = TestPaymentTransaction.SALARY;
			final var transaction = testTransaction.bdo();

			final BigDecimal expected = testTransaction.getAmount();

			assertThat(transaction.getSignedAmount()).isPositive()
					.isEqualByComparingTo(expected);
		}

		@Test
		void outgoing() {
			final var testTransaction = TestPaymentTransaction.GROCERY;
			final var transaction = testTransaction.bdo();

			final BigDecimal expected = testTransaction.getAmount().negate();

			assertThat(transaction.getSignedAmount()).isNegative()
					.isEqualByComparingTo(expected);
		}
	}

	@Override
	public PaymentTransaction getTransaction() {
		return TRANSACTION.bdo();
	}
}
