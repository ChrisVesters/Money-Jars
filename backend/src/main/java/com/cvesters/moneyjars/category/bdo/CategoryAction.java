package com.cvesters.moneyjars.category.bdo;

import java.util.Objects;

import org.apache.commons.lang3.Validate;

public final class CategoryAction {

	private CategoryAction() {
	}

	public static record Create(String name, String description) {

		public Create {
			Validate.notBlank(name);
			Objects.requireNonNull(description);
		}

		public Category toBdo() {
			return new Category(name, description);
		}
	}

	public static record Update(String name, String description) {

		public Update {
			Validate.notBlank(name);
			Objects.requireNonNull(description);
		}

		public void applyOn(final Category Category) {
			Objects.requireNonNull(Category);

			Category.setName(name);
			Category.setDescription(description);
		}
	}
}
