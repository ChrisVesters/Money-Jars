package com.cvesters.moneyjars.budget;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.budget.bdo.Budget;
import com.cvesters.moneyjars.budget.dao.BudgetDao;
import com.cvesters.moneyjars.common.exceptions.MissingEntityException;

@Service
public class BudgetStorageGateway {

	public final BudgetRepository repository;

	public BudgetStorageGateway(final BudgetRepository repository) {
		this.repository = repository;
	}

	public List<Budget> getAll() {
		return repository.findAll().stream().map(BudgetDao::toBdo).toList();
	}

	public Budget get(final long id) {
		return repository.findById(id)
				.map(BudgetDao::toBdo)
				.orElseThrow(MissingEntityException::new);
	}

	public Budget create(final Budget budget) {
		Objects.requireNonNull(budget);

		final var dao = new BudgetDao(budget);
		final BudgetDao created = repository.save(dao);

		return created.toBdo();
	}

	public Budget update(final Budget budget) {
		Objects.requireNonNull(budget);

		final BudgetDao found = repository.findById(budget.getId())
				.orElseThrow(IllegalArgumentException::new);
		found.updateWith(budget);
		final BudgetDao updated = repository.save(found);

		return updated.toBdo();
	}

	public void delete(final long id) {
		repository.deleteById(id);
	}
}
