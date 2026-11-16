package com.cvesters.moneyjars.account.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.account.TestAccount;
import com.cvesters.moneyjars.account.TestAccountEntry;
import com.cvesters.moneyjars.account.bdo.AccountBalanceHistory;

class AccountBalanceHistoryDtoTest {

	private static final TestAccount JAR = TestAccount.CHECKING;

	@Nested
	class Constructor {

		@Test
		void success() {
			final TestAccountEntry first = TestAccountEntry.CHECKING_SALARY;
			final TestAccountEntry second = TestAccountEntry.CHECKING_RENT;
			final TestAccountEntry third = TestAccountEntry.CHECKING_CASH_WITHDRAWAL;
			final var history = new AccountBalanceHistory(JAR.getId());
			history.addSnapshot(third.getTransaction().getDate(),
					third.getBalanceAfter());
			history.addSnapshot(first.getTransaction().getDate(),
					first.getBalanceAfter());
			history.addSnapshot(second.getTransaction().getDate(),
					second.getBalanceAfter());

			final var dto = new AccountBalanceHistoryDto(history);

			assertThat(dto.getAccountId()).isEqualTo(JAR.getId());
			assertThat(dto.getSnapshots()).satisfiesExactly(
					snapshot -> assertSnapshot(snapshot, first),
					snapshot -> assertSnapshot(snapshot, second),
					snapshot -> assertSnapshot(snapshot, third));
		}

		@Test
		void noSnapshots() {
			final var history = new AccountBalanceHistory(JAR.getId());

			final var dto = new AccountBalanceHistoryDto(history);

			assertThat(dto.getAccountId()).isEqualTo(JAR.getId());
			assertThat(dto.getSnapshots()).isEmpty();
		}

		@Test
		void bdoNull() {
			assertThatThrownBy(() -> new AccountBalanceHistoryDto(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	private static void assertSnapshot(final AccountBalanceSnapshotDto snapshot,
			final TestAccountEntry entry) {
		assertThat(snapshot.getDate())
				.isEqualTo(entry.getTransaction().getDate().toString());
		assertThat(snapshot.getBalance())
				.isEqualTo(entry.getBalanceAfter().toPlainString());
	}
}
