package com.cvesters.moneyjars.budget;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.budget.bdo.BudgetLine;
import com.cvesters.moneyjars.budget.bdo.BudgetLineAction;
import com.cvesters.moneyjars.common.exceptions.DuplicateEntityException;

@Service
public class BudgetLineService {

	private final BudgetLineStorageGateway storageGateway;

	public BudgetLineService(final BudgetLineStorageGateway storageGateway) {
		this.storageGateway = storageGateway;
	}

	public BudgetLine create(final BudgetLineAction.Create action) {
		Objects.requireNonNull(action);

		if (storageGateway.exists(action.budgetId(), action.categoryId())) {
			throw new DuplicateEntityException();
		}

		final BudgetLine line = action.toBdo();
		return storageGateway.create(line);
	}

	public BudgetLine update(final long id,
			final BudgetLineAction.Update action) {
		Objects.requireNonNull(action);

		final BudgetLine line = storageGateway.get(id);
		action.applyOn(line);

		return storageGateway.update(line);
	}

	public void delete(final long id) {
		storageGateway.delete(id);
	}
}
