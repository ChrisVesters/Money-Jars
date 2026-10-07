package com.cvesters.moneyjars.jarentry;

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

import com.cvesters.moneyjars.jarentry.bdo.JarEntry;
import com.cvesters.moneyjars.transaction.TestPaymentTransaction;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;

class JarEntryServiceTest {

	private final JarEntryStorageGateway gateway = mock();
	private final JarEntryService service = new JarEntryService(gateway);

	@Nested
	class Create {

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
			assertThatThrownBy(() -> service.create(null))
					.isInstanceOf(NullPointerException.class);

			verifyNoInteractions(gateway);
		}
	}
}
