package com.cvesters.moneyjars.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.common.exceptions.MissingEntityException;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionDirection;
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

		final LocalDate date = action.date();
		final int sequence = getNextSequence(date);

		final BigDecimal amount = action.amount();
		final String beneficiary = action.beneficiary();
		final String description = action.description();
		final long jarId = action.jarId();
		final long accountId = action.accountId();
		final PaymentTransactionDirection direction = action.direction();

		final Transaction transaction = new PaymentTransaction(date, sequence,
				amount, beneficiary, description, jarId, accountId, direction);

		return storage.create(transaction);
	}

	public Transaction update(final long id,
			final TransactionAction.UpdatePayment action) {
		Objects.requireNonNull(action);

		final PaymentTransaction transaction = storage.find(id)
				.filter(PaymentTransaction.class::isInstance)
				.map(PaymentTransaction.class::cast)
				.orElseThrow(MissingEntityException::new);

		final LocalDate currentDate = transaction.getDate();
		final LocalDate newDate = action.date();
		if (!currentDate.equals(newDate)) {
			transaction.setDate(newDate);
			transaction.setSequence(getNextSequence(newDate));
		}

		transaction.setAmount(action.amount());
		transaction.setBeneficiary(action.beneficiary());
		transaction.setDescription(action.description());
		transaction.setJarId(action.jarId());
		transaction.setAccountId(action.accountId());
		transaction.setDirection(action.direction());

		return storage.update(transaction);
	}

	public void delete(final long id) {
		storage.delete(id);
	}

	private int getNextSequence(final LocalDate date) {
		final List<Transaction> existingTransactions = storage
				.getAllInPeriod(date, date);
		if (existingTransactions.isEmpty()) {
			return 0;
		} else {
			return existingTransactions.getLast().getSequence() + 1;
		}
	}

}
