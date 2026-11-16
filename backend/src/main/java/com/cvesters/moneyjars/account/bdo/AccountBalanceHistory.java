package com.cvesters.moneyjars.account.bdo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.TreeMap;

import lombok.Getter;

@Getter
public class AccountBalanceHistory {
	private final long accountId;
	private final TreeMap<LocalDate, BigDecimal> snapshots;

	public AccountBalanceHistory(final long accountId) {
		this.accountId = accountId;
		this.snapshots = new TreeMap<>();
	}

	public void addSnapshot(final LocalDate date, final BigDecimal amount) {
		snapshots.put(date, amount);
	}
}
