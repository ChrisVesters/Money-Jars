package com.cvesters.moneyjars.jar.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.jar.TestJar;
import com.cvesters.moneyjars.jar.TestJarEntry;
import com.cvesters.moneyjars.jar.bdo.JarBalanceHistory;

class JarBalanceHistoryDtoTest {

	private static final TestJar JAR = TestJar.HOUSEHOLD;

	@Nested
	class Constructor {

		@Test
		void success() {
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

			final var dto = new JarBalanceHistoryDto(history);

			assertThat(dto.getJarId()).isEqualTo(JAR.getId());
			assertThat(dto.getSnapshots()).satisfiesExactly(
					snapshot -> assertSnapshot(snapshot, first),
					snapshot -> assertSnapshot(snapshot, second),
					snapshot -> assertSnapshot(snapshot, third));
		}

		@Test
		void noSnapshots() {
			final var history = new JarBalanceHistory(JAR.getId());

			final var dto = new JarBalanceHistoryDto(history);

			assertThat(dto.getJarId()).isEqualTo(JAR.getId());
			assertThat(dto.getSnapshots()).isEmpty();
		}

		@Test
		void bdoNull() {
			assertThatThrownBy(() -> new JarBalanceHistoryDto(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	private static void assertSnapshot(final JarBalanceSnapshotDto snapshot,
			final TestJarEntry entry) {
		assertThat(snapshot.getDate())
				.isEqualTo(entry.getTransaction().getDate().toString());
		assertThat(snapshot.getBalance())
				.isEqualTo(entry.getBalanceAfter().toPlainString());
	}
}
