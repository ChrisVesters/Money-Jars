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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InOrder;

import com.cvesters.moneyjars.budget.bdo.BudgetLine;
import com.cvesters.moneyjars.budget.dao.BudgetLineDao;
import com.cvesters.moneyjars.budget.dao.BudgetLineDirectionDao;
import com.cvesters.moneyjars.common.exceptions.MissingEntityException;

class BudgetLineStorageGatewayTest {

	private static final TestBudgetLine LINE = TestBudgetLine.NOVEMBER_RENT;

	private final BudgetLineRepository repository = mock();
	private final BudgetLineStorageGateway gateway = new BudgetLineStorageGateway(
			repository);

	@Nested
	class GetAllByBudget {

		private static final long BUDGET_ID = LINE.getBudget().getId();

		@Test
		void success() {
			final BudgetLineDao dao = mock();
			final BudgetLine bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);
			when(repository.findAllByBudgetId(BUDGET_ID))
					.thenReturn(List.of(dao));

			final List<BudgetLine> result = gateway.getAllByBudget(BUDGET_ID);

			assertThat(result).containsExactly(bdo);
		}
	}

	@Nested
	class Exists {

		private static final long BUDGET_ID = LINE.getBudget().getId();
		private static final long CATEGORY_ID = LINE.getCategory().getId();

		@ParameterizedTest
		@ValueSource(booleans = { true, false })
		void success(final boolean exists) {
			when(repository.existsByBudgetIdAndCategoryId(BUDGET_ID,
					CATEGORY_ID)).thenReturn(exists);

			final boolean result = gateway.exists(BUDGET_ID, CATEGORY_ID);

			assertThat(result).isEqualTo(exists);
		}
	}

	@Nested
	class Get {

		@Test
		void success() {
			final BudgetLineDao dao = mock();
			final BudgetLine bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);
			when(repository.findById(LINE.getId()))
					.thenReturn(Optional.of(dao));

			final BudgetLine result = gateway.get(LINE.getId());

			assertThat(result).isSameAs(bdo);
		}

		@Test
		void missingEntity() {
			when(repository.findById(LINE.getId()))
					.thenReturn(Optional.empty());

			assertThatThrownBy(() -> gateway.get(LINE.getId()))
					.isInstanceOf(MissingEntityException.class);
		}
	}

	@Nested
	class Create {

		@Test
		void success() {
			final BudgetLineDao createdDao = mock();
			final BudgetLine createdBdo = mock();
			when(createdDao.toBdo()).thenReturn(createdBdo);

			final BudgetLine line = LINE.bdo();
			when(repository.save(argThat(v -> {
				assertThat(v.getId()).isNull();
				assertThat(v.getBudgetId()).isEqualTo(LINE.getBudget().getId());
				assertThat(v.getCategoryId())
						.isEqualTo(LINE.getCategory().getId());
				assertThat(v.getAmount()).isEqualTo(LINE.getAmount());
				assertThat(v.getDirection()).isEqualTo(
						BudgetLineDirectionDao.toDao(LINE.getDirection()));
				return true;
			}))).thenReturn(createdDao);

			final BudgetLine result = gateway.create(line);

			assertThat(result).isSameAs(createdBdo);
		}

		@Test
		void lineNull() {
			assertThatThrownBy(() -> gateway.create(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Update {

		@Test
		void success() {
			final BudgetLineDao updatedDao = mock();
			final BudgetLine updatedBdo = mock();
			when(updatedDao.toBdo()).thenReturn(updatedBdo);

			final BudgetLine update = mock();
			when(update.getId()).thenReturn(LINE.getId());

			final BudgetLineDao existing = mock();
			when(repository.findById(LINE.getId()))
					.thenReturn(Optional.of(existing));
			when(repository.save(existing)).thenReturn(updatedDao);

			final BudgetLine result = gateway.update(update);

			assertThat(result).isSameAs(updatedBdo);

			final InOrder inOrder = inOrder(existing, repository);
			inOrder.verify(existing).updateWith(update);
			inOrder.verify(repository).save(existing);
		}

		@Test
		void notFound() {
			when(repository.findById(LINE.getId()))
					.thenReturn(Optional.empty());

			final BudgetLine update = mock();
			when(update.getId()).thenReturn(LINE.getId());

			assertThatThrownBy(() -> gateway.update(update))
					.isInstanceOf(MissingEntityException.class);
		}

		@Test
		void lineNull() {
			assertThatThrownBy(() -> gateway.update(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Delete {

		@Test
		void success() {
			gateway.delete(LINE.getId());

			verify(repository).deleteById(LINE.getId());
		}
	}
}
