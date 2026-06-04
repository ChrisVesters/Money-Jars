package com.cvesters.moneyjars.account.dao;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cvesters.moneyjars.account.bdo.Account;

@Getter
@Entity
@Table(name = "accounts")
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class AccountDao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private BigDecimal balance;

	public AccountDao(final Account account) {
		Objects.requireNonNull(account);

		this.name = account.getName();
		this.description = account.getDescription();
		this.balance = account.getBalance();
	}

	public void updateWith(final Account account) {
		Objects.requireNonNull(account);

		this.name = account.getName();
		this.description = account.getDescription();
	}

	public Account toBdo() {
		return new Account(id, name, description, balance);
	}
}
