package com.cvesters.moneyjars.account.dto;

import java.math.BigDecimal;
import java.util.Objects;

import lombok.Getter;

import com.cvesters.moneyjars.account.bdo.Account;

@Getter
public class AccountDto {

	private Long id;
	private String name;
	private String description;
	private BigDecimal balance;

	public AccountDto(final Account account) {
		Objects.requireNonNull(account);

		this.id = account.getId();
		this.name = account.getName();
		this.description = account.getDescription();
		this.balance = account.getBalance();
	}
}
