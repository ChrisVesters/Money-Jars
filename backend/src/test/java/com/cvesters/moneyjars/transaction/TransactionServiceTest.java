package com.cvesters.moneyjars.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;
import com.cvesters.moneyjars.transaction.bdo.Transaction;

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
	class Delete {

		private static final long TRANSACTION_ID = 1L;

		@Test
		void success() {
			service.delete(TRANSACTION_ID);

			verify(storage).delete(TRANSACTION_ID);
		}
	}
}
