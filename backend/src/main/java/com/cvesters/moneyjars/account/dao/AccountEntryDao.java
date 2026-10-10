package com.cvesters.moneyjars.account.dao;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cvesters.moneyjars.account.bdo.AccountEntry;

@Getter
@Entity
@Table(name = "account_entries", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "transaction_id", "account_id" }) })
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class AccountEntryDao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "transaction_id", nullable = false, updatable = false)
	private long transactionId;

	@Column(name = "account_id", nullable = false, updatable = false)
	private long accountId;

	@Column(name = "balance_before", nullable = false)
	private BigDecimal balanceBefore;

	@Column(name = "balance_after", nullable = false)
	private BigDecimal balanceAfter;

	public AccountEntryDao(final AccountEntry accountEntry) {
		Objects.requireNonNull(accountEntry);

		this.transactionId = accountEntry.getTransactionId();
		this.accountId = accountEntry.getAccountId();
		this.balanceBefore = accountEntry.getBalanceBefore();
		this.balanceAfter = accountEntry.getBalanceAfter();
	}

	public void updateWith(final AccountEntry accountEntry) {
		Objects.requireNonNull(accountEntry);

		this.balanceBefore = accountEntry.getBalanceBefore();
		this.balanceAfter = accountEntry.getBalanceAfter();
	}

	public AccountEntry toBdo() {
		return new AccountEntry(id, transactionId, accountId, balanceBefore,
				balanceAfter);
	}
}
