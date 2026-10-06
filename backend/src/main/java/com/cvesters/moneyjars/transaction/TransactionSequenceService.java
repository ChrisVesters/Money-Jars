package com.cvesters.moneyjars.transaction;

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.transaction.bdo.Transaction;

@Service
public class TransactionSequenceService {

	private final TransactionStorageGateway storage;

	public TransactionSequenceService(final TransactionStorageGateway storage) {
		this.storage = storage;
	}

	public int getNextSequence(final LocalDate date) {
		Objects.requireNonNull(date);

		return storage.findLastForDate(date)
				.map(Transaction::getSequence)
				.map(sequence -> sequence + 1)
				.orElse(0);
	}
}
