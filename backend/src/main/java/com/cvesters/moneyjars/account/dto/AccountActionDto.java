package com.cvesters.moneyjars.account.dto;

import com.cvesters.moneyjars.account.bdo.AccountAction;

public final class AccountActionDto {

	private AccountActionDto() {
	}

	public static record Create(String name, String description) {

		public AccountAction.Create toBdo() {
			return new AccountAction.Create(name, description);
		}
	}

	public static record Update(String name, String description) {

		public AccountAction.Update toBdo() {
			return new AccountAction.Update(name, description);
		}
	}
}
