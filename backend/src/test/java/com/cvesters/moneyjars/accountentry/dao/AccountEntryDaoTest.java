package com.cvesters.moneyjars.accountentry.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.accountentry.TestAccountEntry;
import com.cvesters.moneyjars.accountentry.bdo.AccountEntry;

class AccountEntryDaoTest {

	private static final TestAccountEntry ACCOUNT_ENTRY = TestAccountEntry.WALLET_CAFE;

	@Nested
	class Constructor {

		@Test
		void success() {
			final var dao = new AccountEntryDao(ACCOUNT_ENTRY.bdo());

			assertThat(dao.getId()).isNull();
			assertThat(dao.getTransactionId())
					.isEqualTo(ACCOUNT_ENTRY.getTransaction().getId());
			assertThat(dao.getAccountId())
					.isEqualTo(ACCOUNT_ENTRY.getAccount().getId());
			assertThat(dao.getBalanceBefore())
					.isEqualByComparingTo(ACCOUNT_ENTRY.getBalanceBefore());
			assertThat(dao.getBalanceAfter())
					.isEqualByComparingTo(ACCOUNT_ENTRY.getBalanceAfter());
		}

		@Test
		void entryNull() {
			assertThatThrownBy(() -> new AccountEntryDao(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class UpdateWith {

		@Test
		void success() {
			final var updatedBalanceBefore = new BigDecimal("1000.00");
			final var updatedBalanceAfter = new BigDecimal("985.00");

			final var updatedEntry = new AccountEntry(ACCOUNT_ENTRY.getId(),
					ACCOUNT_ENTRY.getTransaction().getId(),
					ACCOUNT_ENTRY.getAccount().getId(), updatedBalanceBefore,
					updatedBalanceAfter);

			final var dao = new AccountEntryDao(ACCOUNT_ENTRY.bdo());
			dao.updateWith(updatedEntry);

			assertThat(dao.getId()).isNull();
			assertThat(dao.getTransactionId())
					.isEqualTo(ACCOUNT_ENTRY.getTransaction().getId());
			assertThat(dao.getAccountId())
					.isEqualTo(ACCOUNT_ENTRY.getAccount().getId());
			assertThat(dao.getBalanceBefore())
					.isEqualByComparingTo(updatedBalanceBefore);
			assertThat(dao.getBalanceAfter())
					.isEqualByComparingTo(updatedBalanceAfter);
		}

		@Test
		void entryNull() {
			final var dao = new AccountEntryDao(ACCOUNT_ENTRY.bdo());

			assertThatThrownBy(() -> dao.updateWith(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class ToBdo {

		@Test
		void succes() {
			final var dao = new AccountEntryDao(ACCOUNT_ENTRY.bdo());
			final var bdo = dao.toBdo();

			assertThat(bdo.getId()).isNull();
			assertThat(bdo.getTransactionId())
					.isEqualTo(ACCOUNT_ENTRY.getTransaction().getId());
			assertThat(bdo.getAccountId())
					.isEqualTo(ACCOUNT_ENTRY.getAccount().getId());
			assertThat(bdo.getBalanceBefore())
					.isEqualByComparingTo(ACCOUNT_ENTRY.getBalanceBefore());
			assertThat(bdo.getBalanceAfter())
					.isEqualByComparingTo(ACCOUNT_ENTRY.getBalanceAfter());
		}
	}
}
