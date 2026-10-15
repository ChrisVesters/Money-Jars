package com.cvesters.moneyjars.common.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PagedResultTest {

	private static final List<String> CONTENT = List.of("a", "b");

	@Test
	void success() {
		final var result = new PagedResult<>(CONTENT, 1, 2, 5);

		assertThat(result.content()).isEqualTo(CONTENT);
		assertThat(result.page()).isEqualTo(1);
		assertThat(result.size()).isEqualTo(2);
		assertThat(result.totalElements()).isEqualTo(5);
	}

	@Test
	void empty() {
		final var result = new PagedResult<String>(Collections.emptyList(), 0,
				20, 0);

		assertThat(result.content()).isEmpty();
		assertThat(result.page()).isZero();
		assertThat(result.size()).isEqualTo(20);
		assertThat(result.totalElements()).isZero();
	}

	@Test
	void contentNull() {
		assertThatThrownBy(() -> new PagedResult<String>(null, 0, 20, 0))
				.isInstanceOf(NullPointerException.class);
	}

	@Test
	void contentLargerThanSize() {
		assertThatThrownBy(() -> new PagedResult<>(CONTENT, 0, 1, 2))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void pageNegative() {
		assertThatThrownBy(() -> new PagedResult<>(CONTENT, -1, 20, 2))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@ParameterizedTest
	@ValueSource(ints = { 0, -1 })
	void sizeInvalid(final int size) {
		assertThatThrownBy(
				() -> new PagedResult<>(Collections.emptyList(), 0, size, 0))
						.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void totalElementsNegative() {
		assertThatThrownBy(() -> new PagedResult<>(CONTENT, 0, 20, -1))
				.isInstanceOf(IllegalArgumentException.class);
	}
}
