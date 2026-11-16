package com.cvesters.moneyjars.account.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.account.TestAccount;
import com.cvesters.moneyjars.account.TestAccountEntry;

class AccountBalanceHistoryTest {

	private static final TestAccount JAR = TestAccount.CHECKING;

	@Nested
	class Constructor {

		@Test
		void success() {
			final var history = new AccountBalanceHistory(JAR.getId());

			assertThat(history.getAccountId()).isEqualTo(JAR.getId());
			assertThat(history.getSnapshots()).isEmpty();
		}
	}

	@Nested
	class AddSnapshot {

		@Test
		void single() {
			final TestAccountEntry testEntry = TestAccountEntry.CHECKING_RENT;
			final var history = new AccountBalanceHistory(JAR.getId());

			history.addSnapshot(testEntry.getTransaction().getDate(),
					testEntry.getBalanceAfter());

			assertThat(history.getAccountId()).isEqualTo(JAR.getId());
			assertThat(history.getSnapshots()).containsExactly(
					entry(testEntry.getTransaction().getDate(),
							testEntry.getBalanceAfter()));
		}

		@Test
		void multipleSortedByDate() {
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

			assertThat(history.getAccountId()).isEqualTo(JAR.getId());
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
			final TestAccountEntry earlier = TestAccountEntry.CHECKING_GROCERY;
			final TestAccountEntry later = TestAccountEntry.CHECKING_RENT;
			final var history = new AccountBalanceHistory(JAR.getId());

			history.addSnapshot(earlier.getTransaction().getDate(),
					earlier.getBalanceAfter());
			history.addSnapshot(later.getTransaction().getDate(),
					later.getBalanceAfter());

			assertThat(history.getAccountId()).isEqualTo(JAR.getId());
			assertThat(history.getSnapshots()).containsExactly(
					entry(later.getTransaction().getDate(),
							later.getBalanceAfter()));
		}
	}
}
