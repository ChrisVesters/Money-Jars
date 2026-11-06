package com.cvesters.moneyjars.budget;

import java.util.List;

import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.budget.dto.BudgetDto;
import com.cvesters.moneyjars.budget.dto.BudgetLineDto;

@Controller
public class BudgetResolver {

	private final BudgetLineService budgetLineService;

	public BudgetResolver(final BudgetLineService budgetLineService) {
		this.budgetLineService = budgetLineService;
	}

	@SchemaMapping(typeName = "Budget", field = "lines")
	public List<BudgetLineDto> lines(final BudgetDto budget) {
		return budgetLineService.getAllByBudget(budget.getId())
				.stream()
				.map(BudgetLineDto::new)
				.toList();
	}
}
