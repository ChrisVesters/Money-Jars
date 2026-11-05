package com.cvesters.moneyjars.budget.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.budget.TestBudgetLine;

class BudgetLineTest {

	private static final TestBudgetLine LINE = TestBudgetLine.NOVEMBER_RENT;

	@Nested
	class Constructor {

		@Test
		void withoutId() {
			final var line = new BudgetLine(LINE.getBudget().getId(),
					LINE.getCategory().getId(), LINE.getAmount(),
					LINE.getDirection());

			assertThat(line.getId()).isNull();
			assertThat(line.getBudgetId()).isEqualTo(LINE.getBudget().getId());
			assertThat(line.getCategoryId())
					.isEqualTo(LINE.getCategory().getId());
			assertThat(line.getAmount()).isEqualByComparingTo(LINE.getAmount());
			assertThat(line.getDirection()).isEqualTo(LINE.getDirection());
		}

		@Test
		void withId() {
			final var line = new BudgetLine(LINE.getId(),
					LINE.getBudget().getId(), LINE.getCategory().getId(),
					LINE.getAmount(), LINE.getDirection());

			assertThat(line.getId()).isEqualTo(LINE.getId());
			assertThat(line.getBudgetId()).isEqualTo(LINE.getBudget().getId());
			assertThat(line.getCategoryId())
					.isEqualTo(LINE.getCategory().getId());
			assertThat(line.getAmount()).isEqualByComparingTo(LINE.getAmount());
			assertThat(line.getDirection()).isEqualTo(LINE.getDirection());
		}

		@Test
		void amountZero() {
			final var line = new BudgetLine(LINE.getId(),
					LINE.getBudget().getId(), LINE.getCategory().getId(),
					BigDecimal.ZERO, LINE.getDirection());

			assertThat(line.getId()).isEqualTo(LINE.getId());
			assertThat(line.getBudgetId()).isEqualTo(LINE.getBudget().getId());
			assertThat(line.getCategoryId())
					.isEqualTo(LINE.getCategory().getId());
			assertThat(line.getAmount()).isEqualByComparingTo(BigDecimal.ZERO);
			assertThat(line.getDirection()).isEqualTo(LINE.getDirection());
		}

		@Test
		void amountNull() {
			final long id = LINE.getId();
			final long budgetId = LINE.getBudget().getId();
			final long categoryId = LINE.getCategory().getId();
			final BigDecimal amount = null;
			final BudgetLineDirection direction = LINE.getDirection();

			assertThatThrownBy(() -> new BudgetLine(id, budgetId, categoryId,
					amount, direction))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void amountNegative() {
			final long id = LINE.getId();
			final long budgetId = LINE.getBudget().getId();
			final long categoryId = LINE.getCategory().getId();
			final BigDecimal amount = LINE.getAmount().negate();
			final BudgetLineDirection direction = LINE.getDirection();

			assertThatThrownBy(() -> new BudgetLine(id, budgetId, categoryId,
					amount, direction))
							.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void directionNull() {
			final long id = LINE.getId();
			final long budgetId = LINE.getBudget().getId();
			final long categoryId = LINE.getCategory().getId();
			final BigDecimal amount = LINE.getAmount();
			final BudgetLineDirection direction = null;

			assertThatThrownBy(() -> new BudgetLine(id, budgetId, categoryId,
					amount, direction))
							.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class SetAmount {

		private static final TestBudgetLine OTHER = TestBudgetLine.DECEMBER_FUEL;

		private final BudgetLine line = LINE.bdo();

		@Test
		void success() {
			line.setAmount(OTHER.getAmount());

			assertThat(line.getId()).isEqualTo(LINE.getId());
			assertThat(line.getBudgetId()).isEqualTo(LINE.getBudget().getId());
			assertThat(line.getCategoryId())
					.isEqualTo(LINE.getCategory().getId());
			assertThat(line.getAmount())
					.isEqualByComparingTo(OTHER.getAmount());
			assertThat(line.getDirection()).isEqualTo(LINE.getDirection());
		}

		@Test
		void zero() {
			line.setAmount(BigDecimal.ZERO);

			assertThat(line.getAmount()).isEqualByComparingTo(BigDecimal.ZERO);
		}

		@Test
		void amountNull() {
			assertThatThrownBy(() -> line.setAmount(null))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void amountNegative() {
			final BigDecimal amount = OTHER.getAmount().negate();

			assertThatThrownBy(() -> line.setAmount(amount))
					.isInstanceOf(IllegalArgumentException.class);
		}
	}

	@Nested
	class SetDirection {

		private static final TestBudgetLine OTHER = TestBudgetLine.DECEMBER_FUEL;

		private final BudgetLine line = LINE.bdo();

		@Test
		void success() {
			line.setDirection(OTHER.getDirection());

			assertThat(line.getId()).isEqualTo(LINE.getId());
			assertThat(line.getBudgetId()).isEqualTo(LINE.getBudget().getId());
			assertThat(line.getCategoryId())
					.isEqualTo(LINE.getCategory().getId());
			assertThat(line.getAmount()).isEqualByComparingTo(LINE.getAmount());
			assertThat(line.getDirection()).isEqualTo(OTHER.getDirection());
		}

		@Test
		void directionNull() {
			assertThatThrownBy(() -> line.setDirection(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
