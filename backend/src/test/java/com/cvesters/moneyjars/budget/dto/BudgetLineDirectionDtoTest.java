package com.cvesters.moneyjars.budget.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.cvesters.moneyjars.budget.bdo.BudgetLineDirection;

class BudgetLineDirectionDtoTest {

	@Nested
	class ToBdo {

		@ParameterizedTest
		@MethodSource("com.cvesters.moneyjars.budget.dto.BudgetLineDirectionDtoTest#mapping")
		void success(final String dto, final BudgetLineDirection bdo) {
			final BudgetLineDirection result = BudgetLineDirectionDto
					.toBdo(dto);

			assertThat(result).isEqualTo(bdo);
		}

		@Test
		void invalid() {
			assertThatThrownBy(() -> BudgetLineDirectionDto.toBdo("INVALID"))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void dtoNull() {
			assertThatThrownBy(() -> BudgetLineDirectionDto.toBdo(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class ToDto {

		@ParameterizedTest
		@MethodSource("com.cvesters.moneyjars.budget.dto.BudgetLineDirectionDtoTest#mapping")
		void success(final String dto, final BudgetLineDirection bdo) {
			final String result = BudgetLineDirectionDto.toDto(bdo);

			assertThat(result).isEqualTo(dto);
		}

		@Test
		void bdoNull() {
			assertThatThrownBy(() -> BudgetLineDirectionDto.toDto(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	static Stream<Arguments> mapping() {
		return Stream.of(Arguments.of("INCOME", BudgetLineDirection.INCOME),
				Arguments.of("EXPENSE", BudgetLineDirection.EXPENSE));
	}
}
