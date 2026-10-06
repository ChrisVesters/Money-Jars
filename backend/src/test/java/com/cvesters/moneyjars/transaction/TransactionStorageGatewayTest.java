package com.cvesters.moneyjars.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import com.cvesters.moneyjars.common.exceptions.MissingEntityException;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;
import com.cvesters.moneyjars.transaction.dao.PaymentTransactionDao;
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

	@Nested
	class Create {

		@Test
		void success() {
			final TransactionDao createdDao = mock();
			final PaymentTransaction createdBdo = mock();
			when(createdDao.toBdo()).thenReturn(createdBdo);

			final PaymentTransaction transaction = TRANSACTION.bdo();
			when(repository.save(argThat(v -> {
				assertThat(v).isInstanceOf(PaymentTransactionDao.class);
				final PaymentTransactionDao paymentDao = (PaymentTransactionDao) v;

				assertThat(paymentDao.getId()).isNull();
				assertThat(paymentDao.getDate())
						.isEqualTo(TRANSACTION.getDate());
				assertThat(paymentDao.getAmount())
						.isEqualTo(TRANSACTION.getAmount());
				assertThat(paymentDao.getDescription())
						.isEqualTo(TRANSACTION.getDescription());
				assertThat(paymentDao.getJarId())
						.isEqualTo(TRANSACTION.getJar().getId());
				assertThat(paymentDao.getAccountId())
						.isEqualTo(TRANSACTION.getAccount().getId());
				assertThat(paymentDao.getCounterparty())
						.isEqualTo(TRANSACTION.getCounterparty());
				assertThat(paymentDao.getDirection())
						.isEqualTo(TRANSACTION.getDirectionId());
				return true;
			}))).thenReturn(createdDao);

			final var result = gateway.create(transaction);

			assertThat(result).isSameAs(createdBdo);
		}

		@Test
		void transactionNull() {
			assertThatThrownBy(() -> gateway.create(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Update {

		@Test
		void success() {
			final TransactionDao updatedDao = mock();
			final PaymentTransaction updatedBdo = mock();
			when(updatedDao.toBdo()).thenReturn(updatedBdo);

			final PaymentTransaction update = mock();
			when(update.getId()).thenReturn(TRANSACTION.getId());

			final TransactionDao existing = mock();
			when(repository.findById(TRANSACTION.getId()))
					.thenReturn(Optional.of(existing));
			when(repository.save(existing)).thenReturn(updatedDao);

			final var result = gateway.update(update);

			assertThat(result).isSameAs(updatedBdo);

			final InOrder inOrder = inOrder(existing, repository);
			inOrder.verify(existing).updateWith(update);
			inOrder.verify(repository).save(existing);
		}

		@Test
		void notFound() {
			when(repository.findById(TRANSACTION.getId()))
					.thenReturn(Optional.empty());

			final PaymentTransaction update = mock();
			when(update.getId()).thenReturn(TRANSACTION.getId());

			assertThatThrownBy(() -> gateway.update(update))
					.isInstanceOf(MissingEntityException.class);
		}

		@Test
		void transactionNull() {
			assertThatThrownBy(() -> gateway.update(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Delete {

		@Test
		void success() {
			gateway.delete(TRANSACTION.getId());

			verify(repository).deleteById(TRANSACTION.getId());
		}
	}
}
