package com.cvesters.moneyjars.jar;

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

import com.cvesters.moneyjars.jar.bdo.JarEntry;
import com.cvesters.moneyjars.transaction.TestJarTransferTransaction;
import com.cvesters.moneyjars.transaction.TestPaymentTransaction;
import com.cvesters.moneyjars.transaction.bdo.JarTransferTransaction;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;

class JarEntryServiceTest {

	private final JarEntryStorageGateway gateway = mock();
	private final JarEntryService service = new JarEntryService(gateway);

	@Nested
	class FindLast {

		@Test
		void found() {
			final long jarId = 1L;
			final JarEntry entry = mock();

			when(gateway.findLast(jarId)).thenReturn(Optional.of(entry));

			final Optional<JarEntry> result = service.findLast(jarId);

			assertThat(result).contains(entry);
		}

		@Test
		void notFound() {
			final long jarId = 1L;

			when(gateway.findLast(jarId)).thenReturn(Optional.empty());

			final Optional<JarEntry> result = service.findLast(jarId);

			assertThat(result).isEmpty();
		}
	}

	@Nested
	class GetAll {

		@Test
		void found() {
			final long jarId = 1L;
			final JarEntry entry1 = mock();
			final JarEntry entry2 = mock();

			when(gateway.getAll(jarId)).thenReturn(List.of(entry1, entry2));

			final List<JarEntry> result = service.getAll(jarId);

			assertThat(result).containsExactly(entry1, entry2);
		}

		@Test
		void empty() {
			final long jarId = 1L;

			when(gateway.getAll(jarId)).thenReturn(Collections.emptyList());

			final List<JarEntry> result = service.getAll(jarId);

			assertThat(result).isEmpty();
		}
	}

	@Nested
	class CreatePaymentTransaction {

		private static final TestPaymentTransaction TRANSACTION = TestPaymentTransaction.SALARY;

		@Test
		void firstWithoutNext() {
			final PaymentTransaction transaction = TRANSACTION.bdo();
			final long jarId = TRANSACTION.getJar().getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();

			when(gateway.findBefore(jarId, date, sequence))
					.thenReturn(Optional.empty());
			when(gateway.getAllAfter(jarId, date, sequence))
					.thenReturn(Collections.emptyList());

			service.create(transaction);

			final InOrder inOrder = inOrder(gateway);
			inOrder.verify(gateway).create(argThat(entry -> {
				assertThat(entry.getId()).isNull();
				assertThat(entry.getTransactionId())
						.isEqualTo(TRANSACTION.getId());
				assertThat(entry.getJarId()).isEqualTo(jarId);
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
			final long jarId = TRANSACTION.getJar().getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();

			final JarEntry next1 = mock();
			final JarEntry next2 = mock();
			final List<JarEntry> nextEntries = List.of(next1, next2);

			when(gateway.findBefore(jarId, date, sequence))
					.thenReturn(Optional.empty());
			when(gateway.getAllAfter(jarId, date, sequence))
					.thenReturn(nextEntries);

			service.create(transaction);

			final InOrder inOrder = inOrder(gateway, next1, next2);
			inOrder.verify(next1).shift(TRANSACTION.getAmount());
			inOrder.verify(next2).shift(TRANSACTION.getAmount());
			inOrder.verify(gateway).create(argThat(entry -> {
				assertThat(entry.getId()).isNull();
				assertThat(entry.getTransactionId())
						.isEqualTo(TRANSACTION.getId());
				assertThat(entry.getJarId()).isEqualTo(jarId);
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
			final long jarId = TRANSACTION.getJar().getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();

			final JarEntry previous = mock();
			final JarEntry entry = mock();
			when(previous.next(transaction)).thenReturn(entry);

			when(gateway.findBefore(jarId, date, sequence))
					.thenReturn(Optional.of(previous));
			when(gateway.getAllAfter(jarId, date, sequence))
					.thenReturn(Collections.emptyList());

			service.create(transaction);

			final InOrder inOrder = inOrder(gateway);
			inOrder.verify(gateway).create(entry);
			inOrder.verify(gateway).updateAll(Collections.emptyList());
		}

		@Test
		void withPreviousWithNext() {
			final PaymentTransaction transaction = TRANSACTION.bdo();
			final long jarId = TRANSACTION.getJar().getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();

			final JarEntry previous = mock();
			final JarEntry entry = mock();
			when(previous.next(transaction)).thenReturn(entry);

			final JarEntry next1 = mock();
			final JarEntry next2 = mock();
			final List<JarEntry> nextEntries = List.of(next1, next2);

			when(gateway.findBefore(jarId, date, sequence))
					.thenReturn(Optional.of(previous));
			when(gateway.getAllAfter(jarId, date, sequence))
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
			final long jarId = testTransaction.getJar().getId();
			final LocalDate date = testTransaction.getDate();
			final int sequence = testTransaction.getSequence();
			final BigDecimal amount = testTransaction.getAmount().negate();

			final JarEntry previous = mock();
			final JarEntry entry = mock();
			when(previous.next(transaction)).thenReturn(entry);

			final JarEntry next1 = mock();
			final JarEntry next2 = mock();
			final List<JarEntry> nextEntries = List.of(next1, next2);

			when(gateway.findBefore(jarId, date, sequence))
					.thenReturn(Optional.of(previous));
			when(gateway.getAllAfter(jarId, date, sequence))
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
	class CreateJarTransferTransaction {

		private static final TestJarTransferTransaction TRANSACTION = TestJarTransferTransaction.CAR_SAVINGS;

		@Test
		void firstWithoutNext() {
			final JarTransferTransaction transaction = TRANSACTION.bdo();
			final long sourceJarId = TRANSACTION.getSourceJar().getId();
			final long targetJarId = TRANSACTION.getTargetJar().getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount();

			when(gateway.findBefore(sourceJarId, date, sequence))
					.thenReturn(Optional.empty());
			when(gateway.getAllAfter(sourceJarId, date, sequence))
					.thenReturn(Collections.emptyList());
			when(gateway.findBefore(targetJarId, date, sequence))
					.thenReturn(Optional.empty());
			when(gateway.getAllAfter(targetJarId, date, sequence))
					.thenReturn(Collections.emptyList());

			service.create(transaction);

			final InOrder inOrder = inOrder(gateway);
			inOrder.verify(gateway).create(argThat(entry -> {
				if (entry.getJarId() != sourceJarId) {
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
				if (entry.getJarId() != targetJarId) {
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
			final JarTransferTransaction transaction = TRANSACTION.bdo();
			final long sourceJarId = TRANSACTION.getSourceJar().getId();
			final long targetJarId = TRANSACTION.getTargetJar().getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount();

			final JarEntry sourceNext1 = mock();
			final JarEntry sourceNext2 = mock();
			final List<JarEntry> sourceNextEntries = List.of(sourceNext1,
					sourceNext2);
			final JarEntry targetNext1 = mock();
			final JarEntry targetNext2 = mock();
			final List<JarEntry> targetNextEntries = List.of(targetNext1,
					targetNext2);

			when(gateway.findBefore(sourceJarId, date, sequence))
					.thenReturn(Optional.empty());
			when(gateway.getAllAfter(sourceJarId, date, sequence))
					.thenReturn(sourceNextEntries);
			when(gateway.findBefore(targetJarId, date, sequence))
					.thenReturn(Optional.empty());
			when(gateway.getAllAfter(targetJarId, date, sequence))
					.thenReturn(targetNextEntries);

			service.create(transaction);

			final InOrder inOrder = inOrder(gateway, sourceNext1, sourceNext2,
					targetNext1, targetNext2);
			inOrder.verify(sourceNext1).shift(amount.negate());
			inOrder.verify(sourceNext2).shift(amount.negate());
			inOrder.verify(gateway).create(argThat(entry -> {
				if (entry.getJarId() != sourceJarId) {
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
				if (entry.getJarId() != targetJarId) {
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
			final JarTransferTransaction transaction = TRANSACTION.bdo();
			final long sourceJarId = TRANSACTION.getSourceJar().getId();
			final long targetJarId = TRANSACTION.getTargetJar().getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();

			final JarEntry sourcePrevious = mock();
			final JarEntry sourceEntry = mock();
			when(sourcePrevious.next(transaction)).thenReturn(sourceEntry);
			final JarEntry targetPrevious = mock();
			final JarEntry targetEntry = mock();
			when(targetPrevious.next(transaction)).thenReturn(targetEntry);

			when(gateway.findBefore(sourceJarId, date, sequence))
					.thenReturn(Optional.of(sourcePrevious));
			when(gateway.getAllAfter(sourceJarId, date, sequence))
					.thenReturn(Collections.emptyList());
			when(gateway.findBefore(targetJarId, date, sequence))
					.thenReturn(Optional.of(targetPrevious));
			when(gateway.getAllAfter(targetJarId, date, sequence))
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
			final JarTransferTransaction transaction = TRANSACTION.bdo();
			final long sourceJarId = TRANSACTION.getSourceJar().getId();
			final long targetJarId = TRANSACTION.getTargetJar().getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount();

			final JarEntry sourcePrevious = mock();
			final JarEntry sourceEntry = mock();
			when(sourcePrevious.next(transaction)).thenReturn(sourceEntry);
			final JarEntry targetPrevious = mock();
			final JarEntry targetEntry = mock();
			when(targetPrevious.next(transaction)).thenReturn(targetEntry);

			final JarEntry sourceNext1 = mock();
			final JarEntry sourceNext2 = mock();
			final List<JarEntry> sourceNextEntries = List.of(sourceNext1,
					sourceNext2);
			final JarEntry targetNext1 = mock();
			final JarEntry targetNext2 = mock();
			final List<JarEntry> targetNextEntries = List.of(targetNext1,
					targetNext2);

			when(gateway.findBefore(sourceJarId, date, sequence))
					.thenReturn(Optional.of(sourcePrevious));
			when(gateway.getAllAfter(sourceJarId, date, sequence))
					.thenReturn(sourceNextEntries);
			when(gateway.findBefore(targetJarId, date, sequence))
					.thenReturn(Optional.of(targetPrevious));
			when(gateway.getAllAfter(targetJarId, date, sequence))
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
			final JarTransferTransaction transaction = TRANSACTION.bdo();
			final long sourceJarId = TRANSACTION.getSourceJar().getId();
			final long targetJarId = TRANSACTION.getTargetJar().getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount();

			final JarEntry sourcePrevious = mock();
			final JarEntry sourceEntry = mock();
			when(sourcePrevious.next(transaction)).thenReturn(sourceEntry);

			when(gateway.findBefore(sourceJarId, date, sequence))
					.thenReturn(Optional.of(sourcePrevious));
			when(gateway.getAllAfter(sourceJarId, date, sequence))
					.thenReturn(Collections.emptyList());
			when(gateway.findBefore(targetJarId, date, sequence))
					.thenReturn(Optional.empty());
			when(gateway.getAllAfter(targetJarId, date, sequence))
					.thenReturn(Collections.emptyList());

			service.create(transaction);

			final InOrder inOrder = inOrder(gateway);
			inOrder.verify(gateway).create(sourceEntry);
			inOrder.verify(gateway).updateAll(Collections.emptyList());
			inOrder.verify(gateway).create(argThat(entry -> {
				if (entry.getJarId() != targetJarId) {
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
			final JarTransferTransaction transaction = null;

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
			final long jarId = TRANSACTION.getJar().getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();

			final JarEntry entry = mock();
			when(entry.getAmount()).thenReturn(TRANSACTION.getAmount());

			when(gateway.getByJarIdAndTransactionId(jarId, TRANSACTION.getId()))
					.thenReturn(entry);
			when(gateway.getAllAfter(jarId, date, sequence))
					.thenReturn(Collections.emptyList());

			service.delete(transaction);

			final InOrder inOrder = inOrder(gateway);
			inOrder.verify(gateway).delete(entry);
			inOrder.verify(gateway).updateAll(Collections.emptyList());
		}

		@Test
		void withNext() {
			final PaymentTransaction transaction = TRANSACTION.bdo();
			final long jarId = TRANSACTION.getJar().getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount().negate();

			final JarEntry entry = mock();
			when(entry.getAmount()).thenReturn(TRANSACTION.getAmount());

			final JarEntry next1 = mock();
			final JarEntry next2 = mock();
			final List<JarEntry> nextEntries = List.of(next1, next2);

			when(gateway.getByJarIdAndTransactionId(jarId, TRANSACTION.getId()))
					.thenReturn(entry);
			when(gateway.getAllAfter(jarId, date, sequence))
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
			final long jarId = testTransaction.getJar().getId();
			final LocalDate date = testTransaction.getDate();
			final int sequence = testTransaction.getSequence();
			final BigDecimal amount = testTransaction.getAmount();

			final JarEntry entry = mock();
			when(entry.getAmount())
					.thenReturn(testTransaction.getAmount().negate());

			final JarEntry next1 = mock();
			final JarEntry next2 = mock();
			final List<JarEntry> nextEntries = List.of(next1, next2);

			when(gateway.getByJarIdAndTransactionId(jarId,
					testTransaction.getId())).thenReturn(entry);
			when(gateway.getAllAfter(jarId, date, sequence))
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
	class DeleteJarTransferTransaction {

		private static final TestJarTransferTransaction TRANSACTION = TestJarTransferTransaction.CAR_SAVINGS;

		@Test
		void withoutNext() {
			final JarTransferTransaction transaction = TRANSACTION.bdo();
			final long sourceJarId = TRANSACTION.getSourceJar().getId();
			final long targetJarId = TRANSACTION.getTargetJar().getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();

			final JarEntry sourceEntry = mock();
			when(sourceEntry.getAmount())
					.thenReturn(TRANSACTION.getAmount().negate());
			final JarEntry targetEntry = mock();
			when(targetEntry.getAmount()).thenReturn(TRANSACTION.getAmount());

			when(gateway.getByJarIdAndTransactionId(sourceJarId,
					TRANSACTION.getId())).thenReturn(sourceEntry);
			when(gateway.getAllAfter(sourceJarId, date, sequence))
					.thenReturn(Collections.emptyList());
			when(gateway.getByJarIdAndTransactionId(targetJarId,
					TRANSACTION.getId())).thenReturn(targetEntry);
			when(gateway.getAllAfter(targetJarId, date, sequence))
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
			final JarTransferTransaction transaction = TRANSACTION.bdo();
			final long sourceJarId = TRANSACTION.getSourceJar().getId();
			final long targetJarId = TRANSACTION.getTargetJar().getId();
			final LocalDate date = TRANSACTION.getDate();
			final int sequence = TRANSACTION.getSequence();
			final BigDecimal amount = TRANSACTION.getAmount();

			final JarEntry sourceEntry = mock();
			when(sourceEntry.getAmount())
					.thenReturn(TRANSACTION.getAmount().negate());
			final JarEntry targetEntry = mock();
			when(targetEntry.getAmount()).thenReturn(TRANSACTION.getAmount());

			final JarEntry sourceNext1 = mock();
			final JarEntry sourceNext2 = mock();
			final List<JarEntry> sourceNextEntries = List.of(sourceNext1,
					sourceNext2);
			final JarEntry targetNext1 = mock();
			final JarEntry targetNext2 = mock();
			final List<JarEntry> targetNextEntries = List.of(targetNext1,
					targetNext2);

			when(gateway.getByJarIdAndTransactionId(sourceJarId,
					TRANSACTION.getId())).thenReturn(sourceEntry);
			when(gateway.getAllAfter(sourceJarId, date, sequence))
					.thenReturn(sourceNextEntries);
			when(gateway.getByJarIdAndTransactionId(targetJarId,
					TRANSACTION.getId())).thenReturn(targetEntry);
			when(gateway.getAllAfter(targetJarId, date, sequence))
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
			final JarTransferTransaction transaction = null;
			assertThatThrownBy(() -> service.delete(transaction))
					.isInstanceOf(NullPointerException.class);

			verifyNoInteractions(gateway);
		}
	}
}
