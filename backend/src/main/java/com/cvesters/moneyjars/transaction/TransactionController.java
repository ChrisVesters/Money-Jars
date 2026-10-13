package com.cvesters.moneyjars.transaction;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.transaction.bdo.Transaction;
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
				.map(TransactionDto::fromBdo)
				.toList();
	}

	@QueryMapping
	public TransactionDto getTransaction(@Argument final long id) {
		final Transaction transaction = transactionService.get(id);
		return TransactionDto.fromBdo(transaction);
	}

}
