package com.cvesters.moneyjars.transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.common.bdo.PagedResult;
import com.cvesters.moneyjars.common.exceptions.MissingEntityException;
import com.cvesters.moneyjars.transaction.bdo.Transaction;
import com.cvesters.moneyjars.transaction.bdo.TransactionQuery;
import com.cvesters.moneyjars.transaction.dao.TransactionDao;
import com.cvesters.moneyjars.transaction.dao.TransactionQueryDao;

@Service
public class TransactionStorageGateway {

	private final TransactionRepository repository;

	public TransactionStorageGateway(TransactionRepository repository) {
		this.repository = repository;
	}

	public List<Transaction> getAll() {
		return repository.findAll()
				.stream()
				.map(TransactionDao::toBdo)
				.toList();
	}

	public List<Transaction> getAllInPeriod(final LocalDate start,
			final LocalDate end) {
		Objects.requireNonNull(start);
		Objects.requireNonNull(end);

		return repository.findAllInPeriod(start, end)
				.stream()
				.map(TransactionDao::toBdo)
				.toList();
	}

	public Transaction get(final long id) {
		return repository.findById(id)
				.map(TransactionDao::toBdo)
				.orElseThrow(MissingEntityException::new);
	}

	public Optional<Transaction> findFirstForDate(final LocalDate date) {
		Objects.requireNonNull(date);

		return repository.findFirstForDate(date).map(TransactionDao::toBdo);
	}

	public Optional<Transaction> findLastForDate(final LocalDate date) {
		Objects.requireNonNull(date);

		return repository.findLastForDate(date).map(TransactionDao::toBdo);
	}

	public PagedResult<Transaction> findAll(final TransactionQuery query) {
		Objects.requireNonNull(query);

		final Specification<TransactionDao> spec = TransactionQueryDao
				.toSpecification(query.filter());
		final Pageable pageable = TransactionQueryDao
				.toPageable(query.pagination());

		final Page<TransactionDao> result = repository.findAll(spec, pageable);

		final List<Transaction> transactions = result.getContent()
				.stream()
				.map(TransactionDao::toBdo)
				.toList();
		final int page = result.getNumber();
		final int size = result.getSize();
		final long totalElements = result.getTotalElements();

		return new PagedResult<>(transactions, page, size, totalElements);
	}
}
