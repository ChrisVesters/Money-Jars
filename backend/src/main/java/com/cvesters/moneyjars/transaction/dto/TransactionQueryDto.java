package com.cvesters.moneyjars.transaction.dto;

import java.time.LocalDate;
import java.util.Optional;

import com.cvesters.moneyjars.common.bdo.Pagination;
import com.cvesters.moneyjars.transaction.bdo.TransactionFilter;
import com.cvesters.moneyjars.transaction.bdo.TransactionQuery;

public class TransactionQueryDto {

	private final FilterDto filter;
	private final int page;
	private final int size;

	public TransactionQueryDto(final FilterDto filter, final int page,
			final int size) {
		this.filter = filter;
		this.page = page;
		this.size = size;
	}

	public TransactionQuery toBdo() {
		final Pagination pagination = new Pagination(page, size);
		return new TransactionQuery(filter.toBdo(), pagination);
	}

	public static record FilterDto(String from, String to) {

		private LocalDate toDate(final String value) {
			return Optional.ofNullable(value)
					.map(LocalDate::parse)
					.orElse(null);
		}

		public TransactionFilter toBdo() {
			return new TransactionFilter(toDate(from), toDate(to));
		}
	}
}
