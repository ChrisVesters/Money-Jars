package com.cvesters.moneyjars.jar;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.jar.bdo.JarBalanceHistory;
import com.cvesters.moneyjars.jar.bdo.JarEntry;

@Service
public class JarBalanceService {

	private final JarEntryStorageGateway gateway;

	public JarBalanceService(final JarEntryStorageGateway gateway) {
		this.gateway = gateway;
	}

	public List<JarBalanceHistory> getHistory(final List<Long> jarIds) {
		Objects.requireNonNull(jarIds);
		Validate.noNullElements(jarIds);

		return jarIds.stream().map(this::getHistory).toList();
	}

	public JarBalanceHistory getHistory(final long jarId) {
		final LocalDate start = LocalDate.now()
				.minusMonths(11)
				.withDayOfMonth(1);
		final Period interval = Period.ofMonths(1);

		final var history = new JarBalanceHistory(jarId);
		for (int i = 0; i < 12; i++) {
			final Period offset = interval.multipliedBy(i);
			final LocalDate date = start.plus(offset);

			gateway.findBefore(jarId, date)
					.map(JarEntry::getBalanceAfter)
					.ifPresent(balance -> history.addSnapshot(date, balance));
		}

		return history;
	}
}
