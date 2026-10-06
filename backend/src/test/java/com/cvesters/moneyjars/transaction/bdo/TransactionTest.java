package com.cvesters.moneyjars.transaction.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

abstract class TransactionTest {

	public abstract Transaction getTransaction();

	private final Transaction transaction = getTransaction();

	@Nested
	class SetDate {

		@Test
		void success() {
			final var date = LocalDate.of(2027, Month.JANUARY, 1);

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
	class SetDescription {

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
