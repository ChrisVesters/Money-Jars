package com.cvesters.moneyjars.jar;

import java.math.BigDecimal;
import java.util.Optional;

import lombok.Getter;

import com.cvesters.moneyjars.jar.bdo.Jar;

@Getter
public enum TestJar {

	HOUSEHOLD(1L, "Household", "General expenses", null),
	HOLIDAY(2L, "Holiday", "We need some time off", new BigDecimal("3000.00")),
	CAR(3L, "Car", "Fuel and maintenance", new BigDecimal("1500.00"));

	private final long id;
	private final String name;
	private final String description;
	private final BigDecimal goal;

	private TestJar(final long id, final String name, final String description,
			final BigDecimal goal) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.goal = goal;
	}

	public Jar bdo() {
		return new Jar(id, name, description, goal);
	}

	public String getGoalString() {
		return Optional.ofNullable(goal)
				.map(BigDecimal::toPlainString)
				.orElse(null);
	}

}
