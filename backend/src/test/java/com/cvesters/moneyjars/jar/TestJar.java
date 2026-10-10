package com.cvesters.moneyjars.jar;

import lombok.Getter;

import com.cvesters.moneyjars.jar.bdo.Jar;

@Getter
public enum TestJar {

	HOUSEHOLD(1L, "Household", "General expenses"),
	HOLIDAY(2L, "Holiday", "We need some time off"),
	CAR(3L, "Car", "Fuel and maintenance");

	private final long id;
	private final String name;
	private final String description;

	private TestJar(final long id, final String name, final String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public Jar bdo() {
		return new Jar(id, name, description);
	}

}
