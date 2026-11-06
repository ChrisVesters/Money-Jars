package com.cvesters.moneyjars.budget;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.budget.bdo.Budget;
import com.cvesters.moneyjars.budget.bdo.BudgetAction;

@Service 
public class BudgetService {
	
	private final BudgetStorageGateway storageGateway;
	
	public BudgetService(final BudgetStorageGateway storageGateway) {
		this.storageGateway = storageGateway;
	}

	public List<Budget> getAll() {
		return storageGateway.getAll();
	}

	public Budget get(final long id) {
		return storageGateway.get(id);
	}

	public Budget create(final BudgetAction.Create action) {
		Objects.requireNonNull(action);

		final var budget = action.toBdo();
		return storageGateway.create(budget);
	}

	public Budget update(final long id, final BudgetAction.Update action) {
		Objects.requireNonNull(action);

		final Budget budget = storageGateway.get(id);
		action.applyOn(budget);

		return storageGateway.update(budget);
	}

	public void delete(final long id) {
		storageGateway.delete(id);
	}
}
