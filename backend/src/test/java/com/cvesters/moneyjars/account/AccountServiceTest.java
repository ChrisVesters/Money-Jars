package com.cvesters.moneyjars.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import com.cvesters.moneyjars.account.bdo.Account;
import com.cvesters.moneyjars.account.bdo.AccountAction;
import com.cvesters.moneyjars.common.exceptions.MissingEntityException;

class AccountServiceTest {

	private final AccountStorageGateway storage = mock();
	private final AccountService service = new AccountService(storage);

	@Nested
	class GetAll {

		@Test
		void success() {
			final Account expected = mock();
			when(storage.getAll()).thenReturn(List.of(expected));

			final List<Account> result = service.getAll();

			assertThat(result).containsExactly(expected);
		}
	}

	@Nested
	class Find {

		private static final long ACCOUNT_ID = 2L;

		@Test
		void success() {
			final Optional<Account> expected = Optional.of(mock());
			when(storage.find(ACCOUNT_ID)).thenReturn(expected);

			final Optional<Account> result = service.find(ACCOUNT_ID);

			assertThat(result).isSameAs(expected);
		}
	}

	@Nested
	class Create {

		@Test
		void success() {
			final AccountAction.Create action = mock();
			final Account create = mock();
			when(action.toBdo()).thenReturn(create);

			final Account created = mock();
			when(storage.create(create)).thenReturn(created);

			final Account result = service.create(action);

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

		private static final long ACCOUNT_ID = 2L;

		@Test
		void success() {
			final AccountAction.Update action = mock();
			final Account existing = mock();

			final Account updated = mock();
			when(storage.find(ACCOUNT_ID)).thenReturn(Optional.of(existing));
			when(storage.update(existing)).thenReturn(updated);

			final Account result = service.update(ACCOUNT_ID, action);

			assertThat(result).isSameAs(updated);

			final InOrder inOrder = inOrder(storage, action);
			inOrder.verify(storage).find(ACCOUNT_ID);
			inOrder.verify(action).applyOn(existing);
			inOrder.verify(storage).update(existing);
		}

		@Test
		void actionNull() {
			assertThatThrownBy(() -> service.update(ACCOUNT_ID, null))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void missingEntity() {
			final AccountAction.Update action = mock();
			when(storage.find(ACCOUNT_ID)).thenReturn(Optional.empty());

			assertThatThrownBy(() -> service.update(ACCOUNT_ID, action))
					.isInstanceOf(MissingEntityException.class);
		}
	}

	@Nested
	class Delete {

		private static final long ACCOUNT_ID = 2L;

		@Test
		void success() {
			service.delete(ACCOUNT_ID);

			org.mockito.Mockito.verify(storage).delete(ACCOUNT_ID);
		}
	}
}
