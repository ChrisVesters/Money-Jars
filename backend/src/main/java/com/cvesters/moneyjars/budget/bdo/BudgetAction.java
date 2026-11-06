package com.cvesters.moneyjars.budget.bdo;

import java.time.LocalDate;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

public final class BudgetAction {

	private BudgetAction() {
	}

	public static record Create(String name, String description,
			LocalDate startDate, LocalDate endDate) {

		public Create {
			Validate.notBlank(name);
			Objects.requireNonNull(description);
			Objects.requireNonNull(startDate);
			Objects.requireNonNull(endDate);
			Validate.isTrue(!endDate.isBefore(startDate));
		}

		public Budget toBdo() {
			return new Budget(name, description, startDate, endDate);
		}
	}

	public static record Update(String name, String description,
			LocalDate startDate, LocalDate endDate) {

		public Update {
			Validate.notBlank(name);
			Objects.requireNonNull(description);
			Objects.requireNonNull(startDate);
			Objects.requireNonNull(endDate);
			Validate.isTrue(!endDate.isBefore(startDate));
		}

		public void applyOn(final Budget budget) {
			Objects.requireNonNull(budget);

			budget.setName(name);
			budget.setDescription(description);
			budget.setPeriod(startDate, endDate);
		}
	}
}
