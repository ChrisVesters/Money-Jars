package com.cvesters.moneyjars.transaction.dto;

import java.util.List;
import java.util.Objects;

import lombok.Getter;

import com.cvesters.moneyjars.common.bdo.PagedResult;
import com.cvesters.moneyjars.transaction.bdo.Transaction;

@Getter
public class TransactionPageDto {

	private final List<TransactionDto> content;
	private final int page;
	private final int size;
	private final long totalElements;

	public TransactionPageDto(final PagedResult<Transaction> bdo) {
		Objects.requireNonNull(bdo);

		content = bdo.content().stream().map(TransactionDto::fromBdo).toList();

		page = bdo.page();
		size = bdo.size();
		totalElements = bdo.totalElements();
	}
}
