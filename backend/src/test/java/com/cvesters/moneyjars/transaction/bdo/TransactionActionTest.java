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

class TransactionActionTest {

	private static final TestPaymentTransaction TRANSACTION = TestPaymentTransaction.RENT;

	@Nested
	class CreatePayment {

		@Test
		void success() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final PaymentTransactionDirection direction = TRANSACTION.getDirection();

			final var action = new TransactionAction.CreatePayment(date, amount,
					beneficiary, description, jarId, accountId, direction);

			assertThat(action.date()).isEqualTo(TRANSACTION.getDate());
			assertThat(action.amount())
					.isEqualByComparingTo(TRANSACTION.getAmount());
			assertThat(action.jarId()).isEqualTo(TRANSACTION.getJar().getId());
			assertThat(action.beneficiary())
					.isEqualTo(TRANSACTION.getBeneficiary());
			assertThat(action.description())
					.isEqualTo(TRANSACTION.getDescription());
		}

		@Test
		void dateNull() {
			final LocalDate date = null;
			final BigDecimal amount = TRANSACTION.getAmount();
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final PaymentTransactionDirection direction = TRANSACTION.getDirection();

			assertThatThrownBy(() -> new TransactionAction.CreatePayment(date,
					amount, beneficiary, description, jarId, accountId,
					direction)).isInstanceOf(NullPointerException.class);
		}

		@Test
		void amountNull() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = null;
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final PaymentTransactionDirection direction = TRANSACTION.getDirection();

			assertThatThrownBy(() -> new TransactionAction.CreatePayment(date,
					amount, beneficiary, description, jarId, accountId,
					direction)).isInstanceOf(NullPointerException.class);
		}

		@Test
		void beneficiaryNull() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String beneficiary = null;
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final PaymentTransactionDirection direction = TRANSACTION.getDirection();

			assertThatThrownBy(() -> new TransactionAction.CreatePayment(date,
					amount, beneficiary, description, jarId, accountId,
					direction)).isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void beneficiaryInvalid(final String beneficiary) {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final PaymentTransactionDirection direction = TRANSACTION.getDirection();

			assertThatThrownBy(() -> new TransactionAction.CreatePayment(date,
					amount, beneficiary, description, jarId, accountId,
					direction)).isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = null;
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final PaymentTransactionDirection direction = TRANSACTION.getDirection();

			assertThatThrownBy(() -> new TransactionAction.CreatePayment(date,
					amount, beneficiary, description, jarId, accountId,
					direction)).isInstanceOf(NullPointerException.class);
		}

		@Test
		void directionNull() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final PaymentTransactionDirection direction = null;

			assertThatThrownBy(() -> new TransactionAction.CreatePayment(date,
					amount, beneficiary, description, jarId, accountId,
					direction)).isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Update {

		@Test
		void success() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final PaymentTransactionDirection direction = TRANSACTION.getDirection();

			final var action = new TransactionAction.UpdatePayment(date, amount,
					beneficiary, description, jarId, accountId, direction);

			assertThat(action.date()).isEqualTo(TRANSACTION.getDate());
			assertThat(action.amount())
					.isEqualByComparingTo(TRANSACTION.getAmount());
			assertThat(action.beneficiary())
					.isEqualTo(TRANSACTION.getBeneficiary());
			assertThat(action.description())
					.isEqualTo(TRANSACTION.getDescription());
			assertThat(action.jarId()).isEqualTo(TRANSACTION.getJar().getId());
			assertThat(action.accountId())
					.isEqualTo(TRANSACTION.getAccount().getId());
			assertThat(action.direction())
					.isEqualTo(TRANSACTION.getDirection());
		}

		@Test
		void dateNull() {
			final LocalDate date = null;
			final BigDecimal amount = TRANSACTION.getAmount();
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final PaymentTransactionDirection direction = TRANSACTION.getDirection();

			assertThatThrownBy(() -> new TransactionAction.UpdatePayment(date,
					amount, beneficiary, description, jarId, accountId,
					direction)).isInstanceOf(NullPointerException.class);
		}

		@Test
		void amountNull() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = null;
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final PaymentTransactionDirection direction = TRANSACTION.getDirection();

			assertThatThrownBy(() -> new TransactionAction.UpdatePayment(date,
					amount, beneficiary, description, jarId, accountId,
					direction)).isInstanceOf(NullPointerException.class);
		}

		@Test
		void beneficiaryNull() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String beneficiary = null;
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final PaymentTransactionDirection direction = TRANSACTION.getDirection();

			assertThatThrownBy(() -> new TransactionAction.UpdatePayment(date,
					amount, beneficiary, description, jarId, accountId,
					direction)).isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void beneficiaryInvalid(final String beneficiary) {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final PaymentTransactionDirection direction = TRANSACTION.getDirection();

			assertThatThrownBy(() -> new TransactionAction.UpdatePayment(date,
					amount, beneficiary, description, jarId, accountId,
					direction)).isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = null;
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final PaymentTransactionDirection direction = TRANSACTION.getDirection();

			assertThatThrownBy(() -> new TransactionAction.UpdatePayment(date,
					amount, beneficiary, description, jarId, accountId,
					direction)).isInstanceOf(NullPointerException.class);
		}

		@Test
		void directionNull() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final PaymentTransactionDirection direction = null;

			assertThatThrownBy(() -> new TransactionAction.UpdatePayment(date,
					amount, beneficiary, description, jarId, accountId,
					direction)).isInstanceOf(NullPointerException.class);
		}
	}

}
