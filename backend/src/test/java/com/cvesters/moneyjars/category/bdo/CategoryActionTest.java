package com.cvesters.moneyjars.category.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.cvesters.moneyjars.category.TestCategory;

class CategoryActionTest {

	private static final TestCategory CATEGORY = TestCategory.GROCERIES;

	@Nested
	class Create {

		@Test
		void success() {
			final var action = new CategoryAction.Create(CATEGORY.getName(),
					CATEGORY.getDescription());

			assertThat(action.name()).isEqualTo(CATEGORY.getName());
			assertThat(action.description())
					.isEqualTo(CATEGORY.getDescription());
		}

		@Test
		void nameNull() {
			final String name = null;
			final String description = CATEGORY.getDescription();

			assertThatThrownBy(
					() -> new CategoryAction.Create(name, description))
							.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void nameInvalid(final String name) {
			final String description = CATEGORY.getDescription();

			assertThatThrownBy(
					() -> new CategoryAction.Create(name, description))
							.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			final String name = CATEGORY.getName();
			final String description = null;

			assertThatThrownBy(
					() -> new CategoryAction.Create(name, description))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void toBdo() {
			final var action = new CategoryAction.Create(CATEGORY.getName(),
					CATEGORY.getDescription());

			final Category bdo = action.toBdo();

			assertThat(bdo.getId()).isNull();
			assertThat(bdo.getName()).isEqualTo(CATEGORY.getName());
			assertThat(bdo.getDescription())
					.isEqualTo(CATEGORY.getDescription());
		}
	}

	@Nested
	class Update {

		@Test
		void success() {
			final var action = new CategoryAction.Update(CATEGORY.getName(),
					CATEGORY.getDescription());

			assertThat(action.name()).isEqualTo(CATEGORY.getName());
			assertThat(action.description())
					.isEqualTo(CATEGORY.getDescription());
		}

		@Test
		void nameNull() {
			final String name = null;
			final String description = CATEGORY.getDescription();

			assertThatThrownBy(
					() -> new CategoryAction.Update(name, description))
							.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void nameInvalid(final String name) {
			final String description = CATEGORY.getDescription();

			assertThatThrownBy(
					() -> new CategoryAction.Update(name, description))
							.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			final String name = CATEGORY.getName();
			final String description = null;

			assertThatThrownBy(
					() -> new CategoryAction.Update(name, description))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void applyOn() {
			final var action = new CategoryAction.Update(CATEGORY.getName(),
					CATEGORY.getDescription());

			final Category target = mock();

			action.applyOn(target);

			verify(target).setName(CATEGORY.getName());
			verify(target).setDescription(CATEGORY.getDescription());
		}

		@Test
		void applyOnTargetNull() {
			final var action = new CategoryAction.Update(CATEGORY.getName(),
					CATEGORY.getDescription());

			assertThatThrownBy(() -> action.applyOn(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
