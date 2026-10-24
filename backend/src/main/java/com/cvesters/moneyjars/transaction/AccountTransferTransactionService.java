package com.cvesters.moneyjars.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.account.AccountEntryService;
import com.cvesters.moneyjars.transaction.bdo.AccountTransferTransaction;
import com.cvesters.moneyjars.transaction.bdo.AccountTransferTransactionAction;

@Service
public class AccountTransferTransactionService {

	private final TransactionSequenceService sequenceService;
	private final AccountEntryService accountEntryService;

	private final AccountTransferTransactionStorageGateway storage;

	public AccountTransferTransactionService(
			final TransactionSequenceService sequenceService,
			final AccountEntryService accountEntryService,
			final AccountTransferTransactionStorageGateway storage) {
		this.sequenceService = sequenceService;
		this.accountEntryService = accountEntryService;
		this.storage = storage;
	}

	@Transactional
	public AccountTransferTransaction create(
			final AccountTransferTransactionAction.Create action) {
		Objects.requireNonNull(action);

		final LocalDate date = action.date();
		final int sequence = sequenceService.getNextSequence(date);

		final BigDecimal amount = action.amount();
		final String description = action.description();
		final long sourceAccountId = action.sourceAccountId();
		final long targetAccountId = action.targetAccountId();

		final var transaction = new AccountTransferTransaction(date, sequence,
				amount, description, sourceAccountId, targetAccountId);

		final AccountTransferTransaction created = storage.create(transaction);
		accountEntryService.create(created);

		return created;
	}

	@Transactional
	public AccountTransferTransaction update(final long id,
			final AccountTransferTransactionAction.Update action) {
		Objects.requireNonNull(action);

		final AccountTransferTransaction transaction = storage.get(id);
		accountEntryService.delete(transaction);

		final LocalDate currentDate = transaction.getDate();
		final LocalDate newDate = action.date();
		if (!currentDate.equals(newDate)) {
			transaction.setDate(newDate);
			final int sequence = sequenceService.getNextSequence(newDate);
			transaction.setSequence(sequence);
		}

		transaction.setAmount(action.amount());
		transaction.setDescription(action.description());
		transaction.setSourceAccountId(action.sourceAccountId());
		transaction.setTargetAccountId(action.targetAccountId());

		final AccountTransferTransaction updated = storage.update(transaction);
		accountEntryService.create(updated);

		return updated;
	}

	@Transactional
	public void delete(final long id) {
		final AccountTransferTransaction transaction = storage.get(id);
		accountEntryService.delete(transaction);
		storage.delete(transaction);
	}
}
