package com.cvesters.moneyjars.transaction.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.transaction.TestTransaction;

class TransactionDtoTest {

	private static final TestTransaction TRANSACTION = TestTransaction.RENT;

	@Nested
	class Constructor {

		@Test
		void success() {
			final var dto = new TransactionDto(TRANSACTION.bdo());

			assertThat(dto.getId()).isEqualTo(TRANSACTION.getId());
			assertThat(dto.getDate())
					.isEqualTo(TRANSACTION.getDate().toString());
			assertThat(dto.getAmount()).isEqualTo(TRANSACTION.getAmount());
			assertThat(dto.getJarId()).isEqualTo(TRANSACTION.getJar().getId());
			assertThat(dto.getBeneficiary())
					.isEqualTo(TRANSACTION.getBeneficiary());
			assertThat(dto.getDescription())
					.isEqualTo(TRANSACTION.getDescription());
		}

		@Test
		void bdoNull() {
			assertThatThrownBy(() -> new TransactionDto(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
