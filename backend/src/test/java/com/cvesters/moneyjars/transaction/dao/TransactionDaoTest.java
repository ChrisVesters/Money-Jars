package com.cvesters.moneyjars.transaction.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.transaction.TestTransaction;
import com.cvesters.moneyjars.transaction.bdo.Transaction;

class TransactionDaoTest {

	private static final TestTransaction TRANSACTION = TestTransaction.RENT;

	@Nested
	class Constructor {

		@Test
		void success() {
			final var dao = new TransactionDao(TRANSACTION.bdo());

			assertThat(dao.getId()).isNull();
			assertThat(dao.getDate()).isEqualTo(TRANSACTION.getDate());
			assertThat(dao.getAmount()).isEqualTo(TRANSACTION.getAmount());
			assertThat(dao.getJarId()).isEqualTo(TRANSACTION.getJar().getId());
			assertThat(dao.getBeneficiary())
					.isEqualTo(TRANSACTION.getBeneficiary());
			assertThat(dao.getDescription())
					.isEqualTo(TRANSACTION.getDescription());
		}

		@Test
		void bdoNull() {
			assertThatThrownBy(() -> new TransactionDao(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class UpdateWith {

		@Test
		void success() {
			final var updatedDate = TRANSACTION.getDate().plusDays(1);
			final var updatedAmount = TRANSACTION.getAmount().negate();
			final var updatedJar = TRANSACTION.getJar().getId();
			final var updatedBeneficiary = "Vendor";
			final var updatedDescription = "Updated description";

			final var updatedTransaction = new Transaction(updatedDate,
					updatedAmount, updatedJar, updatedBeneficiary,
					updatedDescription);

			final var dao = new TransactionDao(TRANSACTION.bdo());
			dao.updateWith(updatedTransaction);

			assertThat(dao.getId()).isNull();
			assertThat(dao.getDate()).isEqualTo(updatedDate);
			assertThat(dao.getAmount()).isEqualTo(updatedAmount);
			assertThat(dao.getJarId()).isEqualTo(updatedJar);
			assertThat(dao.getBeneficiary()).isEqualTo(updatedBeneficiary);
			assertThat(dao.getDescription()).isEqualTo(updatedDescription);
		}
	}

	@Nested
	class ToBdo {

		@Test
		void success() {
			final var dao = new TransactionDao(TRANSACTION.bdo());
			final var bdo = dao.toBdo();

			assertThat(bdo.getId()).isNull();
			assertThat(bdo.getDate()).isEqualTo(TRANSACTION.getDate());
			assertThat(bdo.getAmount()).isEqualTo(TRANSACTION.getAmount());
			assertThat(bdo.getJarId()).isEqualTo(TRANSACTION.getJar().getId());
			assertThat(bdo.getBeneficiary())
					.isEqualTo(TRANSACTION.getBeneficiary());
			assertThat(bdo.getDescription())
					.isEqualTo(TRANSACTION.getDescription());
		}
	}
}
