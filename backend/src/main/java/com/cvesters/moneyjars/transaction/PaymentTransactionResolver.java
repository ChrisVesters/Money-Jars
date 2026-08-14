package com.cvesters.moneyjars.transaction;

import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.account.AccountService;
import com.cvesters.moneyjars.account.dto.AccountDto;
import com.cvesters.moneyjars.common.exceptions.MissingEntityException;
import com.cvesters.moneyjars.jar.JarService;
import com.cvesters.moneyjars.jar.dto.JarDto;
import com.cvesters.moneyjars.transaction.dto.PaymentTransactionDto;

@Controller
public class PaymentTransactionResolver {

	private final JarService jarService;
	private final AccountService accountService;

	public PaymentTransactionResolver(final JarService jarService,
			final AccountService accountService) {
		this.jarService = jarService;
		this.accountService = accountService;
	}

	@SchemaMapping(typeName = "PaymentTransaction", field = "jar")
	public JarDto jar(final PaymentTransactionDto transaction) {
		return jarService.find(transaction.getJarId())
				.map(JarDto::new)
				.orElseThrow(MissingEntityException::new);
	}

	@SchemaMapping(typeName = "PaymentTransaction", field = "account")
	public AccountDto account(final PaymentTransactionDto transaction) {
		return accountService.find(transaction.getAccountId())
				.map(AccountDto::new)
				.orElseThrow(MissingEntityException::new);
	}
}
