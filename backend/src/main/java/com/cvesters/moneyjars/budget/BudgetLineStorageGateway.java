package com.cvesters.moneyjars.budget;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.budget.bdo.BudgetLine;
import com.cvesters.moneyjars.budget.dao.BudgetLineDao;
import com.cvesters.moneyjars.common.exceptions.MissingEntityException;

@Service
public class BudgetLineStorageGateway {

	private final BudgetLineRepository repository;

	public BudgetLineStorageGateway(final BudgetLineRepository repository) {
		this.repository = repository;
	}

	public List<BudgetLine> getAllByBudget(final long budgetId) {
		return repository.findAllByBudgetId(budgetId)
				.stream()
				.map(BudgetLineDao::toBdo)
				.toList();
	}

	public boolean exists(final long budgetId, final long categoryId) {
		return repository.existsByBudgetIdAndCategoryId(budgetId, categoryId);
	}

	public BudgetLine get(final long id) {
		return repository.findById(id)
				.map(BudgetLineDao::toBdo)
				.orElseThrow(MissingEntityException::new);
	}

	public BudgetLine create(final BudgetLine line) {
		Objects.requireNonNull(line);

		final var dao = new BudgetLineDao(line);
		final BudgetLineDao created = repository.save(dao);

		return created.toBdo();
	}

	public BudgetLine update(final BudgetLine line) {
		Objects.requireNonNull(line);

		final BudgetLineDao found = repository.findById(line.getId())
				.orElseThrow(MissingEntityException::new);
		found.updateWith(line);
		final BudgetLineDao updated = repository.save(found);

		return updated.toBdo();
	}

	public void delete(final long id) {
		repository.deleteById(id);
	}
}
