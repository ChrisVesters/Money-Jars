package com.cvesters.moneyjars.budget;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import com.cvesters.moneyjars.budget.bdo.BudgetLine;
import com.cvesters.moneyjars.budget.bdo.BudgetLineAction;
import com.cvesters.moneyjars.common.exceptions.DuplicateEntityException;

class BudgetLineServiceTest {

	private static final long LINE_ID = 1L;

	private final BudgetLineStorageGateway storage = mock();
	private final BudgetLineService service = new BudgetLineService(storage);

	@Nested
	class Create {

		private static final TestBudgetLine LINE = TestBudgetLine.NOVEMBER_RENT;
		private static final long BUDGET_ID = LINE.getBudget().getId();
		private static final long CATEGORY_ID = LINE.getCategory().getId();

		@Test
		void success() {
			final BudgetLineAction.Create action = mock();
			when(action.budgetId()).thenReturn(BUDGET_ID);
			when(action.categoryId()).thenReturn(CATEGORY_ID);
			when(storage.exists(BUDGET_ID, CATEGORY_ID)).thenReturn(false);

			final BudgetLine create = mock();
			when(action.toBdo()).thenReturn(create);

			final BudgetLine created = mock();
			when(storage.create(create)).thenReturn(created);

			final BudgetLine result = service.create(action);

			assertThat(result).isSameAs(created);
		}

		@Test
		void duplicateCategory() {
			final BudgetLineAction.Create action = mock();
			when(action.budgetId()).thenReturn(BUDGET_ID);
			when(action.categoryId()).thenReturn(CATEGORY_ID);
			when(storage.exists(BUDGET_ID, CATEGORY_ID)).thenReturn(true);

			assertThatThrownBy(() -> service.create(action))
					.isInstanceOf(DuplicateEntityException.class);

			verify(storage, never()).create(any());
		}

		@Test
		void actionNull() {
			assertThatThrownBy(() -> service.create(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Update {

		@Test
		void success() {
			final BudgetLineAction.Update action = mock();
			final BudgetLine existing = mock();
			final BudgetLine updated = mock();
			when(storage.get(LINE_ID)).thenReturn(existing);
			when(storage.update(existing)).thenReturn(updated);

			final BudgetLine result = service.update(LINE_ID, action);

			assertThat(result).isSameAs(updated);

			final InOrder inOrder = inOrder(storage, action);
			inOrder.verify(storage).get(LINE_ID);
			inOrder.verify(action).applyOn(existing);
			inOrder.verify(storage).update(existing);
		}

		@Test
		void actionNull() {
			assertThatThrownBy(() -> service.update(LINE_ID, null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Delete {

		@Test
		void success() {
			service.delete(LINE_ID);

			verify(storage).delete(LINE_ID);
		}
	}
}
