package com.cvesters.moneyjars.transaction.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionDirection;

class PaymentTransactionDirectionDaoTest {

	@Nested
	class ToBdo {

		@ParameterizedTest
		@MethodSource("com.cvesters.moneyjars.transaction.dao.PaymentTransactionDirectionDaoTest#mapping")
		void success(final short dao, final PaymentTransactionDirection bdo) {
			final PaymentTransactionDirection result = PaymentTransactionDirectionDao
					.toBdo(dao);

			assertThat(result).isEqualTo(bdo);
		}

		@Test
		void invalid() {
			assertThatThrownBy(
					() -> PaymentTransactionDirectionDao.toBdo(Short.MAX_VALUE))
							.isInstanceOf(IllegalArgumentException.class);
		}
	}

	@Nested
	class ToDao {

		@ParameterizedTest
		@MethodSource("com.cvesters.moneyjars.transaction.dao.PaymentTransactionDirectionDaoTest#mapping")
		void test(final short dao, final PaymentTransactionDirection bdo) {
			final short result = PaymentTransactionDirectionDao.toDao(bdo);

			assertThat(result).isEqualTo(dao);
		}

		@Test
		void invalid() {
			assertThatThrownBy(() -> PaymentTransactionDirectionDao.toDao(null))
					.isInstanceOf(NullPointerException.class);
		}

	}

	static Stream<Arguments> mapping() {
		return Stream.of(
				Arguments.of((short) 0, PaymentTransactionDirection.INCOMING),
				Arguments.of((short) 1, PaymentTransactionDirection.OUTGOING));
	}
}
