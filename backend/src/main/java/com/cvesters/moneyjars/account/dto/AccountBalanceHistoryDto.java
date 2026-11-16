package com.cvesters.moneyjars.account.dto;

import java.util.List;
import java.util.Objects;

import lombok.Getter;

import com.cvesters.moneyjars.account.bdo.AccountBalanceHistory;

@Getter 
public class AccountBalanceHistoryDto {
	private final long accountId;
	private final List<AccountBalanceSnapshotDto> snapshots;

	public AccountBalanceHistoryDto(final AccountBalanceHistory bdo) {
		Objects.requireNonNull(bdo);

		this.accountId = bdo.getAccountId();
		this.snapshots = bdo.getSnapshots()
				.entrySet()
				.stream()
				.map(AccountBalanceSnapshotDto::new)
				.toList();
	}
}
