package com.cvesters.moneyjars.jar;

import java.math.BigDecimal;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.common.bdo.PagedResult;
import com.cvesters.moneyjars.jar.bdo.JarEntry;
import com.cvesters.moneyjars.jar.dto.JarDto;
import com.cvesters.moneyjars.transaction.bdo.Transaction;
import com.cvesters.moneyjars.transaction.bdo.TransactionSearch;
import com.cvesters.moneyjars.transaction.dto.TransactionPageDto;
import com.cvesters.moneyjars.transaction.dto.TransactionSearchDto;

@Controller
public class JarResolver {

	private final JarEntryService entryService;

	public JarResolver(final JarEntryService entryService) {
		this.entryService = entryService;
	}

	@SchemaMapping(typeName = "Jar", field = "balance")
	public String balance(final JarDto jar) {
		return entryService.findLast(jar.getId())
				.map(JarEntry::getBalanceAfter)
				.orElse(BigDecimal.ZERO)
				.toPlainString();
	}

	@SchemaMapping(typeName = "Jar", field = "inUse")
	public boolean inUse(final JarDto jar) {
		return entryService.exists(jar.getId());
	}

	@SchemaMapping(typeName = "Jar", field = "transactions")
	public TransactionPageDto getTransactions(final JarDto jar,
			@Argument final TransactionSearchDto query) {
		final TransactionSearch bdo = query.toBdo();
		final PagedResult<Transaction> result = entryService
				.searchTransactions(jar.getId(), bdo);
		return new TransactionPageDto(result);
	}
}
