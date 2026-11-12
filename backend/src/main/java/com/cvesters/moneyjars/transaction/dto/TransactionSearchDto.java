package com.cvesters.moneyjars.transaction.dto;

import java.time.LocalDate;
import java.util.Optional;

import com.cvesters.moneyjars.common.bdo.Pagination;
import com.cvesters.moneyjars.transaction.bdo.TransactionFilter;
import com.cvesters.moneyjars.transaction.bdo.TransactionSearch;

public class TransactionSearchDto {

	private final FilterDto filter;
	private final int page;
	private final int size;

	public TransactionSearchDto(final FilterDto filter, final int page,
			final int size) {
		this.filter = filter;
		this.page = page;
		this.size = size;
	}

	public TransactionSearch toBdo() {
		final Pagination pagination = new Pagination(page, size);
		return new TransactionSearch(filter.toBdo(), pagination);
	}

	public static class FilterDto {

		private final String from;
		private final String to;

		public FilterDto(final String from, final String to) {
			this.from = from;
			this.to = to;
		}

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
