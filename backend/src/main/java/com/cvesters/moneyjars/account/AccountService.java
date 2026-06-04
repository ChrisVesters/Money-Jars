package com.cvesters.moneyjars.account;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.account.bdo.Account;
import com.cvesters.moneyjars.account.bdo.AccountAction;
import com.cvesters.moneyjars.common.exceptions.MissingEntityException;

@Service
public class AccountService {

	private final AccountStorageGateway storage;

	public AccountService(final AccountStorageGateway storage) {
		this.storage = storage;
	}

	public List<Account> getAll() {
		return storage.getAll();
	}

	public Optional<Account> find(final long id) {
		return storage.find(id);
	}

	public Account create(final AccountAction.Create action) {
		Objects.requireNonNull(action);

		final var account = action.toBdo();
		return storage.create(account);
	}

	public Account update(final long id, final AccountAction.Update action) {
		Objects.requireNonNull(action);

		final Account account = storage.find(id)
				.orElseThrow(MissingEntityException::new);
		action.applyOn(account);

		return storage.update(account);
	}

	public void delete(final long id) {
		storage.delete(id);
	}
}
