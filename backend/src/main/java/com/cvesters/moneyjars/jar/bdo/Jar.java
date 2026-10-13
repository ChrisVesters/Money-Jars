package com.cvesters.moneyjars.jar.bdo;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import lombok.Getter;

@Getter
public class Jar {

	private final Long id;
	private String name;
	private String description;
	private BigDecimal goal;

	public Jar(final String name, final String description,
			final BigDecimal goal) {
		this(null, name, description, goal);
	}

	public Jar(final Long id, final String name, final String description,
			final BigDecimal goal) {
		Validate.notBlank(name);
		Objects.requireNonNull(description);

		this.id = id;
		this.name = name;
		this.description = description;
		this.goal = goal;
	}

	public void setName(final String name) {
		Validate.notBlank(name);
		this.name = name;
	}

	public void setDescription(final String description) {
		Objects.requireNonNull(description);
		this.description = description;
	}

	public Optional<BigDecimal> getGoal() {
		return Optional.ofNullable(goal);
	}

	public void setGoal(final BigDecimal goal) {
		Validate.isTrue(goal == null || goal.signum() > 0);

		this.goal = goal;
	}
}
