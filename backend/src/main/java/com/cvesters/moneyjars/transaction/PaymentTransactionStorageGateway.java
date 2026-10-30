package com.cvesters.moneyjars.transaction;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.common.exceptions.MissingEntityException;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionFilter;
import com.cvesters.moneyjars.transaction.dao.PaymentTransactionDao;
import com.cvesters.moneyjars.transaction.dao.PaymentTransactionQueryDao;

@Service
public class PaymentTransactionStorageGateway {

	private final PaymentTransactionRepository repository;

	public PaymentTransactionStorageGateway(
			final PaymentTransactionRepository repository) {
		this.repository = repository;
	}

	public long count(final PaymentTransactionFilter filter) {
		Objects.requireNonNull(filter);

		return repository
				.count(PaymentTransactionQueryDao.toSpecification(filter));
	}

	public PaymentTransaction get(final long id) {
		return repository.findById(id)
				.map(PaymentTransactionDao::toBdo)
				.orElseThrow(MissingEntityException::new);
	}

	public PaymentTransaction create(final PaymentTransaction transaction) {
		Objects.requireNonNull(transaction);

		final var dao = new PaymentTransactionDao(transaction);
		final PaymentTransactionDao created = repository.save(dao);

		return created.toBdo();
	}

	public PaymentTransaction update(final PaymentTransaction transaction) {
		Objects.requireNonNull(transaction);

		final PaymentTransactionDao dao = repository
				.findById(transaction.getId())
				.orElseThrow(MissingEntityException::new);
		dao.updateWith(transaction);
		final PaymentTransactionDao updated = repository.save(dao);

		return updated.toBdo();
	}

	public void delete(final PaymentTransaction transaction) {
		Objects.requireNonNull(transaction);

		final PaymentTransactionDao dao = repository
				.findById(transaction.getId())
				.orElseThrow(MissingEntityException::new);
		repository.delete(dao);
	}
}
