package com.cvesters.moneyjars.transaction;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;
import com.cvesters.moneyjars.transaction.bdo.TransactionAction;
import com.cvesters.moneyjars.transaction.dto.PaymentTransactionActionDto;
import com.cvesters.moneyjars.transaction.dto.PaymentTransactionDto;
import com.cvesters.moneyjars.transaction.dto.TransactionDto;

@Controller
public class PaymentTransactionController {

	private final PaymentTransactionService paymentTransactionService;

	public PaymentTransactionController(
			final PaymentTransactionService paymentTransactionService) {
		this.paymentTransactionService = paymentTransactionService;
	}

	@MutationMapping
	public PaymentTransactionDto createPaymentTransaction(
			@Argument final PaymentTransactionActionDto.CreatePayment req) {
		final TransactionAction.CreatePayment action = req.toBdo();
		final PaymentTransaction created = paymentTransactionService
				.create(action);
		return new PaymentTransactionDto(created);
	}

	@MutationMapping
	public TransactionDto updatePaymentTransaction(@Argument final long id,
			@Argument final PaymentTransactionActionDto.UpdatePayment req) {
		final TransactionAction.UpdatePayment action = req.toBdo();
		final PaymentTransaction updated = paymentTransactionService.update(id, action);
		return new PaymentTransactionDto(updated);
	}
}
