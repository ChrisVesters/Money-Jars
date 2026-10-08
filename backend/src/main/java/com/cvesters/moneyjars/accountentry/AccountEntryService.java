package com.cvesters.moneyjars.accountentry;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.accountentry.bdo.AccountEntry;
import com.cvesters.moneyjars.common.exceptions.MissingEntityException;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;

@Service
public class AccountEntryService {

	private final AccountEntryStorageGateway gateway;

	public AccountEntryService(final AccountEntryStorageGateway gateway) {
		this.gateway = gateway;
	}

	@Transactional
	public void create(final PaymentTransaction transaction) {
		Objects.requireNonNull(transaction);

		final long accountId = transaction.getAccountId();
		final LocalDate date = transaction.getDate();
		final int sequence = transaction.getSequence();
		final BigDecimal amount = transaction.getSignedAmount();

		final AccountEntry entry = gateway.findBefore(accountId, date, sequence)
				.map(previous -> previous.next(transaction))
				.orElseGet(() -> AccountEntry.first(transaction));

		final List<AccountEntry> nextEntries = gateway.getAllAfter(accountId,
				date, sequence);
		nextEntries.forEach(e -> e.shift(amount));

		gateway.create(entry);
		gateway.updateAll(nextEntries);
	}

	@Transactional
	public void delete(final PaymentTransaction transaction) {
		Objects.requireNonNull(transaction);

		final long accountId = transaction.getAccountId();
		final LocalDate date = transaction.getDate();
		final int sequence = transaction.getSequence();
		final BigDecimal amount = transaction.getSignedAmount().negate();

		final AccountEntry entry = gateway
				.findByAccountIdAndTransactionId(accountId, transaction.getId())
				.orElseThrow(MissingEntityException::new);

		final List<AccountEntry> nextEntries = gateway.getAllAfter(accountId,
				date, sequence);
		nextEntries.forEach(e -> e.shift(amount));

		gateway.delete(entry);
		gateway.updateAll(nextEntries);
	}
}
