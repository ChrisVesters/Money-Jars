package com.cvesters.moneyjars.transaction;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.common.exceptions.MissingEntityException;
import com.cvesters.moneyjars.transaction.bdo.JarTransferTransaction;
import com.cvesters.moneyjars.transaction.dao.JarTransferTransactionDao;

@Service
public class JarTransferTransactionStorageGateway {

	private final JarTransferTransactionRepository repository;

	public JarTransferTransactionStorageGateway(
			final JarTransferTransactionRepository repository) {
		this.repository = repository;
	}

	public JarTransferTransaction get(final long id) {
		return repository.findById(id)
				.map(JarTransferTransactionDao::toBdo)
				.orElseThrow(MissingEntityException::new);
	}

	public JarTransferTransaction create(
			final JarTransferTransaction transaction) {
		Objects.requireNonNull(transaction);

		final var dao = new JarTransferTransactionDao(transaction);
		final JarTransferTransactionDao created = repository.save(dao);

		return created.toBdo();
	}

	public JarTransferTransaction update(
			final JarTransferTransaction transaction) {
		Objects.requireNonNull(transaction);

		final JarTransferTransactionDao dao = repository
				.findById(transaction.getId())
				.orElseThrow(MissingEntityException::new);
		dao.updateWith(transaction);
		final JarTransferTransactionDao updated = repository.save(dao);

		return updated.toBdo();
	}

	public void delete(final JarTransferTransaction transaction) {
		Objects.requireNonNull(transaction);

		final JarTransferTransactionDao dao = repository
				.findById(transaction.getId())
				.orElseThrow(MissingEntityException::new);
		repository.delete(dao);
	}
}
