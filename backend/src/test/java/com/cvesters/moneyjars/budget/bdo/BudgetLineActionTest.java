package com.cvesters.moneyjars.budget.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import com.cvesters.moneyjars.budget.TestBudgetLine;

class BudgetLineActionTest {

	private static final TestBudgetLine LINE = TestBudgetLine.NOVEMBER_RENT;

	@Nested
	class Create {

		@Test
		void success() {
			final var action = new BudgetLineAction.Create(
					LINE.getBudget().getId(), LINE.getCategory().getId(),
					LINE.getAmount(), LINE.getDirection());

			assertThat(action.budgetId()).isEqualTo(LINE.getBudget().getId());
			assertThat(action.categoryId())
					.isEqualTo(LINE.getCategory().getId());
			assertThat(action.amount()).isEqualTo(LINE.getAmount());
			assertThat(action.direction()).isEqualTo(LINE.getDirection());
		}

		@Test
		void amountZero() {
			final var action = new BudgetLineAction.Create(
					LINE.getBudget().getId(), LINE.getCategory().getId(),
					BigDecimal.ZERO, LINE.getDirection());

			assertThat(action.amount()).isEqualTo(BigDecimal.ZERO);
		}

		@Test
		void amountNull() {
			final long budgetId = LINE.getBudget().getId();
			final long categoryId = LINE.getCategory().getId();
			final BudgetLineDirection direction = LINE.getDirection();

			assertThatThrownBy(() -> new BudgetLineAction.Create(budgetId,
					categoryId, null, direction))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void amountNegative() {
			final long budgetId = LINE.getBudget().getId();
			final long categoryId = LINE.getCategory().getId();
			final BigDecimal amount = LINE.getAmount().negate();
			final BudgetLineDirection direction = LINE.getDirection();

			assertThatThrownBy(() -> new BudgetLineAction.Create(budgetId,
					categoryId, amount, direction))
							.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void directionNull() {
			final long budgetId = LINE.getBudget().getId();
			final long categoryId = LINE.getCategory().getId();
			final BigDecimal amount = LINE.getAmount();

			assertThatThrownBy(() -> new BudgetLineAction.Create(budgetId,
					categoryId, amount, null))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void toBdo() {
			final var action = new BudgetLineAction.Create(
					LINE.getBudget().getId(), LINE.getCategory().getId(),
					LINE.getAmount(), LINE.getDirection());

			final BudgetLine line = action.toBdo();

			assertThat(line.getId()).isNull();
			assertThat(line.getBudgetId()).isEqualTo(LINE.getBudget().getId());
			assertThat(line.getCategoryId())
					.isEqualTo(LINE.getCategory().getId());
			assertThat(line.getAmount()).isEqualTo(LINE.getAmount());
			assertThat(line.getDirection()).isEqualTo(LINE.getDirection());
		}
	}

	@Nested
	class Update {

		@Test
		void success() {
			final var action = new BudgetLineAction.Update(LINE.getAmount(),
					LINE.getDirection());

			assertThat(action.amount()).isEqualTo(LINE.getAmount());
			assertThat(action.direction()).isEqualTo(LINE.getDirection());
		}

		@Test
		void amountZero() {
			final var action = new BudgetLineAction.Update(BigDecimal.ZERO,
					LINE.getDirection());

			assertThat(action.amount()).isEqualTo(BigDecimal.ZERO);
		}

		@Test
		void amountNull() {
			final BudgetLineDirection direction = LINE.getDirection();

			assertThatThrownBy(
					() -> new BudgetLineAction.Update(null, direction))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void amountNegative() {
			final BigDecimal amount = LINE.getAmount().negate();
			final BudgetLineDirection direction = LINE.getDirection();

			assertThatThrownBy(
					() -> new BudgetLineAction.Update(amount, direction))
							.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void directionNull() {
			final BigDecimal amount = LINE.getAmount();

			assertThatThrownBy(() -> new BudgetLineAction.Update(amount, null))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void applyOn() {
			final var action = new BudgetLineAction.Update(LINE.getAmount(),
					LINE.getDirection());
			final BudgetLine line = mock();

			action.applyOn(line);

			final InOrder inOrder = inOrder(line);
			inOrder.verify(line).setAmount(LINE.getAmount());
			inOrder.verify(line).setDirection(LINE.getDirection());
			inOrder.verifyNoMoreInteractions();
		}

		@Test
		void applyOnNull() {
			final var action = new BudgetLineAction.Update(LINE.getAmount(),
					LINE.getDirection());

			assertThatThrownBy(() -> action.applyOn(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
