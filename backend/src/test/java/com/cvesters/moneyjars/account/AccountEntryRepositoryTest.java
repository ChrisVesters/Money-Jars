package com.cvesters.moneyjars.account;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.cvesters.moneyjars.account.dao.AccountEntryDao;
import com.cvesters.moneyjars.test.RepositoryTest;

@Sql({ "/db/accounts.sql", "/db/jars.sql", "/db/transactions.sql",
		"/db/account_entries.sql" })
class AccountEntryRepositoryTest extends RepositoryTest {

	@Autowired
	private AccountEntryRepository repository;

	private static final TestAccount ACCOUNT = TestAccount.CHECKING;

	@Nested
	class FindBefore {

		@Test
		void empty() {
			final LocalDate date = LocalDate.of(2025, 1, 1);

			final Optional<AccountEntryDao> result = repository
					.findBefore(ACCOUNT.getId(), date, 1);

			assertThat(result).isEmpty();
		}

		@Test
		void invalidAccount() {
			final LocalDate date = LocalDate.of(2026, 1, 5);

			final Optional<AccountEntryDao> result = repository
					.findBefore(Integer.MAX_VALUE, date, 0);

			assertThat(result).isEmpty();
		}

		@Test
		void otherAccountSameDate() {
			final LocalDate date = LocalDate.of(2026, 1, 10);

			final Optional<AccountEntryDao> result = repository
					.findBefore(TestAccount.WALLET.getId(), date, 1);

			assertThat(result).isEmpty();
		}

		@Test
		void previousDate() {
			final LocalDate date = LocalDate.of(2026, 1, 10);

			final Optional<AccountEntryDao> result = repository
					.findBefore(ACCOUNT.getId(), date, 0);

			assertThat(result).hasValueSatisfying(e -> assertEquals(e,
					TestAccountEntry.CHECKING_CAR_ALLOWANCE));
		}

		@Test
		void sameDate() {
			final LocalDate date = LocalDate.of(2026, 1, 10);

			final Optional<AccountEntryDao> result = repository
					.findBefore(ACCOUNT.getId(), date, 1);

			assertThat(result).hasValueSatisfying(
					e -> assertEquals(e, TestAccountEntry.CHECKING_GROCERY));
		}

		@Test
		void dateNull() {
			final Optional<AccountEntryDao> result = repository
					.findBefore(ACCOUNT.getId(), null, 1);

			assertThat(result).isEmpty();
		}
	}

	@Nested
	class FindAllAfter {

		@Test
		void empty() {
			final LocalDate date = LocalDate.of(2035, 12, 31);

			final List<AccountEntryDao> result = repository
					.findAllAfter(ACCOUNT.getId(), date, 0);

			assertThat(result).isEmpty();
		}

		@Test
		void invalidAccount() {
			final LocalDate date = LocalDate.of(2026, 1, 5);

			final List<AccountEntryDao> result = repository
					.findAllAfter(Integer.MAX_VALUE, date, 0);

			assertThat(result).isEmpty();
		}

		@Test
		void otherAccountSameDate() {
			final LocalDate date = LocalDate.of(2026, 1, 1);

			final List<AccountEntryDao> result = repository
					.findAllAfter(TestAccount.SAVINGS.getId(), date, 1);

			assertThat(result).isEmpty();
		}

		@Test
		void nextDate() {
			final LocalDate date = LocalDate.of(2026, 1, 5);

			final List<AccountEntryDao> result = repository
					.findAllAfter(ACCOUNT.getId(), date, 0);

			assertThat(result).satisfiesExactly(
					e -> assertEquals(e, TestAccountEntry.CHECKING_GROCERY),
					e -> assertEquals(e, TestAccountEntry.CHECKING_RENT));
		}

		@Test
		void sameDate() {
			final LocalDate date = LocalDate.of(2026, 1, 1);

			final List<AccountEntryDao> result = repository
					.findAllAfter(ACCOUNT.getId(), date, 0);

			assertThat(result).satisfiesExactly(
					e -> assertEquals(e,
							TestAccountEntry.CHECKING_CAR_ALLOWANCE),
					e -> assertEquals(e, TestAccountEntry.CHECKING_GROCERY),
					e -> assertEquals(e, TestAccountEntry.CHECKING_RENT));
		}

		@Test
		void dateNull() {
			final List<AccountEntryDao> result = repository
					.findAllAfter(ACCOUNT.getId(), null, 1);

			assertThat(result).isEmpty();
		}
	}

	@Nested
	class FindLastByAccountId {

		@Test
		void invalidAccount() {
			final Optional<AccountEntryDao> result = repository
					.findLastByAccountId(Integer.MAX_VALUE);

			assertThat(result).isEmpty();
		}

		@Test
		void latestDate() {
			final Optional<AccountEntryDao> result = repository
					.findLastByAccountId(TestAccount.WALLET.getId());

			assertThat(result).hasValueSatisfying(
					e -> assertEquals(e, TestAccountEntry.WALLET_MARKET));
		}

		@Test
		void highestSequenceOnLatestDate() {
			final Optional<AccountEntryDao> result = repository
					.findLastByAccountId(ACCOUNT.getId());

			assertThat(result).hasValueSatisfying(
					e -> assertEquals(e, TestAccountEntry.CHECKING_RENT));
		}
	}

	private static void assertEquals(final AccountEntryDao e,
			final TestAccountEntry expected) {
		assertThat(e.getId()).isEqualTo(expected.getId());
		assertThat(e.getTransactionId())
				.isEqualTo(expected.getTransaction().getId());
		assertThat(e.getAccountId()).isEqualTo(expected.getAccount().getId());
		assertThat(e.getBalanceBefore())
				.isEqualByComparingTo(expected.getBalanceBefore());
		assertThat(e.getBalanceAfter())
				.isEqualByComparingTo(expected.getBalanceAfter());
	}
}
