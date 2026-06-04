package com.cvesters.moneyjars.account;

import java.math.BigDecimal;

import lombok.Getter;

import com.cvesters.moneyjars.account.bdo.Account;

@Getter
public enum TestAccount {

  CHECKING(1L, "Checking", "Everyday bank account", new BigDecimal("1245.30")),
  WALLET(2L, "Wallet", "Cash in pocket", new BigDecimal("82.45"));

  private final long id;
  private final String name;
  private final String description;
  private final BigDecimal balance;

  TestAccount(final long id, final String name, final String description,
      final BigDecimal balance) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.balance = balance;
  }

  public Account bdo() {
    return new Account(id, name, description, balance);
  }
}
