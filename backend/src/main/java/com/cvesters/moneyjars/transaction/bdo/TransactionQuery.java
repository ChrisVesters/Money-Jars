package com.cvesters.moneyjars.transaction.bdo;

import java.util.Objects;

import com.cvesters.moneyjars.common.bdo.Pagination;

public record TransactionQuery(TransactionFilter filter,
		Pagination pagination) {

	public TransactionQuery {
		Objects.requireNonNull(filter);
		Objects.requireNonNull(pagination);
	}

}
