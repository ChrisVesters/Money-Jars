package com.cvesters.moneyjars.jarentry;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.cvesters.moneyjars.jar.TestJar;
import com.cvesters.moneyjars.jarentry.dao.JarEntryDao;
import com.cvesters.moneyjars.test.RepositoryTest;

@Sql({ "/db/accounts.sql", "/db/jars.sql", "/db/transactions.sql",
		"/db/jar_entries.sql" })
class JarEntryRepositoryTest extends RepositoryTest {

	@Autowired
	private JarEntryRepository repository;

	private static final TestJar JAR = TestJar.HOUSEHOLD;

	@Nested
	class FindBefore {

		@Test
		void empty() {
			final LocalDate date = LocalDate.of(2025, 1, 1);

			final Optional<JarEntryDao> result = repository
					.findBefore(JAR.getId(), date, 1);

			assertThat(result).isEmpty();
		}

		@Test
		void invalidJar() {
			final LocalDate date = LocalDate.of(2026, 1, 5);

			final Optional<JarEntryDao> result = repository
					.findBefore(Integer.MAX_VALUE, date, 0);

			assertThat(result).isEmpty();
		}

		@Test
		void otherJarSameDate() {
			final LocalDate date = LocalDate.of(2026, 1, 1);

			final Optional<JarEntryDao> result = repository
					.findBefore(TestJar.CAR.getId(), date, 2);

			assertThat(result).isEmpty();
		}

		@Test
		void previousDate() {
			final LocalDate date = LocalDate.of(2026, 1, 10);

			final Optional<JarEntryDao> result = repository
					.findBefore(JAR.getId(), date, 0);

			assertThat(result).hasValueSatisfying(
					e -> assertEquals(e, TestJarEntry.HOUSEHOLD_SALARY));
		}

		@Test
		void sameDate() {
			final LocalDate date = LocalDate.of(2026, 1, 10);

			final Optional<JarEntryDao> result = repository
					.findBefore(JAR.getId(), date, 1);

			assertThat(result).hasValueSatisfying(
					e -> assertEquals(e, TestJarEntry.HOUSEHOLD_GROCERY));
		}

		@Test
		void dateNull() {
			final Optional<JarEntryDao> result = repository
					.findBefore(JAR.getId(), null, 1);

			assertThat(result).isEmpty();
		}
	}

	@Nested
	class FindAllAfter {

		@Test
		void empty() {
			final LocalDate date = LocalDate.of(2035, 12, 31);

			final List<JarEntryDao> result = repository
					.findAllAfter(JAR.getId(), date, 0);

			assertThat(result).isEmpty();
		}

		@Test
		void invalidJar() {
			final LocalDate date = LocalDate.of(2026, 1, 5);

			final List<JarEntryDao> result = repository
					.findAllAfter(Integer.MAX_VALUE, date, 0);

			assertThat(result).isEmpty();
		}

		@Test
		void otherJarSameDate() {
			final LocalDate date = LocalDate.of(2026, 1, 1);

			final List<JarEntryDao> result = repository
					.findAllAfter(TestJar.HOUSEHOLD.getId(), date, 0);

			assertThat(result).satisfiesExactly(
					e -> assertEquals(e, TestJarEntry.HOUSEHOLD_GROCERY),
					e -> assertEquals(e, TestJarEntry.HOUSEHOLD_RENT),
					e -> assertEquals(e, TestJarEntry.HOUSEHOLD_CAFE),
					e -> assertEquals(e, TestJarEntry.HOUSEHOLD_MARKET));
		}

		@Test
		void nextDate() {
			final LocalDate date = LocalDate.of(2026, 1, 10);

			final List<JarEntryDao> result = repository
					.findAllAfter(JAR.getId(), date, 1);

			assertThat(result).satisfiesExactly(
					e -> assertEquals(e, TestJarEntry.HOUSEHOLD_CAFE),
					e -> assertEquals(e, TestJarEntry.HOUSEHOLD_MARKET));
		}

		@Test
		void sameDate() {
			final LocalDate date = LocalDate.of(2026, 1, 10);

			final List<JarEntryDao> result = repository
					.findAllAfter(JAR.getId(), date, 0);

			assertThat(result).satisfiesExactly(
					e -> assertEquals(e, TestJarEntry.HOUSEHOLD_RENT),
					e -> assertEquals(e, TestJarEntry.HOUSEHOLD_CAFE),
					e -> assertEquals(e, TestJarEntry.HOUSEHOLD_MARKET));
		}

		@Test
		void dateNull() {
			final List<JarEntryDao> result = repository
					.findAllAfter(JAR.getId(), null, 1);

			assertThat(result).isEmpty();
		}
	}

	@Nested
	class FindLastByJarId {

		@Test
		void invalidJar() {
			final Optional<JarEntryDao> result = repository
					.findLastByJarId(Integer.MAX_VALUE);

			assertThat(result).isEmpty();
		}

		@Test
		void latestDate() {
			final Optional<JarEntryDao> result = repository
					.findLastByJarId(TestJar.CAR.getId());

			assertThat(result).hasValueSatisfying(
					e -> assertEquals(e, TestJarEntry.CAR_FUEL));
		}

		@Test
		void highestSequenceOnLatestDate() {
			final Optional<JarEntryDao> result = repository
					.findLastByJarId(TestJar.HOLIDAY.getId());

			assertThat(result).hasValueSatisfying(
					e -> assertEquals(e, TestJarEntry.HOLIDAY_HOTEL));
		}
	}

	private static void assertEquals(final JarEntryDao e,
			final TestJarEntry expected) {
		assertThat(e.getId()).isEqualTo(expected.getId());
		assertThat(e.getTransactionId())
				.isEqualTo(expected.getTransaction().getId());
		assertThat(e.getJarId()).isEqualTo(expected.getJar().getId());
		assertThat(e.getBalanceBefore())
				.isEqualByComparingTo(expected.getBalanceBefore());
		assertThat(e.getBalanceAfter())
				.isEqualByComparingTo(expected.getBalanceAfter());
	}
}
