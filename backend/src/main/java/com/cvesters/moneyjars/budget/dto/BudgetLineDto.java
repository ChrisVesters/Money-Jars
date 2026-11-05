package com.cvesters.moneyjars.budget.dto;

import java.util.Objects;

import lombok.Getter;

import com.cvesters.moneyjars.budget.bdo.BudgetLine;

@Getter
public class BudgetLineDto {

	private Long id;
	private long categoryId;
	private String amount;
	private String direction;

	public BudgetLineDto(final BudgetLine line) {
		Objects.requireNonNull(line);

		this.id = line.getId();
		this.categoryId = line.getCategoryId();
		this.amount = line.getAmount().toPlainString();
		this.direction = BudgetLineDirectionDto.toDto(line.getDirection());
	}
}
