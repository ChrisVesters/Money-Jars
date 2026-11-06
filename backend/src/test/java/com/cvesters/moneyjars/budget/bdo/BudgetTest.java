package com.cvesters.moneyjars.budget.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.cvesters.moneyjars.budget.TestBudget;

class BudgetTest {

	private static final TestBudget BUDGET = TestBudget.NOVEMBER_2026;

	@Nested
	class Constructor {

		@Test
		void withoutId() {
			final var budget = new Budget(BUDGET.getName(),
					BUDGET.getDescription(), BUDGET.getStartDate(),
					BUDGET.getEndDate());

			assertThat(budget.getId()).isNull();
			assertThat(budget.getName()).isEqualTo(BUDGET.getName());
			assertThat(budget.getDescription())
					.isEqualTo(BUDGET.getDescription());
			assertThat(budget.getStartDate()).isEqualTo(BUDGET.getStartDate());
			assertThat(budget.getEndDate()).isEqualTo(BUDGET.getEndDate());
		}

		@Test
		void withId() {
			final var budget = new Budget(BUDGET.getId(), BUDGET.getName(),
					BUDGET.getDescription(), BUDGET.getStartDate(),
					BUDGET.getEndDate());

			assertThat(budget.getId()).isEqualTo(BUDGET.getId());
			assertThat(budget.getName()).isEqualTo(BUDGET.getName());
			assertThat(budget.getDescription())
					.isEqualTo(BUDGET.getDescription());
			assertThat(budget.getStartDate()).isEqualTo(BUDGET.getStartDate());
			assertThat(budget.getEndDate()).isEqualTo(BUDGET.getEndDate());
		}

		@Test
		void singleDay() {
			final LocalDate date = BUDGET.getStartDate();

			final var budget = new Budget(BUDGET.getId(), BUDGET.getName(),
					BUDGET.getDescription(), date, date);

			assertThat(budget.getId()).isEqualTo(BUDGET.getId());
			assertThat(budget.getName()).isEqualTo(BUDGET.getName());
			assertThat(budget.getDescription())
					.isEqualTo(BUDGET.getDescription());
			assertThat(budget.getStartDate()).isEqualTo(date);
			assertThat(budget.getEndDate()).isEqualTo(date);
		}

		@Test
		void emptyDescription() {
			final var budget = new Budget(BUDGET.getId(), BUDGET.getName(), "",
					BUDGET.getStartDate(), BUDGET.getEndDate());

			assertThat(budget.getId()).isEqualTo(BUDGET.getId());
			assertThat(budget.getName()).isEqualTo(BUDGET.getName());
			assertThat(budget.getDescription()).isEmpty();
			assertThat(budget.getStartDate()).isEqualTo(BUDGET.getStartDate());
			assertThat(budget.getEndDate()).isEqualTo(BUDGET.getEndDate());
		}

		@Test
		void nameNull() {
			final long id = BUDGET.getId();
			final String name = null;
			final String description = BUDGET.getDescription();
			final LocalDate startDate = BUDGET.getStartDate();
			final LocalDate endDate = BUDGET.getEndDate();

			assertThatThrownBy(() -> new Budget(id, name, description,
					startDate, endDate))
							.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void nameInvalid(final String name) {
			final long id = BUDGET.getId();
			final String description = BUDGET.getDescription();
			final LocalDate startDate = BUDGET.getStartDate();
			final LocalDate endDate = BUDGET.getEndDate();

			assertThatThrownBy(() -> new Budget(id, name, description,
					startDate, endDate))
							.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			final long id = BUDGET.getId();
			final String name = BUDGET.getName();
			final String description = null;
			final LocalDate startDate = BUDGET.getStartDate();
			final LocalDate endDate = BUDGET.getEndDate();

			assertThatThrownBy(() -> new Budget(id, name, description,
					startDate, endDate))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void startDateNull() {
			final long id = BUDGET.getId();
			final String name = BUDGET.getName();
			final String description = BUDGET.getDescription();
			final LocalDate startDate = null;
			final LocalDate endDate = BUDGET.getEndDate();

			assertThatThrownBy(() -> new Budget(id, name, description,
					startDate, endDate))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void endDateNull() {
			final long id = BUDGET.getId();
			final String name = BUDGET.getName();
			final String description = BUDGET.getDescription();
			final LocalDate startDate = BUDGET.getStartDate();
			final LocalDate endDate = null;

			assertThatThrownBy(() -> new Budget(id, name, description,
					startDate, endDate))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void endDateBeforeStartDate() {
			final long id = BUDGET.getId();
			final String name = BUDGET.getName();
			final String description = BUDGET.getDescription();
			final LocalDate startDate = BUDGET.getStartDate();
			final LocalDate endDate = startDate.minusDays(1);

			assertThatThrownBy(() -> new Budget(id, name, description,
					startDate, endDate))
							.isInstanceOf(IllegalArgumentException.class);
		}
	}

	@Nested
	class SetName {

		private final Budget budget = BUDGET.bdo();

		@Test
		void success() {
			budget.setName("New name");

			assertThat(budget.getName()).isEqualTo("New name");
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void nameInvalid(final String name) {
			assertThatThrownBy(() -> budget.setName(name))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void nameNull() {
			assertThatThrownBy(() -> budget.setName(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class SetDescription {

		private final Budget budget = BUDGET.bdo();

		@Test
		void success() {
			budget.setDescription("New description");

			assertThat(budget.getDescription()).isEqualTo("New description");
		}

		@Test
		void descriptionNull() {
			assertThatThrownBy(() -> budget.setDescription(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class SetPeriod {

		private static final TestBudget OTHER = TestBudget.DECEMBER_2026;

		private final Budget budget = BUDGET.bdo();

		@Test
		void success() {
			budget.setPeriod(OTHER.getStartDate(), OTHER.getEndDate());

			assertThat(budget.getStartDate()).isEqualTo(OTHER.getStartDate());
			assertThat(budget.getEndDate()).isEqualTo(OTHER.getEndDate());
		}

		@Test
		void singleDay() {
			final LocalDate date = OTHER.getStartDate();

			budget.setPeriod(date, date);

			assertThat(budget.getStartDate()).isEqualTo(date);
			assertThat(budget.getEndDate()).isEqualTo(date);
		}

		@Test
		void startDateNull() {
			final LocalDate endDate = OTHER.getEndDate();

			assertThatThrownBy(() -> budget.setPeriod(null, endDate))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void endDateNull() {
			final LocalDate startDate = OTHER.getStartDate();

			assertThatThrownBy(() -> budget.setPeriod(startDate, null))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void endDateBeforeStartDate() {
			final LocalDate startDate = OTHER.getStartDate();
			final LocalDate endDate = startDate.minusDays(1);

			assertThatThrownBy(() -> budget.setPeriod(startDate, endDate))
					.isInstanceOf(IllegalArgumentException.class);

			assertThat(budget.getStartDate()).isEqualTo(BUDGET.getStartDate());
			assertThat(budget.getEndDate()).isEqualTo(BUDGET.getEndDate());
		}
	}
}
