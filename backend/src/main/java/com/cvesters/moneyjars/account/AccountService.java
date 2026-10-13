package com.cvesters.moneyjars.account;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.account.bdo.Account;
import com.cvesters.moneyjars.account.bdo.AccountAction;

@Service
public class AccountService {

	private final AccountStorageGateway storage;

	public AccountService(final AccountStorageGateway storage) {
		this.storage = storage;
	}

	public List<Account> getAll() {
		return storage.getAll();
	}

	public Account get(final long id) {
		return storage.get(id);
	}

	public Account create(final AccountAction.Create action) {
		Objects.requireNonNull(action);

		final var account = action.toBdo();
		return storage.create(account);
	}

	public Account update(final long id, final AccountAction.Update action) {
		Objects.requireNonNull(action);

		final Account account = storage.get(id);
		action.applyOn(account);

		return storage.update(account);
	}

	public void delete(final long id) {
		storage.delete(id);
	}
}
