package com.cvesters.moneyjars.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.cvesters.moneyjars.test.RepositoryTest;
import com.cvesters.moneyjars.transaction.dao.JarTransferTransactionDao;
import com.cvesters.moneyjars.transaction.dao.PaymentTransactionDao;
import com.cvesters.moneyjars.transaction.dao.TransactionDao;

@Sql({ "/db/accounts.sql", "/db/jars.sql", "/db/transactions.sql" })
class TransactionRepositoryTest extends RepositoryTest {

	@Autowired
	private TransactionRepository repository;

	@Nested 
	class FindAll {

		@Test
		void success() {
			final List<TransactionDao> result = repository.findAll();

			assertThat(result).satisfiesExactly(
					t -> assertEquals(t, TestPaymentTransaction.SALARY),
					t -> assertEquals(t, TestPaymentTransaction.BONUS),
					t -> assertEquals(t, TestPaymentTransaction.CAR_ALLOWANCE),
					t -> assertEquals(t, TestPaymentTransaction.GROCERY),
					t -> assertEquals(t, TestPaymentTransaction.RENT),
					t -> assertEquals(t, TestPaymentTransaction.CAFE),
					t -> assertEquals(t, TestPaymentTransaction.FLIGHTS),
					t -> assertEquals(t, TestPaymentTransaction.HOTEL),
					t -> assertEquals(t, TestPaymentTransaction.FUEL),
					t -> assertEquals(t, TestPaymentTransaction.MARKET),
					t -> assertEquals(t, TestJarTransferTransaction.CAR_SAVINGS),
					t -> assertEquals(t,
							TestJarTransferTransaction.HOLIDAY_SAVINGS),
					t -> assertEquals(t, TestJarTransferTransaction.ROAD_TRIP));
		}
	}

	@Nested
	class FindAllInPeriod {

		@Test
		void single() {
			final LocalDate start = LocalDate.of(2026, 1, 15);
			final LocalDate end = LocalDate.of(2026, 1, 15);

			final List<TransactionDao> result = repository.findAllInPeriod(start,
					end);

			assertThat(result).satisfiesExactly(
					t -> assertEquals(t, TestPaymentTransaction.CAFE));
		}

		@Test
		void multiple() {
			final LocalDate start = LocalDate.of(2026, 1, 1);
			final LocalDate end = LocalDate.of(2026, 1, 31);

			final List<TransactionDao> result = repository.findAllInPeriod(start,
					end);

			assertThat(result).satisfiesExactly(
					t -> assertEquals(t, TestPaymentTransaction.SALARY),
					t -> assertEquals(t, TestPaymentTransaction.BONUS),
					t -> assertEquals(t, TestPaymentTransaction.CAR_ALLOWANCE),
					t -> assertEquals(t, TestPaymentTransaction.GROCERY),
					t -> assertEquals(t, TestPaymentTransaction.RENT),
					t -> assertEquals(t, TestPaymentTransaction.CAFE),
					t -> assertEquals(t, TestPaymentTransaction.FLIGHTS),
					t -> assertEquals(t, TestPaymentTransaction.HOTEL));
		}

		@Test
		void none() {
			final LocalDate start = LocalDate.of(2025, 1, 1);
			final LocalDate end = LocalDate.of(2025, 12, 31);

			final List<TransactionDao> result = repository.findAllInPeriod(start,
					end);

			assertThat(result).isEmpty();
		}

		@Test
		void endBeforeStart() {
			final LocalDate start = LocalDate.of(2026, 1, 31);
			final LocalDate end = LocalDate.of(2026, 1, 1);

			final List<TransactionDao> result = repository.findAllInPeriod(start,
					end);

			assertThat(result).isEmpty();
		}

		@Test
		void startNull() {
			final LocalDate end = LocalDate.of(2026, 12, 31);

			final List<TransactionDao> result = repository.findAllInPeriod(null,
					end);

			assertThat(result).isEmpty();
		}

		@Test
		void endNull() {
			final LocalDate start = LocalDate.of(2026, 1, 1);

			final List<TransactionDao> result = repository.findAllInPeriod(start,
					null);

			assertThat(result).isEmpty();
		}
	}

	@Nested
	class FindFirstForDate {

		@Test
		void single() {
			final LocalDate date = LocalDate.of(2026, 1, 15);

			final Optional<TransactionDao> result = repository
					.findFirstForDate(date);

			assertThat(result).hasValueSatisfying(
					t -> assertEquals(t, TestPaymentTransaction.CAFE));
		}

		@Test
		void multiple() {
			final LocalDate date = LocalDate.of(2026, 1, 10);

			final Optional<TransactionDao> result = repository
					.findFirstForDate(date);

			assertThat(result).hasValueSatisfying(
					t -> assertEquals(t, TestPaymentTransaction.GROCERY));
		}

		@Test
		void none() {
			final LocalDate date = LocalDate.of(2026, 1, 2);

			final Optional<TransactionDao> result = repository
					.findFirstForDate(date);

			assertThat(result).isEmpty();
		}

		@Test
		void dateNull() {
			final Optional<TransactionDao> result = repository
					.findFirstForDate(null);

			assertThat(result).isEmpty();
		}
	}

	@Nested
	class FindLastForDate {

		@Test
		void single() {
			final LocalDate date = LocalDate.of(2026, 1, 15);

			final Optional<TransactionDao> result = repository
					.findLastForDate(date);

			assertThat(result).hasValueSatisfying(
					t -> assertEquals(t, TestPaymentTransaction.CAFE));
		}

		@Test
		void multiple() {
			final LocalDate date = LocalDate.of(2026, 1, 10);

			final Optional<TransactionDao> result = repository
					.findLastForDate(date);

			assertThat(result).hasValueSatisfying(
					t -> assertEquals(t, TestPaymentTransaction.RENT));
		}

		@Test
		void none() {
			final LocalDate date = LocalDate.of(2026, 1, 2);

			final Optional<TransactionDao> result = repository
					.findLastForDate(date);

			assertThat(result).isEmpty();
		}

		@Test
		void dateNull() {
			final Optional<TransactionDao> result = repository
					.findLastForDate(null);

			assertThat(result).isEmpty();
		}
	}

	private static void assertEquals(final TransactionDao t,
			final TestPaymentTransaction expected) {
		assertThat(t).isInstanceOf(PaymentTransactionDao.class);
		final PaymentTransactionDao pt = (PaymentTransactionDao) t;

		assertThat(pt.getId()).isEqualTo(expected.getId());
		assertThat(pt.getDate()).isEqualTo(expected.getDate());
		assertThat(pt.getSequence()).isEqualTo(expected.getSequence());
		assertThat(pt.getDescription()).isEqualTo(expected.getDescription());

		assertThat(pt.getJarId()).isEqualTo(expected.getJar().getId());
		assertThat(pt.getAccountId()).isEqualTo(expected.getAccount().getId());
		assertThat(pt.getCounterparty()).isEqualTo(expected.getCounterparty());
		assertThat(pt.getDirection()).isEqualTo(expected.getDirectionId());
	}

	private static void assertEquals(final TransactionDao t,
			final TestJarTransferTransaction expected) {
		assertThat(t).isInstanceOf(JarTransferTransactionDao.class);
		final JarTransferTransactionDao jt = (JarTransferTransactionDao) t;

		assertThat(jt.getId()).isEqualTo(expected.getId());
		assertThat(jt.getDate()).isEqualTo(expected.getDate());
		assertThat(jt.getSequence()).isEqualTo(expected.getSequence());
		assertThat(jt.getAmount()).isEqualByComparingTo(expected.getAmount());
		assertThat(jt.getDescription()).isEqualTo(expected.getDescription());

		assertThat(jt.getSourceJarId())
				.isEqualTo(expected.getSourceJar().getId());
		assertThat(jt.getTargetJarId())
				.isEqualTo(expected.getTargetJar().getId());
	}
}
