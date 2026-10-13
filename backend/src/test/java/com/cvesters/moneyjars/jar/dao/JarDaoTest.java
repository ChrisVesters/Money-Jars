package com.cvesters.moneyjars.jar.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.jar.TestJar;
import com.cvesters.moneyjars.jar.bdo.Jar;

class JarDaoTest {

	private static final TestJar JAR = TestJar.HOLIDAY;

	@Nested
	class Constructor {

		@Test
		void success() {
			final var dao = new JarDao(JAR.bdo());

			assertThat(dao.getId()).isNull();
			assertThat(dao.getName()).isEqualTo(JAR.getName());
			assertThat(dao.getDescription()).isEqualTo(JAR.getDescription());
			assertThat(dao.getGoal()).isEqualTo(JAR.getGoal());
		}

		@Test
		void goalNull() {
			final TestJar jar = TestJar.HOUSEHOLD;
			final var dao = new JarDao(jar.bdo());

			assertThat(dao.getId()).isNull();
			assertThat(dao.getName()).isEqualTo(jar.getName());
			assertThat(dao.getDescription()).isEqualTo(jar.getDescription());
			assertThat(dao.getGoal()).isNull();
		}

		@Test
		void jarNull() {
			assertThatThrownBy(() -> new JarDao(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class UpdateWith {

		@Test
		void success() {
			final String name = "Groceries";
			final String description = "A man has to eat";
			final BigDecimal goal = new BigDecimal("250.00");
			final Jar update = new Jar(name, description, goal);

			final var dao = new JarDao(JAR.bdo());
			dao.updateWith(update);

			assertThat(dao.getId()).isNull();
			assertThat(dao.getName()).isEqualTo(name);
			assertThat(dao.getDescription()).isEqualTo(description);
			assertThat(dao.getGoal()).isEqualTo(goal);
		}

		@Test
		void goalNull() {
			final String name = "Groceries";
			final String description = "A man has to eat";
			final Jar update = new Jar(name, description, null);

			final var dao = new JarDao(JAR.bdo());
			dao.updateWith(update);

			assertThat(dao.getId()).isNull();
			assertThat(dao.getName()).isEqualTo(name);
			assertThat(dao.getDescription()).isEqualTo(description);
			assertThat(dao.getGoal()).isNull();
		}

		@Test
		void jarNull() {
			final var dao = new JarDao(JAR.bdo());
			assertThatThrownBy(() -> dao.updateWith(null))
					.isInstanceOf(NullPointerException.class);
		}

	}

	@Nested
	class ToBdo {

		@Test
		void success() {
			final var dao = new JarDao(JAR.bdo());
			final var bdo = dao.toBdo();

			assertThat(bdo.getId()).isNull();
			assertThat(bdo.getName()).isEqualTo(JAR.getName());
			assertThat(bdo.getDescription()).isEqualTo(JAR.getDescription());
			assertThat(bdo.getGoal()).contains(JAR.getGoal());
		}

		@Test
		void goalNull() {
			final TestJar jar = TestJar.HOUSEHOLD;
			final var dao = new JarDao(jar.bdo());
			final var bdo = dao.toBdo();

			assertThat(bdo.getId()).isNull();
			assertThat(bdo.getName()).isEqualTo(jar.getName());
			assertThat(bdo.getDescription()).isEqualTo(jar.getDescription());
			assertThat(bdo.getGoal()).isEmpty();
		}
	}
}
