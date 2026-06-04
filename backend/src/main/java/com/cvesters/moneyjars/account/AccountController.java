package com.cvesters.moneyjars.account;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.account.bdo.Account;
import com.cvesters.moneyjars.account.bdo.AccountAction;
import com.cvesters.moneyjars.account.dto.AccountActionDto;
import com.cvesters.moneyjars.account.dto.AccountDto;

@Controller
public class AccountController {

	private final AccountService accountService;

	public AccountController(final AccountService accountService) {
		this.accountService = accountService;
	}

	@QueryMapping
	public List<AccountDto> getAccounts() {
		return accountService.getAll().stream().map(AccountDto::new).toList();
	}

	@QueryMapping
	public AccountDto getAccount(@Argument final long id) {
		return accountService.find(id).map(AccountDto::new).orElse(null);
	}

	@MutationMapping
	public AccountDto createAccount(
			@Argument final AccountActionDto.Create req) {
		final AccountAction.Create action = req.toBdo();
		final Account created = accountService.create(action);
		return new AccountDto(created);
	}

	@MutationMapping
	public AccountDto updateAccount(@Argument final long id,
			@Argument final AccountActionDto.Update req) {
		final AccountAction.Update action = req.toBdo();
		final Account updated = accountService.update(id, action);
		return new AccountDto(updated);
	}

	@MutationMapping
	public boolean deleteAccount(@Argument final long id) {
		accountService.delete(id);
		return true;
	}
}
