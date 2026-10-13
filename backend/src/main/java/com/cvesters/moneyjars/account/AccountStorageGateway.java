package com.cvesters.moneyjars.account;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.account.bdo.Account;
import com.cvesters.moneyjars.account.dao.AccountDao;
import com.cvesters.moneyjars.common.exceptions.MissingEntityException;

@Service
public class AccountStorageGateway {

	private final AccountRepository accountRepository;

	public AccountStorageGateway(final AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public List<Account> getAll() {
		return accountRepository.findAll()
				.stream()
				.map(AccountDao::toBdo)
				.toList();
	}

	public Account get(final long id) {
		return accountRepository.findById(id)
				.map(AccountDao::toBdo)
				.orElseThrow(MissingEntityException::new);
	}

	public Account create(final Account account) {
		Objects.requireNonNull(account);

		final var dao = new AccountDao(account);
		final AccountDao created = accountRepository.save(dao);

		return created.toBdo();
	}

	public Account update(final Account account) {
		Objects.requireNonNull(account);

		final AccountDao found = accountRepository.findById(account.getId())
				.orElseThrow(IllegalArgumentException::new);
		found.updateWith(account);
		final AccountDao updated = accountRepository.save(found);

		return updated.toBdo();
	}

	public void delete(final long id) {
		accountRepository.deleteById(id);
	}
}
