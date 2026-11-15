package com.cvesters.moneyjars.jar.bdo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.TreeMap;

import lombok.Getter;

@Getter
public class JarBalanceHistory {
	private final long jarId;
	private final TreeMap<LocalDate, BigDecimal> snapshots;

	public JarBalanceHistory(final long jarId) {
		this.jarId = jarId;
		this.snapshots = new TreeMap<>();
	}

	public void addSnapshot(final LocalDate date, final BigDecimal amount) {
		snapshots.put(date, amount);
	}
}
