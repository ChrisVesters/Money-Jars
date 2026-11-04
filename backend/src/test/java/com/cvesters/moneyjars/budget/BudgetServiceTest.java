package com.cvesters.moneyjars.budget;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import com.cvesters.moneyjars.budget.bdo.Budget;
import com.cvesters.moneyjars.budget.bdo.BudgetAction;

class BudgetServiceTest {

	private final BudgetStorageGateway storage = mock();
	private final BudgetService service = new BudgetService(storage);

	@Nested
	class GetAll {

		@Test
		void success() {
			final Budget expected = mock();
			when(storage.getAll()).thenReturn(List.of(expected));

			final List<Budget> result = service.getAll();

			assertThat(result).containsExactly(expected);
		}
	}

	@Nested
	class Create {

		@Test
		void success() {
			final BudgetAction.Create action = mock();
			final Budget create = mock();
			when(action.toBdo()).thenReturn(create);

			final Budget created = mock();
			when(storage.create(create)).thenReturn(created);

			final Budget result = service.create(action);

			assertThat(result).isSameAs(created);
		}

		@Test
		void actionNull() {
			assertThatThrownBy(() -> service.create(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Update {

		private static final long BUDGET_ID = 1L;

		@Test
		void success() {
			final BudgetAction.Update action = mock();
			final Budget existing = mock();
			final Budget updated = mock();
			when(storage.get(BUDGET_ID)).thenReturn(existing);
			when(storage.update(existing)).thenReturn(updated);

			final Budget result = service.update(BUDGET_ID, action);

			assertThat(result).isSameAs(updated);

			final InOrder inOrder = inOrder(storage, action);
			inOrder.verify(storage).get(BUDGET_ID);
			inOrder.verify(action).applyOn(existing);
			inOrder.verify(storage).update(existing);
		}

		@Test
		void actionNull() {
			assertThatThrownBy(() -> service.update(BUDGET_ID, null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Delete {

		private static final long BUDGET_ID = 1L;

		@Test
		void success() {
			service.delete(BUDGET_ID);

			verify(storage).delete(BUDGET_ID);
		}
	}
}
