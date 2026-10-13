package com.cvesters.moneyjars.jar.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.jar.TestJar;
import com.cvesters.moneyjars.jar.bdo.JarAction;

class JarActionDtoTest {

	private static final TestJar JAR = TestJar.HOLIDAY;

	@Nested
	class Create {

		@Test
		void success() {
			final var dto = new JarActionDto.Create(JAR.getName(),
					JAR.getDescription(), JAR.getGoalString());

			assertThat(dto.name()).isEqualTo(JAR.getName());
			assertThat(dto.description()).isEqualTo(JAR.getDescription());
			assertThat(dto.goal()).isEqualTo(JAR.getGoalString());
		}

		@Test
		void toBdo() {
			final var dto = new JarActionDto.Create(JAR.getName(),
					JAR.getDescription(), JAR.getGoalString());

			final JarAction.Create bdo = dto.toBdo();

			assertThat(bdo.name()).isEqualTo(JAR.getName());
			assertThat(bdo.description()).isEqualTo(JAR.getDescription());
			assertThat(bdo.goal()).isEqualTo(JAR.getGoal());
		}

		@Test
		void toBdoGoalNull() {
			final var dto = new JarActionDto.Create(JAR.getName(),
					JAR.getDescription(), null);

			final JarAction.Create bdo = dto.toBdo();

			assertThat(bdo.name()).isEqualTo(JAR.getName());
			assertThat(bdo.description()).isEqualTo(JAR.getDescription());
			assertThat(bdo.goal()).isNull();
		}

		@Test
		void toBdoGoalInvalid() {
			final var dto = new JarActionDto.Create(JAR.getName(),
					JAR.getDescription(), "abc");

			assertThatThrownBy(dto::toBdo)
					.isInstanceOf(NumberFormatException.class);
		}
	}

	@Nested
	class Update {

		@Test
		void success() {
			final var dto = new JarActionDto.Update(JAR.getName(),
					JAR.getDescription(), JAR.getGoalString());

			assertThat(dto.name()).isEqualTo(JAR.getName());
			assertThat(dto.description()).isEqualTo(JAR.getDescription());
			assertThat(dto.goal()).isEqualTo(JAR.getGoalString());
		}

		@Test
		void toBdo() {
			final var dto = new JarActionDto.Update(JAR.getName(),
					JAR.getDescription(), JAR.getGoalString());

			final var bdo = dto.toBdo();

			assertThat(bdo.name()).isEqualTo(JAR.getName());
			assertThat(bdo.description()).isEqualTo(JAR.getDescription());
			assertThat(bdo.goal()).isEqualTo(JAR.getGoal());
		}

		@Test
		void toBdoGoalNull() {
			final var dto = new JarActionDto.Update(JAR.getName(),
					JAR.getDescription(), null);

			final var bdo = dto.toBdo();

			assertThat(bdo.name()).isEqualTo(JAR.getName());
			assertThat(bdo.description()).isEqualTo(JAR.getDescription());
			assertThat(bdo.goal()).isNull();
		}

		@Test
		void toBdoGoalInvalid() {
			final var dto = new JarActionDto.Update(JAR.getName(),
					JAR.getDescription(), "abc");

			assertThatThrownBy(dto::toBdo)
					.isInstanceOf(NumberFormatException.class);
		}
	}
}
