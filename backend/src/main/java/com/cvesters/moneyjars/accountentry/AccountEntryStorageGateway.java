package com.cvesters.moneyjars.accountentry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.accountentry.bdo.AccountEntry;
import com.cvesters.moneyjars.accountentry.dao.AccountEntryDao;

@Service
public class AccountEntryStorageGateway {

	private final AccountEntryRepository repository;

	public AccountEntryStorageGateway(final AccountEntryRepository repository) {
		this.repository = repository;
	}

	public Optional<AccountEntry> findByAccountIdAndTransactionId(
			final long accountId, final long transactionId) {
		return repository
				.findByAccountIdAndTransactionId(accountId, transactionId)
				.map(AccountEntryDao::toBdo);
	}

	public Optional<AccountEntry> findBefore(final long accountId,
			final LocalDate date, final int sequence) {
		return repository.findBefore(accountId, date, sequence)
				.map(AccountEntryDao::toBdo);
	}

	public List<AccountEntry> getAllAfter(final long accountId,
			final LocalDate date, final int sequence) {
		return repository.findAllAfter(accountId, date, sequence)
				.stream()
				.map(AccountEntryDao::toBdo)
				.toList();
	}

	public AccountEntry create(final AccountEntry entry) {
		Objects.requireNonNull(entry);

		var dao = new AccountEntryDao(entry);
		final AccountEntryDao created = repository.save(dao);

		return created.toBdo();
	}

	public AccountEntry update(final AccountEntry entry) {
		Objects.requireNonNull(entry);

		final AccountEntryDao found = repository.findById(entry.getId())
				.orElseThrow(IllegalArgumentException::new);
		found.updateWith(entry);
		final AccountEntryDao updated = repository.save(found);

		return updated.toBdo();
	}

	public List<AccountEntry> updateAll(final List<AccountEntry> entries) {
		Objects.requireNonNull(entries);

		final var updatedEntries = new ArrayList<AccountEntry>();
		for (final AccountEntry entry : entries) {
			final AccountEntry updatedEntry = update(entry);
			updatedEntries.add(updatedEntry);
		}

		return updatedEntries;
	}

	public void delete(final AccountEntry entry) {
		Objects.requireNonNull(entry);

		final AccountEntryDao found = repository.findById(entry.getId())
				.orElseThrow(IllegalArgumentException::new);
		repository.delete(found);
	}
}
