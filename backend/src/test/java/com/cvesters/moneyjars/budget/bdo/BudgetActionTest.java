package com.cvesters.moneyjars.budget.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.cvesters.moneyjars.budget.TestBudget;

class BudgetActionTest {

	private static final TestBudget BUDGET = TestBudget.NOVEMBER_2026;

	@Nested
	class Create {

		@Test
		void success() {
			final var action = new BudgetAction.Create(BUDGET.getName(),
					BUDGET.getDescription(), BUDGET.getStartDate(),
					BUDGET.getEndDate());

			assertThat(action.name()).isEqualTo(BUDGET.getName());
			assertThat(action.description())
					.isEqualTo(BUDGET.getDescription());
			assertThat(action.startDate()).isEqualTo(BUDGET.getStartDate());
			assertThat(action.endDate()).isEqualTo(BUDGET.getEndDate());
		}

		@Test
		void singleDay() {
			final LocalDate date = BUDGET.getStartDate();

			final var action = new BudgetAction.Create(BUDGET.getName(),
					BUDGET.getDescription(), date, date);

			assertThat(action.name()).isEqualTo(BUDGET.getName());
			assertThat(action.description())
					.isEqualTo(BUDGET.getDescription());
			assertThat(action.startDate()).isEqualTo(date);
			assertThat(action.endDate()).isEqualTo(date);
		}

		@Test
		void nameNull() {
			final String name = null;
			final String description = BUDGET.getDescription();
			final LocalDate startDate = BUDGET.getStartDate();
			final LocalDate endDate = BUDGET.getEndDate();

			assertThatThrownBy(() -> new BudgetAction.Create(name, description,
					startDate, endDate))
							.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void nameInvalid(final String name) {
			final String description = BUDGET.getDescription();
			final LocalDate startDate = BUDGET.getStartDate();
			final LocalDate endDate = BUDGET.getEndDate();

			assertThatThrownBy(() -> new BudgetAction.Create(name, description,
					startDate, endDate))
							.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			final String name = BUDGET.getName();
			final String description = null;
			final LocalDate startDate = BUDGET.getStartDate();
			final LocalDate endDate = BUDGET.getEndDate();

			assertThatThrownBy(() -> new BudgetAction.Create(name, description,
					startDate, endDate))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void startDateNull() {
			final String name = BUDGET.getName();
			final String description = BUDGET.getDescription();
			final LocalDate startDate = null;
			final LocalDate endDate = BUDGET.getEndDate();

			assertThatThrownBy(() -> new BudgetAction.Create(name, description,
					startDate, endDate))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void endDateNull() {
			final String name = BUDGET.getName();
			final String description = BUDGET.getDescription();
			final LocalDate startDate = BUDGET.getStartDate();
			final LocalDate endDate = null;

			assertThatThrownBy(() -> new BudgetAction.Create(name, description,
					startDate, endDate))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void endDateBeforeStartDate() {
			final String name = BUDGET.getName();
			final String description = BUDGET.getDescription();
			final LocalDate startDate = BUDGET.getStartDate();
			final LocalDate endDate = startDate.minusDays(1);

			assertThatThrownBy(() -> new BudgetAction.Create(name, description,
					startDate, endDate))
							.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void toBdo() {
			final var action = new BudgetAction.Create(BUDGET.getName(),
					BUDGET.getDescription(), BUDGET.getStartDate(),
					BUDGET.getEndDate());

			final Budget bdo = action.toBdo();

			assertThat(bdo.getId()).isNull();
			assertThat(bdo.getName()).isEqualTo(BUDGET.getName());
			assertThat(bdo.getDescription()).isEqualTo(BUDGET.getDescription());
			assertThat(bdo.getStartDate()).isEqualTo(BUDGET.getStartDate());
			assertThat(bdo.getEndDate()).isEqualTo(BUDGET.getEndDate());
		}
	}

	@Nested
	class Update {

		@Test
		void success() {
			final var action = new BudgetAction.Update(BUDGET.getName(),
					BUDGET.getDescription(), BUDGET.getStartDate(),
					BUDGET.getEndDate());

			assertThat(action.name()).isEqualTo(BUDGET.getName());
			assertThat(action.description())
					.isEqualTo(BUDGET.getDescription());
			assertThat(action.startDate()).isEqualTo(BUDGET.getStartDate());
			assertThat(action.endDate()).isEqualTo(BUDGET.getEndDate());
		}

		@Test
		void nameNull() {
			final String name = null;
			final String description = BUDGET.getDescription();
			final LocalDate startDate = BUDGET.getStartDate();
			final LocalDate endDate = BUDGET.getEndDate();

			assertThatThrownBy(() -> new BudgetAction.Update(name, description,
					startDate, endDate))
							.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void nameInvalid(final String name) {
			final String description = BUDGET.getDescription();
			final LocalDate startDate = BUDGET.getStartDate();
			final LocalDate endDate = BUDGET.getEndDate();

			assertThatThrownBy(() -> new BudgetAction.Update(name, description,
					startDate, endDate))
							.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			final String name = BUDGET.getName();
			final String description = null;
			final LocalDate startDate = BUDGET.getStartDate();
			final LocalDate endDate = BUDGET.getEndDate();

			assertThatThrownBy(() -> new BudgetAction.Update(name, description,
					startDate, endDate))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void startDateNull() {
			final String name = BUDGET.getName();
			final String description = BUDGET.getDescription();
			final LocalDate startDate = null;
			final LocalDate endDate = BUDGET.getEndDate();

			assertThatThrownBy(() -> new BudgetAction.Update(name, description,
					startDate, endDate))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void endDateNull() {
			final String name = BUDGET.getName();
			final String description = BUDGET.getDescription();
			final LocalDate startDate = BUDGET.getStartDate();
			final LocalDate endDate = null;

			assertThatThrownBy(() -> new BudgetAction.Update(name, description,
					startDate, endDate))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void endDateBeforeStartDate() {
			final String name = BUDGET.getName();
			final String description = BUDGET.getDescription();
			final LocalDate startDate = BUDGET.getStartDate();
			final LocalDate endDate = startDate.minusDays(1);

			assertThatThrownBy(() -> new BudgetAction.Update(name, description,
					startDate, endDate))
							.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void applyOn() {
			final var action = new BudgetAction.Update(BUDGET.getName(),
					BUDGET.getDescription(), BUDGET.getStartDate(),
					BUDGET.getEndDate());

			final Budget target = mock();

			action.applyOn(target);

			verify(target).setName(BUDGET.getName());
			verify(target).setDescription(BUDGET.getDescription());
			verify(target).setPeriod(BUDGET.getStartDate(),
					BUDGET.getEndDate());
		}

		@Test
		void applyOnTargetNull() {
			final var action = new BudgetAction.Update(BUDGET.getName(),
					BUDGET.getDescription(), BUDGET.getStartDate(),
					BUDGET.getEndDate());

			assertThatThrownBy(() -> action.applyOn(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
