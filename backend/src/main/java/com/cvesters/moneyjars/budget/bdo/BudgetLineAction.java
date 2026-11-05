package com.cvesters.moneyjars.budget.bdo;

import java.math.BigDecimal;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

public final class BudgetLineAction {

	private BudgetLineAction() {
	}

	public static record Create(long budgetId, long categoryId,
			BigDecimal amount, BudgetLineDirection direction) {

		public Create {
			Objects.requireNonNull(amount);
			Validate.isTrue(amount.signum() != -1);
			Objects.requireNonNull(direction);
		}

		public BudgetLine toBdo() {
			return new BudgetLine(budgetId, categoryId, amount, direction);
		}
	}

	public static record Update(BigDecimal amount,
			BudgetLineDirection direction) {

		public Update {
			Objects.requireNonNull(amount);
			Validate.isTrue(amount.signum() != -1);
			Objects.requireNonNull(direction);
		}

		public void applyOn(final BudgetLine line) {
			Objects.requireNonNull(line);

			line.setAmount(amount);
			line.setDirection(direction);
		}
	}
}
