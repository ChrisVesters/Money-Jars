package com.cvesters.moneyjars.category;

import lombok.Getter;

import com.cvesters.moneyjars.category.bdo.Category;

@Getter
public enum TestCategory {

	GROCERIES(1L, "Groceries", "Food and drinks"),
	RENT(2L, "Rent", "Monthly rent of the apartment"),
	FUEL(3L, "Fuel", "Fuel for the car");

	private final long id;
	private final String name;
	private final String description;

	private TestCategory(final long id, final String name,
			final String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public Category bdo() {
		return new Category(id, name, description);
	}

}
