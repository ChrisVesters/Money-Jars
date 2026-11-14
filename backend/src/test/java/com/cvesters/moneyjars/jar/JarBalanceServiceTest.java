package com.cvesters.moneyjars.jar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import com.cvesters.moneyjars.jar.bdo.JarBalanceHistory;

class JarBalanceServiceTest {

	private final JarEntryStorageGateway gateway = mock();
	private final JarBalanceService service = new JarBalanceService(gateway);

	@Nested
	class GetHistoryList {

		private final LocalDate lastMonth = LocalDate.now().withDayOfMonth(1);

		@Test
		void empty() {
			final List<JarBalanceHistory> result = service
					.getHistory(Collections.emptyList());

			assertThat(result).isEmpty();
			verifyNoInteractions(gateway);
		}

		@Test
		void multiple() {
			final TestJar first = TestJar.HOUSEHOLD;
			final TestJar second = TestJar.HOLIDAY;
			final TestJarEntry testEntry = TestJarEntry.HOUSEHOLD_CAFE;
			when(gateway.findBefore(first.getId(), lastMonth))
					.thenReturn(Optional.of(testEntry.bdo()));

			final List<JarBalanceHistory> result = service
					.getHistory(List.of(first.getId(), second.getId()));

			assertThat(result).satisfiesExactly(history -> {
				assertThat(history.getJarId()).isEqualTo(first.getId());
				assertThat(history.getSnapshots()).containsExactly(
						entry(lastMonth, testEntry.getBalanceAfter()));
			}, history -> {
				assertThat(history.getJarId()).isEqualTo(second.getId());
				assertThat(history.getSnapshots()).isEmpty();
			});
		}

		@Test
		void jarIdsNull() {
			assertThatThrownBy(() -> service.getHistory(null))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void jarIdsContainsNull() {
			final List<Long> jarIds = Arrays
					.asList(TestJar.HOUSEHOLD.getId(), null);

			assertThatThrownBy(() -> service.getHistory(jarIds))
					.isInstanceOf(IllegalArgumentException.class);
			verifyNoInteractions(gateway);
		}
	}

	@Nested
	class GetHistory {

		private static final TestJar JAR = TestJar.HOUSEHOLD;

		private final LocalDate firstMonth = LocalDate.now()
				.minusMonths(11)
				.withDayOfMonth(1);
		private final LocalDate lastMonth = LocalDate.now().withDayOfMonth(1);

		@Test
		void success() {
			service.getHistory(JAR.getId());

			final InOrder inOrder = inOrder(gateway);
			for (int month = 0; month < 12; month++) {
				inOrder.verify(gateway)
						.findBefore(JAR.getId(), firstMonth.plusMonths(month));
			}
			inOrder.verifyNoMoreInteractions();
		}

		@Test
		void noEntries() {
			final JarBalanceHistory result = service.getHistory(JAR.getId());

			assertThat(result.getJarId()).isEqualTo(JAR.getId());
			assertThat(result.getSnapshots()).isEmpty();
		}

		@Test
		void single() {
			final TestJarEntry testEntry = TestJarEntry.HOUSEHOLD_CAFE;
			when(gateway.findBefore(JAR.getId(), lastMonth))
					.thenReturn(Optional.of(testEntry.bdo()));

			final JarBalanceHistory result = service.getHistory(JAR.getId());

			assertThat(result.getJarId()).isEqualTo(JAR.getId());
			assertThat(result.getSnapshots()).containsExactly(
					entry(lastMonth, testEntry.getBalanceAfter()));
		}

		@Test
		void multiple() {
			final TestJarEntry first = TestJarEntry.HOUSEHOLD_SALARY;
			when(gateway.findBefore(JAR.getId(), firstMonth))
					.thenReturn(Optional.of(first.bdo()));

			final TestJarEntry second = TestJarEntry.HOUSEHOLD_CAFE;
			when(gateway.findBefore(JAR.getId(), lastMonth))
					.thenReturn(Optional.of(second.bdo()));

			final JarBalanceHistory result = service.getHistory(JAR.getId());

			assertThat(result.getJarId()).isEqualTo(JAR.getId());
			assertThat(result.getSnapshots()).containsExactly(
					entry(firstMonth, first.getBalanceAfter()),
					entry(lastMonth, second.getBalanceAfter()));
		}
	}
}
