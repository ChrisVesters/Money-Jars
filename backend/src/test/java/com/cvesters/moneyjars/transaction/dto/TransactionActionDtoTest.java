package com.cvesters.moneyjars.transaction.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.transaction.TestTransaction;
import com.cvesters.moneyjars.transaction.bdo.TransactionAction;

class TransactionActionDtoTest {

	private static final TestTransaction TRANSACTION = TestTransaction.RENT;

	@Nested
	class Create {

		@Test
		void success() {
			final var dto = new TransactionActionDto.Create(
					TRANSACTION.getDate(), TRANSACTION.getAmount(),
					TRANSACTION.getJar().getId(), TRANSACTION.getBeneficiary(),
					TRANSACTION.getDescription());

			assertThat(dto.date()).isEqualTo(TRANSACTION.getDate().toString());
			assertThat(dto.amount())
					.isEqualByComparingTo(TRANSACTION.getAmount());
			assertThat(dto.jarId()).isEqualTo(TRANSACTION.getJar().getId());
			assertThat(dto.beneficiary())
					.isEqualTo(TRANSACTION.getBeneficiary());
			assertThat(dto.description())
					.isEqualTo(TRANSACTION.getDescription());
		}

		@Test
		void toBdo() {
			final var dto = new TransactionActionDto.Create(
					TRANSACTION.getDate(), TRANSACTION.getAmount(),
					TRANSACTION.getJar().getId(), TRANSACTION.getBeneficiary(),
					TRANSACTION.getDescription());

			final TransactionAction.Create bdo = dto.toBdo();

			assertThat(bdo.date()).isEqualTo(TRANSACTION.getDate());
			assertThat(bdo.amount())
					.isEqualByComparingTo(TRANSACTION.getAmount());
			assertThat(bdo.jarId()).isEqualTo(TRANSACTION.getJar().getId());
			assertThat(bdo.beneficiary())
					.isEqualTo(TRANSACTION.getBeneficiary());
			assertThat(bdo.description())
					.isEqualTo(TRANSACTION.getDescription());
		}
	}

	@Nested
	class Update {

		@Test
		void success() {
			final var dto = new TransactionActionDto.Update(
					TRANSACTION.getDate(), TRANSACTION.getAmount(),
					TRANSACTION.getJar().getId(), TRANSACTION.getBeneficiary(),
					TRANSACTION.getDescription());

			assertThat(dto.date()).isEqualTo(TRANSACTION.getDate().toString());
			assertThat(dto.amount())
					.isEqualByComparingTo(TRANSACTION.getAmount());
			assertThat(dto.jarId()).isEqualTo(TRANSACTION.getJar().getId());
			assertThat(dto.beneficiary())
					.isEqualTo(TRANSACTION.getBeneficiary());
			assertThat(dto.description())
					.isEqualTo(TRANSACTION.getDescription());
		}

		@Test
		void toBdo() {
			final var dto = new TransactionActionDto.Update(
					TRANSACTION.getDate(), TRANSACTION.getAmount(),
					TRANSACTION.getJar().getId(), TRANSACTION.getBeneficiary(),
					TRANSACTION.getDescription());

			final TransactionAction.Update bdo = dto.toBdo();

			assertThat(bdo.date()).isEqualTo(TRANSACTION.getDate());
			assertThat(bdo.amount())
					.isEqualByComparingTo(TRANSACTION.getAmount());
			assertThat(bdo.jarId()).isEqualTo(TRANSACTION.getJar().getId());
			assertThat(bdo.beneficiary())
					.isEqualTo(TRANSACTION.getBeneficiary());
			assertThat(bdo.description())
					.isEqualTo(TRANSACTION.getDescription());
		}
	}
}
