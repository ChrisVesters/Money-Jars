package com.cvesters.moneyjars.transaction.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();

			final var transaction = new PaymentTransaction(date, sequence,
					amount, beneficiary, description, jarId, accountId,
					PaymentTransactionDirection.OUTGOING);

			assertThat(transaction.getId()).isNull();
			assertThat(transaction.getDate()).isEqualTo(date);
			assertThat(transaction.getSequence()).isEqualTo(sequence);
			assertThat(transaction.getAmount()).isEqualByComparingTo(amount);
			assertThat(transaction.getBeneficiary()).isEqualTo(beneficiary);
			assertThat(transaction.getDescription()).isEqualTo(description);
			assertThat(transaction.getJarId()).isEqualTo(jarId);
			assertThat(transaction.getAccountId()).isEqualTo(accountId);
			assertThat(transaction.getDirection())
					.isEqualTo(TRANSACTION.getDirection());
		}

		@Test
		void withId() {
			final long id = TRANSACTION.getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();

			final var transaction = new PaymentTransaction(id, date, sequence,
					amount, beneficiary, description, jarId, accountId,
					PaymentTransactionDirection.OUTGOING);

			assertThat(transaction.getId()).isEqualTo(id);
			assertThat(transaction.getJarId()).isEqualTo(jarId);
			assertThat(transaction.getAccountId()).isEqualTo(accountId);
			assertThat(transaction.getDirection())
					.isEqualTo(TRANSACTION.getDirection());
		}

		@Test
		void directionNull() {
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();

			assertThatThrownBy(() -> new PaymentTransaction(date, sequence,
					amount, beneficiary, description, jarId, accountId, null))
							.isInstanceOf(NullPointerException.class);
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

	@Override
	public PaymentTransaction getTransaction() {
		return TRANSACTION.bdo();
	}
}
