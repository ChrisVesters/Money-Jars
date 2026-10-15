package com.cvesters.moneyjars.common.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PaginationTest {

	@Test
	void success() {
		final var pagination = new Pagination(2, 50);

		assertThat(pagination.page()).isEqualTo(2);
		assertThat(pagination.size()).isEqualTo(50);
	}

	@Test
	void firstPage() {
		final var pagination = new Pagination(0, 50);

		assertThat(pagination.page()).isZero();
	}

	@Test
	void pageNegative() {
		assertThatThrownBy(() -> new Pagination(-1, 50))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@ParameterizedTest
	@ValueSource(ints = { 0, -1 })
	void sizeInvalid(final int size) {
		assertThatThrownBy(() -> new Pagination(0, size))
				.isInstanceOf(IllegalArgumentException.class);
	}
}
