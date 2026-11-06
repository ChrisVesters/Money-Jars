package com.cvesters.moneyjars.budget.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.budget.TestBudgetLine;
import com.cvesters.moneyjars.budget.bdo.BudgetLineAction;

class BudgetLineActionDtoTest {

	private static final TestBudgetLine LINE = TestBudgetLine.NOVEMBER_RENT;

	@Nested
	class Create {

		@Test
		void success() {
			final var dto = new BudgetLineActionDto.Create(
					LINE.getBudget().getId(), LINE.getCategory().getId(),
					LINE.getAmount().toPlainString(),
					LINE.getDirection().name());

			assertThat(dto.budgetId()).isEqualTo(LINE.getBudget().getId());
			assertThat(dto.categoryId()).isEqualTo(LINE.getCategory().getId());
			assertThat(dto.amount()).isEqualTo(LINE.getAmount().toPlainString());
			assertThat(dto.direction()).isEqualTo(LINE.getDirection().name());
		}

		@Test
		void toBdo() {
			final var dto = new BudgetLineActionDto.Create(
					LINE.getBudget().getId(), LINE.getCategory().getId(),
					LINE.getAmount().toPlainString(),
					LINE.getDirection().name());

			final BudgetLineAction.Create bdo = dto.toBdo();

			assertThat(bdo).isEqualTo(new BudgetLineAction.Create(
					LINE.getBudget().getId(), LINE.getCategory().getId(),
					LINE.getAmount(), LINE.getDirection()));
		}
	}

	@Nested
	class Update {

		@Test
		void success() {
			final var dto = new BudgetLineActionDto.Update(
					LINE.getAmount().toPlainString(),
					LINE.getDirection().name());

			assertThat(dto.amount()).isEqualTo(LINE.getAmount().toPlainString());
			assertThat(dto.direction()).isEqualTo(LINE.getDirection().name());
		}

		@Test
		void toBdo() {
			final var dto = new BudgetLineActionDto.Update(
					LINE.getAmount().toPlainString(),
					LINE.getDirection().name());

			final BudgetLineAction.Update bdo = dto.toBdo();

			assertThat(bdo).isEqualTo(new BudgetLineAction.Update(
					LINE.getAmount(), LINE.getDirection()));
		}
	}
}
