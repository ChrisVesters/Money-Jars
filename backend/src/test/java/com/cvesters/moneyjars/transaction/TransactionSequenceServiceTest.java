package com.cvesters.moneyjars.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;

class TransactionSequenceServiceTest {

	private final TransactionStorageGateway storage = mock();
	private final TransactionSequenceService service = new TransactionSequenceService(
			storage);

	@Nested
	class GetNextSequence {

		private static final LocalDate DATE = LocalDate.of(2025, 12, 7);

		@Test
		void firstOnDate() {
			when(storage.findLastForDate(DATE)).thenReturn(Optional.empty());

			final int result = service.getNextSequence(DATE);

			assertThat(result).isZero();
		}

		@Test
		void existingOnDate() {
			final PaymentTransaction existing = mock();
			when(existing.getSequence()).thenReturn(3);
			when(storage.findLastForDate(DATE))
					.thenReturn(Optional.of(existing));

			final int result = service.getNextSequence(DATE);

			assertThat(result).isEqualTo(4);
		}

		@Test
		void dateNull() {
			assertThatThrownBy(() -> service.getNextSequence(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
