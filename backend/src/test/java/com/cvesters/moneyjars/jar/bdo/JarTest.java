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

	private static final TestJar JAR = TestJar.HOUSEHOLD;

	@Nested
	class Constructor {

		@Test
		void withoutId() {
			final var jar = new Jar(JAR.getName(), JAR.getDescription());

			assertThat(jar.getId()).isNull();
			assertThat(jar.getName()).isEqualTo(JAR.getName());
			assertThat(jar.getDescription()).isEqualTo(JAR.getDescription());
			assertThat(jar.getBalance()).isEqualTo(BigDecimal.ZERO);
		}

		@Test
		void withId() {
			final var jar = new Jar(JAR.getId(), JAR.getName(),
					JAR.getDescription(), JAR.getBalance());

			assertThat(jar.getId()).isEqualTo(JAR.getId());
			assertThat(jar.getName()).isEqualTo(JAR.getName());
			assertThat(jar.getDescription()).isEqualTo(JAR.getDescription());
			assertThat(jar.getBalance()).isEqualTo(JAR.getBalance());
		}

		@Test
		void nameNull() {
			final long id = JAR.getId();
			final String name = null;
			final String description = JAR.getDescription();
			final BigDecimal balance = JAR.getBalance();

			assertThatThrownBy(() -> new Jar(id, name, description, balance))
					.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void nameInvalid(final String name) {
			final long id = JAR.getId();
			final String description = JAR.getDescription();
			final BigDecimal balance = JAR.getBalance();

			assertThatThrownBy(() -> new Jar(id, name, description, balance))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			final long id = JAR.getId();
			final String name = JAR.getName();
			final String description = null;
			final BigDecimal balance = JAR.getBalance();

			assertThatThrownBy(() -> new Jar(id, name, description, balance))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void balanceNull() {
			final long id = JAR.getId();
			final String name = JAR.getName();
			final String description = JAR.getDescription();
			final BigDecimal balance = null;

			assertThatThrownBy(() -> new Jar(id, name, description, balance))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class SetName {

		final Jar jar = new Jar(JAR.getId(), JAR.getName(),
				JAR.getDescription(), JAR.getBalance());

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

		final Jar jar = new Jar(JAR.getId(), JAR.getName(),
				JAR.getDescription(), JAR.getBalance());

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
}
