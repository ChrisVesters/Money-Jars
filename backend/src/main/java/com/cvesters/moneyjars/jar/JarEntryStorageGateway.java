package com.cvesters.moneyjars.jar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.jar.bdo.JarEntry;
import com.cvesters.moneyjars.jar.dao.JarEntryDao;

@Service
public class JarEntryStorageGateway {

	private final JarEntryRepository repository;

	public JarEntryStorageGateway(final JarEntryRepository repository) {
		this.repository = repository;
	}

	public Optional<JarEntry> findByJarIdAndTransactionId(final long jarId,
			final long transactionId) {
		return repository.findByJarIdAndTransactionId(jarId, transactionId)
				.map(JarEntryDao::toBdo);
	}

	public Optional<JarEntry> findBefore(final long jarId, final LocalDate date,
			final int sequence) {
		return repository.findBefore(jarId, date, sequence)
				.map(JarEntryDao::toBdo);
	}

	public Optional<JarEntry> findLast(final long jarId) {
		return repository.findLastByJarId(jarId).map(JarEntryDao::toBdo);
	}

	public List<JarEntry> getAllAfter(final long jarId, final LocalDate date,
			final int sequence) {
		return repository.findAllAfter(jarId, date, sequence)
				.stream()
				.map(JarEntryDao::toBdo)
				.toList();
	}

	public JarEntry create(final JarEntry entry) {
		Objects.requireNonNull(entry);

		var dao = new JarEntryDao(entry);
		final JarEntryDao created = repository.save(dao);

		return created.toBdo();
	}

	public JarEntry update(final JarEntry entry) {
		Objects.requireNonNull(entry);

		final JarEntryDao found = repository.findById(entry.getId())
				.orElseThrow(IllegalArgumentException::new);
		found.updateWith(entry);
		final JarEntryDao updated = repository.save(found);

		return updated.toBdo();
	}

	public List<JarEntry> updateAll(final List<JarEntry> entries) {
		Objects.requireNonNull(entries);

		final var updatedEntries = new ArrayList<JarEntry>();
		for (final JarEntry entry : entries) {
			final JarEntry updatedEntry = update(entry);
			updatedEntries.add(updatedEntry);
		}

		return updatedEntries;
	}

	public void delete(final JarEntry entry) {
		Objects.requireNonNull(entry);

		final JarEntryDao found = repository.findById(entry.getId())
				.orElseThrow(IllegalArgumentException::new);
		repository.delete(found);
	}
}
