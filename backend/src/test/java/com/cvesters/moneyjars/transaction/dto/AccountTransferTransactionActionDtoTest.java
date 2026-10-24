package com.cvesters.moneyjars.transaction.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.transaction.TestAccountTransferTransaction;
import com.cvesters.moneyjars.transaction.bdo.AccountTransferTransactionAction;

class AccountTransferTransactionActionDtoTest {

	private static final TestAccountTransferTransaction TRANSACTION = TestAccountTransferTransaction.CASH_WITHDRAWAL;

	@Nested
	class Create {

		@Test
		void constructor() {
			final var dto = new AccountTransferTransactionActionDto.Create(
					TRANSACTION.getDate(), TRANSACTION.getAmountString(),
					TRANSACTION.getDescription(),
					TRANSACTION.getSourceAccount().getId(),
					TRANSACTION.getTargetAccount().getId());

			assertThat(dto.date()).isEqualTo(TRANSACTION.getDate());
			assertThat(dto.amount()).isEqualTo(TRANSACTION.getAmountString());
			assertThat(dto.description())
					.isEqualTo(TRANSACTION.getDescription());
			assertThat(dto.sourceAccountId())
					.isEqualTo(TRANSACTION.getSourceAccount().getId());
			assertThat(dto.targetAccountId())
					.isEqualTo(TRANSACTION.getTargetAccount().getId());
		}

		@Test
		void toBdo() {
			final var dto = new AccountTransferTransactionActionDto.Create(
					TRANSACTION.getDate(), TRANSACTION.getAmountString(),
					TRANSACTION.getDescription(),
					TRANSACTION.getSourceAccount().getId(),
					TRANSACTION.getTargetAccount().getId());

			final AccountTransferTransactionAction.Create bdo = dto.toBdo();

			assertThat(bdo.date()).isEqualTo(TRANSACTION.getDate());
			assertThat(bdo.amount())
					.isEqualByComparingTo(TRANSACTION.getAmount());
			assertThat(bdo.description())
					.isEqualTo(TRANSACTION.getDescription());
			assertThat(bdo.sourceAccountId())
					.isEqualTo(TRANSACTION.getSourceAccount().getId());
			assertThat(bdo.targetAccountId())
					.isEqualTo(TRANSACTION.getTargetAccount().getId());
		}
	}

	@Nested
	class Update {

		@Test
		void constructor() {
			final var dto = new AccountTransferTransactionActionDto.Update(
					TRANSACTION.getDate(), TRANSACTION.getAmountString(),
					TRANSACTION.getDescription(),
					TRANSACTION.getSourceAccount().getId(),
					TRANSACTION.getTargetAccount().getId());

			assertThat(dto.date()).isEqualTo(TRANSACTION.getDate());
			assertThat(dto.amount()).isEqualTo(TRANSACTION.getAmountString());
			assertThat(dto.description())
					.isEqualTo(TRANSACTION.getDescription());
			assertThat(dto.sourceAccountId())
					.isEqualTo(TRANSACTION.getSourceAccount().getId());
			assertThat(dto.targetAccountId())
					.isEqualTo(TRANSACTION.getTargetAccount().getId());
		}

		@Test
		void toBdo() {
			final var dto = new AccountTransferTransactionActionDto.Update(
					TRANSACTION.getDate(), TRANSACTION.getAmountString(),
					TRANSACTION.getDescription(),
					TRANSACTION.getSourceAccount().getId(),
					TRANSACTION.getTargetAccount().getId());

			final AccountTransferTransactionAction.Update bdo = dto.toBdo();

			assertThat(bdo.date()).isEqualTo(TRANSACTION.getDate());
			assertThat(bdo.amount())
					.isEqualByComparingTo(TRANSACTION.getAmount());
			assertThat(bdo.description())
					.isEqualTo(TRANSACTION.getDescription());
			assertThat(bdo.sourceAccountId())
					.isEqualTo(TRANSACTION.getSourceAccount().getId());
			assertThat(bdo.targetAccountId())
					.isEqualTo(TRANSACTION.getTargetAccount().getId());
		}
	}
}
