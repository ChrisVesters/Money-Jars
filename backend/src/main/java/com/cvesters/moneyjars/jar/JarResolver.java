package com.cvesters.moneyjars.jar;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.jar.bdo.JarEntry;
import com.cvesters.moneyjars.jar.dto.JarDto;
import com.cvesters.moneyjars.transaction.TransactionService;
import com.cvesters.moneyjars.transaction.dto.TransactionDto;

@Controller
public class JarResolver {

	private final JarEntryService entryService;
	private final TransactionService transactionService;

	public JarResolver(final JarEntryService entryService,
			final TransactionService transactionService) {
		this.entryService = entryService;
		this.transactionService = transactionService;
	}

	@SchemaMapping(typeName = "Jar", field = "balance")
	public String balance(final JarDto jar) {
		return entryService.findLast(jar.getId())
				.map(JarEntry::getBalanceAfter)
				.orElse(BigDecimal.ZERO)
				.toPlainString();
	}

	@SchemaMapping(typeName = "Jar", field = "transactions")
	public List<TransactionDto> getTransactions(final JarDto jar) {
		return entryService.getAll(jar.getId())
				.stream()
				.map(JarEntry::getTransactionId)
				.map(transactionService::get)
				.map(TransactionDto::fromBdo)
				.toList();
	}
}
