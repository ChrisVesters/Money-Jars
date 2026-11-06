package com.cvesters.moneyjars.budget.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.cvesters.moneyjars.budget.bdo.BudgetLineDirection;

class BudgetLineDirectionDaoTest {

	@Nested
	class ToBdo {

		@ParameterizedTest
		@MethodSource("com.cvesters.moneyjars.budget.dao.BudgetLineDirectionDaoTest#mapping")
		void success(final short dao, final BudgetLineDirection bdo) {
			final BudgetLineDirection result = BudgetLineDirectionDao
					.toBdo(dao);

			assertThat(result).isEqualTo(bdo);
		}

		@Test
		void invalid() {
			assertThatThrownBy(
					() -> BudgetLineDirectionDao.toBdo(Short.MAX_VALUE))
							.isInstanceOf(IllegalArgumentException.class);
		}
	}

	@Nested
	class ToDao {

		@ParameterizedTest
		@MethodSource("com.cvesters.moneyjars.budget.dao.BudgetLineDirectionDaoTest#mapping")
		void success(final short dao, final BudgetLineDirection bdo) {
			final short result = BudgetLineDirectionDao.toDao(bdo);

			assertThat(result).isEqualTo(dao);
		}

		@Test
		void invalid() {
			assertThatThrownBy(() -> BudgetLineDirectionDao.toDao(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	static Stream<Arguments> mapping() {
		return Stream.of(Arguments.of((short) 0, BudgetLineDirection.INCOME),
				Arguments.of((short) 1, BudgetLineDirection.EXPENSE));
	}
}
