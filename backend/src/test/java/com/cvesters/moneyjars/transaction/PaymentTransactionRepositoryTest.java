package com.cvesters.moneyjars.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.cvesters.moneyjars.test.RepositoryTest;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionDirection;
import com.cvesters.moneyjars.transaction.dao.PaymentTransactionCategoryTotalDao;
import com.cvesters.moneyjars.transaction.dao.PaymentTransactionDirectionDao;

@Sql({ "/db/accounts.sql", "/db/jars.sql", "/db/categories.sql",
		"/db/transactions.sql" })
class PaymentTransactionRepositoryTest extends RepositoryTest {

	@Autowired
	private PaymentTransactionRepository repository;

	@Nested
	class FindCategoryTotalsInPeriod {

		@Test
		void multiple() {
			final LocalDate start = LocalDate.of(2026, 1, 1);
			final LocalDate end = LocalDate.of(2026, 1, 31);

			final List<PaymentTransactionCategoryTotalDao> result = repository
					.findCategoryTotalsInPeriod(start, end);

			assertEquals(result,
					TestPaymentTransactionCategoryTotal.JANUARY_GROCERIES,
					TestPaymentTransactionCategoryTotal.JANUARY_RENT,
					TestPaymentTransactionCategoryTotal.JANUARY_UNCATEGORISED);
		}

		@Test
		void sumsPerCategory() {
			final LocalDate start = LocalDate.of(2026, 1, 10);
			final LocalDate end = LocalDate.of(2026, 2, 3);

			final List<PaymentTransactionCategoryTotalDao> result = repository
					.findCategoryTotalsInPeriod(start, end);

			assertEquals(result,
					TestPaymentTransactionCategoryTotal.JANUARY_10_TO_FEBRUARY_3_GROCERIES,
					TestPaymentTransactionCategoryTotal.JANUARY_10_TO_FEBRUARY_3_RENT,
					TestPaymentTransactionCategoryTotal.JANUARY_10_TO_FEBRUARY_3_FUEL,
					TestPaymentTransactionCategoryTotal.JANUARY_10_TO_FEBRUARY_3_UNCATEGORISED);
		}

		@Test
		void none() {
			final LocalDate start = LocalDate.of(2025, 1, 1);
			final LocalDate end = LocalDate.of(2025, 12, 31);

			final List<PaymentTransactionCategoryTotalDao> result = repository
					.findCategoryTotalsInPeriod(start, end);

			assertThat(result).isEmpty();
		}

		@Test
		void endBeforeStart() {
			final LocalDate start = LocalDate.of(2026, 1, 31);
			final LocalDate end = LocalDate.of(2026, 1, 1);

			final List<PaymentTransactionCategoryTotalDao> result = repository
					.findCategoryTotalsInPeriod(start, end);

			assertThat(result).isEmpty();
		}

		@Test
		void excludesTransfers() {
			// Only jar and account transfers happen on this date.
			final LocalDate date = LocalDate.of(2026, 2, 10);

			final List<PaymentTransactionCategoryTotalDao> result = repository
					.findCategoryTotalsInPeriod(date, date);

			assertThat(result).isEmpty();
		}
	}

	private static void assertEquals(
			final List<PaymentTransactionCategoryTotalDao> actual,
			final TestPaymentTransactionCategoryTotal... expected) {
		final Iterator<PaymentTransactionCategoryTotalDao> rows = actual
				.iterator();
		for (final TestPaymentTransactionCategoryTotal total : expected) {
			for (final PaymentTransactionDirection direction : PaymentTransactionDirection
					.values()) {
				if (total.getAmount(direction).signum() != 0) {
					assertThat(rows).hasNext();
					assertEquals(rows.next(), total, direction);
				}
			}
		}
		assertThat(rows).isExhausted();
	}

	private static void assertEquals(
			final PaymentTransactionCategoryTotalDao actual,
			final TestPaymentTransactionCategoryTotal expected,
			final PaymentTransactionDirection direction) {
		assertThat(actual.categoryId()).isEqualTo(expected.getCategoryId());
		assertThat(actual.direction())
				.isEqualTo(PaymentTransactionDirectionDao.toDao(direction));
		assertThat(actual.amount())
				.isEqualByComparingTo(expected.getAmount(direction));
	}
}
