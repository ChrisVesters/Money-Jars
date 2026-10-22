package com.cvesters.moneyjars.jar;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.jar.bdo.JarEntry;
import com.cvesters.moneyjars.transaction.bdo.JarTransferTransaction;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;
import com.cvesters.moneyjars.transaction.bdo.Transaction;

@Service
public class JarEntryService {

	private final JarEntryStorageGateway gateway;

	public JarEntryService(final JarEntryStorageGateway gateway) {
		this.gateway = gateway;
	}

	public Optional<JarEntry> findLast(final long jarId) {
		return gateway.findLast(jarId);
	}

	public List<JarEntry> getAll(final long jarId) {
		return gateway.getAll(jarId);
	}

	@Transactional
	public void create(final PaymentTransaction transaction) {
		Objects.requireNonNull(transaction);

		book(transaction);
	}

	@Transactional
	public void create(final JarTransferTransaction transaction) {
		Objects.requireNonNull(transaction);

		book(transaction, transaction.getSourceJarId());
		book(transaction, transaction.getTargetJarId());
	}

	@Transactional
	public void delete(final PaymentTransaction transaction) {
		Objects.requireNonNull(transaction);

		unbook(transaction, transaction.getJarId());
	}

	@Transactional
	public void delete(final JarTransferTransaction transaction) {
		Objects.requireNonNull(transaction);

		unbook(transaction, transaction.getSourceJarId());
		unbook(transaction, transaction.getTargetJarId());
	}

	private void book(final PaymentTransaction transaction) {
		final long jarId = transaction.getJarId();
		final LocalDate date = transaction.getDate();
		final int sequence = transaction.getSequence();
		final BigDecimal amount = transaction.getSignedAmount();

		final JarEntry entry = gateway.findBefore(jarId, date, sequence)
				.map(previous -> previous.next(transaction))
				.orElseGet(() -> JarEntry.first(transaction));

		final List<JarEntry> nextEntries = gateway.getAllAfter(jarId, date,
				sequence);
		nextEntries.forEach(e -> e.shift(amount));

		gateway.create(entry);
		gateway.updateAll(nextEntries);
	}

	private void book(final JarTransferTransaction transaction,
			final long jarId) {
		final LocalDate date = transaction.getDate();
		final int sequence = transaction.getSequence();
		final BigDecimal amount = transaction.getSignedAmount(jarId);

		final JarEntry entry = gateway.findBefore(jarId, date, sequence)
				.map(previous -> previous.next(transaction))
				.orElseGet(() -> JarEntry.first(jarId, transaction));

		final List<JarEntry> nextEntries = gateway.getAllAfter(jarId, date,
				sequence);
		nextEntries.forEach(e -> e.shift(amount));

		gateway.create(entry);
		gateway.updateAll(nextEntries);
	}

	private void unbook(final Transaction transaction, final long jarId) {
		final long transactionId = transaction.getId();
		final LocalDate date = transaction.getDate();
		final int sequence = transaction.getSequence();

		final JarEntry entry = gateway.getByJarIdAndTransactionId(jarId,
				transactionId);

		final BigDecimal amount = entry.getAmount().negate();

		final List<JarEntry> nextEntries = gateway.getAllAfter(jarId, date,
				sequence);
		nextEntries.forEach(e -> e.shift(amount));

		gateway.delete(entry);
		gateway.updateAll(nextEntries);
	}
}
