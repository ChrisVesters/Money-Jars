package com.cvesters.moneyjars.budget;

import java.math.BigDecimal;

import lombok.Getter;

import com.cvesters.moneyjars.budget.bdo.BudgetLine;
import com.cvesters.moneyjars.budget.bdo.BudgetLineDirection;
import com.cvesters.moneyjars.category.TestCategory;

@Getter
public enum TestBudgetLine {

	NOVEMBER_RENT(1L, TestBudget.NOVEMBER_2026, TestCategory.RENT,
			new BigDecimal("850.00"), BudgetLineDirection.EXPENSE),
	NOVEMBER_GROCERIES(2L, TestBudget.NOVEMBER_2026, TestCategory.GROCERIES,
			new BigDecimal("400.00"), BudgetLineDirection.EXPENSE),
	DECEMBER_FUEL(3L, TestBudget.DECEMBER_2026, TestCategory.FUEL,
			new BigDecimal("120.50"), BudgetLineDirection.INCOME);

	private final long id;
	private final TestBudget budget;
	private final TestCategory category;
	private final BigDecimal amount;
	private final BudgetLineDirection direction;

	private TestBudgetLine(final long id, final TestBudget budget,
			final TestCategory category, final BigDecimal amount,
			final BudgetLineDirection direction) {
		this.id = id;
		this.budget = budget;
		this.category = category;
		this.amount = amount;
		this.direction = direction;
	}

	public BudgetLine bdo() {
		return new BudgetLine(id, budget.getId(), category.getId(), amount,
				direction);
	}
}
