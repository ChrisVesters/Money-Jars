package com.cvesters.moneyjars.jar;

import java.math.BigDecimal;

import lombok.Getter;

import com.cvesters.moneyjars.jar.bdo.Jar;

@Getter
public enum TestJar {

	HOUSEHOLD(1L, "Household", "General expenses", new BigDecimal("734.85")),
	HOLIDAY(2L, "Holiday", "We need some time off", new BigDecimal("2300.00"));

	private final long id;
	private final String name;
	private final String description;
	private final BigDecimal balance;

	private TestJar(final long id, final String name, final String description,
			final BigDecimal balance) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.balance = balance;
	}

	public Jar bdo() {
		return new Jar(id, name, description, balance);
	}

}
