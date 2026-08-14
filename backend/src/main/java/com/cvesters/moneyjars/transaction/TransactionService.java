package com.cvesters.moneyjars.transaction;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.common.exceptions.MissingEntityException;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;
import com.cvesters.moneyjars.transaction.bdo.Transaction;
import com.cvesters.moneyjars.transaction.bdo.TransactionAction;

@Service
public class TransactionService {

	private final TransactionStorageGateway storage;

	public TransactionService(final TransactionStorageGateway storage) {
		this.storage = storage;
	}

	public List<Transaction> getAll() {
		return storage.getAll();
	}

	public Optional<Transaction> find(final long id) {
		return storage.find(id);
	}

	// TODO: abstract?
	public Transaction create(final TransactionAction.CreatePayment action) {
		Objects.requireNonNull(action);

		final Transaction transaction = action.toBdo();
		return storage.create(transaction);
	}

	public Transaction update(final long id,
			final TransactionAction.UpdatePayment action) {
		Objects.requireNonNull(action);

		final PaymentTransaction transaction = storage.find(id)
				.filter(PaymentTransaction.class::isInstance)
				.map(PaymentTransaction.class::cast)
				.orElseThrow(MissingEntityException::new);
		action.applyOn(transaction);
		return storage.update(transaction);
	}

	public void delete(final long id) {
		storage.delete(id);
	}

}
