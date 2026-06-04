package com.cvesters.moneyjars.transaction.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.cvesters.moneyjars.transaction.TestTransaction;

class TransactionTest {

	@Nested
	class Constructor {

		private static final TestTransaction TRANSACTION = TestTransaction.RENT;

		@Test
		void withoutId() {
			final var transaction = new Transaction(TRANSACTION.getDate(),
					TRANSACTION.getAmount(), TRANSACTION.getJar().getId(),
					TRANSACTION.getBeneficiary(), TRANSACTION.getDescription());

			assertThat(transaction.getId()).isNull();
			assertThat(transaction.getDate()).isEqualTo(TRANSACTION.getDate());
			assertThat(transaction.getAmount())
					.isEqualByComparingTo(TRANSACTION.getAmount());
			assertThat(transaction.getJarId())
					.isEqualTo(TRANSACTION.getJar().getId());
			assertThat(transaction.getBeneficiary())
					.isEqualTo(TRANSACTION.getBeneficiary());
			assertThat(transaction.getDescription())
					.isEqualTo(TRANSACTION.getDescription());
		}

		@Test
		void withId() {
			final var transaction = new Transaction(TRANSACTION.getId(),
					TRANSACTION.getDate(), TRANSACTION.getAmount(),
					TRANSACTION.getJar().getId(), TRANSACTION.getBeneficiary(),
					TRANSACTION.getDescription());

			assertThat(transaction.getId()).isEqualTo(TRANSACTION.getId());
			assertThat(transaction.getDate()).isEqualTo(TRANSACTION.getDate());
			assertThat(transaction.getAmount())
					.isEqualByComparingTo(TRANSACTION.getAmount());
			assertThat(transaction.getJarId())
					.isEqualTo(TRANSACTION.getJar().getId());
			assertThat(transaction.getBeneficiary())
					.isEqualTo(TRANSACTION.getBeneficiary());
			assertThat(transaction.getDescription())
					.isEqualTo(TRANSACTION.getDescription());
		}

		@Test
		void dateNull() {
			final long id = TRANSACTION.getId();
			final LocalDate date = null;
			final BigDecimal amount = TRANSACTION.getAmount();
			final long jarId = TRANSACTION.getJar().getId();
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = TRANSACTION.getDescription();

			assertThatThrownBy(() -> new Transaction(id, date, amount, jarId,
					beneficiary, description))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void amountNull() {
			final long id = TRANSACTION.getId();
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = null;
			final long jarId = TRANSACTION.getJar().getId();
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = TRANSACTION.getDescription();

			assertThatThrownBy(() -> new Transaction(id, date, amount, jarId,
					beneficiary, description))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void beneficiaryNull() {
			final long id = TRANSACTION.getId();
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final long jarId = TRANSACTION.getJar().getId();
			final String beneficiary = null;
			final String description = TRANSACTION.getDescription();

			assertThatThrownBy(() -> new Transaction(id, date, amount, jarId,
					beneficiary, description))
							.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void beneficiaryInvalid(final String beneficiary) {
			final long id = TRANSACTION.getId();
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final long jarId = TRANSACTION.getJar().getId();
			final String description = TRANSACTION.getDescription();

			assertThatThrownBy(() -> new Transaction(id, date, amount, jarId,
					beneficiary, description))
							.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			final long id = TRANSACTION.getId();
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final long jarId = TRANSACTION.getJar().getId();
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = null;

			assertThatThrownBy(() -> new Transaction(id, date, amount, jarId,
					beneficiary, description))
							.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class SetDate {

		private final Transaction transaction = TestTransaction.RENT.bdo();

		@Test
		void success() {
			final var date = LocalDate.of(2027, 1, 1);

			transaction.setDate(date);

			assertThat(transaction.getDate()).isEqualTo(date);
		}

		@Test
		void dateNull() {
			assertThatThrownBy(() -> transaction.setDate(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class SetAmount {

		private final Transaction transaction = TestTransaction.RENT.bdo();

		@Test
		void success() {
			final var amount = BigDecimal.ONE;

			transaction.setAmount(amount);

			assertThat(transaction.getAmount()).isEqualTo(amount);
		}

		@Test
		void amountNull() {
			assertThatThrownBy(() -> transaction.setAmount(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class SetJarId {

		private final Transaction transaction = TestTransaction.RENT.bdo();

		@Test
		void success() {
			final var jarId = 34L;

			transaction.setJarId(jarId);

			assertThat(transaction.getJarId()).isEqualTo(jarId);
		}
	}

	@Nested
	class SetBeneficiary {

		private final Transaction transaction = TestTransaction.RENT.bdo();

		@Test
		void success() {
			final var beneficiary = "Bank";

			transaction.setBeneficiary(beneficiary);

			assertThat(transaction.getBeneficiary()).isEqualTo(beneficiary);
		}

		@Test
		void beneficiaryNull() {
			assertThatThrownBy(() -> transaction.setBeneficiary(null))
					.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void beneficiaryInvalid(final String beneficiary) {
			assertThatThrownBy(() -> transaction.setBeneficiary(beneficiary))
					.isInstanceOf(IllegalArgumentException.class);
		}
	}

	@Nested
	class SetDescription {

		private final Transaction transaction = TestTransaction.RENT.bdo();

		@Test
		void success() {
			final var description = "Description";

			transaction.setDescription(description);

			assertThat(transaction.getDescription()).isEqualTo(description);
		}

		@Test
		void descriptionNull() {
			assertThatThrownBy(() -> transaction.setDescription(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

}
