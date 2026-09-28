package com.cvesters.moneyjars.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.cvesters.moneyjars.test.RepositoryTest;
import com.cvesters.moneyjars.transaction.dao.PaymentTransactionDao;
import com.cvesters.moneyjars.transaction.dao.TransactionDao;

@Sql({ "/db/accounts.sql", "/db/jars.sql", "/db/transactions.sql" })
class TransactionRepositoryTest extends RepositoryTest {

	@Autowired
	private TransactionRepository repository;

	@Nested
	class findInPeriod {

		@Test
		void single() {
			final LocalDate start = LocalDate.of(2026, 1, 15);
			final LocalDate end = LocalDate.of(2026, 1, 15);

			final List<TransactionDao> result = repository.findInPeriod(start,
					end);

			assertThat(result).satisfiesExactly(
					t -> assertEquals(t, TestPaymentTransaction.CAFE));
		}

		@Test
		void multiple() {
			final LocalDate start = LocalDate.of(2026, 1, 1);
			final LocalDate end = LocalDate.of(2026, 1, 31);

			final List<TransactionDao> result = repository.findInPeriod(start,
					end);

			assertThat(result).satisfiesExactly(
					t -> assertEquals(t, TestPaymentTransaction.GROCERY),
					t -> assertEquals(t, TestPaymentTransaction.RENT),
					t -> assertEquals(t, TestPaymentTransaction.CAFE));
		}

		@Test
		void none() {
			final LocalDate start = LocalDate.of(2025, 1, 1);
			final LocalDate end = LocalDate.of(2025, 12, 31);

			final List<TransactionDao> result = repository.findInPeriod(start,
					end);

			assertThat(result).isEmpty();
		}

		@Test
		void endBeforeStart() {
			final LocalDate start = LocalDate.of(2026, 1, 31);
			final LocalDate end = LocalDate.of(2026, 1, 1);

			final List<TransactionDao> result = repository.findInPeriod(start,
					end);

			assertThat(result).isEmpty();
		}

		@Test
		void startNull() {
			final LocalDate end = LocalDate.of(2026, 1, 31);

			final List<TransactionDao> result = repository.findInPeriod(null,
					end);

			assertThat(result).isEmpty();
		}

		@Test
		void endNull() {
			final LocalDate start = LocalDate.of(2026, 1, 1);

			final List<TransactionDao> result = repository.findInPeriod(start,
					null);

			assertThat(result).isEmpty();
		}
	}

	private void assertEquals(final TransactionDao t,
			final TestPaymentTransaction expected) {
		assertThat(t).isInstanceOf(PaymentTransactionDao.class);
		final PaymentTransactionDao pt = (PaymentTransactionDao) t;

		assertThat(pt.getId()).isEqualTo(expected.getId());
		assertThat(pt.getDate()).isEqualTo(expected.getDate());
		assertThat(pt.getSequence()).isEqualTo(expected.getSequence());
		assertThat(pt.getBeneficiary()).isEqualTo(expected.getBeneficiary());
		assertThat(pt.getDescription()).isEqualTo(expected.getDescription());

		assertThat(pt.getJarId()).isEqualTo(expected.getJar().getId());
		assertThat(pt.getAccountId()).isEqualTo(expected.getAccount().getId());
		assertThat(pt.getDirection()).isEqualTo(expected.getDirectionId());
	}
}
