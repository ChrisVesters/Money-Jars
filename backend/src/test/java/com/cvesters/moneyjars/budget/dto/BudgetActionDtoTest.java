package com.cvesters.moneyjars.budget.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.budget.TestBudget;
import com.cvesters.moneyjars.budget.bdo.BudgetAction;

class BudgetActionDtoTest {

	private static final TestBudget BUDGET = TestBudget.NOVEMBER_2026;

	@Nested
	class Create {

		@Test
		void success() {
			final var dto = new BudgetActionDto.Create(BUDGET.getName(),
					BUDGET.getDescription(), BUDGET.getStartDate(),
					BUDGET.getEndDate());

			assertThat(dto.name()).isEqualTo(BUDGET.getName());
			assertThat(dto.description()).isEqualTo(BUDGET.getDescription());
			assertThat(dto.startDate()).isEqualTo(BUDGET.getStartDate());
			assertThat(dto.endDate()).isEqualTo(BUDGET.getEndDate());
		}

		@Test
		void toBdo() {
			final var dto = new BudgetActionDto.Create(BUDGET.getName(),
					BUDGET.getDescription(), BUDGET.getStartDate(),
					BUDGET.getEndDate());

			final BudgetAction.Create bdo = dto.toBdo();

			assertThat(bdo).isEqualTo(new BudgetAction.Create(BUDGET.getName(),
					BUDGET.getDescription(), BUDGET.getStartDate(),
					BUDGET.getEndDate()));
		}
	}

	@Nested
	class Update {

		@Test
		void success() {
			final var dto = new BudgetActionDto.Update(BUDGET.getName(),
					BUDGET.getDescription(), BUDGET.getStartDate(),
					BUDGET.getEndDate());

			assertThat(dto.name()).isEqualTo(BUDGET.getName());
			assertThat(dto.description()).isEqualTo(BUDGET.getDescription());
			assertThat(dto.startDate()).isEqualTo(BUDGET.getStartDate());
			assertThat(dto.endDate()).isEqualTo(BUDGET.getEndDate());
		}

		@Test
		void toBdo() {
			final var dto = new BudgetActionDto.Update(BUDGET.getName(),
					BUDGET.getDescription(), BUDGET.getStartDate(),
					BUDGET.getEndDate());

			final BudgetAction.Update bdo = dto.toBdo();

			assertThat(bdo).isEqualTo(new BudgetAction.Update(BUDGET.getName(),
					BUDGET.getDescription(), BUDGET.getStartDate(),
					BUDGET.getEndDate()));
		}
	}
}
