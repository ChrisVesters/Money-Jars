package com.cvesters.moneyjars.account.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

import lombok.Getter;

@Getter 
public class AccountBalanceSnapshotDto {
	private final String date;
	private final String balance;

	public AccountBalanceSnapshotDto(final Map.Entry<LocalDate, BigDecimal> bdo) {
		Objects.requireNonNull(bdo);
		Objects.requireNonNull(bdo.getKey());
		Objects.requireNonNull(bdo.getValue());
		
		this.date = bdo.getKey().toString();
		this.balance = bdo.getValue().toPlainString();
	}
}
