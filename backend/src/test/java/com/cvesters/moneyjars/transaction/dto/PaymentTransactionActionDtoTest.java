package com.cvesters.moneyjars.transaction.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.transaction.TestPaymentTransaction;
import com.cvesters.moneyjars.transaction.bdo.TransactionAction;

class PaymentTransactionActionDtoTest {

	private static final TestPaymentTransaction TRANSACTION = TestPaymentTransaction.RENT;

	@Nested
	class Create {

		@Test
		void success() {
			final var dto = new PaymentTransactionActionDto.CreatePayment(
					TRANSACTION.getDate(), TRANSACTION.getAmount(),
					TRANSACTION.getDescription(), TRANSACTION.getJar().getId(),
					TRANSACTION.getAccount().getId(),
					TRANSACTION.getCounterparty(),
					TRANSACTION.getDirectionString());

			assertThat(dto.date()).isEqualTo(TRANSACTION.getDate().toString());
			assertThat(dto.amount())
					.isEqualByComparingTo(TRANSACTION.getAmount());
			assertThat(dto.description())
					.isEqualTo(TRANSACTION.getDescription());
			assertThat(dto.jarId()).isEqualTo(TRANSACTION.getJar().getId());
			assertThat(dto.accountId())
					.isEqualTo(TRANSACTION.getAccount().getId());
			assertThat(dto.counterparty())
					.isEqualTo(TRANSACTION.getCounterparty());
			assertThat(dto.direction())
					.isEqualTo(TRANSACTION.getDirectionString());
		}

		@Test
		void toBdo() {
			final var dto = new PaymentTransactionActionDto.CreatePayment(
					TRANSACTION.getDate(), TRANSACTION.getAmount(),
					TRANSACTION.getDescription(), TRANSACTION.getJar().getId(),
					TRANSACTION.getAccount().getId(),
					TRANSACTION.getCounterparty(),
					TRANSACTION.getDirectionString());

			final TransactionAction.CreatePayment bdo = dto.toBdo();

			assertThat(bdo.date()).isEqualTo(TRANSACTION.getDate());
			assertThat(bdo.amount())
					.isEqualByComparingTo(TRANSACTION.getAmount());
			assertThat(bdo.description())
					.isEqualTo(TRANSACTION.getDescription());
			assertThat(bdo.jarId()).isEqualTo(TRANSACTION.getJar().getId());
			assertThat(bdo.accountId())
					.isEqualTo(TRANSACTION.getAccount().getId());
			assertThat(bdo.counterparty())
					.isEqualTo(TRANSACTION.getCounterparty());
			assertThat(bdo.direction()).isEqualTo(TRANSACTION.getDirection());
		}
	}

	@Nested
	class Update {

		@Test
		void success() {
			final var dto = new PaymentTransactionActionDto.UpdatePayment(
					TRANSACTION.getDate(), TRANSACTION.getAmount(),
					TRANSACTION.getDescription(), TRANSACTION.getJar().getId(),
					TRANSACTION.getAccount().getId(),
					TRANSACTION.getCounterparty(),
					TRANSACTION.getDirectionString());

			assertThat(dto.date()).isEqualTo(TRANSACTION.getDate().toString());
			assertThat(dto.amount())
					.isEqualByComparingTo(TRANSACTION.getAmount());
			assertThat(dto.description())
					.isEqualTo(TRANSACTION.getDescription());
			assertThat(dto.jarId()).isEqualTo(TRANSACTION.getJar().getId());
			assertThat(dto.accountId())
					.isEqualTo(TRANSACTION.getAccount().getId());
			assertThat(dto.counterparty())
					.isEqualTo(TRANSACTION.getCounterparty());
			assertThat(dto.direction())
					.isEqualTo(TRANSACTION.getDirectionString());
		}

		@Test
		void toBdo() {
			final var dto = new PaymentTransactionActionDto.UpdatePayment(
					TRANSACTION.getDate(), TRANSACTION.getAmount(),
					TRANSACTION.getDescription(), TRANSACTION.getJar().getId(),
					TRANSACTION.getAccount().getId(),
					TRANSACTION.getCounterparty(),
					TRANSACTION.getDirectionString());

			final TransactionAction.UpdatePayment bdo = dto.toBdo();

			assertThat(bdo.date()).isEqualTo(TRANSACTION.getDate());
			assertThat(bdo.amount())
					.isEqualByComparingTo(TRANSACTION.getAmount());
			assertThat(bdo.description())
					.isEqualTo(TRANSACTION.getDescription());
			assertThat(bdo.jarId()).isEqualTo(TRANSACTION.getJar().getId());
			assertThat(bdo.accountId())
					.isEqualTo(TRANSACTION.getAccount().getId());
			assertThat(bdo.counterparty())
					.isEqualTo(TRANSACTION.getCounterparty());
			assertThat(bdo.direction()).isEqualTo(TRANSACTION.getDirection());
		}
	}
}
