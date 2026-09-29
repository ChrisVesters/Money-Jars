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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import com.cvesters.moneyjars.common.exceptions.MissingEntityException;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionDirection;
import com.cvesters.moneyjars.transaction.bdo.Transaction;
import com.cvesters.moneyjars.transaction.bdo.TransactionAction;

class TransactionServiceTest {

	private final TransactionStorageGateway storage = mock();
	private final TransactionService service = new TransactionService(storage);

	@Nested
	class GetAll {

		@Test
		void success() {
			final PaymentTransaction expected = mock();
			when(storage.getAll()).thenReturn(List.of(expected));

			final var result = service.getAll();

			assertThat(result).containsExactly(expected);
		}
	}

	@Nested
	class Find {

		private static final long TRANSACTION_ID = 1L;

		@Test
		void success() {
			final Optional<Transaction> expected = Optional
					.of(mock(PaymentTransaction.class));
			when(storage.find(TRANSACTION_ID)).thenReturn(expected);

			final var result = service.find(TRANSACTION_ID);

			assertThat(result).isSameAs(expected);
		}
	}

	@Nested
	class Create {

		private static final TestPaymentTransaction TRANSACTION = TestPaymentTransaction.RENT;

		@Test
		void firstOnDate() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final PaymentTransactionDirection direction = TRANSACTION
					.getDirection();

			final var action = new TransactionAction.CreatePayment(date, amount,
					beneficiary, description, jarId, accountId, direction);

			when(storage.getAllInPeriod(date, date)).thenReturn(List.of());

			final PaymentTransaction created = mock();
			when(storage.create(argThat(transaction -> {
				assertThat(transaction).isInstanceOf(PaymentTransaction.class);
				final PaymentTransaction pt = (PaymentTransaction) transaction;
				assertThat(pt.getId()).isNull();
				assertThat(pt.getDate()).isEqualTo(date);
				assertThat(pt.getSequence()).isZero();
				assertThat(pt.getAmount()).isEqualTo(amount);
				assertThat(pt.getBeneficiary()).isEqualTo(beneficiary);
				assertThat(pt.getDescription()).isEqualTo(description);
				assertThat(pt.getJarId()).isEqualTo(jarId);
				assertThat(pt.getAccountId()).isEqualTo(accountId);
				assertThat(pt.getDirection()).isEqualTo(direction);
				return true;
			}))).thenReturn(created);

			final var result = service.create(action);

			assertThat(result).isSameAs(created);
		}

		@Test
		void existingOnDate() {
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final PaymentTransactionDirection direction = TRANSACTION
					.getDirection();

			final var action = new TransactionAction.CreatePayment(date, amount,
					beneficiary, description, jarId, accountId, direction);

			final PaymentTransaction existing = mock();
			when(existing.getSequence()).thenReturn(3);
			when(storage.getAllInPeriod(date, date))
					.thenReturn(List.of(existing));

			final PaymentTransaction created = mock();
			when(storage.create(argThat(transaction -> {
				assertThat(transaction).isInstanceOf(PaymentTransaction.class);
				final PaymentTransaction pt = (PaymentTransaction) transaction;
				assertThat(pt.getId()).isNull();
				assertThat(pt.getDate()).isEqualTo(date);
				assertThat(pt.getSequence()).isEqualTo(4);
				assertThat(pt.getAmount()).isEqualTo(amount);
				assertThat(pt.getBeneficiary()).isEqualTo(beneficiary);
				assertThat(pt.getDescription()).isEqualTo(description);
				assertThat(pt.getJarId()).isEqualTo(jarId);
				assertThat(pt.getAccountId()).isEqualTo(accountId);
				assertThat(pt.getDirection()).isEqualTo(direction);
				return true;
			}))).thenReturn(created);

			final var result = service.create(action);

			assertThat(result).isSameAs(created);
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
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final PaymentTransactionDirection direction = TRANSACTION
					.getDirection();

			final var action = new TransactionAction.UpdatePayment(date, amount,
					beneficiary, description, jarId, accountId, direction);

			final PaymentTransaction existing = mock();
			when(existing.getDate()).thenReturn(date);

			final PaymentTransaction updated = mock();
			when(storage.find(id)).thenReturn(Optional.of(existing));
			when(storage.update(existing)).thenReturn(updated);

			final var result = service.update(id, action);

			assertThat(result).isSameAs(updated);

			final InOrder inOrder = inOrder(storage, existing);
			inOrder.verify(storage).find(id);
			inOrder.verify(existing).setAmount(amount);
			inOrder.verify(existing).setBeneficiary(beneficiary);
			inOrder.verify(existing).setDescription(description);
			inOrder.verify(existing).setJarId(jarId);
			inOrder.verify(existing).setAccountId(accountId);
			inOrder.verify(existing).setDirection(direction);
			inOrder.verify(storage).update(existing);

			verify(existing, never()).setDate(any());
			verify(existing, never()).setSequence(anyInt());
		}

		@Test
		void newDateFirst() {
			final long id = TRANSACTION.getId();
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final PaymentTransactionDirection direction = TRANSACTION
					.getDirection();

			final var action = new TransactionAction.UpdatePayment(date, amount,
					beneficiary, description, jarId, accountId, direction);

			final PaymentTransaction existing = mock();
			final LocalDate oldDate = LocalDate.of(2025, 12, 7);
			when(existing.getDate()).thenReturn(oldDate);

			when(storage.getAllInPeriod(date, date))
					.thenReturn(Collections.emptyList());

			final PaymentTransaction updated = mock();
			when(storage.find(id)).thenReturn(Optional.of(existing));
			when(storage.update(existing)).thenReturn(updated);

			final var result = service.update(id, action);

			assertThat(result).isSameAs(updated);

			final InOrder inOrder = inOrder(storage, existing);
			inOrder.verify(storage).find(id);
			inOrder.verify(existing).setDate(date);
			inOrder.verify(existing).setSequence(0);
			inOrder.verify(existing).setAmount(amount);
			inOrder.verify(existing).setBeneficiary(beneficiary);
			inOrder.verify(existing).setDescription(description);
			inOrder.verify(existing).setJarId(jarId);
			inOrder.verify(existing).setAccountId(accountId);
			inOrder.verify(existing).setDirection(direction);
			inOrder.verify(storage).update(existing);
		}

		@Test
		void newDateExisting() {
			final long id = TRANSACTION.getId();
			final LocalDate date = TRANSACTION.getDate();
			final BigDecimal amount = TRANSACTION.getAmount();
			final String beneficiary = TRANSACTION.getBeneficiary();
			final String description = TRANSACTION.getDescription();
			final long jarId = TRANSACTION.getJar().getId();
			final long accountId = TRANSACTION.getAccount().getId();
			final PaymentTransactionDirection direction = TRANSACTION
					.getDirection();

			final var action = new TransactionAction.UpdatePayment(date, amount,
					beneficiary, description, jarId, accountId, direction);

			final PaymentTransaction existing = mock();
			final LocalDate oldDate = LocalDate.of(2025, 12, 7);
			when(existing.getDate()).thenReturn(oldDate);

			final PaymentTransaction other = mock();
			when(other.getSequence()).thenReturn(0);
			final List<Transaction> others = List.of(other);
			when(storage.getAllInPeriod(date, date)).thenReturn(others);

			final PaymentTransaction updated = mock();
			when(storage.find(id)).thenReturn(Optional.of(existing));
			when(storage.update(existing)).thenReturn(updated);

			final var result = service.update(id, action);

			assertThat(result).isSameAs(updated);

			final InOrder inOrder = inOrder(storage, existing);
			inOrder.verify(storage).find(id);
			inOrder.verify(existing).setDate(date);
			inOrder.verify(existing).setSequence(1);
			inOrder.verify(existing).setAmount(amount);
			inOrder.verify(existing).setBeneficiary(beneficiary);
			inOrder.verify(existing).setDescription(description);
			inOrder.verify(existing).setJarId(jarId);
			inOrder.verify(existing).setAccountId(accountId);
			inOrder.verify(existing).setDirection(direction);
			inOrder.verify(storage).update(existing);
		}

		@Test
		void actionNull() {
			final long id = TRANSACTION.getId();

			assertThatThrownBy(() -> service.update(id, null))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void missingEntity() {
			final long id = TRANSACTION.getId();

			final TransactionAction.UpdatePayment action = mock();
			when(storage.find(id)).thenReturn(Optional.empty());

			assertThatThrownBy(() -> service.update(id, action))
					.isInstanceOf(MissingEntityException.class);
		}
	}

	@Nested
	class Delete {

		private static final long TRANSACTION_ID = 1L;

		@Test
		void success() {
			service.delete(TRANSACTION_ID);

			verify(storage).delete(TRANSACTION_ID);
		}
	}
}
