package com.cvesters.moneyjars.transaction.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.transaction.TestJarTransferTransaction;

class JarTransferTransactionDtoTest {

	private static final TestJarTransferTransaction TRANSACTION = TestJarTransferTransaction.CAR_SAVINGS;

	@Nested
	class Constructor {

		@Test
		void success() {
			final var dto = new JarTransferTransactionDto(TRANSACTION.bdo());

			assertThat(dto.getId()).isEqualTo(TRANSACTION.getId());
			assertThat(dto.getDate())
					.isEqualTo(TRANSACTION.getDate().toString());
			assertThat(dto.getAmount()).isEqualTo(TRANSACTION.getAmountString());
			assertThat(dto.getDescription())
					.isEqualTo(TRANSACTION.getDescription());
			assertThat(dto.getSourceJarId())
					.isEqualTo(TRANSACTION.getSourceJar().getId());
			assertThat(dto.getTargetJarId())
					.isEqualTo(TRANSACTION.getTargetJar().getId());
		}

		@Test
		void bdoNull() {
			assertThatThrownBy(() -> new JarTransferTransactionDto(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
