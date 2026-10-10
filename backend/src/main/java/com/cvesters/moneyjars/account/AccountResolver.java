package com.cvesters.moneyjars.account;

import java.math.BigDecimal;

import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.account.bdo.AccountEntry;
import com.cvesters.moneyjars.account.dto.AccountDto;

@Controller
public class AccountResolver {

	private final AccountEntryService entryService;

	public AccountResolver(final AccountEntryService entryService) {
		this.entryService = entryService;
	}

	@SchemaMapping(typeName = "Account", field = "balance")
	public BigDecimal balance(final AccountDto account) {
		return entryService.findLast(account.getId())
				.map(AccountEntry::getBalanceAfter)
				.orElse(BigDecimal.ZERO);
	}

}
