package com.cvesters.moneyjars.transaction;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.common.exceptions.MissingEntityException;
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

	public Transaction create(final TransactionAction.Create action) {
		Objects.requireNonNull(action);

		final Transaction transaction = action.toBdo();
		return storage.create(transaction);
	}

	public Transaction update(final long id, final TransactionAction.Update action) {
		Objects.requireNonNull(action);

		final Transaction transaction = storage.find(id)
				.orElseThrow(MissingEntityException::new);
		action.applyOn(transaction);

		return storage.update(transaction);
	}

	public void delete(final long id) {
		storage.delete(id);
	}




//  Keep list of jar transactions & account transactions.
//  TODO: transaction should not have a reference to jar!?

//  Every transaction can only be part of 1 jar!
//  What about the concept of splitting transactions?
//  CREATE TABLE jar_transactions(
//  	jar_id BIGINT NOT NULL,
//  	transaction_id BIGINT NOT NULL,
//  	before_balance NUMERIC NOT NULL,
//  	after_balance NUMERIC NOT NULL,
//  )

}
