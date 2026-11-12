package com.cvesters.moneyjars.transaction.bdo;

import java.util.Objects;

import com.cvesters.moneyjars.common.bdo.Pagination;

public record TransactionSearch(TransactionFilter filter,
		Pagination pagination) {

	public TransactionSearch {
		Objects.requireNonNull(filter);
		Objects.requireNonNull(pagination);
	}

}
