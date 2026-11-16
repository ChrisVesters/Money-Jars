package com.cvesters.moneyjars.account;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.account.dto.AccountBalanceHistoryDto;
import com.cvesters.moneyjars.account.dto.AccountBalanceQueryDto;

@Controller
public class AccountBalanceController {

	private final AccountBalanceService accountBalanceService;

	public AccountBalanceController(
			final AccountBalanceService accountBalanceService) {
		this.accountBalanceService = accountBalanceService;
	}

	@QueryMapping
	public List<AccountBalanceHistoryDto> queryAccountBalanceHistory(
			@Argument final AccountBalanceQueryDto req) {
		return accountBalanceService.getHistory(req.accountIds())
				.stream()
				.map(AccountBalanceHistoryDto::new)
				.toList();
	}

}
