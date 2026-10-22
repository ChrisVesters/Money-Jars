package com.cvesters.moneyjars.transaction;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.transaction.bdo.JarTransferTransaction;
import com.cvesters.moneyjars.transaction.bdo.JarTransferTransactionAction;
import com.cvesters.moneyjars.transaction.dto.JarTransferTransactionActionDto;
import com.cvesters.moneyjars.transaction.dto.JarTransferTransactionDto;
import com.cvesters.moneyjars.transaction.dto.TransactionDto;

@Controller
public class JarTransferTransactionController {

	private final JarTransferTransactionService transactionService;

	public JarTransferTransactionController(
			final JarTransferTransactionService JarTransferTransactionService) {
		this.transactionService = JarTransferTransactionService;
	}

	@MutationMapping
	public JarTransferTransactionDto createJarTransferTransaction(
			@Argument final JarTransferTransactionActionDto.Create req) {
		final JarTransferTransactionAction.Create action = req.toBdo();
		final JarTransferTransaction created = transactionService
				.create(action);
		return new JarTransferTransactionDto(created);
	}

	@MutationMapping
	public TransactionDto updateJarTransferTransaction(@Argument final long id,
			@Argument final JarTransferTransactionActionDto.Update req) {
		final JarTransferTransactionAction.Update action = req.toBdo();
		final JarTransferTransaction updated = transactionService.update(id,
				action);
		return new JarTransferTransactionDto(updated);
	}

	@MutationMapping
	public boolean deleteJarTransferTransaction(@Argument final long id) {
		transactionService.delete(id);
		return true;
	}
}
