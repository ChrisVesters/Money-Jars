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

import com.cvesters.moneyjars.accountentry.AccountEntryService;
import com.cvesters.moneyjars.jarentry.JarEntryService;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionDirection;
import com.cvesters.moneyjars.transaction.bdo.TransactionAction;

class PaymentTransactionServiceTest {

	private final TransactionSequenceService sequenceService = mock();
	private final JarEntryService jarEntryService = mock();
	private final AccountEntryService accountEntryService = mock();
	private final PaymentTransactionStorageGateway storage = mock();

	private final PaymentTransactionService service = new PaymentTransactionService(
			sequenceService, jarEntryService, accountEntryService, storage);

	@Nested
	class Create {

		private static final TestPaymentTransaction TRANSACTION = TestPaymentTransaction.RENT;

		@Test
		void success() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final String counterparty = TRANSACTION.getCounterparty();
			final PaymentTransactionDirection direction = TRANSACTION
					.getDirection();

			final var action = new TransactionAction.CreatePayment(date, amount,
					description, jarId, accountId, counterparty, direction);

			when(sequenceService.getNextSequence(date)).thenReturn(4);

			final PaymentTransaction created = mock();
			when(storage.create(argThat(transaction -> {
				assertThat(transaction.getId()).isNull();
				assertThat(transaction.getDate()).isEqualTo(date);
				assertThat(transaction.getSequence()).isEqualTo(4);
				assertThat(transaction.getAmount()).isEqualTo(amount);
				assertThat(transaction.getDescription()).isEqualTo(description);
				assertThat(transaction.getJarId()).isEqualTo(jarId);
				assertThat(transaction.getAccountId()).isEqualTo(accountId);
				assertThat(transaction.getCounterparty())
						.isEqualTo(counterparty);
				assertThat(transaction.getDirection()).isEqualTo(direction);
				return true;
			}))).thenReturn(created);

			final var result = service.create(action);

			assertThat(result).isSameAs(created);
			verify(jarEntryService).create(created);
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

		private static final TestPaymentTransaction TRANSACTION = TestPaymentTransaction.RENT;

		@Test
		void sameDate() {
			final long id = TRANSACTION.getId();
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final String counterparty = TRANSACTION.getCounterparty();
			final PaymentTransactionDirection direction = TRANSACTION
					.getDirection();

			final var action = new TransactionAction.UpdatePayment(date, amount,
					description, jarId, accountId, counterparty, direction);

			final PaymentTransaction existing = mock();
			when(existing.getDate()).thenReturn(date);

			final PaymentTransaction updated = mock();
			when(storage.get(id)).thenReturn(existing);
			when(storage.update(existing)).thenReturn(updated);

			final var result = service.update(id, action);

			assertThat(result).isSameAs(updated);

			final InOrder inOrder = inOrder(storage, jarEntryService,
					accountEntryService, existing);
			inOrder.verify(storage).get(id);
			inOrder.verify(jarEntryService).delete(existing);
			inOrder.verify(accountEntryService).delete(existing);
			inOrder.verify(existing).setAmount(amount);
			inOrder.verify(existing).setDescription(description);
			inOrder.verify(existing).setJarId(jarId);
			inOrder.verify(existing).setAccountId(accountId);
			inOrder.verify(existing).setCounterparty(counterparty);
			inOrder.verify(existing).setDirection(direction);
			inOrder.verify(storage).update(existing);
			inOrder.verify(jarEntryService).create(updated);
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
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final String counterparty = TRANSACTION.getCounterparty();
			final PaymentTransactionDirection direction = TRANSACTION
					.getDirection();

			final var action = new TransactionAction.UpdatePayment(date, amount,
					description, jarId, accountId, counterparty, direction);

			final PaymentTransaction existing = mock();
			final LocalDate oldDate = LocalDate.of(2025, 12, 7);
			when(existing.getDate()).thenReturn(oldDate);

			when(sequenceService.getNextSequence(date)).thenReturn(4);

			final PaymentTransaction updated = mock();
			when(storage.get(id)).thenReturn(existing);
			when(storage.update(existing)).thenReturn(updated);

			final var result = service.update(id, action);

			assertThat(result).isSameAs(updated);

			final InOrder inOrder = inOrder(storage, jarEntryService,
					accountEntryService, existing);
			inOrder.verify(storage).get(id);
			inOrder.verify(jarEntryService).delete(existing);
			inOrder.verify(accountEntryService).delete(existing);
			inOrder.verify(existing).setDate(date);
			inOrder.verify(existing).setSequence(4);
			inOrder.verify(existing).setAmount(amount);
			inOrder.verify(existing).setDescription(description);
			inOrder.verify(existing).setJarId(jarId);
			inOrder.verify(existing).setAccountId(accountId);
			inOrder.verify(existing).setCounterparty(counterparty);
			inOrder.verify(existing).setDirection(direction);
			inOrder.verify(storage).update(existing);
			inOrder.verify(jarEntryService).create(updated);
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

		private static final TestPaymentTransaction TRANSACTION = TestPaymentTransaction.RENT;

		@Test
		void success() {
			final long id = TRANSACTION.getId();

			final PaymentTransaction existing = mock();
			when(storage.get(id)).thenReturn(existing);

			service.delete(id);

			final InOrder inOrder = inOrder(storage, jarEntryService,
					accountEntryService);
			inOrder.verify(storage).get(id);
			inOrder.verify(jarEntryService).delete(existing);
			inOrder.verify(accountEntryService).delete(existing);
			inOrder.verify(storage).delete(existing);
		}
	}
}
