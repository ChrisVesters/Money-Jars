package com.cvesters.moneyjars.jar.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.cvesters.moneyjars.jar.TestJar;

class JarTest {

	private static final TestJar JAR = TestJar.HOLIDAY;

	@Nested
	class Constructor {

		@Test
		void withoutId() {
			final var jar = new Jar(JAR.getName(), JAR.getDescription(),
					JAR.getGoal());

			assertThat(jar.getId()).isNull();
			assertThat(jar.getName()).isEqualTo(JAR.getName());
			assertThat(jar.getDescription()).isEqualTo(JAR.getDescription());
			assertThat(jar.getGoal()).contains(JAR.getGoal());
		}

		@Test
		void withId() {
			final var jar = new Jar(JAR.getId(), JAR.getName(),
					JAR.getDescription(), JAR.getGoal());

			assertThat(jar.getId()).isEqualTo(JAR.getId());
			assertThat(jar.getName()).isEqualTo(JAR.getName());
			assertThat(jar.getDescription()).isEqualTo(JAR.getDescription());
			assertThat(jar.getGoal()).contains(JAR.getGoal());
		}

		@Test
		void nameNull() {
			final long id = JAR.getId();
			final String name = null;
			final String description = JAR.getDescription();
			final BigDecimal goal = JAR.getGoal();

			assertThatThrownBy(() -> new Jar(id, name, description, goal))
					.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void nameInvalid(final String name) {
			final long id = JAR.getId();
			final String description = JAR.getDescription();
			final BigDecimal goal = JAR.getGoal();

			assertThatThrownBy(() -> new Jar(id, name, description, goal))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			final long id = JAR.getId();
			final String name = JAR.getName();
			final String description = null;
			final BigDecimal goal = JAR.getGoal();

			assertThatThrownBy(() -> new Jar(id, name, description, goal))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void goalNull() {
			final var jar = new Jar(JAR.getId(), JAR.getName(),
					JAR.getDescription(), null);

			assertThat(jar.getId()).isEqualTo(JAR.getId());
			assertThat(jar.getName()).isEqualTo(JAR.getName());
			assertThat(jar.getDescription()).isEqualTo(JAR.getDescription());
			assertThat(jar.getGoal()).isEmpty();
		}
	}

	@Nested
	class SetName {

		final Jar jar = JAR.bdo();

		@Test
		void success() {
			jar.setName("New name");

			assertThat(jar.getName()).isEqualTo("New name");
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void nameInvalid(final String name) {
			assertThatThrownBy(() -> jar.setName(name))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void nameNull() {
			assertThatThrownBy(() -> jar.setName(null))
					.isInstanceOf(NullPointerException.class);
		}

	}

	@Nested
	class SetDescription {

		final Jar jar = JAR.bdo();

		@Test
		void success() {
			jar.setDescription("New description");

			assertThat(jar.getDescription()).isEqualTo("New description");
		}

		@Test
		void descriptionNull() {
			assertThatThrownBy(() -> jar.setDescription(null))
					.isInstanceOf(NullPointerException.class);
		}

	}

	@Nested
	class SetGoal {

		private final Jar jar = JAR.bdo();

		@Test
		void success() {
			final BigDecimal goal = new BigDecimal("4500.50");

			jar.setGoal(goal);

			assertThat(jar.getGoal()).contains(goal);
		}

		@Test
		void goalNull() {
			jar.setGoal(null);

			assertThat(jar.getGoal()).isEmpty();
		}

		@ParameterizedTest
		@ValueSource(strings = { "-4500.50", "0" })
		void goalInvalid(final String value) {
			final BigDecimal goal = new BigDecimal(value);

			assertThatThrownBy(() -> jar.setGoal(goal))
					.isInstanceOf(IllegalArgumentException.class);
		}
	}
}
