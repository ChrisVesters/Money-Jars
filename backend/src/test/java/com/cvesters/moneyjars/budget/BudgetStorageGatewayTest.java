package com.cvesters.moneyjars.budget;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import com.cvesters.moneyjars.budget.bdo.Budget;
import com.cvesters.moneyjars.budget.dao.BudgetDao;
import com.cvesters.moneyjars.common.exceptions.MissingEntityException;

class BudgetStorageGatewayTest {

	private static final TestBudget BUDGET = TestBudget.NOVEMBER_2026;

	private final BudgetRepository repository = mock();
	private final BudgetStorageGateway gateway = new BudgetStorageGateway(
			repository);

	@Nested
	class GetAll {

		@Test
		void success() {
			final BudgetDao dao = mock();
			final Budget bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);
			when(repository.findAll()).thenReturn(List.of(dao));

			final List<Budget> budgets = gateway.getAll();

			assertThat(budgets).containsExactly(bdo);
		}
	}

	@Nested
	class Get {

		@Test
		void success() {
			final BudgetDao dao = mock();
			final Budget bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);
			when(repository.findById(BUDGET.getId()))
					.thenReturn(Optional.of(dao));

			final Budget result = gateway.get(BUDGET.getId());

			assertThat(result).isSameAs(bdo);
		}

		@Test
		void missingEntity() {
			when(repository.findById(BUDGET.getId()))
					.thenReturn(Optional.empty());

			assertThatThrownBy(() -> gateway.get(BUDGET.getId()))
					.isInstanceOf(MissingEntityException.class);
		}
	}

	@Nested
	class Create {

		@Test
		void success() {
			final BudgetDao createdDao = mock();
			final Budget createdBdo = mock();
			when(createdDao.toBdo()).thenReturn(createdBdo);

			final Budget budget = BUDGET.bdo();
			when(repository.save(argThat(v -> {
				assertThat(v.getId()).isNull();
				assertThat(v.getName()).isEqualTo(BUDGET.getName());
				assertThat(v.getDescription())
						.isEqualTo(BUDGET.getDescription());
				assertThat(v.getStartDate()).isEqualTo(BUDGET.getStartDate());
				assertThat(v.getEndDate()).isEqualTo(BUDGET.getEndDate());
				return true;
			}))).thenReturn(createdDao);

			final Budget result = gateway.create(budget);

			assertThat(result).isSameAs(createdBdo);
		}

		@Test
		void budgetNull() {
			assertThatThrownBy(() -> gateway.create(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Update {

		@Test
		void success() {
			final BudgetDao updatedDao = mock();
			final Budget updatedBdo = mock();
			when(updatedDao.toBdo()).thenReturn(updatedBdo);

			final Budget update = mock();
			when(update.getId()).thenReturn(BUDGET.getId());

			final BudgetDao existing = mock();
			when(repository.findById(BUDGET.getId()))
					.thenReturn(Optional.of(existing));
			when(repository.save(existing)).thenReturn(updatedDao);

			final Budget result = gateway.update(update);

			assertThat(result).isSameAs(updatedBdo);

			final InOrder inOrder = inOrder(existing, repository);
			inOrder.verify(existing).updateWith(update);
			inOrder.verify(repository).save(existing);
		}

		@Test
		void notFound() {
			when(repository.findById(BUDGET.getId()))
					.thenReturn(Optional.empty());

			final Budget update = mock();
			when(update.getId()).thenReturn(BUDGET.getId());

			assertThatThrownBy(() -> gateway.update(update))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void budgetNull() {
			assertThatThrownBy(() -> gateway.update(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Delete {

		@Test
		void success() {
			gateway.delete(BUDGET.getId());

			verify(repository).deleteById(BUDGET.getId());
		}
	}
}
