package com.cvesters.moneyjars.budget.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.budget.TestBudget;
import com.cvesters.moneyjars.budget.bdo.Budget;

class BudgetDaoTest {

	private static final TestBudget BUDGET = TestBudget.NOVEMBER_2026;

	@Nested
	class Constructor {

		@Test
		void success() {
			final var dao = new BudgetDao(BUDGET.bdo());

			assertThat(dao.getId()).isNull();
			assertThat(dao.getName()).isEqualTo(BUDGET.getName());
			assertThat(dao.getDescription()).isEqualTo(BUDGET.getDescription());
			assertThat(dao.getStartDate()).isEqualTo(BUDGET.getStartDate());
			assertThat(dao.getEndDate()).isEqualTo(BUDGET.getEndDate());
		}

		@Test
		void budgetNull() {
			assertThatThrownBy(() -> new BudgetDao(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class UpdateWith {

		private static final TestBudget OTHER = TestBudget.DECEMBER_2026;

		@Test
		void success() {
			final var dao = new BudgetDao(BUDGET.bdo());
			dao.updateWith(OTHER.bdo());

			assertThat(dao.getId()).isNull();
			assertThat(dao.getName()).isEqualTo(OTHER.getName());
			assertThat(dao.getDescription()).isEqualTo(OTHER.getDescription());
			assertThat(dao.getStartDate()).isEqualTo(OTHER.getStartDate());
			assertThat(dao.getEndDate()).isEqualTo(OTHER.getEndDate());
		}

		@Test
		void budgetNull() {
			final var dao = new BudgetDao(BUDGET.bdo());

			assertThatThrownBy(() -> dao.updateWith(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class ToBdo {

		@Test
		void success() {
			final var dao = new BudgetDao(BUDGET.bdo());
			final Budget bdo = dao.toBdo();

			assertThat(bdo.getId()).isNull();
			assertThat(bdo.getName()).isEqualTo(BUDGET.getName());
			assertThat(bdo.getDescription()).isEqualTo(BUDGET.getDescription());
			assertThat(bdo.getStartDate()).isEqualTo(BUDGET.getStartDate());
			assertThat(bdo.getEndDate()).isEqualTo(BUDGET.getEndDate());
		}
	}
}
