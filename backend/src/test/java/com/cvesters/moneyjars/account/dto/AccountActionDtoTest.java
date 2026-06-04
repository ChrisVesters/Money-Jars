package com.cvesters.moneyjars.account.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.account.TestAccount;
import com.cvesters.moneyjars.account.bdo.AccountAction;

class AccountActionDtoTest {

	private static final TestAccount ACCOUNT = TestAccount.CHECKING;

	@Nested
	class Create {

		@Test
		void success() {
			final var dto = new AccountActionDto.Create(ACCOUNT.getName(),
					ACCOUNT.getDescription());

			assertThat(dto.name()).isEqualTo(ACCOUNT.getName());
			assertThat(dto.description()).isEqualTo(ACCOUNT.getDescription());
		}

		@Test
		void toBdo() {
			final var dto = new AccountActionDto.Create(ACCOUNT.getName(),
					ACCOUNT.getDescription());

			final AccountAction.Create bdo = dto.toBdo();

			assertThat(bdo.name()).isEqualTo(ACCOUNT.getName());
			assertThat(bdo.description()).isEqualTo(ACCOUNT.getDescription());
		}
	}

	@Nested
	class Update {

		@Test
		void success() {
			final var dto = new AccountActionDto.Update(ACCOUNT.getName(),
					ACCOUNT.getDescription());

			assertThat(dto.name()).isEqualTo(ACCOUNT.getName());
			assertThat(dto.description()).isEqualTo(ACCOUNT.getDescription());
		}

		@Test
		void toBdo() {
			final var dto = new AccountActionDto.Update(ACCOUNT.getName(),
					ACCOUNT.getDescription());

			final AccountAction.Update bdo = dto.toBdo();

			assertThat(bdo.name()).isEqualTo(ACCOUNT.getName());
			assertThat(bdo.description()).isEqualTo(ACCOUNT.getDescription());
		}
	}
}
