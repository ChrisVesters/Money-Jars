package com.cvesters.moneyjars.transaction;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.common.exceptions.MissingEntityException;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;
import com.cvesters.moneyjars.transaction.dao.PaymentTransactionDao;

@Service
public class PaymentTransactionStorageGateway {

	private final PaymentTransactionRepository repository;

	public PaymentTransactionStorageGateway(
			final PaymentTransactionRepository repository) {
		this.repository = repository;
	}

	public Optional<PaymentTransaction> find(final long id) {
		return repository.findById(id).map(PaymentTransactionDao::toBdo);
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
}
