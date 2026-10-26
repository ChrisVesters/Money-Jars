package com.cvesters.moneyjars.category.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.category.TestCategory;
import com.cvesters.moneyjars.category.bdo.Category;

class CategoryDaoTest {

	private static final TestCategory CATEGORY = TestCategory.GROCERIES;

	@Nested
	class Constructor {

		@Test
		void success() {
			final var dao = new CategoryDao(CATEGORY.bdo());

			assertThat(dao.getId()).isNull();
			assertThat(dao.getName()).isEqualTo(CATEGORY.getName());
			assertThat(dao.getDescription())
					.isEqualTo(CATEGORY.getDescription());
		}

		@Test
		void categoryNull() {
			assertThatThrownBy(() -> new CategoryDao(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class UpdateWith {

		@Test
		void success() {
			final String name = "Restaurants";
			final String description = "Eating out";
			final Category update = new Category(name, description);

			final var dao = new CategoryDao(CATEGORY.bdo());
			dao.updateWith(update);

			assertThat(dao.getId()).isNull();
			assertThat(dao.getName()).isEqualTo(name);
			assertThat(dao.getDescription()).isEqualTo(description);
		}

		@Test
		void categoryNull() {
			final var dao = new CategoryDao(CATEGORY.bdo());
			assertThatThrownBy(() -> dao.updateWith(null))
					.isInstanceOf(NullPointerException.class);
		}

	}

	@Nested
	class ToBdo {

		@Test
		void success() {
			final var dao = new CategoryDao(CATEGORY.bdo());
			final var bdo = dao.toBdo();

			assertThat(bdo.getId()).isNull();
			assertThat(bdo.getName()).isEqualTo(CATEGORY.getName());
			assertThat(bdo.getDescription())
					.isEqualTo(CATEGORY.getDescription());
		}
	}
}
