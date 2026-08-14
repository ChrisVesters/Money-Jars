package com.cvesters.moneyjars.transaction.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionDirection;

class PaymentTransactionDirectionDtoTest {

	@Nested
	class ToBdo {

		@ParameterizedTest
		@MethodSource("com.cvesters.moneyjars.transaction.dto.PaymentTransactionDirectionDtoTest#mapping")
		void success(final String dto, final PaymentTransactionDirection bdo) {
			final PaymentTransactionDirection result = PaymentTransactionDirectionDto
					.toBdo(dto);

			assertThat(result).isEqualTo(bdo);
		}

		@Test
		void invalid() {
			assertThatThrownBy(
					() -> PaymentTransactionDirectionDto.toBdo("INVALID"))
							.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void dtoNull() {
			assertThatThrownBy(() -> PaymentTransactionDirectionDto.toBdo(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class ToDto {

		@ParameterizedTest
		@MethodSource("com.cvesters.moneyjars.transaction.dto.PaymentTransactionDirectionDtoTest#mapping")
		void success(final String dto, final PaymentTransactionDirection bdo) {
			final String result = PaymentTransactionDirectionDto.toDto(bdo);

			assertThat(result).isEqualTo(dto);
		}

		@Test
		void bdoNull() {
			assertThatThrownBy(() -> PaymentTransactionDirectionDto.toDto(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	static Stream<Arguments> mapping() {
		return Stream.of(
				Arguments.of("INCOMING", PaymentTransactionDirection.INCOMING),
				Arguments.of("OUTGOING", PaymentTransactionDirection.OUTGOING));
	}
}
