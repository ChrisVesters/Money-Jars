package com.cvesters.moneyjars.transaction;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.transaction.bdo.Transaction;
import com.cvesters.moneyjars.transaction.bdo.TransactionAction;
import com.cvesters.moneyjars.transaction.dto.TransactionActionDto;
import com.cvesters.moneyjars.transaction.dto.TransactionDto;

@Controller
public class TransactionController {

	private final TransactionService transactionService;

	public TransactionController(final TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@QueryMapping
	public List<TransactionDto> getTransactions() {
		return transactionService.getAll()
				.stream()
				.map(TransactionDto::new)
				.toList();
	}

	@QueryMapping
	public TransactionDto getTransaction(@Argument final long id) {
		return transactionService.find(id)
				.map(TransactionDto::new)
				.orElse(null);
	}

	@MutationMapping
	public TransactionDto createTransaction(
			@Argument final TransactionActionDto.Create req) {
		final TransactionAction.Create action = req.toBdo();
		final Transaction created = transactionService.create(action);
		return new TransactionDto(created);
	}

	@MutationMapping
	public TransactionDto updateTransaction(@Argument final long id,
			@Argument final TransactionActionDto.Update req) {
		final TransactionAction.Update action = req.toBdo();
		final Transaction updated = transactionService.update(id, action);
		return new TransactionDto(updated);
	}

	@MutationMapping
	public boolean deleteTransaction(@Argument final long id) {
		transactionService.delete(id);
		return true;
	}

}
