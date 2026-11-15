package com.cvesters.moneyjars.transaction.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.common.bdo.Pagination;

class TransactionSearchTest {

	@Nested
	class Constructor {

		@Test
		void success() {
			final TransactionFilter filter = mock();
			final Pagination pagination = mock();

			final var transactionSearch = new TransactionSearch(filter,
					pagination);

			assertThat(transactionSearch.filter()).isEqualTo(filter);
			assertThat(transactionSearch.pagination()).isEqualTo(pagination);
		}

		@Test
		void filterNull() {
			final TransactionFilter filter = null;
			final Pagination pagination = mock();

			assertThatThrownBy(() -> new TransactionSearch(filter, pagination))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void paginationNull() {
			final TransactionFilter filter = mock();
			final Pagination pagination = null;

			assertThatThrownBy(() -> new TransactionSearch(filter, pagination))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
