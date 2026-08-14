package com.cvesters.moneyjars.transaction.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.transaction.TestPaymentTransaction;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;

class PaymentTransactionDaoTest {

	private static final TestPaymentTransaction TRANSACTION = TestPaymentTransaction.RENT;

	@Nested
	class Constructor {

		@Test
		void success() {
			final var dao = new PaymentTransactionDao(TRANSACTION.bdo());

			assertThat(dao.getId()).isNull();
			assertThat(dao.getDate()).isEqualTo(TRANSACTION.getDate());
			assertThat(dao.getAmount()).isEqualTo(TRANSACTION.getAmount());
			assertThat(dao.getBeneficiary())
					.isEqualTo(TRANSACTION.getBeneficiary());
			assertThat(dao.getDescription())
					.isEqualTo(TRANSACTION.getDescription());
		}

		@Test
		void transactionNull() {
			assertThatThrownBy(() -> new PaymentTransactionDao(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class UpdateWith {

		@Test
		void success() {
			final var updatedDate = TRANSACTION.getDate().plusDays(1);
			final var updatedAmount = TRANSACTION.getAmount().negate();
			final var updatedBeneficiary = "Vendor";
			final var updatedDescription = "Updated description";
			final var updatedJar = TRANSACTION.getJar().getId();
			final var updatedAccount = TRANSACTION.getAccount().getId();
			final var updatedDirection = TRANSACTION.getDirection();

			final var updatedTransaction = new PaymentTransaction(updatedDate,
					updatedAmount, updatedBeneficiary, updatedDescription,
					updatedJar, updatedAccount, updatedDirection);

			final var dao = new PaymentTransactionDao(TRANSACTION.bdo());
			dao.updateWith(updatedTransaction);

			assertThat(dao.getId()).isNull();
			assertThat(dao.getDate()).isEqualTo(updatedDate);
			assertThat(dao.getAmount()).isEqualTo(updatedAmount);
			assertThat(dao.getBeneficiary()).isEqualTo(updatedBeneficiary);
			assertThat(dao.getDescription()).isEqualTo(updatedDescription);
			assertThat(dao.getJarId()).isEqualTo(updatedJar);
			assertThat(dao.getAccountId()).isEqualTo(updatedAccount);
			assertThat(dao.getDirection())
					.isEqualTo(TRANSACTION.getDirectionId());
		}

		@Test
		void transactionNull() {
			final var dao = new PaymentTransactionDao(TRANSACTION.bdo());

			assertThatThrownBy(() -> dao.updateWith(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class ToBdo {

		@Test
		void success() {
			final var dao = new PaymentTransactionDao(TRANSACTION.bdo());
			final var bdo = dao.toBdo();

			assertThat(bdo.getId()).isNull();
			assertThat(bdo.getDate()).isEqualTo(TRANSACTION.getDate());
			assertThat(bdo.getAmount()).isEqualTo(TRANSACTION.getAmount());
			assertThat(bdo.getBeneficiary())
					.isEqualTo(TRANSACTION.getBeneficiary());
			assertThat(bdo.getDescription())
					.isEqualTo(TRANSACTION.getDescription());
			assertThat(bdo.getJarId()).isEqualTo(TRANSACTION.getJar().getId());
			assertThat(bdo.getAccountId())
					.isEqualTo(TRANSACTION.getAccount().getId());
			assertThat(bdo.getDirection())
					.isEqualTo(TRANSACTION.getDirection());
		}
	}
}
