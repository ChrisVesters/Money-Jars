package com.cvesters.moneyjars.budget;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.budget.bdo.Budget;
import com.cvesters.moneyjars.budget.bdo.BudgetAction;
import com.cvesters.moneyjars.budget.dto.BudgetActionDto;
import com.cvesters.moneyjars.budget.dto.BudgetDto;

@Controller
public class BudgetController {

	private final BudgetService budgetService;

	public BudgetController(final BudgetService budgetService) {
		this.budgetService = budgetService;
	}

	@QueryMapping
	public List<BudgetDto> getBudgets() {
		return budgetService.getAll().stream().map(BudgetDto::new).toList();
	}

	@QueryMapping
	public BudgetDto getBudget(@Argument final long id) {
		final Budget budget = budgetService.get(id);
		return new BudgetDto(budget);
	}

	@MutationMapping
	public BudgetDto createBudget(@Argument final BudgetActionDto.Create req) {
		final BudgetAction.Create action = req.toBdo();
		final Budget created = budgetService.create(action);

		return new BudgetDto(created);
	}

	@MutationMapping
	public BudgetDto updateBudget(@Argument final long id,
			@Argument final BudgetActionDto.Update req) {
		final BudgetAction.Update action = req.toBdo();
		final Budget updated = budgetService.update(id, action);

		return new BudgetDto(updated);
	}

	@MutationMapping
	public boolean deleteBudget(@Argument final long id) {
		budgetService.delete(id);
		return true;
	}

}
