package com.cvesters.moneyjars.account;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.account.bdo.AccountEntry;
import com.cvesters.moneyjars.account.dto.AccountDto;
import com.cvesters.moneyjars.transaction.TransactionService;
import com.cvesters.moneyjars.transaction.dto.TransactionDto;

@Controller
public class AccountResolver {

	private final AccountEntryService entryService;
	private final TransactionService transactionService;

	public AccountResolver(final AccountEntryService entryService,
			final TransactionService transactionService) {
		this.entryService = entryService;
		this.transactionService = transactionService;
	}

	@SchemaMapping(typeName = "Account", field = "balance")
	public String balance(final AccountDto account) {
		return entryService.findLast(account.getId())
				.map(AccountEntry::getBalanceAfter)
				.orElse(BigDecimal.ZERO)
				.toPlainString();
	}

	@SchemaMapping(typeName = "Account", field = "transactions")
	public List<TransactionDto> getTransactions(final AccountDto account) {
		return entryService.getAll(account.getId())
				.stream()
				.map(AccountEntry::getTransactionId)
				.map(transactionService::get)
				.map(TransactionDto::fromBdo)
				.toList();
	}
}
