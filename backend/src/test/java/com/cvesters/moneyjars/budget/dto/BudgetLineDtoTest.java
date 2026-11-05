package com.cvesters.moneyjars.budget.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.budget.TestBudgetLine;

class BudgetLineDtoTest {

	private static final TestBudgetLine LINE = TestBudgetLine.NOVEMBER_RENT;

	@Nested
	class Constructor {

		@Test
		void success() {
			final var dto = new BudgetLineDto(LINE.bdo());

			assertThat(dto.getId()).isEqualTo(LINE.getId());
			assertThat(dto.getCategoryId())
					.isEqualTo(LINE.getCategory().getId());
			assertThat(dto.getAmount())
					.isEqualTo(LINE.getAmount().toPlainString());
			assertThat(dto.getDirection())
					.isEqualTo(LINE.getDirection().name());
		}

		@Test
		void bdoNull() {
			assertThatThrownBy(() -> new BudgetLineDto(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
