package com.cvesters.moneyjars.budget.dao;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cvesters.moneyjars.budget.bdo.BudgetLine;

@Getter
@Entity
@Table(name = "budget_lines")
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class BudgetLineDao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "budget_id", nullable = false, updatable = false)
	private long budgetId;

	@Column(name = "category_id", nullable = false, updatable = false)
	private long categoryId;

	@Column(name = "amount", nullable = false)
	private BigDecimal amount;

	@Column(name = "direction", nullable = false)
	private short direction;

	public BudgetLineDao(final BudgetLine line) {
		Objects.requireNonNull(line);

		this.budgetId = line.getBudgetId();
		this.categoryId = line.getCategoryId();
		this.amount = line.getAmount();
		this.direction = BudgetLineDirectionDao.toDao(line.getDirection());
	}

	public void updateWith(final BudgetLine line) {
		Objects.requireNonNull(line);

		this.amount = line.getAmount();
		this.direction = BudgetLineDirectionDao.toDao(line.getDirection());
	}

	public BudgetLine toBdo() {
		return new BudgetLine(id, budgetId, categoryId, amount,
				BudgetLineDirectionDao.toBdo(direction));
	}
}
