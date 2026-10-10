package com.cvesters.moneyjars.accountentry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import com.cvesters.moneyjars.accountentry.bdo.AccountEntry;
import com.cvesters.moneyjars.accountentry.dao.AccountEntryDao;

class AccountEntryStorageGatewayTest {

	private final AccountEntryRepository repository = mock();
	private final AccountEntryStorageGateway gateway = new AccountEntryStorageGateway(
			repository);

	@Nested
	class FindByAccountIdAndTransactionId {

		@Test
		public void found() {
			final long accountId = 1L;
			final long transactionId = 2L;

			final AccountEntryDao dao = mock();
			final AccountEntry bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);

			when(repository.findByAccountIdAndTransactionId(accountId,
					transactionId)).thenReturn(Optional.of(dao));

			final Optional<AccountEntry> result = gateway
					.findByAccountIdAndTransactionId(accountId, transactionId);

			assertThat(result).contains(bdo);
		}

		@Test
		public void notFound() {
			final long accountId = 1L;
			final long transactionId = 2L;

			when(repository.findByAccountIdAndTransactionId(accountId,
					transactionId)).thenReturn(Optional.empty());

			final Optional<AccountEntry> result = gateway
					.findByAccountIdAndTransactionId(accountId, transactionId);

			assertThat(result).isEmpty();
		}
	}

	@Nested
	class FindBefore {

		@Test
		public void found() {
			final long accountId = 1L;
			final LocalDate date = LocalDate.now();
			final int sequence = 0;

			final AccountEntryDao dao = mock();
			final AccountEntry bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);

			when(repository.findBefore(accountId, date, sequence))
					.thenReturn(Optional.of(dao));

			final Optional<AccountEntry> result = gateway.findBefore(accountId,
					date, sequence);

			assertThat(result).contains(bdo);
		}

		@Test
		public void notFound() {
			final long accountId = 1L;
			final LocalDate date = LocalDate.now();
			final int sequence = 0;

			when(repository.findBefore(accountId, date, sequence))
					.thenReturn(Optional.empty());

			final Optional<AccountEntry> result = gateway.findBefore(accountId,
					date, sequence);

			assertThat(result).isEmpty();
		}
	}

	@Nested
	class FindLast {

		@Test
		public void found() {
			final long accountId = 1L;

			final AccountEntryDao dao = mock();
			final AccountEntry bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);

			when(repository.findLastByAccountId(accountId))
					.thenReturn(Optional.of(dao));

			final Optional<AccountEntry> result = gateway.findLast(accountId);

			assertThat(result).contains(bdo);
		}

		@Test
		public void notFound() {
			final long accountId = 1L;

			when(repository.findLastByAccountId(accountId))
					.thenReturn(Optional.empty());

			final Optional<AccountEntry> result = gateway.findLast(accountId);

			assertThat(result).isEmpty();
		}
	}

	@Nested
	class GetAllAfter {

		@Test
		public void single() {
			final long accountId = 1L;
			final LocalDate date = LocalDate.now();
			final int sequence = 0;

			final AccountEntryDao dao = mock();
			final AccountEntry bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);

			when(repository.findAllAfter(accountId, date, sequence))
					.thenReturn(List.of(dao));

			final List<AccountEntry> result = gateway.getAllAfter(accountId,
					date, sequence);

			assertThat(result).containsExactly(bdo);
		}

		@Test
		public void multiple() {
			final long accountId = 1L;
			final LocalDate date = LocalDate.now();
			final int sequence = 0;

			final AccountEntryDao dao1 = mock();
			final AccountEntry bdo1 = mock();
			when(dao1.toBdo()).thenReturn(bdo1);

			final AccountEntryDao dao2 = mock();
			final AccountEntry bdo2 = mock();
			when(dao2.toBdo()).thenReturn(bdo2);

			when(repository.findAllAfter(accountId, date, sequence))
					.thenReturn(List.of(dao1, dao2));

			final List<AccountEntry> result = gateway.getAllAfter(accountId,
					date, sequence);

			assertThat(result).containsExactly(bdo1, bdo2);
		}

		@Test
		public void empty() {
			final long accountId = 1L;
			final LocalDate date = LocalDate.now();
			final int sequence = 0;

			when(repository.findAllAfter(accountId, date, sequence))
					.thenReturn(Collections.emptyList());

			final List<AccountEntry> result = gateway.getAllAfter(accountId,
					date, sequence);

			assertThat(result).isEmpty();
		}
	}

	@Nested
	class Create {

		private static final TestAccountEntry ACCOUNT_ENTRY = TestAccountEntry.WALLET_CAFE;

		@Test
		void success() {
			final AccountEntryDao createdDao = mock();
			final AccountEntry createdBdo = mock();
			when(createdDao.toBdo()).thenReturn(createdBdo);

			final AccountEntry entry = ACCOUNT_ENTRY.bdo();
			when(repository.save(argThat(v -> {
				assertThat(v.getId()).isNull();
				assertThat(v.getTransactionId())
						.isEqualTo(ACCOUNT_ENTRY.getTransaction().getId());
				assertThat(v.getAccountId())
						.isEqualTo(ACCOUNT_ENTRY.getAccount().getId());
				assertThat(v.getBalanceBefore())
						.isEqualByComparingTo(ACCOUNT_ENTRY.getBalanceBefore());
				assertThat(v.getBalanceAfter())
						.isEqualByComparingTo(ACCOUNT_ENTRY.getBalanceAfter());
				return true;
			}))).thenReturn(createdDao);

			final AccountEntry result = gateway.create(entry);

			assertThat(result).isSameAs(createdBdo);
		}

		@Test
		void entryNull() {
			assertThatThrownBy(() -> gateway.create(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Update {

		private static final TestAccountEntry ACCOUNT_ENTRY = TestAccountEntry.WALLET_CAFE;

		@Test
		void success() {
			final AccountEntryDao updatedDao = mock();
			final AccountEntry updatedBdo = mock();
			when(updatedDao.toBdo()).thenReturn(updatedBdo);

			final AccountEntry update = mock();
			when(update.getId()).thenReturn(ACCOUNT_ENTRY.getId());

			final AccountEntryDao existing = mock();
			when(repository.findById(ACCOUNT_ENTRY.getId()))
					.thenReturn(Optional.of(existing));
			when(repository.save(existing)).thenReturn(updatedDao);

			final AccountEntry result = gateway.update(update);

			assertThat(result).isSameAs(updatedBdo);

			final InOrder inOrder = inOrder(existing, repository);
			inOrder.verify(existing).updateWith(update);
			inOrder.verify(repository).save(existing);
		}

		@Test
		void notFound() {
			when(repository.findById(ACCOUNT_ENTRY.getId()))
					.thenReturn(Optional.empty());

			final AccountEntry update = mock();
			when(update.getId()).thenReturn(ACCOUNT_ENTRY.getId());

			assertThatThrownBy(() -> gateway.update(update))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void entryNull() {
			assertThatThrownBy(() -> gateway.update(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class UpdateAll {

		private static final TestAccountEntry ACCOUNT_ENTRY_1 = TestAccountEntry.WALLET_CAFE;
		private static final TestAccountEntry ACCOUNT_ENTRY_2 = TestAccountEntry.WALLET_MARKET;

		@Test
		void single() {
			final AccountEntryDao updatedDao = mock();
			final AccountEntry updatedBdo = mock();
			when(updatedDao.toBdo()).thenReturn(updatedBdo);

			final AccountEntry update = mock();
			when(update.getId()).thenReturn(ACCOUNT_ENTRY_1.getId());

			final AccountEntryDao existing = mock();
			when(repository.findById(ACCOUNT_ENTRY_1.getId()))
					.thenReturn(Optional.of(existing));
			when(repository.save(existing)).thenReturn(updatedDao);

			final List<AccountEntry> result = gateway
					.updateAll(List.of(update));

			assertThat(result).containsExactly(updatedBdo);

			final InOrder inOrder = inOrder(existing, repository);
			inOrder.verify(existing).updateWith(update);
			inOrder.verify(repository).save(existing);
		}

		@Test
		void multiple() {
			final AccountEntryDao updatedDao1 = mock();
			final AccountEntry updatedBdo1 = mock();
			when(updatedDao1.toBdo()).thenReturn(updatedBdo1);

			final AccountEntryDao updatedDao2 = mock();
			final AccountEntry updatedBdo2 = mock();
			when(updatedDao2.toBdo()).thenReturn(updatedBdo2);

			final AccountEntry update1 = mock();
			when(update1.getId()).thenReturn(ACCOUNT_ENTRY_1.getId());

			final AccountEntry update2 = mock();
			when(update2.getId()).thenReturn(ACCOUNT_ENTRY_2.getId());

			final AccountEntryDao existing1 = mock();
			when(repository.findById(ACCOUNT_ENTRY_1.getId()))
					.thenReturn(Optional.of(existing1));
			when(repository.save(existing1)).thenReturn(updatedDao1);

			final AccountEntryDao existing2 = mock();
			when(repository.findById(ACCOUNT_ENTRY_2.getId()))
					.thenReturn(Optional.of(existing2));
			when(repository.save(existing2)).thenReturn(updatedDao2);

			final List<AccountEntry> result = gateway
					.updateAll(List.of(update1, update2));

			assertThat(result).containsExactly(updatedBdo1, updatedBdo2);

			final InOrder inOrder = inOrder(existing1, existing2, repository);
			inOrder.verify(existing1).updateWith(update1);
			inOrder.verify(repository).save(existing1);
			inOrder.verify(existing2).updateWith(update2);
			inOrder.verify(repository).save(existing2);
		}

		@Test
		void empty() {
			final List<AccountEntry> result = gateway
					.updateAll(Collections.emptyList());

			assertThat(result).isEmpty();
			verify(repository, never()).save(any());
		}

		@Test
		void notFound() {
			final AccountEntry update1 = mock();
			when(update1.getId()).thenReturn(ACCOUNT_ENTRY_1.getId());

			final AccountEntry update2 = mock();
			when(update2.getId()).thenReturn(ACCOUNT_ENTRY_2.getId());

			final AccountEntryDao existing1 = mock();
			when(repository.findById(ACCOUNT_ENTRY_1.getId()))
					.thenReturn(Optional.of(existing1));
			when(repository.save(existing1)).thenReturn(mock());
			when(repository.findById(ACCOUNT_ENTRY_2.getId()))
					.thenReturn(Optional.empty());

			assertThatThrownBy(
					() -> gateway.updateAll(List.of(update1, update2)))
							.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void entriesNull() {
			assertThatThrownBy(() -> gateway.updateAll(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Delete {

		private static final TestAccountEntry ACCOUNT_ENTRY = TestAccountEntry.WALLET_CAFE;

		@Test
		void success() {
			final AccountEntry entry = mock();
			when(entry.getId()).thenReturn(ACCOUNT_ENTRY.getId());

			final AccountEntryDao existing = mock();
			when(repository.findById(ACCOUNT_ENTRY.getId()))
					.thenReturn(Optional.of(existing));

			gateway.delete(entry);

			verify(repository).delete(existing);
		}

		@Test
		void notFound() {
			when(repository.findById(ACCOUNT_ENTRY.getId()))
					.thenReturn(Optional.empty());

			final AccountEntry entry = mock();
			when(entry.getId()).thenReturn(ACCOUNT_ENTRY.getId());

			assertThatThrownBy(() -> gateway.delete(entry))
					.isInstanceOf(IllegalArgumentException.class);

			verify(repository, never()).delete(any());
		}

		@Test
		void entryNull() {
			assertThatThrownBy(() -> gateway.delete(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
