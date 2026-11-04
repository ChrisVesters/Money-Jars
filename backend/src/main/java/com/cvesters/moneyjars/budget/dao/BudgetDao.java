package com.cvesters.moneyjars.budget.dao;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cvesters.moneyjars.budget.bdo.Budget;

@Getter
@Entity
@Table(name = "budgets")
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class BudgetDao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String description;

	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	@Column(name = "end_date", nullable = false)
	private LocalDate endDate;

	public BudgetDao(final Budget budget) {
		Objects.requireNonNull(budget);

		this.name = budget.getName();
		this.description = budget.getDescription();
		this.startDate = budget.getStartDate();
		this.endDate = budget.getEndDate();
	}

	public void updateWith(final Budget budget) {
		Objects.requireNonNull(budget);

		this.name = budget.getName();
		this.description = budget.getDescription();
		this.startDate = budget.getStartDate();
		this.endDate = budget.getEndDate();
	}

	public Budget toBdo() {
		return new Budget(id, name, description, startDate, endDate);
	}
}
