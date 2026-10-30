package com.cvesters.moneyjars.transaction;

import java.util.Optional;

import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.account.AccountService;
import com.cvesters.moneyjars.account.bdo.Account;
import com.cvesters.moneyjars.account.dto.AccountDto;
import com.cvesters.moneyjars.category.CategoryService;
import com.cvesters.moneyjars.category.dto.CategoryDto;
import com.cvesters.moneyjars.jar.JarService;
import com.cvesters.moneyjars.jar.bdo.Jar;
import com.cvesters.moneyjars.jar.dto.JarDto;
import com.cvesters.moneyjars.transaction.dto.PaymentTransactionDto;

@Controller
public class PaymentTransactionResolver {

	private final JarService jarService;
	private final AccountService accountService;
	private final CategoryService categoryService;

	public PaymentTransactionResolver(final JarService jarService,
			final AccountService accountService,
			final CategoryService categoryService) {
		this.jarService = jarService;
		this.accountService = accountService;
		this.categoryService = categoryService;
	}

	@SchemaMapping(typeName = "PaymentTransaction", field = "jar")
	public JarDto jar(final PaymentTransactionDto transaction) {
		final Jar jar = jarService.get(transaction.getJarId());
		return new JarDto(jar);
	}

	@SchemaMapping(typeName = "PaymentTransaction", field = "account")
	public AccountDto account(final PaymentTransactionDto transaction) {
		final Account account = accountService.get(transaction.getAccountId());
		return new AccountDto(account);
	}

	@SchemaMapping(typeName = "PaymentTransaction", field = "category")
	public CategoryDto category(final PaymentTransactionDto transaction) {
		return Optional.ofNullable(transaction.getCategoryId())
				.map(categoryService::get)
				.map(CategoryDto::new)
				.orElse(null);
	}
}
