package com.cvesters.moneyjars.category.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.cvesters.moneyjars.category.TestCategory;

class CategoryTest {

	private static final TestCategory CATEGORY = TestCategory.GROCERIES;

	@Nested
	class Constructor {

		@Test
		void withoutId() {
			final var category = new Category(CATEGORY.getName(),
					CATEGORY.getDescription());

			assertThat(category.getId()).isNull();
			assertThat(category.getName()).isEqualTo(CATEGORY.getName());
			assertThat(category.getDescription())
					.isEqualTo(CATEGORY.getDescription());
		}

		@Test
		void withId() {
			final var category = new Category(CATEGORY.getId(),
					CATEGORY.getName(), CATEGORY.getDescription());

			assertThat(category.getId()).isEqualTo(CATEGORY.getId());
			assertThat(category.getName()).isEqualTo(CATEGORY.getName());
			assertThat(category.getDescription())
					.isEqualTo(CATEGORY.getDescription());
		}

		@Test
		void nameNull() {
			final long id = CATEGORY.getId();
			final String name = null;
			final String description = CATEGORY.getDescription();

			assertThatThrownBy(() -> new Category(id, name, description))
					.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void nameInvalid(final String name) {
			final long id = CATEGORY.getId();
			final String description = CATEGORY.getDescription();

			assertThatThrownBy(() -> new Category(id, name, description))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			final long id = CATEGORY.getId();
			final String name = CATEGORY.getName();
			final String description = null;

			assertThatThrownBy(() -> new Category(id, name, description))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class SetName {

		final Category category = CATEGORY.bdo();

		@Test
		void success() {
			category.setName("New name");

			assertThat(category.getName()).isEqualTo("New name");
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void nameInvalid(final String name) {
			assertThatThrownBy(() -> category.setName(name))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void nameNull() {
			assertThatThrownBy(() -> category.setName(null))
					.isInstanceOf(NullPointerException.class);
		}

	}

	@Nested
	class SetDescription {

		final Category category = CATEGORY.bdo();

		@Test
		void success() {
			category.setDescription("New description");

			assertThat(category.getDescription()).isEqualTo("New description");
		}

		@Test
		void descriptionNull() {
			assertThatThrownBy(() -> category.setDescription(null))
					.isInstanceOf(NullPointerException.class);
		}

	}
}
