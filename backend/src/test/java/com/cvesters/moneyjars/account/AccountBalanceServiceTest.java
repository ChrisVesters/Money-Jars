package com.cvesters.moneyjars.account;

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

import com.cvesters.moneyjars.account.bdo.AccountBalanceHistory;

class AccountBalanceServiceTest {

	private final AccountEntryStorageGateway gateway = mock();
	private final AccountBalanceService service = new AccountBalanceService(
			gateway);

	@Nested
	class GetHistoryList {

		private final LocalDate lastMonth = LocalDate.now().withDayOfMonth(1);

		@Test
		void empty() {
			final List<AccountBalanceHistory> result = service
					.getHistory(Collections.emptyList());

			assertThat(result).isEmpty();
			verifyNoInteractions(gateway);
		}

		@Test
		void multiple() {
			final TestAccount first = TestAccount.CHECKING;
			final TestAccount second = TestAccount.WALLET;
			final TestAccountEntry testEntry = TestAccountEntry.CHECKING_RENT;
			when(gateway.findBefore(first.getId(), lastMonth))
					.thenReturn(Optional.of(testEntry.bdo()));

			final List<AccountBalanceHistory> result = service
					.getHistory(List.of(first.getId(), second.getId()));

			assertThat(result).satisfiesExactly(history -> {
				assertThat(history.getAccountId()).isEqualTo(first.getId());
				assertThat(history.getSnapshots()).containsExactly(
						entry(lastMonth, testEntry.getBalanceAfter()));
			}, history -> {
				assertThat(history.getAccountId()).isEqualTo(second.getId());
				assertThat(history.getSnapshots()).isEmpty();
			});
		}

		@Test
		void accountIdsNull() {
			assertThatThrownBy(() -> service.getHistory(null))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void accountIdsContainsNull() {
			final List<Long> accountIds = Arrays
					.asList(TestAccount.CHECKING.getId(), null);

			assertThatThrownBy(() -> service.getHistory(accountIds))
					.isInstanceOf(IllegalArgumentException.class);
			verifyNoInteractions(gateway);
		}
	}

	@Nested
	class GetHistory {

		private static final TestAccount ACCOUNT = TestAccount.CHECKING;

		private final LocalDate firstMonth = LocalDate.now()
				.minusMonths(11)
				.withDayOfMonth(1);
		private final LocalDate lastMonth = LocalDate.now().withDayOfMonth(1);

		@Test
		void success() {
			service.getHistory(ACCOUNT.getId());

			final InOrder inOrder = inOrder(gateway);
			for (int month = 0; month < 12; month++) {
				inOrder.verify(gateway)
						.findBefore(ACCOUNT.getId(),
								firstMonth.plusMonths(month));
			}
			inOrder.verifyNoMoreInteractions();
		}

		@Test
		void noEntries() {
			final AccountBalanceHistory result = service
					.getHistory(ACCOUNT.getId());

			assertThat(result.getAccountId()).isEqualTo(ACCOUNT.getId());
			assertThat(result.getSnapshots()).isEmpty();
		}

		@Test
		void single() {
			final TestAccountEntry testEntry = TestAccountEntry.CHECKING_RENT;
			when(gateway.findBefore(ACCOUNT.getId(), lastMonth))
					.thenReturn(Optional.of(testEntry.bdo()));

			final AccountBalanceHistory result = service
					.getHistory(ACCOUNT.getId());

			assertThat(result.getAccountId()).isEqualTo(ACCOUNT.getId());
			assertThat(result.getSnapshots()).containsExactly(
					entry(lastMonth, testEntry.getBalanceAfter()));
		}

		@Test
		void multiple() {
			final TestAccountEntry first = TestAccountEntry.CHECKING_SALARY;
			when(gateway.findBefore(ACCOUNT.getId(), firstMonth))
					.thenReturn(Optional.of(first.bdo()));

			final TestAccountEntry second = TestAccountEntry.CHECKING_RENT;
			when(gateway.findBefore(ACCOUNT.getId(), lastMonth))
					.thenReturn(Optional.of(second.bdo()));

			final AccountBalanceHistory result = service
					.getHistory(ACCOUNT.getId());

			assertThat(result.getAccountId()).isEqualTo(ACCOUNT.getId());
			assertThat(result.getSnapshots()).containsExactly(
					entry(firstMonth, first.getBalanceAfter()),
					entry(lastMonth, second.getBalanceAfter()));
		}

		@Test
		void firstEntryMidWindow() {
			final TestAccountEntry testEntry = TestAccountEntry.CHECKING_RENT;
			final int firstSnapshotMonth = 5;
			for (int month = firstSnapshotMonth; month < 12; month++) {
				when(gateway.findBefore(ACCOUNT.getId(),
						firstMonth.plusMonths(month)))
								.thenReturn(Optional.of(testEntry.bdo()));
			}

			final AccountBalanceHistory result = service
					.getHistory(ACCOUNT.getId());

			assertThat(result.getAccountId()).isEqualTo(ACCOUNT.getId());
			assertThat(result.getSnapshots()).hasSize(12 - firstSnapshotMonth);
			assertThat(result.getSnapshots().firstKey())
					.isEqualTo(firstMonth.plusMonths(firstSnapshotMonth));
			assertThat(result.getSnapshots().lastKey()).isEqualTo(lastMonth);
			assertThat(result.getSnapshots().values())
					.containsOnly(testEntry.getBalanceAfter());
		}

		@Test
		void allBeforeWindow() {
			final TestAccountEntry testEntry = TestAccountEntry.CHECKING_RENT;
			for (int month = 0; month < 12; month++) {
				when(gateway.findBefore(ACCOUNT.getId(),
						firstMonth.plusMonths(month)))
								.thenReturn(Optional.of(testEntry.bdo()));
			}

			final AccountBalanceHistory result = service
					.getHistory(ACCOUNT.getId());

			assertThat(result.getAccountId()).isEqualTo(ACCOUNT.getId());
			assertThat(result.getSnapshots()).hasSize(12);
			assertThat(result.getSnapshots().firstKey()).isEqualTo(firstMonth);
			assertThat(result.getSnapshots().lastKey()).isEqualTo(lastMonth);
			assertThat(result.getSnapshots().values())
					.containsOnly(testEntry.getBalanceAfter());
		}

		@Test
		void negativeBalance() {
			final TestAccount creditCard = TestAccount.CREDIT_CARD;
			final TestAccountEntry testEntry = TestAccountEntry.CREDIT_CARD_FUEL;
			when(gateway.findBefore(creditCard.getId(), lastMonth))
					.thenReturn(Optional.of(testEntry.bdo()));

			final AccountBalanceHistory result = service
					.getHistory(creditCard.getId());

			assertThat(result.getAccountId()).isEqualTo(creditCard.getId());
			assertThat(result.getSnapshots()).containsExactly(
					entry(lastMonth, testEntry.getBalanceAfter()));
		}
	}
}
