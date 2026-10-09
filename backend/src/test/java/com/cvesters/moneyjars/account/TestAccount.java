package com.cvesters.moneyjars.account;

import lombok.Getter;

import com.cvesters.moneyjars.account.bdo.Account;

@Getter
public enum TestAccount {

	CHECKING(1L, "Checking", "Everyday bank account"),
	WALLET(2L, "Wallet", "Cash in pocket"),
	SAVINGS(3L, "Savings", "High-interest savings account"),
	CREDIT_CARD(4L, "Credit Card", "Settled at the end of the month");

	private final long id;
	private final String name;
	private final String description;

	TestAccount(final long id, final String name, final String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public Account bdo() {
		return new Account(id, name, description);
	}
}
