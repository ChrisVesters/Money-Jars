package com.cvesters.moneyjars.transaction.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.transaction.TestAccountTransferTransaction;

class AccountTransferTransactionDtoTest {

	private static final TestAccountTransferTransaction TRANSACTION = TestAccountTransferTransaction.CASH_WITHDRAWAL;

	@Nested
	class Constructor {

		@Test
		void success() {
			final var dto = new AccountTransferTransactionDto(
					TRANSACTION.bdo());

			assertThat(dto.getId()).isEqualTo(TRANSACTION.getId());
			assertThat(dto.getDate())
					.isEqualTo(TRANSACTION.getDate().toString());
			assertThat(dto.getAmount())
					.isEqualTo(TRANSACTION.getAmountString());
			assertThat(dto.getDescription())
					.isEqualTo(TRANSACTION.getDescription());
			assertThat(dto.getSourceAccountId())
					.isEqualTo(TRANSACTION.getSourceAccount().getId());
			assertThat(dto.getTargetAccountId())
					.isEqualTo(TRANSACTION.getTargetAccount().getId());
		}

		@Test
		void bdoNull() {
			assertThatThrownBy(() -> new AccountTransferTransactionDto(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
