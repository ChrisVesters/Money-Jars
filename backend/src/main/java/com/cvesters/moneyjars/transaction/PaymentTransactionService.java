package com.cvesters.moneyjars.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.common.exceptions.MissingEntityException;
import com.cvesters.moneyjars.jarentry.JarEntryService;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionDirection;
import com.cvesters.moneyjars.transaction.bdo.TransactionAction;

@Service
public class PaymentTransactionService {

	private final TransactionSequenceService sequenceService;
	private final JarEntryService jarEntryService;

	private final PaymentTransactionStorageGateway storage;

	public PaymentTransactionService(
			final TransactionSequenceService sequenceService,
			final JarEntryService jarEntryService,
			final PaymentTransactionStorageGateway storage) {
		this.sequenceService = sequenceService;
		this.jarEntryService = jarEntryService;
		this.storage = storage;
	}

	@Transactional 
	public PaymentTransaction create(
			final TransactionAction.CreatePayment action) {
		Objects.requireNonNull(action);

		final LocalDate date = action.date();
		final int sequence = sequenceService.getNextSequence(date);

		final BigDecimal amount = action.amount();
		final String description = action.description();
		final long jarId = action.jarId();
		final long accountId = action.accountId();
		final String counterparty = action.counterparty();
		final PaymentTransactionDirection direction = action.direction();

		final var transaction = new PaymentTransaction(date, sequence, amount,
				description, jarId, accountId, counterparty, direction);

		final PaymentTransaction created = storage.create(transaction);
		jarEntryService.create(created);

		return created;
	}

	public PaymentTransaction update(final long id,
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
			final int sequence = sequenceService.getNextSequence(newDate);
			transaction.setSequence(sequence);
		}

		transaction.setAmount(action.amount());
		transaction.setDescription(action.description());
		transaction.setJarId(action.jarId());
		transaction.setAccountId(action.accountId());
		transaction.setCounterparty(action.counterparty());
		transaction.setDirection(action.direction());

		// TODO: update jar entry
		// Or, delete the old one, and create a new one?

		return storage.update(transaction);
	}
}
