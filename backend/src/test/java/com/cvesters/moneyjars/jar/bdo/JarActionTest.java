package com.cvesters.moneyjars.jar.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.cvesters.moneyjars.jar.TestJar;

class JarActionTest {

	private static final TestJar JAR = TestJar.HOUSEHOLD;

	@Nested
	class Create {

		@Test
		void success() {
			final var action = new JarAction.Create(JAR.getName(),
					JAR.getDescription());

			assertThat(action.name()).isEqualTo(JAR.getName());
			assertThat(action.description()).isEqualTo(JAR.getDescription());
		}

		@Test
		void nameNull() {
			final String name = null;
			final String description = JAR.getDescription();

			assertThatThrownBy(() -> new JarAction.Create(name, description))
					.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void nameInvalid(final String name) {
			final String description = JAR.getDescription();

			assertThatThrownBy(() -> new JarAction.Create(name, description))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			final String name = JAR.getName();
			final String description = null;

			assertThatThrownBy(() -> new JarAction.Create(name, description))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void toBdo() {
			final var action = new JarAction.Create(JAR.getName(),
					JAR.getDescription());

			final Jar bdo = action.toBdo();

			assertThat(bdo.getId()).isNull();
			assertThat(bdo.getName()).isEqualTo(JAR.getName());
			assertThat(bdo.getDescription()).isEqualTo(JAR.getDescription());
			assertThat(bdo.getBalance()).isEqualTo(BigDecimal.ZERO);
		}
	}

	@Nested
	class Update {

		@Test
		void success() {
			final var action = new JarAction.Update(JAR.getName(),
					JAR.getDescription());

			assertThat(action.name()).isEqualTo(JAR.getName());
			assertThat(action.description()).isEqualTo(JAR.getDescription());
		}

		@Test
		void nameNull() {
			final String name = null;
			final String description = JAR.getDescription();

			assertThatThrownBy(() -> new JarAction.Update(name, description))
					.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void nameInvalid(final String name) {
			final String description = JAR.getDescription();

			assertThatThrownBy(() -> new JarAction.Update(name, description))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			final String name = JAR.getName();
			final String description = null;

			assertThatThrownBy(() -> new JarAction.Update(name, description))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void applyOn() {
			final var action = new JarAction.Update(JAR.getName(),
					JAR.getDescription());

			final Jar target = mock();

			action.applyOn(target);

			verify(target).setName(JAR.getName());
			verify(target).setDescription(JAR.getDescription());
		}

		@Test
		void applyOnTargetNull() {
			final var action = new JarAction.Update(JAR.getName(),
					JAR.getDescription());

			assertThatThrownBy(() -> action.applyOn(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
