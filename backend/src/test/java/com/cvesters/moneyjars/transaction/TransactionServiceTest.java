package com.cvesters.moneyjars.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import com.cvesters.moneyjars.common.exceptions.MissingEntityException;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;
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
			final Optional<Transaction> expected = Optional.of(mock(PaymentTransaction.class));
			when(storage.find(TRANSACTION_ID)).thenReturn(expected);

			final var result = service.find(TRANSACTION_ID);

			assertThat(result).isSameAs(expected);
		}
	}

	@Nested
	class Create {

		@Test
		void success() {
			final TransactionAction.CreatePayment action = mock();
			final PaymentTransaction create = mock();
			when(action.toBdo()).thenReturn(create);

			final PaymentTransaction created = mock();
			when(storage.create(create)).thenReturn(created);

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

		private static final long TRANSACTION_ID = 1L;

		@Test
		void success() {
			final TransactionAction.UpdatePayment action = mock();
			final PaymentTransaction existing = mock();

			final PaymentTransaction updated = mock();
			when(storage.find(TRANSACTION_ID))
					.thenReturn(Optional.of(existing));
			when(storage.update(existing)).thenReturn(updated);

			final var result = service.update(TRANSACTION_ID, action);

			assertThat(result).isSameAs(updated);

			final InOrder inOrder = inOrder(storage, action);
			inOrder.verify(storage).find(TRANSACTION_ID);
			inOrder.verify(action).applyOn(existing);
			inOrder.verify(storage).update(existing);
		}

		@Test
		void actionNull() {
			assertThatThrownBy(() -> service.update(TRANSACTION_ID, null))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void missingEntity() {
			final TransactionAction.UpdatePayment action = mock();
			when(storage.find(TRANSACTION_ID)).thenReturn(Optional.empty());

			assertThatThrownBy(() -> service.update(TRANSACTION_ID, action))
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
