package com.cvesters.moneyjars.account;

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

import com.cvesters.moneyjars.account.bdo.AccountEntry;
import com.cvesters.moneyjars.transaction.TestAccountTransferTransaction;
import com.cvesters.moneyjars.transaction.TestPaymentTransaction;
import com.cvesters.moneyjars.transaction.bdo.AccountTransferTransaction;
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
	class GetAll {

		@Test
		void found() {
			final long accountId = 1L;
			final AccountEntry entry1 = mock();
			final AccountEntry entry2 = mock();

			when(gateway.getAll(accountId)).thenReturn(List.of(entry1, entry2));

			final List<AccountEntry> result = service.getAll(accountId);

			assertThat(result).containsExactly(entry1, entry2);
		}

		@Test
		void empty() {
			final long accountId = 1L;

			when(gateway.getAll(accountId)).thenReturn(Collections.emptyList());

			final List<AccountEntry> result = service.getAll(accountId);

			assertThat(result).isEmpty();
		}
	}

	@Nested
	class CreatePaymentTransaction {

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
			final PaymentTransaction transaction = null;
			assertThatThrownBy(() -> service.create(transaction))
					.isInstanceOf(NullPointerException.class);

			verifyNoInteractions(gateway);
		}
	}

	@Nested
	class CreateAccountTransferTransaction {

		private static final TestAccountTransferTransaction TRANSACTION = TestAccountTransferTransaction.CASH_WITHDRAWAL;

		@Test
		void firstWithoutNext() {
			final AccountTransferTransaction transaction = TRANSACTION.bdo();
			final long sourceAccountId = TRANSACTION.getSourceAccount().getId();
			final long targetAccountId = TRANSACTION.getTargetAccount().getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount();

			when(gateway.findBefore(sourceAccountId, date, sequence))
					.thenReturn(Optional.empty());
			when(gateway.getAllAfter(sourceAccountId, date, sequence))
					.thenReturn(Collections.emptyList());
			when(gateway.findBefore(targetAccountId, date, sequence))
					.thenReturn(Optional.empty());
			when(gateway.getAllAfter(targetAccountId, date, sequence))
					.thenReturn(Collections.emptyList());

			service.create(transaction);

			final InOrder inOrder = inOrder(gateway);
			inOrder.verify(gateway).create(argThat(entry -> {
				if (entry.getAccountId() != sourceAccountId) {
					return false;
				}
				assertThat(entry.getId()).isNull();
				assertThat(entry.getTransactionId())
						.isEqualTo(TRANSACTION.getId());
				assertThat(entry.getBalanceBefore())
						.isEqualByComparingTo(BigDecimal.ZERO);
				assertThat(entry.getBalanceAfter())
						.isEqualByComparingTo(amount.negate());
				return true;
			}));
			inOrder.verify(gateway).updateAll(Collections.emptyList());
			inOrder.verify(gateway).create(argThat(entry -> {
				if (entry.getAccountId() != targetAccountId) {
					return false;
				}
				assertThat(entry.getId()).isNull();
				assertThat(entry.getTransactionId())
						.isEqualTo(TRANSACTION.getId());
				assertThat(entry.getBalanceBefore())
						.isEqualByComparingTo(BigDecimal.ZERO);
				assertThat(entry.getBalanceAfter())
						.isEqualByComparingTo(amount);
				return true;
			}));
			inOrder.verify(gateway).updateAll(Collections.emptyList());
		}

		@Test
		void firstWithNext() {
			final AccountTransferTransaction transaction = TRANSACTION.bdo();
			final long sourceAccountId = TRANSACTION.getSourceAccount().getId();
			final long targetAccountId = TRANSACTION.getTargetAccount().getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount();

			final AccountEntry sourceNext1 = mock();
			final AccountEntry sourceNext2 = mock();
			final List<AccountEntry> sourceNextEntries = List.of(sourceNext1,
					sourceNext2);
			final AccountEntry targetNext1 = mock();
			final AccountEntry targetNext2 = mock();
			final List<AccountEntry> targetNextEntries = List.of(targetNext1,
					targetNext2);

			when(gateway.findBefore(sourceAccountId, date, sequence))
					.thenReturn(Optional.empty());
			when(gateway.getAllAfter(sourceAccountId, date, sequence))
					.thenReturn(sourceNextEntries);
			when(gateway.findBefore(targetAccountId, date, sequence))
					.thenReturn(Optional.empty());
			when(gateway.getAllAfter(targetAccountId, date, sequence))
					.thenReturn(targetNextEntries);

			service.create(transaction);

			final InOrder inOrder = inOrder(gateway, sourceNext1, sourceNext2,
					targetNext1, targetNext2);
			inOrder.verify(sourceNext1).shift(amount.negate());
			inOrder.verify(sourceNext2).shift(amount.negate());
			inOrder.verify(gateway).create(argThat(entry -> {
				if (entry.getAccountId() != sourceAccountId) {
					return false;
				}
				assertThat(entry.getId()).isNull();
				assertThat(entry.getTransactionId())
						.isEqualTo(TRANSACTION.getId());
				assertThat(entry.getBalanceBefore())
						.isEqualByComparingTo(BigDecimal.ZERO);
				assertThat(entry.getBalanceAfter())
						.isEqualByComparingTo(amount.negate());
				return true;
			}));
			inOrder.verify(gateway).updateAll(sourceNextEntries);
			inOrder.verify(targetNext1).shift(amount);
			inOrder.verify(targetNext2).shift(amount);
			inOrder.verify(gateway).create(argThat(entry -> {
				if (entry.getAccountId() != targetAccountId) {
					return false;
				}
				assertThat(entry.getId()).isNull();
				assertThat(entry.getTransactionId())
						.isEqualTo(TRANSACTION.getId());
				assertThat(entry.getBalanceBefore())
						.isEqualByComparingTo(BigDecimal.ZERO);
				assertThat(entry.getBalanceAfter())
						.isEqualByComparingTo(amount);
				return true;
			}));
			inOrder.verify(gateway).updateAll(targetNextEntries);
		}

		@Test
		void withPreviousWithoutNext() {
			final AccountTransferTransaction transaction = TRANSACTION.bdo();
			final long sourceAccountId = TRANSACTION.getSourceAccount().getId();
			final long targetAccountId = TRANSACTION.getTargetAccount().getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();

			final AccountEntry sourcePrevious = mock();
			final AccountEntry sourceEntry = mock();
			when(sourcePrevious.next(transaction)).thenReturn(sourceEntry);
			final AccountEntry targetPrevious = mock();
			final AccountEntry targetEntry = mock();
			when(targetPrevious.next(transaction)).thenReturn(targetEntry);

			when(gateway.findBefore(sourceAccountId, date, sequence))
					.thenReturn(Optional.of(sourcePrevious));
			when(gateway.getAllAfter(sourceAccountId, date, sequence))
					.thenReturn(Collections.emptyList());
			when(gateway.findBefore(targetAccountId, date, sequence))
					.thenReturn(Optional.of(targetPrevious));
			when(gateway.getAllAfter(targetAccountId, date, sequence))
					.thenReturn(Collections.emptyList());

			service.create(transaction);

			final InOrder inOrder = inOrder(gateway);
			inOrder.verify(gateway).create(sourceEntry);
			inOrder.verify(gateway).updateAll(Collections.emptyList());
			inOrder.verify(gateway).create(targetEntry);
			inOrder.verify(gateway).updateAll(Collections.emptyList());
		}

		@Test
		void withPreviousWithNext() {
			final AccountTransferTransaction transaction = TRANSACTION.bdo();
			final long sourceAccountId = TRANSACTION.getSourceAccount().getId();
			final long targetAccountId = TRANSACTION.getTargetAccount().getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount();

			final AccountEntry sourcePrevious = mock();
			final AccountEntry sourceEntry = mock();
			when(sourcePrevious.next(transaction)).thenReturn(sourceEntry);
			final AccountEntry targetPrevious = mock();
			final AccountEntry targetEntry = mock();
			when(targetPrevious.next(transaction)).thenReturn(targetEntry);

			final AccountEntry sourceNext1 = mock();
			final AccountEntry sourceNext2 = mock();
			final List<AccountEntry> sourceNextEntries = List.of(sourceNext1,
					sourceNext2);
			final AccountEntry targetNext1 = mock();
			final AccountEntry targetNext2 = mock();
			final List<AccountEntry> targetNextEntries = List.of(targetNext1,
					targetNext2);

			when(gateway.findBefore(sourceAccountId, date, sequence))
					.thenReturn(Optional.of(sourcePrevious));
			when(gateway.getAllAfter(sourceAccountId, date, sequence))
					.thenReturn(sourceNextEntries);
			when(gateway.findBefore(targetAccountId, date, sequence))
					.thenReturn(Optional.of(targetPrevious));
			when(gateway.getAllAfter(targetAccountId, date, sequence))
					.thenReturn(targetNextEntries);

			service.create(transaction);

			final InOrder inOrder = inOrder(gateway, sourceNext1, sourceNext2,
					targetNext1, targetNext2);
			inOrder.verify(sourceNext1).shift(amount.negate());
			inOrder.verify(sourceNext2).shift(amount.negate());
			inOrder.verify(gateway).create(sourceEntry);
			inOrder.verify(gateway).updateAll(sourceNextEntries);
			inOrder.verify(targetNext1).shift(amount);
			inOrder.verify(targetNext2).shift(amount);
			inOrder.verify(gateway).create(targetEntry);
			inOrder.verify(gateway).updateAll(targetNextEntries);

			verify(sourceEntry, never()).shift(any());
			verify(targetEntry, never()).shift(any());
		}

		@Test
		void sourceWithPreviousTargetFirst() {
			final AccountTransferTransaction transaction = TRANSACTION.bdo();
			final long sourceAccountId = TRANSACTION.getSourceAccount().getId();
			final long targetAccountId = TRANSACTION.getTargetAccount().getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount();

			final AccountEntry sourcePrevious = mock();
			final AccountEntry sourceEntry = mock();
			when(sourcePrevious.next(transaction)).thenReturn(sourceEntry);

			when(gateway.findBefore(sourceAccountId, date, sequence))
					.thenReturn(Optional.of(sourcePrevious));
			when(gateway.getAllAfter(sourceAccountId, date, sequence))
					.thenReturn(Collections.emptyList());
			when(gateway.findBefore(targetAccountId, date, sequence))
					.thenReturn(Optional.empty());
			when(gateway.getAllAfter(targetAccountId, date, sequence))
					.thenReturn(Collections.emptyList());

			service.create(transaction);

			final InOrder inOrder = inOrder(gateway);
			inOrder.verify(gateway).create(sourceEntry);
			inOrder.verify(gateway).updateAll(Collections.emptyList());
			inOrder.verify(gateway).create(argThat(entry -> {
				if (entry.getAccountId() != targetAccountId) {
					return false;
				}
				assertThat(entry.getId()).isNull();
				assertThat(entry.getTransactionId())
						.isEqualTo(TRANSACTION.getId());
				assertThat(entry.getBalanceBefore())
						.isEqualByComparingTo(BigDecimal.ZERO);
				assertThat(entry.getBalanceAfter())
						.isEqualByComparingTo(amount);
				return true;
			}));
			inOrder.verify(gateway).updateAll(Collections.emptyList());
		}

		@Test
		void transactionNull() {
			final AccountTransferTransaction transaction = null;

			assertThatThrownBy(() -> service.create(transaction))
					.isInstanceOf(NullPointerException.class);

			verifyNoInteractions(gateway);
		}
	}

	@Nested
	class DeletePaymentTransaction {

		private static final TestPaymentTransaction TRANSACTION = TestPaymentTransaction.SALARY;

		@Test
		void withoutNext() {
			final PaymentTransaction transaction = TRANSACTION.bdo();
			final long accountId = TRANSACTION.getAccount().getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();

			final AccountEntry entry = mock();
			when(entry.getAmount()).thenReturn(TRANSACTION.getAmount());

			when(gateway.getByAccountIdAndTransactionId(accountId,
					TRANSACTION.getId())).thenReturn(entry);
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
			when(entry.getAmount()).thenReturn(TRANSACTION.getAmount());

			final AccountEntry next1 = mock();
			final AccountEntry next2 = mock();
			final List<AccountEntry> nextEntries = List.of(next1, next2);

			when(gateway.getByAccountIdAndTransactionId(accountId,
					TRANSACTION.getId())).thenReturn(entry);
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
		void outgoing() {
			final var testTransaction = TestPaymentTransaction.GROCERY;
			final PaymentTransaction transaction = testTransaction.bdo();
			final long accountId = testTransaction.getAccount().getId();
			final LocalDate date = testTransaction.getDate();
			final int sequence = testTransaction.getSequence();
			final BigDecimal amount = testTransaction.getAmount();

			final AccountEntry entry = mock();
			when(entry.getAmount())
					.thenReturn(testTransaction.getAmount().negate());

			final AccountEntry next1 = mock();
			final AccountEntry next2 = mock();
			final List<AccountEntry> nextEntries = List.of(next1, next2);

			when(gateway.getByAccountIdAndTransactionId(accountId,
					testTransaction.getId())).thenReturn(entry);
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
		void transactionNull() {
			final PaymentTransaction transaction = null;
			assertThatThrownBy(() -> service.delete(transaction))
					.isInstanceOf(NullPointerException.class);

			verifyNoInteractions(gateway);
		}
	}

	@Nested
	class DeleteAccountTransferTransaction {

		private static final TestAccountTransferTransaction TRANSACTION = TestAccountTransferTransaction.CASH_WITHDRAWAL;

		@Test
		void withoutNext() {
			final AccountTransferTransaction transaction = TRANSACTION.bdo();
			final long sourceAccountId = TRANSACTION.getSourceAccount().getId();
			final long targetAccountId = TRANSACTION.getTargetAccount().getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();

			final AccountEntry sourceEntry = mock();
			when(sourceEntry.getAmount())
					.thenReturn(TRANSACTION.getAmount().negate());
			final AccountEntry targetEntry = mock();
			when(targetEntry.getAmount()).thenReturn(TRANSACTION.getAmount());

			when(gateway.getByAccountIdAndTransactionId(sourceAccountId,
					TRANSACTION.getId())).thenReturn(sourceEntry);
			when(gateway.getAllAfter(sourceAccountId, date, sequence))
					.thenReturn(Collections.emptyList());
			when(gateway.getByAccountIdAndTransactionId(targetAccountId,
					TRANSACTION.getId())).thenReturn(targetEntry);
			when(gateway.getAllAfter(targetAccountId, date, sequence))
					.thenReturn(Collections.emptyList());

			service.delete(transaction);

			final InOrder inOrder = inOrder(gateway);
			inOrder.verify(gateway).delete(sourceEntry);
			inOrder.verify(gateway).updateAll(Collections.emptyList());
			inOrder.verify(gateway).delete(targetEntry);
			inOrder.verify(gateway).updateAll(Collections.emptyList());
		}

		@Test
		void withNext() {
			final AccountTransferTransaction transaction = TRANSACTION.bdo();
			final long sourceAccountId = TRANSACTION.getSourceAccount().getId();
			final long targetAccountId = TRANSACTION.getTargetAccount().getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount();

			final AccountEntry sourceEntry = mock();
			when(sourceEntry.getAmount())
					.thenReturn(TRANSACTION.getAmount().negate());
			final AccountEntry targetEntry = mock();
			when(targetEntry.getAmount()).thenReturn(TRANSACTION.getAmount());

			final AccountEntry sourceNext1 = mock();
			final AccountEntry sourceNext2 = mock();
			final List<AccountEntry> sourceNextEntries = List.of(sourceNext1,
					sourceNext2);
			final AccountEntry targetNext1 = mock();
			final AccountEntry targetNext2 = mock();
			final List<AccountEntry> targetNextEntries = List.of(targetNext1,
					targetNext2);

			when(gateway.getByAccountIdAndTransactionId(sourceAccountId,
					TRANSACTION.getId())).thenReturn(sourceEntry);
			when(gateway.getAllAfter(sourceAccountId, date, sequence))
					.thenReturn(sourceNextEntries);
			when(gateway.getByAccountIdAndTransactionId(targetAccountId,
					TRANSACTION.getId())).thenReturn(targetEntry);
			when(gateway.getAllAfter(targetAccountId, date, sequence))
					.thenReturn(targetNextEntries);

			service.delete(transaction);

			final InOrder inOrder = inOrder(gateway, sourceNext1, sourceNext2,
					targetNext1, targetNext2);
			inOrder.verify(sourceNext1).shift(amount);
			inOrder.verify(sourceNext2).shift(amount);
			inOrder.verify(gateway).delete(sourceEntry);
			inOrder.verify(gateway).updateAll(sourceNextEntries);
			inOrder.verify(targetNext1).shift(amount.negate());
			inOrder.verify(targetNext2).shift(amount.negate());
			inOrder.verify(gateway).delete(targetEntry);
			inOrder.verify(gateway).updateAll(targetNextEntries);

			verify(sourceEntry, never()).shift(any());
			verify(targetEntry, never()).shift(any());
		}

		@Test
		void transactionNull() {
			final AccountTransferTransaction transaction = null;
			assertThatThrownBy(() -> service.delete(transaction))
					.isInstanceOf(NullPointerException.class);

			verifyNoInteractions(gateway);
		}
	}
}
