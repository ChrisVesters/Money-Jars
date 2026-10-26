package com.cvesters.moneyjars.category;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.category.bdo.Category;
import com.cvesters.moneyjars.category.bdo.CategoryAction;

@Service
public class CategoryService {

	private final CategoryStorageGateway storage;

	public CategoryService(final CategoryStorageGateway storage) {
		this.storage = storage;
	}

	public List<Category> getAll() {
		return storage.getAll();
	}

	public Category get(final long id) {
		return storage.get(id);
	}

	public Category create(final CategoryAction.Create action) {
		Objects.requireNonNull(action);

		final var Category = action.toBdo();
		return storage.create(Category);
	}

	public Category update(final long id, final CategoryAction.Update action) {
		Objects.requireNonNull(action);

		final Category Category = storage.get(id);
		action.applyOn(Category);

		return storage.update(Category);
	}

	public void delete(final long id) {
		storage.delete(id);
	}
}
