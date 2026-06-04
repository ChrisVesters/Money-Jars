package com.cvesters.moneyjars.account.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.cvesters.moneyjars.account.TestAccount;

class AccountTest {

	private static final TestAccount ACCOUNT = TestAccount.CHECKING;

	@Nested
	class Constructor {

		@Test
		void withoutId() {
			final var account = new Account(ACCOUNT.getName(),
					ACCOUNT.getDescription());

			assertThat(account.getId()).isNull();
			assertThat(account.getName()).isEqualTo(ACCOUNT.getName());
			assertThat(account.getDescription())
					.isEqualTo(ACCOUNT.getDescription());
			assertThat(account.getBalance()).isEqualTo(BigDecimal.ZERO);
		}

		@Test
		void withId() {
			final long id = ACCOUNT.getId();
			final String name = ACCOUNT.getName();
			final String description = ACCOUNT.getDescription();
			final BigDecimal balance = ACCOUNT.getBalance();

			final var account = new Account(id, name, description, balance);

			assertThat(account.getId()).isEqualTo(ACCOUNT.getId());
			assertThat(account.getName()).isEqualTo(ACCOUNT.getName());
			assertThat(account.getDescription())
					.isEqualTo(ACCOUNT.getDescription());
			assertThat(account.getBalance()).isEqualTo(ACCOUNT.getBalance());
		}

		@Test
		void nameNull() {
			final long id = ACCOUNT.getId();
			final String name = null;
			final String description = ACCOUNT.getDescription();
			final BigDecimal balance = ACCOUNT.getBalance();

			assertThatThrownBy(
					() -> new Account(id, name, description, balance))
							.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void nameInvalid(final String name) {
			final long id = ACCOUNT.getId();
			final String description = ACCOUNT.getDescription();
			final BigDecimal balance = ACCOUNT.getBalance();

			assertThatThrownBy(
					() -> new Account(id, name, description, balance))
							.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			final long id = ACCOUNT.getId();
			final String name = ACCOUNT.getName();
			final String description = null;
			final BigDecimal balance = ACCOUNT.getBalance();

			assertThatThrownBy(
					() -> new Account(id, name, description, balance))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void balanceNull() {
			final long id = ACCOUNT.getId();
			final String name = ACCOUNT.getName();
			final String description = ACCOUNT.getDescription();
			final BigDecimal balance = null;

			assertThatThrownBy(
					() -> new Account(id, name, description, balance))
							.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class SetName {

		private final Account account = new Account(ACCOUNT.getId(),
				ACCOUNT.getName(), ACCOUNT.getDescription(),
				ACCOUNT.getBalance());

		@Test
		void success() {
			account.setName("New name");

			assertThat(account.getName()).isEqualTo("New name");
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void nameInvalid(final String name) {
			assertThatThrownBy(() -> account.setName(name))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void nameNull() {
			assertThatThrownBy(() -> account.setName(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class SetDescription {

		private final Account account = new Account(ACCOUNT.getId(),
				ACCOUNT.getName(), ACCOUNT.getDescription(),
				ACCOUNT.getBalance());

		@Test
		void success() {
			account.setDescription("New description");

			assertThat(account.getDescription()).isEqualTo("New description");
		}

		@Test
		void descriptionNull() {
			assertThatThrownBy(() -> account.setDescription(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
