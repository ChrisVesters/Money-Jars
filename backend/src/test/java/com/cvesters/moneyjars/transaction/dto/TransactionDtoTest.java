package com.cvesters.moneyjars.transaction.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.transaction.TestAccountTransferTransaction;
import com.cvesters.moneyjars.transaction.TestJarTransferTransaction;
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
		void jarTransferTransaction() {
			final TestJarTransferTransaction transaction = TestJarTransferTransaction.CAR_SAVINGS;

			final TransactionDto dto = TransactionDto
					.fromBdo(transaction.bdo());

			assertThat(dto).isInstanceOf(JarTransferTransactionDto.class);
			final var jarTransferDto = (JarTransferTransactionDto) dto;
			assertThat(jarTransferDto.getId()).isEqualTo(transaction.getId());
			assertThat(jarTransferDto.getDate())
					.isEqualTo(transaction.getDate().toString());
			assertThat(jarTransferDto.getAmount())
					.isEqualTo(transaction.getAmountString());
			assertThat(jarTransferDto.getDescription())
					.isEqualTo(transaction.getDescription());
			assertThat(jarTransferDto.getSourceJarId())
					.isEqualTo(transaction.getSourceJar().getId());
			assertThat(jarTransferDto.getTargetJarId())
					.isEqualTo(transaction.getTargetJar().getId());
		}

		@Test
		void accountTransferTransaction() {
			final TestAccountTransferTransaction transaction = TestAccountTransferTransaction.CASH_WITHDRAWAL;

			final TransactionDto dto = TransactionDto
					.fromBdo(transaction.bdo());

			assertThat(dto).isInstanceOf(AccountTransferTransactionDto.class);
			final var accountTransferDto = (AccountTransferTransactionDto) dto;
			assertThat(accountTransferDto.getId())
					.isEqualTo(transaction.getId());
			assertThat(accountTransferDto.getDate())
					.isEqualTo(transaction.getDate().toString());
			assertThat(accountTransferDto.getAmount())
					.isEqualTo(transaction.getAmountString());
			assertThat(accountTransferDto.getDescription())
					.isEqualTo(transaction.getDescription());
			assertThat(accountTransferDto.getSourceAccountId())
					.isEqualTo(transaction.getSourceAccount().getId());
			assertThat(accountTransferDto.getTargetAccountId())
					.isEqualTo(transaction.getTargetAccount().getId());
		}

		@Test
		void transactionNull() {
			assertThatThrownBy(() -> TransactionDto.fromBdo(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
