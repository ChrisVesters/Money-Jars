package com.cvesters.moneyjars.jar.bdo;

import java.util.Objects;

import org.apache.commons.lang3.Validate;

import lombok.Getter;

@Getter
public class Jar {

	private final Long id;
	private String name;
	private String description;

	public Jar(final String name, final String description) {
		this(null, name, description);
	}

	public Jar(final Long id, final String name, final String description) {
		Validate.notBlank(name);
		Objects.requireNonNull(description);

		this.id = id;
		this.name = name;
		this.description = description;
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
