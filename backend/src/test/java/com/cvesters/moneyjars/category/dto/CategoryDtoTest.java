package com.cvesters.moneyjars.category.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.category.TestCategory;

class CategoryDtoTest {

	private static final TestCategory CATEGORY = TestCategory.GROCERIES;

	@Nested
	class Constructor {

		@Test
		void success() {
			final var dto = new CategoryDto(CATEGORY.bdo());

			assertThat(dto.getId()).isEqualTo(CATEGORY.getId());
			assertThat(dto.getName()).isEqualTo(CATEGORY.getName());
			assertThat(dto.getDescription())
					.isEqualTo(CATEGORY.getDescription());
		}

		@Test
		void bdoNull() {
			assertThatThrownBy(() -> new CategoryDto(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
