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

	private static final TestJar JAR = TestJar.HOLIDAY;

	@Nested
	class Create {

		@Test
		void success() {
			final var action = new JarAction.Create(JAR.getName(),
					JAR.getDescription(), JAR.getGoal());

			assertThat(action.name()).isEqualTo(JAR.getName());
			assertThat(action.description()).isEqualTo(JAR.getDescription());
			assertThat(action.goal()).isEqualTo(JAR.getGoal());
		}

		@Test
		void nameNull() {
			final String name = null;
			final String description = JAR.getDescription();
			final BigDecimal goal = JAR.getGoal();

			assertThatThrownBy(
					() -> new JarAction.Create(name, description, goal))
							.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void nameInvalid(final String name) {
			final String description = JAR.getDescription();
			final BigDecimal goal = JAR.getGoal();

			assertThatThrownBy(
					() -> new JarAction.Create(name, description, goal))
							.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			final String name = JAR.getName();
			final String description = null;
			final BigDecimal goal = JAR.getGoal();

			assertThatThrownBy(
					() -> new JarAction.Create(name, description, goal))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void goalNull() {
			final var action = new JarAction.Create(JAR.getName(),
					JAR.getDescription(), null);

			assertThat(action.name()).isEqualTo(JAR.getName());
			assertThat(action.description()).isEqualTo(JAR.getDescription());
			assertThat(action.goal()).isNull();
		}

		@Test
		void toBdo() {
			final var action = new JarAction.Create(JAR.getName(),
					JAR.getDescription(), JAR.getGoal());

			final Jar bdo = action.toBdo();

			assertThat(bdo.getId()).isNull();
			assertThat(bdo.getName()).isEqualTo(JAR.getName());
			assertThat(bdo.getDescription()).isEqualTo(JAR.getDescription());
			assertThat(bdo.getGoal()).contains(JAR.getGoal());
		}

		@Test
		void toBdoGoalNull() {
			final var action = new JarAction.Create(JAR.getName(),
					JAR.getDescription(), null);

			final Jar bdo = action.toBdo();

			assertThat(bdo.getId()).isNull();
			assertThat(bdo.getName()).isEqualTo(JAR.getName());
			assertThat(bdo.getDescription()).isEqualTo(JAR.getDescription());
			assertThat(bdo.getGoal()).isEmpty();
		}
	}

	@Nested
	class Update {

		@Test
		void success() {
			final var action = new JarAction.Update(JAR.getName(),
					JAR.getDescription(), JAR.getGoal());

			assertThat(action.name()).isEqualTo(JAR.getName());
			assertThat(action.description()).isEqualTo(JAR.getDescription());
			assertThat(action.goal()).isEqualTo(JAR.getGoal());
		}

		@Test
		void nameNull() {
			final String name = null;
			final String description = JAR.getDescription();
			final BigDecimal goal = JAR.getGoal();

			assertThatThrownBy(
					() -> new JarAction.Update(name, description, goal))
							.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void nameInvalid(final String name) {
			final String description = JAR.getDescription();
			final BigDecimal goal = JAR.getGoal();

			assertThatThrownBy(
					() -> new JarAction.Update(name, description, goal))
							.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			final String name = JAR.getName();
			final String description = null;
			final BigDecimal goal = JAR.getGoal();

			assertThatThrownBy(
					() -> new JarAction.Update(name, description, goal))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void goalNull() {
			final var action = new JarAction.Update(JAR.getName(),
					JAR.getDescription(), null);

			assertThat(action.name()).isEqualTo(JAR.getName());
			assertThat(action.description()).isEqualTo(JAR.getDescription());
			assertThat(action.goal()).isNull();
		}

		@Test
		void applyOn() {
			final var action = new JarAction.Update(JAR.getName(),
					JAR.getDescription(), JAR.getGoal());

			final Jar target = mock();

			action.applyOn(target);

			verify(target).setName(JAR.getName());
			verify(target).setDescription(JAR.getDescription());
			verify(target).setGoal(JAR.getGoal());
		}

		@Test
		void applyOnGoalNull() {
			final var action = new JarAction.Update(JAR.getName(),
					JAR.getDescription(), null);

			final Jar target = mock();

			action.applyOn(target);

			verify(target).setName(JAR.getName());
			verify(target).setDescription(JAR.getDescription());
			verify(target).setGoal(null);
		}

		@Test
		void applyOnTargetNull() {
			final var action = new JarAction.Update(JAR.getName(),
					JAR.getDescription(), JAR.getGoal());

			assertThatThrownBy(() -> action.applyOn(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
