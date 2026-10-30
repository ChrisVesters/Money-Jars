package com.cvesters.moneyjars.category;

import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.cvesters.moneyjars.category.dto.CategoryDto;
import com.cvesters.moneyjars.transaction.PaymentTransactionService;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionFilter;

@Controller
public class CategoryResolver {

	private final PaymentTransactionService paymentTransactionService;

	public CategoryResolver(final PaymentTransactionService paymentTransactionService) {
		this.paymentTransactionService = paymentTransactionService;
	}

	@SchemaMapping(typeName = "Category", field = "inUse")
	public boolean inUse(final CategoryDto category) {
		final var filter = new PaymentTransactionFilter(category.getId());
		return paymentTransactionService.count(filter) > 0;
	}
}
