package com.cvesters.moneyjars.account.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.account.TestAccount;

class AccountDtoTest {

	private static final TestAccount ACCOUNT = TestAccount.CHECKING;

	@Nested
	class Constructor {

		@Test
		void success() {
			final var dto = new AccountDto(ACCOUNT.bdo());

			assertThat(dto.getId()).isEqualTo(ACCOUNT.getId());
			assertThat(dto.getName()).isEqualTo(ACCOUNT.getName());
			assertThat(dto.getDescription())
					.isEqualTo(ACCOUNT.getDescription());
			assertThat(dto.getBalance()).isEqualTo(ACCOUNT.getBalance());
		}

		@Test
		void bdoNull() {
			assertThatThrownBy(() -> new AccountDto(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
