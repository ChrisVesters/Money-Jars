package com.cvesters.moneyjars.transaction.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.transaction.TestPaymentTransaction;

class TransactionDtoTest {

	@Nested
	class FromBdo {

		@Test
		void paymentTransaction() {
			final TestPaymentTransaction transaction = TestPaymentTransaction.RENT;

			final TransactionDto dto = TransactionDto
					.fromBdo(transaction.bdo());

			assertThat(dto).isInstanceOf(PaymentTransactionDto.class);
			final var paymentDto = (PaymentTransactionDto) dto;
			assertThat(paymentDto.getId()).isEqualTo(transaction.getId());
			assertThat(paymentDto.getDate())
					.isEqualTo(transaction.getDate().toString());
			assertThat(paymentDto.getAmount())
					.isEqualTo(transaction.getAmountString());
			assertThat(paymentDto.getDescription())
					.isEqualTo(transaction.getDescription());
			assertThat(paymentDto.getJarId())
					.isEqualTo(transaction.getJar().getId());
			assertThat(paymentDto.getAccountId())
					.isEqualTo(transaction.getAccount().getId());
			assertThat(paymentDto.getCounterparty())
					.isEqualTo(transaction.getCounterparty());
			assertThat(paymentDto.getDirection())
					.isEqualTo(transaction.getDirectionString());
		}

		@Test
		void transactionNull() {
			assertThatThrownBy(() -> TransactionDto.fromBdo(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
