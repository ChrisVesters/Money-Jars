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

import com.cvesters.moneyjars.jar.JarEntryService;
import com.cvesters.moneyjars.transaction.bdo.JarTransferTransaction;
import com.cvesters.moneyjars.transaction.bdo.JarTransferTransactionAction;

class JarTransferTransactionServiceTest {

	private final TransactionSequenceService sequenceService = mock();
	private final JarEntryService jarEntryService = mock();
	private final JarTransferTransactionStorageGateway storage = mock();

	private final JarTransferTransactionService service = new JarTransferTransactionService(
			sequenceService, jarEntryService, storage);

	@Nested
	class Create {

		private static final TestJarTransferTransaction TRANSACTION = TestJarTransferTransaction.CAR_SAVINGS;

		@Test
		void success() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = TRANSACTION.getDescription();
			final long sourceJarId = TRANSACTION.getSourceJar().getId();
			final long targetJarId = TRANSACTION.getTargetJar().getId();

			final var action = new JarTransferTransactionAction.Create(date,
					amount, description, sourceJarId, targetJarId);

			when(sequenceService.getNextSequence(date)).thenReturn(4);

			final JarTransferTransaction created = mock();
			when(storage.create(argThat(transaction -> {
				assertThat(transaction.getId()).isNull();
				assertThat(transaction.getDate()).isEqualTo(date);
				assertThat(transaction.getSequence()).isEqualTo(4);
				assertThat(transaction.getAmount()).isEqualTo(amount);
				assertThat(transaction.getDescription()).isEqualTo(description);
				assertThat(transaction.getSourceJarId()).isEqualTo(sourceJarId);
				assertThat(transaction.getTargetJarId()).isEqualTo(targetJarId);
				return true;
			}))).thenReturn(created);

			final var result = service.create(action);

			assertThat(result).isSameAs(created);
			verify(jarEntryService).create(created);
		}

		@Test
		void actionNull() {
			assertThatThrownBy(() -> service.create(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Update {

		private static final TestJarTransferTransaction TRANSACTION = TestJarTransferTransaction.CAR_SAVINGS;

		@Test
		void sameDate() {
			final long id = TRANSACTION.getId();
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String description = TRANSACTION.getDescription();
			final long sourceJarId = TRANSACTION.getSourceJar().getId();
			final long targetJarId = TRANSACTION.getTargetJar().getId();

			final var action = new JarTransferTransactionAction.Update(date,
					amount, description, sourceJarId, targetJarId);

			final JarTransferTransaction existing = mock();
			when(existing.getDate()).thenReturn(date);

			final JarTransferTransaction updated = mock();
			when(storage.get(id)).thenReturn(existing);
			when(storage.update(existing)).thenReturn(updated);

			final var result = service.update(id, action);

			assertThat(result).isSameAs(updated);

			final InOrder inOrder = inOrder(storage, jarEntryService, existing);
			inOrder.verify(storage).get(id);
			inOrder.verify(jarEntryService).delete(existing);
			inOrder.verify(existing).setAmount(amount);
			inOrder.verify(existing).setDescription(description);
			inOrder.verify(existing).setSourceJarId(sourceJarId);
			inOrder.verify(existing).setTargetJarId(targetJarId);
			inOrder.verify(storage).update(existing);
			inOrder.verify(jarEntryService).create(updated);

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
			final long sourceJarId = TRANSACTION.getSourceJar().getId();
			final long targetJarId = TRANSACTION.getTargetJar().getId();

			final var action = new JarTransferTransactionAction.Update(date,
					amount, description, sourceJarId, targetJarId);

			final JarTransferTransaction existing = mock();
			final LocalDate oldDate = LocalDate.of(2025, 12, 7);
			when(existing.getDate()).thenReturn(oldDate);

			when(sequenceService.getNextSequence(date)).thenReturn(4);

			final JarTransferTransaction updated = mock();
			when(storage.get(id)).thenReturn(existing);
			when(storage.update(existing)).thenReturn(updated);

			final var result = service.update(id, action);

			assertThat(result).isSameAs(updated);

			final InOrder inOrder = inOrder(storage, jarEntryService, existing);
			inOrder.verify(storage).get(id);
			inOrder.verify(jarEntryService).delete(existing);
			inOrder.verify(existing).setDate(date);
			inOrder.verify(existing).setSequence(4);
			inOrder.verify(existing).setAmount(amount);
			inOrder.verify(existing).setDescription(description);
			inOrder.verify(existing).setSourceJarId(sourceJarId);
			inOrder.verify(existing).setTargetJarId(targetJarId);
			inOrder.verify(storage).update(existing);
			inOrder.verify(jarEntryService).create(updated);
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

		private static final TestJarTransferTransaction TRANSACTION = TestJarTransferTransaction.CAR_SAVINGS;

		@Test
		void success() {
			final long id = TRANSACTION.getId();

			final JarTransferTransaction existing = mock();
			when(storage.get(id)).thenReturn(existing);

			service.delete(id);

			final InOrder inOrder = inOrder(storage, jarEntryService);
			inOrder.verify(storage).get(id);
			inOrder.verify(jarEntryService).delete(existing);
			inOrder.verify(storage).delete(existing);
		}
	}
}
