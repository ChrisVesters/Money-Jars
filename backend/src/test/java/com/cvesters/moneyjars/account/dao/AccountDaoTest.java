package com.cvesters.moneyjars.account.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.account.TestAccount;
import com.cvesters.moneyjars.account.bdo.Account;

class AccountDaoTest {

	private static final TestAccount ACCOUNT = TestAccount.CHECKING;

	@Nested
	class Constructor {

		@Test
		void success() {
			final var dao = new AccountDao(ACCOUNT.bdo());

			assertThat(dao.getId()).isNull();
			assertThat(dao.getName()).isEqualTo(ACCOUNT.getName());
			assertThat(dao.getDescription())
					.isEqualTo(ACCOUNT.getDescription());
			assertThat(dao.getBalance()).isEqualTo(ACCOUNT.getBalance());
		}

		@Test
		void accountNull() {
			assertThatThrownBy(() -> new AccountDao(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class UpdateWith {

		@Test
		void success() {
			final String name = "Savings";
			final String description = "Long-term account";
			final Account update = new Account(name, description);

			final var dao = new AccountDao(ACCOUNT.bdo());
			dao.updateWith(update);

			assertThat(dao.getId()).isNull();
			assertThat(dao.getName()).isEqualTo(name);
			assertThat(dao.getDescription()).isEqualTo(description);
			assertThat(dao.getBalance()).isEqualTo(ACCOUNT.getBalance());
		}

		@Test
		void accountNull() {
			final var dao = new AccountDao(ACCOUNT.bdo());

			assertThatThrownBy(() -> dao.updateWith(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class ToBdo {

		@Test
		void success() {
			final var dao = new AccountDao(ACCOUNT.bdo());
			final var bdo = dao.toBdo();

			assertThat(bdo.getId()).isNull();
			assertThat(bdo.getName()).isEqualTo(ACCOUNT.getName());
			assertThat(bdo.getDescription())
					.isEqualTo(ACCOUNT.getDescription());
			assertThat(bdo.getBalance()).isEqualTo(ACCOUNT.getBalance());
		}
	}
}
