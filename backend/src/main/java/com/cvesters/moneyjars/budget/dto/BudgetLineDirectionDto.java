package com.cvesters.moneyjars.budget.dto;

import java.util.Objects;

import com.cvesters.moneyjars.budget.bdo.BudgetLineDirection;

public final class BudgetLineDirectionDto {

	private BudgetLineDirectionDto() {
	}

	public static BudgetLineDirection toBdo(final String direction) {
		Objects.requireNonNull(direction);

		return switch (direction) {
			case "INCOME" -> BudgetLineDirection.INCOME;
			case "EXPENSE" -> BudgetLineDirection.EXPENSE;
			default -> throw new IllegalArgumentException();
		};
	}

	public static String toDto(final BudgetLineDirection direction) {
		Objects.requireNonNull(direction);

		return switch (direction) {
			case INCOME -> "INCOME";
			case EXPENSE -> "EXPENSE";
		};
	}
}
