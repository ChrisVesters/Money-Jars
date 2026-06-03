package com.cvesters.moneyjars.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.transaction.bdo.Transaction;
import com.cvesters.moneyjars.transaction.dao.TransactionDao;

class TransactionStorageGatewayTest {

	private static final TestTransaction TRANSACTION = TestTransaction.RENT;

	private final TransactionRepository repository = mock();
	private final TransactionStorageGateway gateway = new TransactionStorageGateway(
			repository);

	@Nested
	class GetAll {

		@Test
		void success() {
			final TransactionDao dao = mock();
			final Transaction bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);
			when(repository.findAll()).thenReturn(List.of(dao));

			final var result = gateway.getAll();

			assertThat(result).containsExactly(bdo);
		}
	}

	@Nested
	class Find {

		@Test
		void success() {
			final TransactionDao dao = mock();
			final Transaction bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);

			when(repository.findById(TRANSACTION.getId()))
					.thenReturn(Optional.of(dao));

			final var result = gateway.find(TRANSACTION.getId());

			assertThat(result).containsSame(bdo);
		}
	}

	@Nested
	class Create {

		@Test
		void success() {
			final TransactionDao createdDao = mock();
			final Transaction createdBdo = mock();
			when(createdDao.toBdo()).thenReturn(createdBdo);

			final Transaction transaction = TRANSACTION.bdo();
			when(repository.save(argThat(v -> {
				assertThat(v.getId()).isNull();
				assertThat(v.getDate()).isEqualTo(TRANSACTION.getDate());
				assertThat(v.getAmount()).isEqualTo(TRANSACTION.getAmount());
				assertThat(v.getJarId())
						.isEqualTo(TRANSACTION.getJar().getId());
				assertThat(v.getBeneficiary())
						.isEqualTo(TRANSACTION.getBeneficiary());
				assertThat(v.getDescription())
						.isEqualTo(TRANSACTION.getDescription());
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
			final Transaction updatedBdo = mock();
			when(updatedDao.toBdo()).thenReturn(updatedBdo);

			final Transaction update = mock();
			when(update.getId()).thenReturn(TRANSACTION.getId());

			final TransactionDao existing = mock();
			when(repository.findById(TRANSACTION.getId()))
					.thenReturn(Optional.of(existing));
			when(repository.save(existing)).thenReturn(updatedDao);

			final var result = gateway.update(update);

			assertThat(result).isSameAs(updatedBdo);
		}

		@Test
		void notFound() {
			when(repository.findById(TRANSACTION.getId()))
					.thenReturn(Optional.empty());

			final Transaction update = mock();
			when(update.getId()).thenReturn(TRANSACTION.getId());

			assertThatThrownBy(() -> gateway.update(update))
					.isInstanceOf(IllegalArgumentException.class);
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
