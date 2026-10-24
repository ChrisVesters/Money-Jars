package com.cvesters.moneyjars.transaction;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.common.exceptions.MissingEntityException;
import com.cvesters.moneyjars.transaction.bdo.AccountTransferTransaction;
import com.cvesters.moneyjars.transaction.dao.AccountTransferTransactionDao;

@Service
public class AccountTransferTransactionStorageGateway {

	private final AccountTransferTransactionRepository repository;

	public AccountTransferTransactionStorageGateway(
			final AccountTransferTransactionRepository repository) {
		this.repository = repository;
	}

	public AccountTransferTransaction get(final long id) {
		return repository.findById(id)
				.map(AccountTransferTransactionDao::toBdo)
				.orElseThrow(MissingEntityException::new);
	}

	public AccountTransferTransaction create(
			final AccountTransferTransaction transaction) {
		Objects.requireNonNull(transaction);

		final var dao = new AccountTransferTransactionDao(transaction);
		final AccountTransferTransactionDao created = repository.save(dao);

		return created.toBdo();
	}

	public AccountTransferTransaction update(
			final AccountTransferTransaction transaction) {
		Objects.requireNonNull(transaction);

		final AccountTransferTransactionDao dao = repository
				.findById(transaction.getId())
				.orElseThrow(MissingEntityException::new);
		dao.updateWith(transaction);
		final AccountTransferTransactionDao updated = repository.save(dao);

		return updated.toBdo();
	}

	public void delete(final AccountTransferTransaction transaction) {
		Objects.requireNonNull(transaction);

		final AccountTransferTransactionDao dao = repository
				.findById(transaction.getId())
				.orElseThrow(MissingEntityException::new);
		repository.delete(dao);
	}
}
