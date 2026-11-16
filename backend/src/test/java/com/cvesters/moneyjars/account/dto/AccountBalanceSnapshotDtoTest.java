package com.cvesters.moneyjars.account.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.Map;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.account.TestAccountEntry;

class AccountBalanceSnapshotDtoTest {

	private static final TestAccountEntry ENTRY = TestAccountEntry.CHECKING_RENT;

	@Nested
	class Constructor {

		@Test
		void success() {
			final Map.Entry<LocalDate, BigDecimal> bdo = Map.entry(
					ENTRY.getTransaction().getDate(), ENTRY.getBalanceAfter());

			final var dto = new AccountBalanceSnapshotDto(bdo);

			assertThat(dto.getDate())
					.isEqualTo(ENTRY.getTransaction().getDate().toString());
			assertThat(dto.getBalance())
					.isEqualTo(ENTRY.getBalanceAfter().toPlainString());
		}

		@Test
		void negativeBalance() {
			final TestAccountEntry negative = TestAccountEntry.CREDIT_CARD_FUEL;
			final Map.Entry<LocalDate, BigDecimal> bdo = Map.entry(
					negative.getTransaction().getDate(),
					negative.getBalanceAfter());

			final var dto = new AccountBalanceSnapshotDto(bdo);

			assertThat(dto.getDate())
					.isEqualTo(negative.getTransaction().getDate().toString());
			assertThat(dto.getBalance())
					.isEqualTo(negative.getBalanceAfter().toPlainString());
			assertThat(dto.getBalance()).startsWith("-");
		}

		@Test
		void bdoNull() {
			assertThatThrownBy(() -> new AccountBalanceSnapshotDto(null))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void dateNull() {
			final Map.Entry<LocalDate, BigDecimal> bdo = new AbstractMap.SimpleEntry<>(
					null, ENTRY.getBalanceAfter());

			assertThatThrownBy(() -> new AccountBalanceSnapshotDto(bdo))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void balanceNull() {
			final Map.Entry<LocalDate, BigDecimal> bdo = new AbstractMap.SimpleEntry<>(
					ENTRY.getTransaction().getDate(), null);

			assertThatThrownBy(() -> new AccountBalanceSnapshotDto(bdo))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
