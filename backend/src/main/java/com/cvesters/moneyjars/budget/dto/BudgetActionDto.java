package com.cvesters.moneyjars.budget.dto;

import java.time.LocalDate;

import com.cvesters.moneyjars.budget.bdo.BudgetAction;

public final class BudgetActionDto {

	private BudgetActionDto() {
	}

	public static record Create(String name, String description,
			LocalDate startDate, LocalDate endDate) {

		public BudgetAction.Create toBdo() {
			return new BudgetAction.Create(name, description, startDate,
					endDate);
		}
	}

	public static record Update(String name, String description,
			LocalDate startDate, LocalDate endDate) {

		public BudgetAction.Update toBdo() {
			return new BudgetAction.Update(name, description, startDate,
					endDate);
		}
	}

}
