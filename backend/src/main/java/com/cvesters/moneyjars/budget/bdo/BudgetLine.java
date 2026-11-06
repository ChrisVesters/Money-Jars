package com.cvesters.moneyjars.budget.bdo;

import java.math.BigDecimal;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

import lombok.Getter;

@Getter
public class BudgetLine {

	private final Long id;
	private final long budgetId;
	private final long categoryId;
	private BigDecimal amount;
	private BudgetLineDirection direction;

	public BudgetLine(final long budgetId, final long categoryId,
			final BigDecimal amount, final BudgetLineDirection direction) {
		this(null, budgetId, categoryId, amount, direction);
	}

	public BudgetLine(final Long id, final long budgetId, final long categoryId,
			final BigDecimal amount, final BudgetLineDirection direction) {
		Objects.requireNonNull(amount);
		Validate.isTrue(amount.signum() != -1);
		Objects.requireNonNull(direction);

		this.id = id;
		this.budgetId = budgetId;
		this.categoryId = categoryId;
		this.amount = amount;
		this.direction = direction;
	}

	public void setAmount(final BigDecimal amount) {
		Objects.requireNonNull(amount);
		Validate.isTrue(amount.signum() != -1);

		this.amount = amount;
	}

	public void setDirection(final BudgetLineDirection direction) {
		Objects.requireNonNull(direction);

		this.direction = direction;
	}
}
