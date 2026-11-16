package com.cvesters.moneyjars.account;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import com.cvesters.moneyjars.account.bdo.AccountBalanceHistory;
import com.cvesters.moneyjars.account.bdo.AccountEntry;

@Service
public class AccountBalanceService {

	private final AccountEntryStorageGateway gateway;

	public AccountBalanceService(final AccountEntryStorageGateway gateway) {
		this.gateway = gateway;
	}

	public List<AccountBalanceHistory> getHistory(final List<Long> accountIds) {
		Objects.requireNonNull(accountIds);
		Validate.noNullElements(accountIds);

		return accountIds.stream().map(this::getHistory).toList();
	}

	public AccountBalanceHistory getHistory(final long accountId) {
		final LocalDate start = LocalDate.now()
				.minusMonths(11)
				.withDayOfMonth(1);
		final Period interval = Period.ofMonths(1);

		final var history = new AccountBalanceHistory(accountId);
		for (int i = 0; i < 12; i++) {
			final Period offset = interval.multipliedBy(i);
			final LocalDate date = start.plus(offset);

			gateway.findBefore(accountId, date)
					.map(AccountEntry::getBalanceAfter)
					.ifPresent(balance -> history.addSnapshot(date, balance));
		}

		return history;
	}
}
