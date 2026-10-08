package com.cvesters.moneyjars.transaction.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.account.TestAccount;
import com.cvesters.moneyjars.jar.TestJar;
import com.cvesters.moneyjars.transaction.TestPaymentTransaction;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionDirection;

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
			assertThat(dao.getDescription())
					.isEqualTo(TRANSACTION.getDescription());
			assertThat(dao.getJarId()).isEqualTo(TRANSACTION.getJar().getId());
			assertThat(dao.getAccountId())
					.isEqualTo(TRANSACTION.getAccount().getId());
			assertThat(dao.getCounterparty())
					.isEqualTo(TRANSACTION.getCounterparty());
			assertThat(dao.getDirection())
					.isEqualTo(TRANSACTION.getDirectionId());
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
			final var updatedSequence = 1;
			final var updatedAmount = TRANSACTION.getAmount().negate();
			final var updatedDescription = "Updated description";
			final var updatedJar = TestJar.HOLIDAY.getId();
			final var updatedAccount = TestAccount.SAVINGS.getId();
			final var updatedCounterparty = "Vendor";
			final var updatedDirection = PaymentTransactionDirection.INCOMING;

			final var updatedTransaction = new PaymentTransaction(updatedDate,
					updatedSequence, updatedAmount, updatedDescription,
					updatedJar, updatedAccount, updatedCounterparty,
					updatedDirection);

			final var dao = new PaymentTransactionDao(TRANSACTION.bdo());
			dao.updateWith(updatedTransaction);

			assertThat(dao.getId()).isNull();
			assertThat(dao.getDate()).isEqualTo(updatedDate);
			assertThat(dao.getSequence()).isEqualTo(updatedSequence);
			assertThat(dao.getAmount()).isEqualTo(updatedAmount);
			assertThat(dao.getDescription()).isEqualTo(updatedDescription);
			assertThat(dao.getJarId()).isEqualTo(updatedJar);
			assertThat(dao.getAccountId()).isEqualTo(updatedAccount);
			assertThat(dao.getCounterparty()).isEqualTo(updatedCounterparty);
			assertThat(dao.getDirection()).isEqualTo(
					PaymentTransactionDirectionDao.toDao(updatedDirection));
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
			assertThat(bdo.getDescription())
					.isEqualTo(TRANSACTION.getDescription());
			assertThat(bdo.getJarId()).isEqualTo(TRANSACTION.getJar().getId());
			assertThat(bdo.getAccountId())
					.isEqualTo(TRANSACTION.getAccount().getId());
			assertThat(bdo.getCounterparty())
					.isEqualTo(TRANSACTION.getCounterparty());
			assertThat(bdo.getDirection())
					.isEqualTo(TRANSACTION.getDirection());
		}
	}
}
