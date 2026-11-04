package com.cvesters.moneyjars.budget.dto;

import java.time.LocalDate;
import java.util.Objects;

import lombok.Getter;

import com.cvesters.moneyjars.budget.bdo.Budget;

@Getter
public class BudgetDto {

	private Long id;
	private String name;
	private String description;
	private LocalDate startDate;
	private LocalDate endDate;

	public BudgetDto(final Budget budget) {
		Objects.requireNonNull(budget);

		this.id = budget.getId();
		this.name = budget.getName();
		this.description = budget.getDescription();
		this.startDate = budget.getStartDate();
		this.endDate = budget.getEndDate();
	}
}
