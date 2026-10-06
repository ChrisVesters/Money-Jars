package com.cvesters.moneyjars.transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.common.exceptions.MissingEntityException;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;
import com.cvesters.moneyjars.transaction.bdo.Transaction;
import com.cvesters.moneyjars.transaction.dao.PaymentTransactionDao;
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

	public List<Transaction> getAllInPeriod(final LocalDate start,
			final LocalDate end) {
		return transactionRepository.findAllInPeriod(start, end)
				.stream()
				.map(TransactionDao::toBdo)
				.toList();
	}

	public Optional<Transaction> find(final long id) {
		return transactionRepository.findById(id).map(TransactionDao::toBdo);
	}

	public Optional<Transaction> findFirstForDate(final LocalDate date) {
		return transactionRepository.findFirstForDate(date)
				.map(TransactionDao::toBdo);
	}

	public Optional<Transaction> findLastForDate(final LocalDate date) {
		return transactionRepository.findLastForDate(date)
				.map(TransactionDao::toBdo);
	}

	public Transaction create(final Transaction transaction) {
		Objects.requireNonNull(transaction);

		final TransactionDao dao = createDao(transaction);
		final TransactionDao created = transactionRepository.save(dao);

		return created.toBdo();
	}

	public Transaction update(final Transaction transaction) {
		Objects.requireNonNull(transaction);

		final TransactionDao dao = transactionRepository
				.findById(transaction.getId())
				.orElseThrow(MissingEntityException::new);
		dao.updateWith(transaction);
		final TransactionDao updated = transactionRepository.save(dao);

		return updated.toBdo();
	}

	public void delete(final long id) {
		// TODO: delete by id, or first get it?
		transactionRepository.deleteById(id);
	}

	private static TransactionDao createDao(final Transaction transaction) {
		return switch (transaction) {
			case PaymentTransaction paymentTransaction -> new PaymentTransactionDao(
					paymentTransaction);
		};
	}
}
