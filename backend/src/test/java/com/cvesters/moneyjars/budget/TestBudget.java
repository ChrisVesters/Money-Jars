package com.cvesters.moneyjars.budget;

import java.time.LocalDate;

import lombok.Getter;

import com.cvesters.moneyjars.budget.bdo.Budget;

@Getter
public enum TestBudget {

	NOVEMBER_2026(1L, "November 2026", "Regular month",
			LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30)),
	DECEMBER_2026(2L, "December 2026", "Holidays and gifts",
			LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 31));

	private final long id;
	private final String name;
	private final String description;
	private final LocalDate startDate;
	private final LocalDate endDate;

	private TestBudget(final long id, final String name,
			final String description, final LocalDate startDate,
			final LocalDate endDate) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Budget bdo() {
		return new Budget(id, name, description, startDate, endDate);
	}

}
