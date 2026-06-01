package com.cvesters.moneyjars.jar.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.jar.TestJar;
import com.cvesters.moneyjars.jar.bdo.Jar;

class JarDaoTest {

	private static final TestJar JAR = TestJar.HOUSEHOLD;

	@Nested
	class Constructor {

		@Test
		void success() {
			final var dao = new JarDao(JAR.bdo());

			assertThat(dao.getId()).isNull();
			assertThat(dao.getName()).isEqualTo(JAR.getName());
			assertThat(dao.getDescription()).isEqualTo(JAR.getDescription());
			assertThat(dao.getBalance()).isEqualTo(JAR.getBalance());
		}

		@Test
		void bdoNull() {
			assertThatThrownBy(() -> new JarDao(null))
					.isInstanceOf(NullPointerException.class);
		}

	}

	@Nested
	class Update {

		@Test
		void success() {
			final String name = "Groceries";
			final String description = "A man has to eat";
			final Jar update = new Jar(name, description);

			final var dao = new JarDao(JAR.bdo());
			dao.updateWith(update);

			assertThat(dao.getId()).isNull();
			assertThat(dao.getName()).isEqualTo(name);
			assertThat(dao.getDescription()).isEqualTo(description);
			assertThat(dao.getBalance()).isEqualTo(JAR.getBalance());
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
			assertThat(bdo.getBalance()).isEqualTo(JAR.getBalance());
		}
	}
}
