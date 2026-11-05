package com.cvesters.moneyjars.budget;

import org.springframework.data.repository.ListCrudRepository;

import com.cvesters.moneyjars.budget.dao.BudgetLineDao;

public interface BudgetLineRepository
		extends ListCrudRepository<BudgetLineDao, Long> {

	boolean existsByBudgetIdAndCategoryId(long budgetId, long categoryId);

}
