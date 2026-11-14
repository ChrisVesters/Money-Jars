package com.cvesters.moneyjars.jar.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.jar.TestJar;
import com.cvesters.moneyjars.jar.TestJarEntry;

class JarBalanceHistoryTest {

	private static final TestJar JAR = TestJar.HOUSEHOLD;

	@Nested
	class Constructor {

		@Test
		void success() {
			final var history = new JarBalanceHistory(JAR.getId());

			assertThat(history.getJarId()).isEqualTo(JAR.getId());
			assertThat(history.getSnapshots()).isEmpty();
		}
	}

	@Nested
	class AddSnapshot {

		@Test
		void single() {
			final TestJarEntry testEntry = TestJarEntry.HOUSEHOLD_CAFE;
			final var history = new JarBalanceHistory(JAR.getId());

			history.addSnapshot(testEntry.getTransaction().getDate(),
					testEntry.getBalanceAfter());

			assertThat(history.getJarId()).isEqualTo(JAR.getId());
			assertThat(history.getSnapshots()).containsExactly(
					entry(testEntry.getTransaction().getDate(),
							testEntry.getBalanceAfter()));
		}

		@Test
		void multipleSortedByDate() {
			final TestJarEntry first = TestJarEntry.HOUSEHOLD_SALARY;
			final TestJarEntry second = TestJarEntry.HOUSEHOLD_CAFE;
			final TestJarEntry third = TestJarEntry.HOUSEHOLD_MARKET;
			final var history = new JarBalanceHistory(JAR.getId());

			history.addSnapshot(third.getTransaction().getDate(),
					third.getBalanceAfter());
			history.addSnapshot(first.getTransaction().getDate(),
					first.getBalanceAfter());
			history.addSnapshot(second.getTransaction().getDate(),
					second.getBalanceAfter());

			assertThat(history.getJarId()).isEqualTo(JAR.getId());
			assertThat(history.getSnapshots()).containsExactly(
					entry(first.getTransaction().getDate(),
							first.getBalanceAfter()),
					entry(second.getTransaction().getDate(),
							second.getBalanceAfter()),
					entry(third.getTransaction().getDate(),
							third.getBalanceAfter()));
		}

		@Test
		void sameDateReplaces() {
			final TestJarEntry earlier = TestJarEntry.HOUSEHOLD_GROCERY;
			final TestJarEntry later = TestJarEntry.HOUSEHOLD_RENT;
			final var history = new JarBalanceHistory(JAR.getId());

			history.addSnapshot(earlier.getTransaction().getDate(),
					earlier.getBalanceAfter());
			history.addSnapshot(later.getTransaction().getDate(),
					later.getBalanceAfter());

			assertThat(history.getJarId()).isEqualTo(JAR.getId());
			assertThat(history.getSnapshots()).containsExactly(
					entry(later.getTransaction().getDate(),
							later.getBalanceAfter()));
		}
	}
}
