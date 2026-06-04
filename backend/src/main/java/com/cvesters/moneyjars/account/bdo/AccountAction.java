package com.cvesters.moneyjars.account.bdo;

import java.util.Objects;

import org.apache.commons.lang3.Validate;

public final class AccountAction {

	private AccountAction() {
	}

	public static record Create(String name, String description) {

		public Create {
			Validate.notBlank(name);
			Objects.requireNonNull(description);
		}

		public Account toBdo() {
			return new Account(name, description);
		}
	}

	public static record Update(String name, String description) {

		public Update {
			Validate.notBlank(name);
			Objects.requireNonNull(description);
		}

		public void applyOn(final Account account) {
			Objects.requireNonNull(account);

			account.setName(name);
			account.setDescription(description);
		}
	}
}
