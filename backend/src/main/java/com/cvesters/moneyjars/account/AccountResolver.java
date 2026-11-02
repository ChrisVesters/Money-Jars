package com.cvesters.moneyjars.account;

import java.math.BigDecimal;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.account.bdo.AccountEntry;
import com.cvesters.moneyjars.account.dto.AccountDto;
import com.cvesters.moneyjars.common.bdo.PagedResult;
import com.cvesters.moneyjars.transaction.bdo.Transaction;
import com.cvesters.moneyjars.transaction.bdo.TransactionQuery;
import com.cvesters.moneyjars.transaction.dto.TransactionPageDto;
import com.cvesters.moneyjars.transaction.dto.TransactionQueryDto;

@Controller
public class AccountResolver {

	private final AccountEntryService entryService;

	public AccountResolver(final AccountEntryService entryService) {
		this.entryService = entryService;
	}

	@SchemaMapping(typeName = "Account", field = "balance")
	public String balance(final AccountDto account) {
		return entryService.findLast(account.getId())
				.map(AccountEntry::getBalanceAfter)
				.orElse(BigDecimal.ZERO)
				.toPlainString();
	}

	@SchemaMapping(typeName = "Account", field = "inUse")
	public boolean inUse(final AccountDto account) {
		return entryService.exists(account.getId());
	}

	@SchemaMapping(typeName = "Account", field = "transactions")
	public TransactionPageDto getTransactions(final AccountDto account,
			@Argument final TransactionQueryDto query) {
		final TransactionQuery bdo = query.toBdo();
		final PagedResult<Transaction> result = entryService
				.queryTransactions(account.getId(), bdo);
		return new TransactionPageDto(result);
	}
}
