package com.cvesters.moneyjars.budget;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.budget.bdo.BudgetLine;
import com.cvesters.moneyjars.budget.bdo.BudgetLineAction;
import com.cvesters.moneyjars.budget.dto.BudgetLineActionDto;
import com.cvesters.moneyjars.budget.dto.BudgetLineDto;

@Controller
public class BudgetLineController {

	private final BudgetLineService budgetLineService;

	public BudgetLineController(final BudgetLineService budgetLineService) {
		this.budgetLineService = budgetLineService;
	}

	@MutationMapping
	public BudgetLineDto createBudgetLine(
			@Argument final BudgetLineActionDto.Create req) {
		final BudgetLineAction.Create action = req.toBdo();
		final BudgetLine created = budgetLineService.create(action);

		return new BudgetLineDto(created);
	}

	@MutationMapping
	public BudgetLineDto updateBudgetLine(@Argument final long id,
			@Argument final BudgetLineActionDto.Update req) {
		final BudgetLineAction.Update action = req.toBdo();
		final BudgetLine updated = budgetLineService.update(id, action);

		return new BudgetLineDto(updated);
	}

	@MutationMapping
	public boolean deleteBudgetLine(@Argument final long id) {
		budgetLineService.delete(id);
		return true;
	}
}
