package com.cvesters.moneyjars.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import com.cvesters.moneyjars.account.AccountEntryService;
import com.cvesters.moneyjars.transaction.bdo.AccountTransferTransaction;
import com.cvesters.moneyjars.transaction.bdo.AccountTransferTransactionAction;

class AccountTransferTransactionServiceTest {

	private final TransactionSequenceService sequenceService = mock();
	private final AccountEntryService accountEntryService = mock();
	private final AccountTransferTransactionStorageGateway storage = mock();

	private final AccountTransferTransactionService service = new AccountTransferTransactionService(
			sequenceService, accountEntryService, storage);

	@Nested
	class Create {

		private static final TestAccountTransferTransaction TRANSACTION = TestAccountTransferTransaction.CASH_WITHDRAWAL;

		@Test
		void success() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = TRANSACTION.getDescription();
			final long sourceAccountId = TRANSACTION.getSourceAccount().getId();
			final long targetAccountId = TRANSACTION.getTargetAccount().getId();

			final var action = new AccountTransferTransactionAction.Create(date,
					amount, description, sourceAccountId, targetAccountId);

			when(sequenceService.getNextSequence(date)).thenReturn(4);

			final AccountTransferTransaction created = mock();
			when(storage.create(argThat(transaction -> {
				assertThat(transaction.getId()).isNull();
				assertThat(transaction.getDate()).isEqualTo(date);
				assertThat(transaction.getSequence()).isEqualTo(4);
				assertThat(transaction.getAmount()).isEqualTo(amount);
				assertThat(transaction.getDescription()).isEqualTo(description);
				assertThat(transaction.getSourceAccountId())
						.isEqualTo(sourceAccountId);
				assertThat(transaction.getTargetAccountId())
						.isEqualTo(targetAccountId);
				return true;
			}))).thenReturn(created);

			final var result = service.create(action);

			assertThat(result).isSameAs(created);
			verify(accountEntryService).create(created);
		}

		@Test
		void actionNull() {
			assertThatThrownBy(() -> service.create(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Update {

		private static final TestAccountTransferTransaction TRANSACTION = TestAccountTransferTransaction.CASH_WITHDRAWAL;

		@Test
		void sameDate() {
			final long id = TRANSACTION.getId();
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = TRANSACTION.getDescription();
			final long sourceAccountId = TRANSACTION.getSourceAccount().getId();
			final long targetAccountId = TRANSACTION.getTargetAccount().getId();

			final var action = new AccountTransferTransactionAction.Update(date,
					amount, description, sourceAccountId, targetAccountId);

			final AccountTransferTransaction existing = mock();
			when(existing.getDate()).thenReturn(date);

			final AccountTransferTransaction updated = mock();
			when(storage.get(id)).thenReturn(existing);
			when(storage.update(existing)).thenReturn(updated);

			final var result = service.update(id, action);

			assertThat(result).isSameAs(updated);

			final InOrder inOrder = inOrder(storage, accountEntryService,
					existing);
			inOrder.verify(storage).get(id);
			inOrder.verify(accountEntryService).delete(existing);
			inOrder.verify(existing).setAmount(amount);
			inOrder.verify(existing).setDescription(description);
			inOrder.verify(existing).setSourceAccountId(sourceAccountId);
			inOrder.verify(existing).setTargetAccountId(targetAccountId);
			inOrder.verify(storage).update(existing);
			inOrder.verify(accountEntryService).create(updated);

			verify(existing, never()).setDate(any());
			verify(existing, never()).setSequence(anyInt());
			verify(sequenceService, never()).getNextSequence(any());
		}

		@Test
		void newDate() {
			final long id = TRANSACTION.getId();
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = TRANSACTION.getDescription();
			final long sourceAccountId = TRANSACTION.getSourceAccount().getId();
			final long targetAccountId = TRANSACTION.getTargetAccount().getId();

			final var action = new AccountTransferTransactionAction.Update(date,
					amount, description, sourceAccountId, targetAccountId);

			final AccountTransferTransaction existing = mock();
			final LocalDate oldDate = LocalDate.of(2025, 12, 7);
			when(existing.getDate()).thenReturn(oldDate);

			when(sequenceService.getNextSequence(date)).thenReturn(4);

			final AccountTransferTransaction updated = mock();
			when(storage.get(id)).thenReturn(existing);
			when(storage.update(existing)).thenReturn(updated);

			final var result = service.update(id, action);

			assertThat(result).isSameAs(updated);

			final InOrder inOrder = inOrder(storage, accountEntryService,
					existing);
			inOrder.verify(storage).get(id);
			inOrder.verify(accountEntryService).delete(existing);
			inOrder.verify(existing).setDate(date);
			inOrder.verify(existing).setSequence(4);
			inOrder.verify(existing).setAmount(amount);
			inOrder.verify(existing).setDescription(description);
			inOrder.verify(existing).setSourceAccountId(sourceAccountId);
			inOrder.verify(existing).setTargetAccountId(targetAccountId);
			inOrder.verify(storage).update(existing);
			inOrder.verify(accountEntryService).create(updated);
		}

		@Test
		void actionNull() {
			final long id = TRANSACTION.getId();

			assertThatThrownBy(() -> service.update(id, null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Delete {

		private static final TestAccountTransferTransaction TRANSACTION = TestAccountTransferTransaction.CASH_WITHDRAWAL;

		@Test
		void success() {
			final long id = TRANSACTION.getId();

			final AccountTransferTransaction existing = mock();
			when(storage.get(id)).thenReturn(existing);

			service.delete(id);

			final InOrder inOrder = inOrder(storage, accountEntryService);
			inOrder.verify(storage).get(id);
			inOrder.verify(accountEntryService).delete(existing);
			inOrder.verify(storage).delete(existing);
		}
	}
}
