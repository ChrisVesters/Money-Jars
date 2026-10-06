package com.cvesters.moneyjars.transaction.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.transaction.TestPaymentTransaction;

class PaymentTransactionDtoTest {

	private static final TestPaymentTransaction TRANSACTION = TestPaymentTransaction.RENT;

	@Nested
	class Constructor {

		@Test
		void success() {
			final var dto = new PaymentTransactionDto(TRANSACTION.bdo());

			assertThat(dto).isInstanceOf(PaymentTransactionDto.class);
			assertThat(dto.getId()).isEqualTo(TRANSACTION.getId());
			assertThat(dto.getDate())
					.isEqualTo(TRANSACTION.getDate().toString());
			assertThat(dto.getAmount()).isEqualTo(TRANSACTION.getAmount());
			assertThat(dto.getDescription())
					.isEqualTo(TRANSACTION.getDescription());
			assertThat(dto.getJarId()).isEqualTo(TRANSACTION.getJar().getId());
			assertThat(dto.getAccountId())
					.isEqualTo(TRANSACTION.getAccount().getId());
			assertThat(dto.getCounterparty())
					.isEqualTo(TRANSACTION.getCounterparty());
			assertThat(dto.getDirection())
					.isEqualTo(TRANSACTION.getDirectionString());
		}

		@Test
		void bdoNull() {
			assertThatThrownBy(() -> new PaymentTransactionDto(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
