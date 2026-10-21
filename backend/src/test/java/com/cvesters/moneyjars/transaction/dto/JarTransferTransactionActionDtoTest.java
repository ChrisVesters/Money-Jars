package com.cvesters.moneyjars.transaction.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.transaction.TestJarTransferTransaction;
import com.cvesters.moneyjars.transaction.bdo.JarTransferTransactionAction;

class JarTransferTransactionActionDtoTest {

	private static final TestJarTransferTransaction TRANSACTION = TestJarTransferTransaction.CAR_SAVINGS;

	@Nested
	class Create {

		@Test
		void constructor() {
			final var dto = new JarTransferTransactionActionDto.Create(
					TRANSACTION.getDate(), TRANSACTION.getAmountString(),
					TRANSACTION.getDescription(),
					TRANSACTION.getSourceJar().getId(),
					TRANSACTION.getTargetJar().getId());

			assertThat(dto.date()).isEqualTo(TRANSACTION.getDate());
			assertThat(dto.amount()).isEqualTo(TRANSACTION.getAmountString());
			assertThat(dto.description())
					.isEqualTo(TRANSACTION.getDescription());
			assertThat(dto.sourceJarId())
					.isEqualTo(TRANSACTION.getSourceJar().getId());
			assertThat(dto.targetJarId())
					.isEqualTo(TRANSACTION.getTargetJar().getId());
		}

		@Test
		void toBdo() {
			final var dto = new JarTransferTransactionActionDto.Create(
					TRANSACTION.getDate(), TRANSACTION.getAmountString(),
					TRANSACTION.getDescription(),
					TRANSACTION.getSourceJar().getId(),
					TRANSACTION.getTargetJar().getId());

			final JarTransferTransactionAction.Create bdo = dto.toBdo();

			assertThat(bdo.date()).isEqualTo(TRANSACTION.getDate());
			assertThat(bdo.amount())
					.isEqualByComparingTo(TRANSACTION.getAmount());
			assertThat(bdo.description())
					.isEqualTo(TRANSACTION.getDescription());
			assertThat(bdo.sourceJarId())
					.isEqualTo(TRANSACTION.getSourceJar().getId());
			assertThat(bdo.targetJarId())
					.isEqualTo(TRANSACTION.getTargetJar().getId());
		}
	}

	@Nested
	class Update {

		@Test
		void constructor() {
			final var dto = new JarTransferTransactionActionDto.Update(
					TRANSACTION.getDate(), TRANSACTION.getAmountString(),
					TRANSACTION.getDescription(),
					TRANSACTION.getSourceJar().getId(),
					TRANSACTION.getTargetJar().getId());

			assertThat(dto.date()).isEqualTo(TRANSACTION.getDate());
			assertThat(dto.amount()).isEqualTo(TRANSACTION.getAmountString());
			assertThat(dto.description())
					.isEqualTo(TRANSACTION.getDescription());
			assertThat(dto.sourceJarId())
					.isEqualTo(TRANSACTION.getSourceJar().getId());
			assertThat(dto.targetJarId())
					.isEqualTo(TRANSACTION.getTargetJar().getId());
		}

		@Test
		void toBdo() {
			final var dto = new JarTransferTransactionActionDto.Update(
					TRANSACTION.getDate(), TRANSACTION.getAmountString(),
					TRANSACTION.getDescription(),
					TRANSACTION.getSourceJar().getId(),
					TRANSACTION.getTargetJar().getId());

			final JarTransferTransactionAction.Update bdo = dto.toBdo();

			assertThat(bdo.date()).isEqualTo(TRANSACTION.getDate());
			assertThat(bdo.amount())
					.isEqualByComparingTo(TRANSACTION.getAmount());
			assertThat(bdo.description())
					.isEqualTo(TRANSACTION.getDescription());
			assertThat(bdo.sourceJarId())
					.isEqualTo(TRANSACTION.getSourceJar().getId());
			assertThat(bdo.targetJarId())
					.isEqualTo(TRANSACTION.getTargetJar().getId());
		}
	}
}
