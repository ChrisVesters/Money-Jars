package com.cvesters.moneyjars.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;
import com.cvesters.moneyjars.transaction.dao.TransactionDao;

class TransactionStorageGatewayTest {

	private static final TestPaymentTransaction TRANSACTION = TestPaymentTransaction.RENT;

	private final TransactionRepository repository = mock();
	private final TransactionStorageGateway gateway = new TransactionStorageGateway(
			repository);

	@Nested
	class GetAll {

		@Test
		void success() {
			final TransactionDao dao = mock();
			final PaymentTransaction bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);
			when(repository.findAll()).thenReturn(List.of(dao));

			final var result = gateway.getAll();

			assertThat(result).containsExactly(bdo);
		}
	}

	@Nested
	class GetAllInPeriod {

		@Test
		void success() {
			final LocalDate start = LocalDate.of(2026, 1, 1);
			final LocalDate end = LocalDate.of(2026, 1, 31);

			final TransactionDao dao = mock();
			final PaymentTransaction bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);
			when(repository.findAllInPeriod(start, end))
					.thenReturn(List.of(dao));

			final var result = gateway.getAllInPeriod(start, end);

			assertThat(result).containsExactly(bdo);
		}
	}

	@Nested
	class Find {

		@Test
		void success() {
			final TransactionDao dao = mock();
			final PaymentTransaction bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);

			when(repository.findById(TRANSACTION.getId()))
					.thenReturn(Optional.of(dao));

			final var result = gateway.find(TRANSACTION.getId());

			assertThat(result).containsSame(bdo);
		}
	}

	@Nested
	class FindFirstForDate {

		@Test
		void success() {
			final TransactionDao dao = mock();
			final PaymentTransaction bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);

			when(repository.findFirstForDate(TRANSACTION.getDate()))
					.thenReturn(Optional.of(dao));

			final var result = gateway.findFirstForDate(TRANSACTION.getDate());

			assertThat(result).containsSame(bdo);
		}

		@Test
		void notFound() {
			when(repository.findFirstForDate(TRANSACTION.getDate()))
					.thenReturn(Optional.empty());

			final var result = gateway.findFirstForDate(TRANSACTION.getDate());

			assertThat(result).isEmpty();
		}
	}

	@Nested
	class FindLastForDate {

		@Test
		void success() {
			final TransactionDao dao = mock();
			final PaymentTransaction bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);

			when(repository.findLastForDate(TRANSACTION.getDate()))
					.thenReturn(Optional.of(dao));

			final var result = gateway.findLastForDate(TRANSACTION.getDate());

			assertThat(result).containsSame(bdo);
		}

		@Test
		void notFound() {
			when(repository.findLastForDate(TRANSACTION.getDate()))
					.thenReturn(Optional.empty());

			final var result = gateway.findLastForDate(TRANSACTION.getDate());

			assertThat(result).isEmpty();
		}
	}

}
