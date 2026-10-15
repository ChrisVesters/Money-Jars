package com.cvesters.moneyjars.transaction;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.common.bdo.PagedResult;
import com.cvesters.moneyjars.transaction.bdo.Transaction;
import com.cvesters.moneyjars.transaction.bdo.TransactionQuery;

@Service
public class TransactionService {

	private final TransactionStorageGateway storage;

	public TransactionService(final TransactionStorageGateway storage) {
		this.storage = storage;
	}

	public List<Transaction> getAll() {
		return storage.getAll();
	}

	public PagedResult<Transaction> query(final TransactionQuery query) {
		Objects.requireNonNull(query);

		return storage.findAll(query);
	}

	public Transaction get(final long id) {
		return storage.get(id);
	}
}
