package com.cvesters.moneyjars.transaction;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.transaction.bdo.Transaction;

@Service
public class TransactionService {

	private final TransactionStorageGateway storage;

	public TransactionService(final TransactionStorageGateway storage) {
		this.storage = storage;
	}

	public List<Transaction> getAll() {
		return storage.getAll();
	}

	public Transaction get(final long id) {
		return storage.get(id);
	}
}
