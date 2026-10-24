package com.cvesters.moneyjars.transaction;

import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.account.AccountService;
import com.cvesters.moneyjars.account.bdo.Account;
import com.cvesters.moneyjars.account.dto.AccountDto;
import com.cvesters.moneyjars.transaction.dto.AccountTransferTransactionDto;

@Controller
public class AccountTransferTransactionResolver {

	private final AccountService accountService;

	public AccountTransferTransactionResolver(
			final AccountService accountService) {
		this.accountService = accountService;
	}

	@SchemaMapping(typeName = "AccountTransferTransaction", field = "sourceAccount")
	public AccountDto sourceAccount(
			final AccountTransferTransactionDto transaction) {
		final Account account = accountService
				.get(transaction.getSourceAccountId());
		return new AccountDto(account);
	}

	@SchemaMapping(typeName = "AccountTransferTransaction", field = "targetAccount")
	public AccountDto targetAccount(
			final AccountTransferTransactionDto transaction) {
		final Account account = accountService
				.get(transaction.getTargetAccountId());
		return new AccountDto(account);
	}
}
