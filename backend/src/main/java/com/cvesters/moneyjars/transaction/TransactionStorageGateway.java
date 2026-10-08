package com.cvesters.moneyjars.transaction;

import java.time.LocalDate;
import java.util.List;
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

	public List<Transaction> getAllInPeriod(final LocalDate start,
			final LocalDate end) {
		return transactionRepository.findAllInPeriod(start, end)
				.stream()
				.map(TransactionDao::toBdo)
				.toList();
	}

	// TODO: get?
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
}
