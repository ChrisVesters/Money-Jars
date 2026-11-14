package com.cvesters.moneyjars.jar.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

import lombok.Getter;

@Getter 
public class JarBalanceSnapshotDto {
	private final String date;
	private final String balance;

	public JarBalanceSnapshotDto(final Map.Entry<LocalDate, BigDecimal> bdo) {
		Objects.requireNonNull(bdo);
		Objects.requireNonNull(bdo.getKey());
		Objects.requireNonNull(bdo.getValue());
		
		this.date = bdo.getKey().toString();
		this.balance = bdo.getValue().toPlainString();
	}
}
