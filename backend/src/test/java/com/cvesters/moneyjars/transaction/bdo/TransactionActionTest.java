package com.cvesters.moneyjars.transaction.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.cvesters.moneyjars.transaction.TestTransaction;

class TransactionActionTest {

	private static final TestTransaction TRANSACTION = TestTransaction.RENT;

	@Nested
	class Create {

		@Test
		void success() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final long jarId = TRANSACTION.getJar().getId();
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = TRANSACTION.getDescription();

			final var action = new TransactionAction.Create(date, amount, jarId,
					beneficiary, description);

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
			final long jarId = TRANSACTION.getJar().getId();
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = TRANSACTION.getDescription();

			assertThatThrownBy(() -> new TransactionAction.Create(date, amount,
					jarId, beneficiary, description))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void amountNull() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = null;
			final long jarId = TRANSACTION.getJar().getId();
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = TRANSACTION.getDescription();

			assertThatThrownBy(() -> new TransactionAction.Create(date, amount,
					jarId, beneficiary, description))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void beneficiaryNull() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final long jarId = TRANSACTION.getJar().getId();
			final String beneficiary = null;
			final String description = TRANSACTION.getDescription();

			assertThatThrownBy(() -> new TransactionAction.Create(date, amount,
					jarId, beneficiary, description))
							.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void beneficiaryInvalid(final String beneficiary) {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final long jarId = TRANSACTION.getJar().getId();
			final String description = TRANSACTION.getDescription();

			assertThatThrownBy(() -> new TransactionAction.Create(date, amount,
					jarId, beneficiary, description))
							.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final long jarId = TRANSACTION.getJar().getId();
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = null;

			assertThatThrownBy(() -> new TransactionAction.Create(date, amount,
					jarId, beneficiary, description))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void toBdo() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final long jarId = TRANSACTION.getJar().getId();
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = TRANSACTION.getDescription();

			final var action = new TransactionAction.Create(date, amount, jarId,
					beneficiary, description);

			final Transaction bdo = action.toBdo();

			assertThat(bdo.getDate()).isEqualTo(TRANSACTION.getDate());
			assertThat(bdo.getAmount())
					.isEqualByComparingTo(TRANSACTION.getAmount());
			assertThat(bdo.getJarId()).isEqualTo(TRANSACTION.getJar().getId());
			assertThat(bdo.getBeneficiary())
					.isEqualTo(TRANSACTION.getBeneficiary());
			assertThat(bdo.getDescription())
					.isEqualTo(TRANSACTION.getDescription());
		}
	}

	@Nested
	class Update {

		@Test
		void success() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final long jarId = TRANSACTION.getJar().getId();
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = TRANSACTION.getDescription();

			final var action = new TransactionAction.Update(date, amount, jarId,
					beneficiary, description);

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
			final long jarId = TRANSACTION.getJar().getId();
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = TRANSACTION.getDescription();

			assertThatThrownBy(() -> new TransactionAction.Update(date, amount,
					jarId, beneficiary, description))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void amountNull() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = null;
			final long jarId = TRANSACTION.getJar().getId();
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = TRANSACTION.getDescription();

			assertThatThrownBy(() -> new TransactionAction.Update(date, amount,
					jarId, beneficiary, description))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void beneficiaryNull() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final long jarId = TRANSACTION.getJar().getId();
			final String beneficiary = null;
			final String description = TRANSACTION.getDescription();

			assertThatThrownBy(() -> new TransactionAction.Update(date, amount,
					jarId, beneficiary, description))
							.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void beneficiaryInvalid(final String beneficiary) {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final long jarId = TRANSACTION.getJar().getId();
			final String description = TRANSACTION.getDescription();

			assertThatThrownBy(() -> new TransactionAction.Update(date, amount,
					jarId, beneficiary, description))
							.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final long jarId = TRANSACTION.getJar().getId();
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = null;

			assertThatThrownBy(() -> new TransactionAction.Update(date, amount,
					jarId, beneficiary, description))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void applyOn() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final long jarId = TRANSACTION.getJar().getId();
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = TRANSACTION.getDescription();

			final var action = new TransactionAction.Update(date, amount, jarId,
					beneficiary, description);

			final Transaction transaction = mock();

			action.applyOn(transaction);

			verify(transaction).setDate(date);
			verify(transaction).setAmount(amount);
			verify(transaction).setJarId(jarId);
			verify(transaction).setBeneficiary(beneficiary);
			verify(transaction).setDescription(description);
		}

	}

}
