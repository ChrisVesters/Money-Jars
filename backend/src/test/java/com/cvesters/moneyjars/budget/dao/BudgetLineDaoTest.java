package com.cvesters.moneyjars.budget.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.budget.TestBudgetLine;
import com.cvesters.moneyjars.budget.bdo.BudgetLine;

class BudgetLineDaoTest {

	private static final TestBudgetLine LINE = TestBudgetLine.NOVEMBER_RENT;

	@Nested
	class Constructor {

		@Test
		void success() {
			final var dao = new BudgetLineDao(LINE.bdo());

			assertThat(dao.getId()).isNull();
			assertThat(dao.getBudgetId()).isEqualTo(LINE.getBudget().getId());
			assertThat(dao.getCategoryId())
					.isEqualTo(LINE.getCategory().getId());
			assertThat(dao.getAmount()).isEqualTo(LINE.getAmount());
			assertThat(dao.getDirection()).isEqualTo(
					BudgetLineDirectionDao.toDao(LINE.getDirection()));
		}

		@Test
		void lineNull() {
			assertThatThrownBy(() -> new BudgetLineDao(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class UpdateWith {

		private static final TestBudgetLine OTHER = TestBudgetLine.DECEMBER_FUEL;

		@Test
		void success() {
			final var dao = new BudgetLineDao(LINE.bdo());
			dao.updateWith(OTHER.bdo());

			assertThat(dao.getId()).isNull();
			assertThat(dao.getBudgetId()).isEqualTo(LINE.getBudget().getId());
			assertThat(dao.getCategoryId())
					.isEqualTo(LINE.getCategory().getId());
			assertThat(dao.getAmount()).isEqualTo(OTHER.getAmount());
			assertThat(dao.getDirection()).isEqualTo(
					BudgetLineDirectionDao.toDao(OTHER.getDirection()));
		}

		@Test
		void lineNull() {
			final var dao = new BudgetLineDao(LINE.bdo());

			assertThatThrownBy(() -> dao.updateWith(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class ToBdo {

		@Test
		void success() {
			final var dao = new BudgetLineDao(LINE.bdo());
			final BudgetLine bdo = dao.toBdo();

			assertThat(bdo.getId()).isNull();
			assertThat(bdo.getBudgetId()).isEqualTo(LINE.getBudget().getId());
			assertThat(bdo.getCategoryId())
					.isEqualTo(LINE.getCategory().getId());
			assertThat(bdo.getAmount()).isEqualTo(LINE.getAmount());
			assertThat(bdo.getDirection()).isEqualTo(LINE.getDirection());
		}
	}
}
