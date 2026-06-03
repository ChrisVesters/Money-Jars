package com.cvesters.moneyjars.transaction;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.transaction.bdo.Transaction;
import com.cvesters.moneyjars.transaction.dao.TransactionDao;

@Service
public class TransactionStorageGateway {

	private final TransactionRepository transactionRepository;

	public TransactionStorageGateway(
			TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	public List<Transaction> getAll() {
		return transactionRepository.findAll()
				.stream()
				.map(TransactionDao::toBdo)
				.toList();
	}

	public Optional<Transaction> find(final long id) {
		return transactionRepository.findById(id).map(TransactionDao::toBdo);
	}

	public Transaction create(final Transaction transaction) {
		Objects.requireNonNull(transaction);

		final var dao = new TransactionDao(transaction);
		final TransactionDao created = transactionRepository.save(dao);

		return created.toBdo();
	}

	public Transaction update(final Transaction transaction) {
		Objects.requireNonNull(transaction);

		final TransactionDao dao = transactionRepository
				.findById(transaction.getId())
				.orElseThrow(IllegalArgumentException::new);
		dao.updateWith(transaction);
		final TransactionDao updated = transactionRepository.save(dao);

		return updated.toBdo();
	}

	public void delete(final long id) {
		transactionRepository.deleteById(id);
	}

}
