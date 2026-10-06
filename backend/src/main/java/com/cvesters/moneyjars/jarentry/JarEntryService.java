package com.cvesters.moneyjars.jarentry;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.jarentry.bdo.JarEntry;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;

@Service
public class JarEntryService {

	private final JarEntryStorageGateway gateway;

	public JarEntryService(final JarEntryStorageGateway gateway) {
		this.gateway = gateway;
	}

	@Transactional
	public void create(final PaymentTransaction transaction) {
		Objects.requireNonNull(transaction);

		final long jarId = transaction.getJarId();
		final LocalDate date = transaction.getDate();
		final int sequence = transaction.getSequence();

		final JarEntry entry = gateway.findBefore(jarId, date, sequence)
				.map(previous -> previous.next(transaction))
				.orElseGet(() -> JarEntry.first(transaction));

		final List<JarEntry> nextEntries = gateway.getAllAfter(jarId, date,
				sequence);
		nextEntries.forEach(e -> e.shift(transaction.getAmount()));

		gateway.create(entry);
		gateway.updateAll(nextEntries);
	}
}
