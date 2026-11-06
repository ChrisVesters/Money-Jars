package com.cvesters.moneyjars.budget.dao;

import java.util.Objects;

import com.cvesters.moneyjars.budget.bdo.BudgetLineDirection;

public class BudgetLineDirectionDao {
	
	private BudgetLineDirectionDao() {
	}

	public static BudgetLineDirection toBdo(final short type) {
		return switch (type) {
			case 0 -> BudgetLineDirection.INCOME;
			case 1 -> BudgetLineDirection.EXPENSE;
			default -> throw new IllegalArgumentException();
		};
	}

	public static short toDao(final BudgetLineDirection direction) {
		Objects.requireNonNull(direction);

		return switch (direction) {
			case INCOME -> 0;
			case EXPENSE -> 1;
		};
	}
}