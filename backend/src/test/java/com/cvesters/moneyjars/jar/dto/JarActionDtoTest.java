package com.cvesters.moneyjars.jar.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.jar.TestJar;

class JarActionDtoTest {

	private static final TestJar JAR = TestJar.HOUSEHOLD;

	@Nested
	class Create {

		@Test
		void success() {
			final var dto = new JarActionDto.Create(JAR.getName(),
					JAR.getDescription());

			assertThat(dto.name()).isEqualTo(JAR.getName());
			assertThat(dto.description()).isEqualTo(JAR.getDescription());
		}

		@Test
		void toBdo() {
			final var dto = new JarActionDto.Create(JAR.getName(),
					JAR.getDescription());

			final var bdo = dto.toBdo();

			assertThat(bdo.name()).isEqualTo(JAR.getName());
			assertThat(bdo.description()).isEqualTo(JAR.getDescription());
		}
	}

	@Nested
	class Update {

		@Test
		void success() {
			final var dto = new JarActionDto.Update(JAR.getName(),
					JAR.getDescription());

			assertThat(dto.name()).isEqualTo(JAR.getName());
			assertThat(dto.description()).isEqualTo(JAR.getDescription());
		}

		@Test
		void toBdo() {
			final var dto = new JarActionDto.Update(JAR.getName(),
					JAR.getDescription());

			final var bdo = dto.toBdo();

			assertThat(bdo.name()).isEqualTo(JAR.getName());
			assertThat(bdo.description()).isEqualTo(JAR.getDescription());
		}
	}
}
