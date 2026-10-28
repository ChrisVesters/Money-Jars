package com.cvesters.moneyjars.category;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.category.bdo.Category;
import com.cvesters.moneyjars.category.dao.CategoryDao;
import com.cvesters.moneyjars.common.exceptions.MissingEntityException;

@Service
public class CategoryStorageGateway {

	private final CategoryRepository repository;

	public CategoryStorageGateway(final CategoryRepository repository) {
		this.repository = repository;
	}

	public List<Category> getAll() {
		return repository.findAll().stream().map(CategoryDao::toBdo).toList();
	}

	public Category get(final long id) {
		return repository.findById(id)
				.map(CategoryDao::toBdo)
				.orElseThrow(MissingEntityException::new);
	}

	public Category create(final Category Category) {
		Objects.requireNonNull(Category);

		final var dao = new CategoryDao(Category);
		final CategoryDao created = repository.save(dao);

		return created.toBdo();
	}

	public Category update(final Category Category) {
		Objects.requireNonNull(Category);

		final CategoryDao found = repository.findById(Category.getId())
				.orElseThrow(IllegalArgumentException::new);
		found.updateWith(Category);
		final CategoryDao updated = repository.save(found);

		return updated.toBdo();
	}

	public void delete(final long id) {
		repository.deleteById(id);
	}
}
