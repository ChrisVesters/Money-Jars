package com.cvesters.moneyjars.jar.bdo;

import java.math.BigDecimal;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

import lombok.Getter;

@Getter
public class Jar {

	private Long id;
	private String name;
	private String description;
	private BigDecimal balance;

	public Jar(final String name, final String description) {
		this(null, name, description, BigDecimal.ZERO);
	}

	public Jar(final Long id, final String name, final String description,
			final BigDecimal balance) {
		Validate.notBlank(name);
		Objects.requireNonNull(description);
		Objects.requireNonNull(balance);

		this.id = id;
		this.name = name;
		this.description = description;
		this.balance = balance;
	}

	public void setName(final String name) {
		Validate.notBlank(name);
		this.name = name;
	}

	public void setDescription(final String description) {
		Objects.requireNonNull(description);
		this.description = description;
	}
}
