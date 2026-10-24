package com.cvesters.moneyjars.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import com.cvesters.moneyjars.common.exceptions.MissingEntityException;
import com.cvesters.moneyjars.transaction.bdo.AccountTransferTransaction;
import com.cvesters.moneyjars.transaction.dao.AccountTransferTransactionDao;

class AccountTransferTransactionStorageGatewayTest {

	private static final TestAccountTransferTransaction TRANSACTION = TestAccountTransferTransaction.CASH_WITHDRAWAL;

	private final AccountTransferTransactionRepository repository = mock();
	private final AccountTransferTransactionStorageGateway gateway = new AccountTransferTransactionStorageGateway(
			repository);

	@Nested
	class Get {

		@Test
		void success() {
			final AccountTransferTransactionDao dao = mock();
			final AccountTransferTransaction bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);

			when(repository.findById(TRANSACTION.getId()))
					.thenReturn(Optional.of(dao));

			final var result = gateway.get(TRANSACTION.getId());

			assertThat(result).isSameAs(bdo);
		}

		@Test
		void notFound() {
			final long id = TRANSACTION.getId();
			when(repository.findById(id)).thenReturn(Optional.empty());

			assertThatThrownBy(() -> gateway.get(id))
					.isInstanceOf(MissingEntityException.class);
		}
	}

	@Nested
	class Create {

		@Test
		void success() {
			final AccountTransferTransactionDao createdDao = mock();
			final AccountTransferTransaction createdBdo = mock();
			when(createdDao.toBdo()).thenReturn(createdBdo);

			final AccountTransferTransaction transaction = TRANSACTION.bdo();
			when(repository.save(argThat(dao -> {
				assertThat(dao.getId()).isNull();
				assertThat(dao.getDate()).isEqualTo(TRANSACTION.getDate());
				assertThat(dao.getSequence())
						.isEqualTo(TRANSACTION.getSequence());
				assertThat(dao.getAmount()).isEqualTo(TRANSACTION.getAmount());
				assertThat(dao.getDescription())
						.isEqualTo(TRANSACTION.getDescription());
				assertThat(dao.getSourceAccountId())
						.isEqualTo(TRANSACTION.getSourceAccount().getId());
				assertThat(dao.getTargetAccountId())
						.isEqualTo(TRANSACTION.getTargetAccount().getId());
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
			final AccountTransferTransactionDao updatedDao = mock();
			final AccountTransferTransaction updatedBdo = mock();
			when(updatedDao.toBdo()).thenReturn(updatedBdo);

			final AccountTransferTransaction update = mock();
			when(update.getId()).thenReturn(TRANSACTION.getId());

			final AccountTransferTransactionDao existing = mock();
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

			final AccountTransferTransaction update = mock();
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
			final AccountTransferTransaction transaction = mock();
			when(transaction.getId()).thenReturn(TRANSACTION.getId());

			final AccountTransferTransactionDao existing = mock();
			when(repository.findById(TRANSACTION.getId()))
					.thenReturn(Optional.of(existing));

			gateway.delete(transaction);

			verify(repository).delete(existing);
		}

		@Test
		void notFound() {
			when(repository.findById(TRANSACTION.getId()))
					.thenReturn(Optional.empty());

			final AccountTransferTransaction transaction = mock();
			when(transaction.getId()).thenReturn(TRANSACTION.getId());

			assertThatThrownBy(() -> gateway.delete(transaction))
					.isInstanceOf(MissingEntityException.class);

			verify(repository, never()).delete(any());
		}

		@Test
		void transactionNull() {
			assertThatThrownBy(() -> gateway.delete(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
