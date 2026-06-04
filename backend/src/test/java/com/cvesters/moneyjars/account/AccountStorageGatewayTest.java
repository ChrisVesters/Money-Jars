package com.cvesters.moneyjars.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import com.cvesters.moneyjars.account.bdo.Account;
import com.cvesters.moneyjars.account.dao.AccountDao;

class AccountStorageGatewayTest {

	private static final TestAccount ACCOUNT = TestAccount.CHECKING;

	private final AccountRepository repository = mock();
	private final AccountStorageGateway gateway = new AccountStorageGateway(
			repository);

	@Nested
	class GetAll {

		@Test
		void success() {
			final AccountDao dao = mock();
			final Account bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);
			when(repository.findAll()).thenReturn(List.of(dao));

			final var accounts = gateway.getAll();

			assertThat(accounts).containsExactly(bdo);
		}
	}

	@Nested
	class Find {

		@Test
		void success() {
			final AccountDao dao = mock();
			final Account bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);

			when(repository.findById(ACCOUNT.getId()))
					.thenReturn(Optional.of(dao));

			final var result = gateway.find(ACCOUNT.getId());

			assertThat(result).containsSame(bdo);
		}
	}

	@Nested
	class Create {

		@Test
		void success() {
			final AccountDao createdDao = mock();
			final Account createdBdo = mock();
			when(createdDao.toBdo()).thenReturn(createdBdo);

			final Account account = ACCOUNT.bdo();
			when(repository.save(argThat(v -> {
				assertThat(v.getId()).isNull();
				assertThat(v.getName()).isEqualTo(ACCOUNT.getName());
				assertThat(v.getDescription())
						.isEqualTo(ACCOUNT.getDescription());
				assertThat(v.getBalance()).isEqualTo(ACCOUNT.getBalance());
				return true;
			}))).thenReturn(createdDao);

			final var result = gateway.create(account);

			assertThat(result).isSameAs(createdBdo);
		}

		@Test
		void accountNull() {
			assertThatThrownBy(() -> gateway.create(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Update {

		@Test
		void success() {
			final AccountDao updatedDao = mock();
			final Account updatedBdo = mock();
			when(updatedDao.toBdo()).thenReturn(updatedBdo);

			final Account update = mock();
			when(update.getId()).thenReturn(ACCOUNT.getId());

			final AccountDao existing = mock();
			when(repository.findById(ACCOUNT.getId()))
					.thenReturn(Optional.of(existing));
			when(repository.save(existing)).thenReturn(updatedDao);

			final Account result = gateway.update(update);

			assertThat(result).isSameAs(updatedBdo);

			final InOrder inOrder = inOrder(existing, repository);
			inOrder.verify(existing).updateWith(update);
			inOrder.verify(repository).save(existing);
		}

		@Test
		void notFound() {
			when(repository.findById(ACCOUNT.getId()))
					.thenReturn(Optional.empty());

			final Account update = mock();
			when(update.getId()).thenReturn(ACCOUNT.getId());

			assertThatThrownBy(() -> gateway.update(update))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void accountNull() {
			assertThatThrownBy(() -> gateway.update(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Delete {

		@Test
		void success() {
			gateway.delete(ACCOUNT.getId());

			verify(repository).deleteById(ACCOUNT.getId());
		}
	}
}
