package com.cvesters.moneyjars.account.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.account.TestAccount;

class AccountActionTest {

	private static final TestAccount ACCOUNT = TestAccount.CHECKING;

	@Nested
	class Create {

		@Test
		void success() {
			final var action = new AccountAction.Create(ACCOUNT.getName(),
					ACCOUNT.getDescription());

			assertThat(action.name()).isEqualTo(ACCOUNT.getName());
			assertThat(action.description())
					.isEqualTo(ACCOUNT.getDescription());
		}

		@Test
		void nameNull() {
			final String name = null;
			final String description = ACCOUNT.getDescription();

			assertThatThrownBy(
					() -> new AccountAction.Create(name, description))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void descriptionNull() {
			final String name = ACCOUNT.getName();
			final String description = null;

			assertThatThrownBy(
					() -> new AccountAction.Create(name, description))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void toBdo() {
			final var action = new AccountAction.Create(ACCOUNT.getName(),
					ACCOUNT.getDescription());

			final Account bdo = action.toBdo();

			assertThat(bdo.getId()).isNull();
			assertThat(bdo.getName()).isEqualTo(ACCOUNT.getName());
			assertThat(bdo.getDescription())
					.isEqualTo(ACCOUNT.getDescription());
			assertThat(bdo.getBalance()).isEqualTo(BigDecimal.ZERO);
		}
	}

	@Nested
	class Update {

		@Test
		void success() {
			final var action = new AccountAction.Update(ACCOUNT.getName(),
					ACCOUNT.getDescription());

			assertThat(action.name()).isEqualTo(ACCOUNT.getName());
			assertThat(action.description())
					.isEqualTo(ACCOUNT.getDescription());
		}

		@Test
		void nameNull() {
			final String name = null;
			final String description = ACCOUNT.getDescription();

			assertThatThrownBy(
					() -> new AccountAction.Update(name, description))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void descriptionNull() {
			final String description = null;
			final String name = ACCOUNT.getName();

			assertThatThrownBy(
					() -> new AccountAction.Update(name, description))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void applyOn() {
			final var action = new AccountAction.Update(ACCOUNT.getName(),
					ACCOUNT.getDescription());

			final Account target = mock();

			action.applyOn(target);

			verify(target).setName(ACCOUNT.getName());
			verify(target).setDescription(ACCOUNT.getDescription());
		}

		@Test
		void applyOnTargetNull() {
			final var action = new AccountAction.Update(ACCOUNT.getName(),
					ACCOUNT.getDescription());

			assertThatThrownBy(() -> action.applyOn(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
