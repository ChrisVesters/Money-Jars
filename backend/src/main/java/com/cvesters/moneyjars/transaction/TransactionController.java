package com.cvesters.moneyjars.transaction;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;
import com.cvesters.moneyjars.transaction.bdo.Transaction;
import com.cvesters.moneyjars.transaction.bdo.TransactionAction;
import com.cvesters.moneyjars.transaction.dto.PaymentTransactionActionDto;
import com.cvesters.moneyjars.transaction.dto.PaymentTransactionDto;
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
				.map(TransactionController::toDto)
				.toList();
	}

	@QueryMapping
	public TransactionDto getTransaction(@Argument final long id) {
		return transactionService.find(id)
				.map(TransactionController::toDto)
				.orElse(null);
	}

	@MutationMapping
	public TransactionDto createPaymentTransaction(
			@Argument final PaymentTransactionActionDto.CreatePayment req) {
		final TransactionAction.CreatePayment action = req.toBdo();
		final Transaction created = transactionService.create(action);
		return toDto(created);
	}

	@MutationMapping
	public TransactionDto updatePaymentTransaction(@Argument final long id,
			@Argument final PaymentTransactionActionDto.UpdatePayment req) {
		final TransactionAction.UpdatePayment action = req.toBdo();
		final Transaction updated = transactionService.update(id, action);
		return toDto(updated);
	}

	@MutationMapping
	public boolean deleteTransaction(@Argument final long id) {
		transactionService.delete(id);
		return true;
	}

	private static TransactionDto toDto(final Transaction transaction) {
		return switch (transaction) {
			case PaymentTransaction paymentTransaction -> new PaymentTransactionDto(
					paymentTransaction);
		};
	}

}
