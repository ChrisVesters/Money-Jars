package com.cvesters.moneyjars.category.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.category.TestCategory;
import com.cvesters.moneyjars.category.bdo.CategoryAction;

class CategoryActionDtoTest {

	private static final TestCategory CATEGORY = TestCategory.GROCERIES;

	@Nested
	class Create {

		@Test
		void success() {
			final var dto = new CategoryActionDto.Create(CATEGORY.getName(),
					CATEGORY.getDescription());

			assertThat(dto.name()).isEqualTo(CATEGORY.getName());
			assertThat(dto.description()).isEqualTo(CATEGORY.getDescription());
		}

		@Test
		void toBdo() {
			final var dto = new CategoryActionDto.Create(CATEGORY.getName(),
					CATEGORY.getDescription());

			final CategoryAction.Create bdo = dto.toBdo();

			assertThat(bdo).isEqualTo(new CategoryAction.Create(
					CATEGORY.getName(), CATEGORY.getDescription()));
		}
	}

	@Nested
	class Update {

		@Test
		void success() {
			final var dto = new CategoryActionDto.Update(CATEGORY.getName(),
					CATEGORY.getDescription());

			assertThat(dto.name()).isEqualTo(CATEGORY.getName());
			assertThat(dto.description()).isEqualTo(CATEGORY.getDescription());
		}

		@Test
		void toBdo() {
			final var dto = new CategoryActionDto.Update(CATEGORY.getName(),
					CATEGORY.getDescription());

			final CategoryAction.Update bdo = dto.toBdo();

			assertThat(bdo).isEqualTo(new CategoryAction.Update(
					CATEGORY.getName(), CATEGORY.getDescription()));
		}
	}
}
