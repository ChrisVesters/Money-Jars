package com.cvesters.moneyjars.transaction.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.cvesters.moneyjars.common.bdo.Pagination;
import com.cvesters.moneyjars.transaction.bdo.TransactionFilter;
import com.cvesters.moneyjars.transaction.bdo.TransactionQuery;

class TransactionQueryDtoTest {

	private static final String FROM = "2026-01-10";
	private static final String TO = "2026-01-15";

	@Nested
	class ToBdo {

		@Test
		void success() {
			final var filter = new TransactionQueryDto.FilterDto(FROM, TO);
			final var dto = new TransactionQueryDto(filter, 2, 50);

			final TransactionQuery bdo = dto.toBdo();

			final var expected = new TransactionQuery(
					new TransactionFilter(LocalDate.of(2026, 1, 10),
							LocalDate.of(2026, 1, 15)),
					new Pagination(2, 50));
			assertThat(bdo).isEqualTo(expected);
		}

		@Test
		void fromNull() {
			final var filter = new TransactionQueryDto.FilterDto(null, TO);
			final var dto = new TransactionQueryDto(filter, 0, 100);

			final TransactionQuery bdo = dto.toBdo();

			final var expected = new TransactionQuery(
					new TransactionFilter(null, LocalDate.of(2026, 1, 15)),
					new Pagination(0, 100));
			assertThat(bdo).isEqualTo(expected);
		}

		@Test
		void toNull() {
			final var filter = new TransactionQueryDto.FilterDto(FROM, null);
			final var dto = new TransactionQueryDto(filter, 0, 100);

			final TransactionQuery bdo = dto.toBdo();

			final var expected = new TransactionQuery(
					new TransactionFilter(LocalDate.of(2026, 1, 10), null),
					new Pagination(0, 100));
			assertThat(bdo).isEqualTo(expected);
		}

		@Test
		void fromAndToNull() {
			final var filter = new TransactionQueryDto.FilterDto(null, null);
			final var dto = new TransactionQueryDto(filter, 0, 100);

			final TransactionQuery bdo = dto.toBdo();

			final var expected = new TransactionQuery(
					new TransactionFilter(null, null), new Pagination(0, 100));
			assertThat(bdo).isEqualTo(expected);
		}

		@Test
		void fromInvalid() {
			final var filter = new TransactionQueryDto.FilterDto("invalid", TO);
			final var dto = new TransactionQueryDto(filter, 0, 100);

			assertThatThrownBy(dto::toBdo)
					.isInstanceOf(DateTimeParseException.class);
		}

		@Test
		void toInvalid() {
			final var filter = new TransactionQueryDto.FilterDto(FROM,
					"invalid");
			final var dto = new TransactionQueryDto(filter, 0, 100);

			assertThatThrownBy(dto::toBdo)
					.isInstanceOf(DateTimeParseException.class);
		}

		@Test
		void filterNull() {
			final var dto = new TransactionQueryDto(null, 0, 100);

			assertThatThrownBy(dto::toBdo)
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void pageNegative() {
			final var filter = new TransactionQueryDto.FilterDto(FROM, TO);
			final var dto = new TransactionQueryDto(filter, -1, 100);

			assertThatThrownBy(dto::toBdo)
					.isInstanceOf(IllegalArgumentException.class);
		}

		@ParameterizedTest
		@ValueSource(ints = { 0, -1 })
		void sizeInvalid(final int size) {
			final var filter = new TransactionQueryDto.FilterDto(FROM, TO);
			final var dto = new TransactionQueryDto(filter, 0, size);

			assertThatThrownBy(dto::toBdo)
					.isInstanceOf(IllegalArgumentException.class);
		}
	}
}
