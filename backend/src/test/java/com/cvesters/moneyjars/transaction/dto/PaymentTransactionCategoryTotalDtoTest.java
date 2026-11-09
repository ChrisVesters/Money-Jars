package com.cvesters.moneyjars.transaction.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.transaction.TestPaymentTransactionCategoryTotal;

class PaymentTransactionCategoryTotalDtoTest {

	@Nested
	class Constructor {

		@Test
		void success() {
			final TestPaymentTransactionCategoryTotal total = TestPaymentTransactionCategoryTotal.JANUARY_RENT;

			final var dto = new PaymentTransactionCategoryTotalDto(total.bdo());

			assertThat(dto.getCategoryId()).isEqualTo(total.getCategoryId());
			assertThat(dto.getIncoming())
					.isEqualTo(total.getIncoming().toPlainString());
			assertThat(dto.getOutgoing())
					.isEqualTo(total.getOutgoing().toPlainString());
		}

		@Test
		void categoryNull() {
			final TestPaymentTransactionCategoryTotal total = TestPaymentTransactionCategoryTotal.JANUARY_UNCATEGORISED;

			final var dto = new PaymentTransactionCategoryTotalDto(total.bdo());

			assertThat(dto.getCategoryId()).isNull();
			assertThat(dto.getIncoming())
					.isEqualTo(total.getIncoming().toPlainString());
			assertThat(dto.getOutgoing())
					.isEqualTo(total.getOutgoing().toPlainString());
		}

		@Test
		void totalNull() {
			assertThatThrownBy(
					() -> new PaymentTransactionCategoryTotalDto(null))
							.isInstanceOf(NullPointerException.class);
		}
	}
}
