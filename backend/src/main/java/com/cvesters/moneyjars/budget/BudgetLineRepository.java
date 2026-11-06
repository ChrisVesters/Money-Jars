package com.cvesters.moneyjars.budget;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

import com.cvesters.moneyjars.budget.dao.BudgetLineDao;

public interface BudgetLineRepository
		extends ListCrudRepository<BudgetLineDao, Long> {

	List<BudgetLineDao> findAllByBudgetId(long budgetId);

	boolean existsByBudgetIdAndCategoryId(long budgetId, long categoryId);

}
