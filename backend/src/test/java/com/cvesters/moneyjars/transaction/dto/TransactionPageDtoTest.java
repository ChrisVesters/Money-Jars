package com.cvesters.moneyjars.transaction.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.common.bdo.PagedResult;
import com.cvesters.moneyjars.transaction.TestPaymentTransaction;
import com.cvesters.moneyjars.transaction.bdo.Transaction;

class TransactionPageDtoTest {

	@Nested
	class Constructor {

		@Test
		void success() {
			final List<Transaction> transactions = List.of(
					TestPaymentTransaction.RENT.bdo(),
					TestPaymentTransaction.GROCERY.bdo());
			final var bdo = new PagedResult<>(transactions, 1, 10, 12);

			final var dto = new TransactionPageDto(bdo);

			assertThat(dto.getContent()).satisfiesExactly(
					t -> assertEquals(t, TestPaymentTransaction.RENT),
					t -> assertEquals(t, TestPaymentTransaction.GROCERY));
			assertThat(dto.getPage()).isEqualTo(1);
			assertThat(dto.getSize()).isEqualTo(10);
			assertThat(dto.getTotalElements()).isEqualTo(12);
		}

		@Test
		void empty() {
			final var bdo = new PagedResult<Transaction>(
					Collections.emptyList(), 0, 10, 0);

			final var dto = new TransactionPageDto(bdo);

			assertThat(dto.getContent()).isEmpty();
			assertThat(dto.getPage()).isZero();
			assertThat(dto.getSize()).isEqualTo(10);
			assertThat(dto.getTotalElements()).isZero();
		}

		@Test
		void bdoNull() {
			assertThatThrownBy(() -> new TransactionPageDto(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	private static void assertEquals(final TransactionDto dto,
			final TestPaymentTransaction expected) {
		assertThat(dto).isInstanceOf(PaymentTransactionDto.class);
		final var paymentDto = (PaymentTransactionDto) dto;

		assertThat(paymentDto.getId()).isEqualTo(expected.getId());
		assertThat(paymentDto.getDate())
				.isEqualTo(expected.getDate().toString());
		assertThat(paymentDto.getAmount())
				.isEqualTo(expected.getAmountString());
		assertThat(paymentDto.getDescription())
				.isEqualTo(expected.getDescription());
		assertThat(paymentDto.getJarId()).isEqualTo(expected.getJar().getId());
		assertThat(paymentDto.getAccountId())
				.isEqualTo(expected.getAccount().getId());
		assertThat(paymentDto.getCounterparty())
				.isEqualTo(expected.getCounterparty());
		assertThat(paymentDto.getDirection())
				.isEqualTo(expected.getDirectionString());
	}
}
