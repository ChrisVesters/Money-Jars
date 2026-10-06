package com.cvesters.moneyjars.transaction;

import java.util.List;
import java.util.Optional;

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

	public Optional<Transaction> find(final long id) {
		return storage.find(id);
	}

	public void delete(final long id) {
		storage.delete(id);
	}
}
