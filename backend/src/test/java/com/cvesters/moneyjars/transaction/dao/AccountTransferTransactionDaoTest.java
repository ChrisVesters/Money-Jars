package com.cvesters.moneyjars.transaction.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.account.TestAccount;
import com.cvesters.moneyjars.transaction.TestAccountTransferTransaction;
import com.cvesters.moneyjars.transaction.bdo.AccountTransferTransaction;

class AccountTransferTransactionDaoTest {

	private static final TestAccountTransferTransaction TRANSACTION = TestAccountTransferTransaction.CASH_WITHDRAWAL;

	@Nested
	class Constructor {

		@Test
		void success() {
			final var dao = new AccountTransferTransactionDao(
					TRANSACTION.bdo());

			assertThat(dao.getId()).isNull();
			assertThat(dao.getDate()).isEqualTo(TRANSACTION.getDate());
			assertThat(dao.getSequence()).isEqualTo(TRANSACTION.getSequence());
			assertThat(dao.getAmount()).isEqualTo(TRANSACTION.getAmount());
			assertThat(dao.getDescription())
					.isEqualTo(TRANSACTION.getDescription());
			assertThat(dao.getSourceAccountId())
					.isEqualTo(TRANSACTION.getSourceAccount().getId());
			assertThat(dao.getTargetAccountId())
					.isEqualTo(TRANSACTION.getTargetAccount().getId());
		}

		@Test
		void transactionNull() {
			assertThatThrownBy(() -> new AccountTransferTransactionDao(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class UpdateWith {

		@Test
		void success() {
			final var updatedDate = TRANSACTION.getDate().plusDays(1);
			final var updatedSequence = 1;
			final var updatedAmount = TRANSACTION.getAmount()
					.add(BigDecimal.ONE);
			final var updatedDescription = "Updated description";
			final var updatedSourceAccount = TestAccount.SAVINGS.getId();
			final var updatedTargetAccount = TestAccount.CHECKING.getId();

			final var updatedTransaction = new AccountTransferTransaction(
					updatedDate, updatedSequence, updatedAmount,
					updatedDescription, updatedSourceAccount,
					updatedTargetAccount);

			final var dao = new AccountTransferTransactionDao(
					TRANSACTION.bdo());
			dao.updateWith(updatedTransaction);

			assertThat(dao.getId()).isNull();
			assertThat(dao.getDate()).isEqualTo(updatedDate);
			assertThat(dao.getSequence()).isEqualTo(updatedSequence);
			assertThat(dao.getAmount()).isEqualTo(updatedAmount);
			assertThat(dao.getDescription()).isEqualTo(updatedDescription);
			assertThat(dao.getSourceAccountId())
					.isEqualTo(updatedSourceAccount);
			assertThat(dao.getTargetAccountId())
					.isEqualTo(updatedTargetAccount);
		}

		@Test
		void transactionNull() {
			final var dao = new AccountTransferTransactionDao(
					TRANSACTION.bdo());

			assertThatThrownBy(() -> dao.updateWith(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class ToBdo {

		@Test
		void success() {
			final var dao = new AccountTransferTransactionDao(
					TRANSACTION.bdo());
			final var bdo = dao.toBdo();

			assertThat(bdo.getId()).isNull();
			assertThat(bdo.getDate()).isEqualTo(TRANSACTION.getDate());
			assertThat(bdo.getSequence()).isEqualTo(TRANSACTION.getSequence());
			assertThat(bdo.getAmount()).isEqualTo(TRANSACTION.getAmount());
			assertThat(bdo.getDescription())
					.isEqualTo(TRANSACTION.getDescription());
			assertThat(bdo.getSourceAccountId())
					.isEqualTo(TRANSACTION.getSourceAccount().getId());
			assertThat(bdo.getTargetAccountId())
					.isEqualTo(TRANSACTION.getTargetAccount().getId());
		}
	}
}
