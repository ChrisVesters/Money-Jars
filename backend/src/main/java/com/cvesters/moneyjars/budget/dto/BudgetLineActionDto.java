package com.cvesters.moneyjars.budget.dto;

import java.math.BigDecimal;

import com.cvesters.moneyjars.budget.bdo.BudgetLineAction;

public final class BudgetLineActionDto {

	private BudgetLineActionDto() {
	}

	public static record Create(long budgetId, long categoryId, String amount,
			String direction) {

		public BudgetLineAction.Create toBdo() {
			return new BudgetLineAction.Create(budgetId, categoryId,
					new BigDecimal(amount),
					BudgetLineDirectionDto.toBdo(direction));
		}
	}

	public static record Update(String amount, String direction) {

		public BudgetLineAction.Update toBdo() {
			return new BudgetLineAction.Update(new BigDecimal(amount),
					BudgetLineDirectionDto.toBdo(direction));
		}
	}
}
