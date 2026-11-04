package com.cvesters.moneyjars.budget.bdo;

import java.time.LocalDate;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

import lombok.Getter;

@Getter
public class Budget {

	private final Long id;
	private String name;
	private String description;
	private LocalDate startDate;
	private LocalDate endDate;

	public Budget(final String name, final String description,
			final LocalDate startDate, final LocalDate endDate) {
		this(null, name, description, startDate, endDate);
	}

	public Budget(final Long id, final String name, final String description,
			final LocalDate startDate, final LocalDate endDate) {
		Validate.notBlank(name);
		Objects.requireNonNull(description);
		Objects.requireNonNull(startDate);
		Objects.requireNonNull(endDate);
		Validate.isTrue(!endDate.isBefore(startDate));

		this.id = id;
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public void setName(final String name) {
		Validate.notBlank(name);

		this.name = name;
	}

	public void setDescription(final String description) {
		Objects.requireNonNull(description);

		this.description = description;
	}

	public void setPeriod(final LocalDate startDate, final LocalDate endDate) {
		Objects.requireNonNull(startDate);
		Objects.requireNonNull(endDate);
		Validate.isTrue(!endDate.isBefore(startDate));

		this.startDate = startDate;
		this.endDate = endDate;
	}
}
