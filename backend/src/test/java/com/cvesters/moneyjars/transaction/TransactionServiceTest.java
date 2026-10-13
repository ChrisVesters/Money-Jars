package com.cvesters.moneyjars.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;

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
	class Get {

		private static final long TRANSACTION_ID = 1L;

		@Test
		void success() {
			final PaymentTransaction expected = mock();
			when(storage.get(TRANSACTION_ID)).thenReturn(expected);

			final var result = service.get(TRANSACTION_ID);

			assertThat(result).isSameAs(expected);
		}
	}
}
