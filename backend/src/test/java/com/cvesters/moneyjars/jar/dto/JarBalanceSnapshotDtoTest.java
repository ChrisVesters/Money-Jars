package com.cvesters.moneyjars.jar.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.Map;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.jar.TestJarEntry;

class JarBalanceSnapshotDtoTest {

	private static final TestJarEntry ENTRY = TestJarEntry.HOUSEHOLD_CAFE;

	@Nested
	class Constructor {

		@Test
		void success() {
			final Map.Entry<LocalDate, BigDecimal> bdo = Map.entry(
					ENTRY.getTransaction().getDate(), ENTRY.getBalanceAfter());

			final var dto = new JarBalanceSnapshotDto(bdo);

			assertThat(dto.getDate())
					.isEqualTo(ENTRY.getTransaction().getDate().toString());
			assertThat(dto.getBalance())
					.isEqualTo(ENTRY.getBalanceAfter().toPlainString());
		}

		@Test
		void bdoNull() {
			assertThatThrownBy(() -> new JarBalanceSnapshotDto(null))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void dateNull() {
			final Map.Entry<LocalDate, BigDecimal> bdo = new AbstractMap.SimpleEntry<>(
					null, ENTRY.getBalanceAfter());

			assertThatThrownBy(() -> new JarBalanceSnapshotDto(bdo))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void balanceNull() {
			final Map.Entry<LocalDate, BigDecimal> bdo = new AbstractMap.SimpleEntry<>(
					ENTRY.getTransaction().getDate(), null);

			assertThatThrownBy(() -> new JarBalanceSnapshotDto(bdo))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
