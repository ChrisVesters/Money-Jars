package com.cvesters.moneyjars.budget;

import org.springframework.data.repository.ListCrudRepository;

import com.cvesters.moneyjars.budget.dao.BudgetDao;

public interface BudgetRepository extends ListCrudRepository<BudgetDao, Long> {

}
