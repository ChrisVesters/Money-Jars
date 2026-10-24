package com.cvesters.moneyjars.transaction;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.transaction.bdo.AccountTransferTransaction;
import com.cvesters.moneyjars.transaction.bdo.AccountTransferTransactionAction;
import com.cvesters.moneyjars.transaction.dto.AccountTransferTransactionActionDto;
import com.cvesters.moneyjars.transaction.dto.AccountTransferTransactionDto;
import com.cvesters.moneyjars.transaction.dto.TransactionDto;

@Controller
public class AccountTransferTransactionController {

	private final AccountTransferTransactionService transactionService;

	public AccountTransferTransactionController(
			final AccountTransferTransactionService accountTransferTransactionService) {
		this.transactionService = accountTransferTransactionService;
	}

	@MutationMapping
	public AccountTransferTransactionDto createAccountTransferTransaction(
			@Argument final AccountTransferTransactionActionDto.Create req) {
		final AccountTransferTransactionAction.Create action = req.toBdo();
		final AccountTransferTransaction created = transactionService
				.create(action);
		return new AccountTransferTransactionDto(created);
	}

	@MutationMapping
	public TransactionDto updateAccountTransferTransaction(
			@Argument final long id,
			@Argument final AccountTransferTransactionActionDto.Update req) {
		final AccountTransferTransactionAction.Update action = req.toBdo();
		final AccountTransferTransaction updated = transactionService.update(id,
				action);
		return new AccountTransferTransactionDto(updated);
	}

	@MutationMapping
	public boolean deleteAccountTransferTransaction(@Argument final long id) {
		transactionService.delete(id);
		return true;
	}
}
