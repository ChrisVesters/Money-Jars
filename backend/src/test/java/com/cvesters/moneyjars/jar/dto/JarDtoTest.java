package com.cvesters.moneyjars.jar.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.jar.TestJar;

class JarDtoTest {

	private static final TestJar JAR = TestJar.HOUSEHOLD;

	@Nested
	class FromBdo {

		@Test
		void success() {
			final var dto = new JarDto(JAR.bdo());

			assertThat(dto.getId()).isEqualTo(JAR.getId());
			assertThat(dto.getName()).isEqualTo(JAR.getName());
			assertThat(dto.getDescription()).isEqualTo(JAR.getDescription());
			assertThat(dto.getBalance()).isEqualTo(JAR.getBalance());
		}

		@Test
		void bdoNull() {
			assertThatThrownBy(() -> new JarDto(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
