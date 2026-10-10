package com.cvesters.moneyjars.jar;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.common.exceptions.MissingEntityException;
import com.cvesters.moneyjars.jar.bdo.JarEntry;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;

@Service
public class JarEntryService {

	private final JarEntryStorageGateway gateway;

	public JarEntryService(final JarEntryStorageGateway gateway) {
		this.gateway = gateway;
	}

	public Optional<JarEntry> findLast(final long jarId) {
		return gateway.findLast(jarId);
	}

	@Transactional
	public void create(final PaymentTransaction transaction) {
		Objects.requireNonNull(transaction);

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

	@Transactional
	public void delete(final PaymentTransaction transaction) {
		Objects.requireNonNull(transaction);

		final long jarId = transaction.getJarId();
		final LocalDate date = transaction.getDate();
		final int sequence = transaction.getSequence();
		final BigDecimal amount = transaction.getSignedAmount().negate();

		final JarEntry entry = gateway
				.findByJarIdAndTransactionId(jarId, transaction.getId())
				.orElseThrow(MissingEntityException::new);

		final List<JarEntry> nextEntries = gateway.getAllAfter(jarId, date,
				sequence);
		nextEntries.forEach(e -> e.shift(amount));

		gateway.delete(entry);
		gateway.updateAll(nextEntries);
	}
}
