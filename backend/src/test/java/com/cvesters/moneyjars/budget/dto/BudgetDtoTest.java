package com.cvesters.moneyjars.budget.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.budget.TestBudget;

class BudgetDtoTest {

	private static final TestBudget BUDGET = TestBudget.NOVEMBER_2026;

	@Nested
	class Constructor {

		@Test
		void success() {
			final var dto = new BudgetDto(BUDGET.bdo());

			assertThat(dto.getId()).isEqualTo(BUDGET.getId());
			assertThat(dto.getName()).isEqualTo(BUDGET.getName());
			assertThat(dto.getDescription()).isEqualTo(BUDGET.getDescription());
			assertThat(dto.getStartDate()).isEqualTo(BUDGET.getStartDate());
			assertThat(dto.getEndDate()).isEqualTo(BUDGET.getEndDate());
		}

		@Test
		void bdoNull() {
			assertThatThrownBy(() -> new BudgetDto(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
