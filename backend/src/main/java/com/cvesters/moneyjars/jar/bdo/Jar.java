package com.cvesters.moneyjars.jar.bdo;

import java.math.BigDecimal;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Jar {

	private Long id;
	private String name;
	private String description;
	private BigDecimal balance = BigDecimal.ZERO;

	public Jar(final String name, final String description) {
		this(null, name, description, BigDecimal.ZERO);
	}

	public Jar(final Long id, final String name, final String description, final BigDecimal balance) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(description);
		Objects.requireNonNull(balance);

		this.id = id;
		this.name = name;
		this.description = description;
		this.balance = balance;
	}
}
