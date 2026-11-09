package com.cvesters.moneyjars.transaction.dao;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionCategoryTotal;

public record PaymentTransactionCategoryTotalDao(Long categoryId,
		short direction, BigDecimal amount) {

	public PaymentTransactionCategoryTotal toBdo() {
		return PaymentTransactionCategoryTotal.of(categoryId,
				PaymentTransactionDirectionDao.toBdo(direction), amount);
	}

	public static List<PaymentTransactionCategoryTotal> toBdo(
			final List<PaymentTransactionCategoryTotalDao> daos) {
		Objects.requireNonNull(daos);

		final Map<Long, PaymentTransactionCategoryTotal> totals = new LinkedHashMap<>();
		for (final PaymentTransactionCategoryTotalDao dao : daos) {
			totals.merge(dao.categoryId(), dao.toBdo(),
					PaymentTransactionCategoryTotal::add);
		}
		return List.copyOf(totals.values());
	}
}
