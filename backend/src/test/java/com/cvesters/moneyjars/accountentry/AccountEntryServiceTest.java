package com.cvesters.moneyjars.accountentry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import com.cvesters.moneyjars.accountentry.bdo.AccountEntry;
import com.cvesters.moneyjars.common.exceptions.MissingEntityException;
import com.cvesters.moneyjars.transaction.TestPaymentTransaction;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;

class AccountEntryServiceTest {

	private final AccountEntryStorageGateway gateway = mock();
	private final AccountEntryService service = new AccountEntryService(
			gateway);

	@Nested
	class FindLast {

		@Test
		void found() {
			final long accountId = 1L;
			final AccountEntry entry = mock();

			when(gateway.findLast(accountId)).thenReturn(Optional.of(entry));

			final Optional<AccountEntry> result = service.findLast(accountId);

			assertThat(result).contains(entry);
		}

		@Test
		void notFound() {
			final long accountId = 1L;

			when(gateway.findLast(accountId)).thenReturn(Optional.empty());

			final Optional<AccountEntry> result = service.findLast(accountId);

			assertThat(result).isEmpty();
		}
	}

	@Nested
	class Create {

		private static final TestPaymentTransaction TRANSACTION = TestPaymentTransaction.SALARY;

		@Test
		void firstWithoutNext() {
			final PaymentTransaction transaction = TRANSACTION.bdo();
			final long accountId = TRANSACTION.getAccount().getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();

			when(gateway.findBefore(accountId, date, sequence))
					.thenReturn(Optional.empty());
			when(gateway.getAllAfter(accountId, date, sequence))
					.thenReturn(Collections.emptyList());

			service.create(transaction);

			final InOrder inOrder = inOrder(gateway);
			inOrder.verify(gateway).create(argThat(entry -> {
				assertThat(entry.getId()).isNull();
				assertThat(entry.getTransactionId())
						.isEqualTo(TRANSACTION.getId());
				assertThat(entry.getAccountId()).isEqualTo(accountId);
				assertThat(entry.getBalanceBefore())
						.isEqualByComparingTo(BigDecimal.ZERO);
				assertThat(entry.getBalanceAfter())
						.isEqualByComparingTo(TRANSACTION.getAmount());
				return true;
			}));
			inOrder.verify(gateway).updateAll(Collections.emptyList());
		}

		@Test
		void firstWithNext() {
			final PaymentTransaction transaction = TRANSACTION.bdo();
			final long accountId = TRANSACTION.getAccount().getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();

			final AccountEntry next1 = mock();
			final AccountEntry next2 = mock();
			final List<AccountEntry> nextEntries = List.of(next1, next2);

			when(gateway.findBefore(accountId, date, sequence))
					.thenReturn(Optional.empty());
			when(gateway.getAllAfter(accountId, date, sequence))
					.thenReturn(nextEntries);

			service.create(transaction);

			final InOrder inOrder = inOrder(gateway, next1, next2);
			inOrder.verify(next1).shift(TRANSACTION.getAmount());
			inOrder.verify(next2).shift(TRANSACTION.getAmount());
			inOrder.verify(gateway).create(argThat(entry -> {
				assertThat(entry.getId()).isNull();
				assertThat(entry.getTransactionId())
						.isEqualTo(TRANSACTION.getId());
				assertThat(entry.getAccountId()).isEqualTo(accountId);
				assertThat(entry.getBalanceBefore())
						.isEqualByComparingTo(BigDecimal.ZERO);
				assertThat(entry.getBalanceAfter())
						.isEqualByComparingTo(TRANSACTION.getAmount());
				return true;
			}));
			inOrder.verify(gateway).updateAll(nextEntries);
		}

		@Test
		void withPreviousWithoutNext() {
			final PaymentTransaction transaction = TRANSACTION.bdo();
			final long accountId = TRANSACTION.getAccount().getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();

			final AccountEntry previous = mock();
			final AccountEntry entry = mock();
			when(previous.next(transaction)).thenReturn(entry);

			when(gateway.findBefore(accountId, date, sequence))
					.thenReturn(Optional.of(previous));
			when(gateway.getAllAfter(accountId, date, sequence))
					.thenReturn(Collections.emptyList());

			service.create(transaction);

			final InOrder inOrder = inOrder(gateway);
			inOrder.verify(gateway).create(entry);
			inOrder.verify(gateway).updateAll(Collections.emptyList());
		}

		@Test
		void withPreviousWithNext() {
			final PaymentTransaction transaction = TRANSACTION.bdo();
			final long accountId = TRANSACTION.getAccount().getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();

			final AccountEntry previous = mock();
			final AccountEntry entry = mock();
			when(previous.next(transaction)).thenReturn(entry);

			final AccountEntry next1 = mock();
			final AccountEntry next2 = mock();
			final List<AccountEntry> nextEntries = List.of(next1, next2);

			when(gateway.findBefore(accountId, date, sequence))
					.thenReturn(Optional.of(previous));
			when(gateway.getAllAfter(accountId, date, sequence))
					.thenReturn(nextEntries);

			service.create(transaction);

			final InOrder inOrder = inOrder(gateway, next1, next2);
			inOrder.verify(next1).shift(TRANSACTION.getAmount());
			inOrder.verify(next2).shift(TRANSACTION.getAmount());
			inOrder.verify(gateway).create(entry);
			inOrder.verify(gateway).updateAll(nextEntries);

			verify(entry, never()).shift(any());
		}

		@Test
		void negativeAmount() {
			final var testTransaction = TestPaymentTransaction.GROCERY;
			final PaymentTransaction transaction = testTransaction.bdo();
			final long accountId = testTransaction.getAccount().getId();
			final LocalDate date = testTransaction.getDate();
			final int sequence = testTransaction.getSequence();
			final BigDecimal amount = testTransaction.getAmount().negate();

			final AccountEntry previous = mock();
			final AccountEntry entry = mock();
			when(previous.next(transaction)).thenReturn(entry);

			final AccountEntry next1 = mock();
			final AccountEntry next2 = mock();
			final List<AccountEntry> nextEntries = List.of(next1, next2);

			when(gateway.findBefore(accountId, date, sequence))
					.thenReturn(Optional.of(previous));
			when(gateway.getAllAfter(accountId, date, sequence))
					.thenReturn(nextEntries);

			service.create(transaction);

			final InOrder inOrder = inOrder(gateway, next1, next2);
			inOrder.verify(next1).shift(amount);
			inOrder.verify(next2).shift(amount);
			inOrder.verify(gateway).create(entry);
			inOrder.verify(gateway).updateAll(nextEntries);

			verify(entry, never()).shift(any());
		}

		@Test
		void transactionNull() {
			assertThatThrownBy(() -> service.create(null))
					.isInstanceOf(NullPointerException.class);

			verifyNoInteractions(gateway);
		}
	}

	@Nested
	class Delete {

		private static final TestPaymentTransaction TRANSACTION = TestPaymentTransaction.SALARY;

		@Test
		void withoutNext() {
			final PaymentTransaction transaction = TRANSACTION.bdo();
			final long accountId = TRANSACTION.getAccount().getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();

			final AccountEntry entry = mock();

			when(gateway.findByAccountIdAndTransactionId(accountId,
					TRANSACTION.getId())).thenReturn(Optional.of(entry));
			when(gateway.getAllAfter(accountId, date, sequence))
					.thenReturn(Collections.emptyList());

			service.delete(transaction);

			final InOrder inOrder = inOrder(gateway);
			inOrder.verify(gateway).delete(entry);
			inOrder.verify(gateway).updateAll(Collections.emptyList());
		}

		@Test
		void withNext() {
			final PaymentTransaction transaction = TRANSACTION.bdo();
			final long accountId = TRANSACTION.getAccount().getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount().negate();

			final AccountEntry entry = mock();

			final AccountEntry next1 = mock();
			final AccountEntry next2 = mock();
			final List<AccountEntry> nextEntries = List.of(next1, next2);

			when(gateway.findByAccountIdAndTransactionId(accountId,
					TRANSACTION.getId())).thenReturn(Optional.of(entry));
			when(gateway.getAllAfter(accountId, date, sequence))
					.thenReturn(nextEntries);

			service.delete(transaction);

			final InOrder inOrder = inOrder(gateway, next1, next2);
			inOrder.verify(next1).shift(amount);
			inOrder.verify(next2).shift(amount);
			inOrder.verify(gateway).delete(entry);
			inOrder.verify(gateway).updateAll(nextEntries);

			verify(entry, never()).shift(any());
		}

		@Test
		void negativeAmount() {
			final var testTransaction = TestPaymentTransaction.GROCERY;
			final PaymentTransaction transaction = testTransaction.bdo();
			final long accountId = testTransaction.getAccount().getId();
			final LocalDate date = testTransaction.getDate();
			final int sequence = testTransaction.getSequence();
			final BigDecimal amount = testTransaction.getAmount();

			final AccountEntry entry = mock();

			final AccountEntry next1 = mock();
			final AccountEntry next2 = mock();
			final List<AccountEntry> nextEntries = List.of(next1, next2);

			when(gateway.findByAccountIdAndTransactionId(accountId,
					testTransaction.getId())).thenReturn(Optional.of(entry));
			when(gateway.getAllAfter(accountId, date, sequence))
					.thenReturn(nextEntries);

			service.delete(transaction);

			final InOrder inOrder = inOrder(gateway, next1, next2);
			inOrder.verify(next1).shift(amount);
			inOrder.verify(next2).shift(amount);
			inOrder.verify(gateway).delete(entry);
			inOrder.verify(gateway).updateAll(nextEntries);

			verify(entry, never()).shift(any());
		}

		@Test
		void notFound() {
			final PaymentTransaction transaction = TRANSACTION.bdo();
			final long accountId = TRANSACTION.getAccount().getId();

			when(gateway.findByAccountIdAndTransactionId(accountId,
					TRANSACTION.getId())).thenReturn(Optional.empty());

			assertThatThrownBy(() -> service.delete(transaction))
					.isInstanceOf(MissingEntityException.class);

			verify(gateway, never()).delete(any());
			verify(gateway, never()).updateAll(any());
		}

		@Test
		void transactionNull() {
			assertThatThrownBy(() -> service.delete(null))
					.isInstanceOf(NullPointerException.class);

			verifyNoInteractions(gateway);
		}
	}
}
