package com.cvesters.moneyjars.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.jar.JarEntryService;
import com.cvesters.moneyjars.transaction.bdo.JarTransferTransaction;
import com.cvesters.moneyjars.transaction.bdo.JarTransferTransactionAction;

@Service
public class JarTransferTransactionService {

	private final TransactionSequenceService sequenceService;
	private final JarEntryService jarEntryService;

	private final JarTransferTransactionStorageGateway storage;

	public JarTransferTransactionService(
			final TransactionSequenceService sequenceService,
			final JarEntryService jarEntryService,
			final JarTransferTransactionStorageGateway storage) {
		this.sequenceService = sequenceService;
		this.jarEntryService = jarEntryService;
		this.storage = storage;
	}

	@Transactional
	public JarTransferTransaction create(
			final JarTransferTransactionAction.Create action) {
		Objects.requireNonNull(action);

		final LocalDate date = action.date();
		final int sequence = sequenceService.getNextSequence(date);

		final BigDecimal amount = action.amount();
		final String description = action.description();
		final long sourceJarId = action.sourceJarId();
		final long targetJarId = action.targetJarId();

		final var transaction = new JarTransferTransaction(date, sequence,
				amount, description, sourceJarId, targetJarId);

		final JarTransferTransaction created = storage.create(transaction);
		jarEntryService.create(created);

		return created;
	}

	@Transactional
	public JarTransferTransaction update(final long id,
			final JarTransferTransactionAction.Update action) {
		Objects.requireNonNull(action);

		final JarTransferTransaction transaction = storage.get(id);
		jarEntryService.delete(transaction);

		final LocalDate currentDate = transaction.getDate();
		final LocalDate newDate = action.date();
		if (!currentDate.equals(newDate)) {
			transaction.setDate(newDate);
			final int sequence = sequenceService.getNextSequence(newDate);
			transaction.setSequence(sequence);
		}

		transaction.setAmount(action.amount());
		transaction.setDescription(action.description());
		transaction.setSourceJarId(action.sourceJarId());
		transaction.setTargetJarId(action.targetJarId());

		final JarTransferTransaction updated = storage.update(transaction);
		jarEntryService.create(updated);

		return updated;
	}

	@Transactional
	public void delete(final long id) {
		final JarTransferTransaction transaction = storage.get(id);
		jarEntryService.delete(transaction);
		storage.delete(transaction);
	}
}
