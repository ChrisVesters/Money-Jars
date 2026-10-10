package com.cvesters.moneyjars.jar.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.jar.TestJarEntry;
import com.cvesters.moneyjars.jar.bdo.JarEntry;

class JarEntryDaoTest {

	private static final TestJarEntry JAR_ENTRY = TestJarEntry.HOUSEHOLD_CAFE;

	@Nested
	class Constructor {

		@Test
		void success() {
			final var dao = new JarEntryDao(JAR_ENTRY.bdo());

			assertThat(dao.getId()).isNull();
			assertThat(dao.getTransactionId())
					.isEqualTo(JAR_ENTRY.getTransaction().getId());
			assertThat(dao.getJarId()).isEqualTo(JAR_ENTRY.getJar().getId());
			assertThat(dao.getBalanceBefore())
					.isEqualByComparingTo(JAR_ENTRY.getBalanceBefore());
			assertThat(dao.getBalanceAfter())
					.isEqualByComparingTo(JAR_ENTRY.getBalanceAfter());
		}

		@Test
		void entryNull() {
			assertThatThrownBy(() -> new JarEntryDao(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class UpdateWith {

		@Test
		void success() {
			final var updatedBalanceBefore = new BigDecimal("1000.00");
			final var updatedBalanceAfter = new BigDecimal("985.00");

			final var updatedEntry = new JarEntry(JAR_ENTRY.getId(),
					JAR_ENTRY.getTransaction().getId(),
					JAR_ENTRY.getJar().getId(), updatedBalanceBefore,
					updatedBalanceAfter);

			final var dao = new JarEntryDao(JAR_ENTRY.bdo());
			dao.updateWith(updatedEntry);

			assertThat(dao.getId()).isNull();
			assertThat(dao.getTransactionId())
					.isEqualTo(JAR_ENTRY.getTransaction().getId());
			assertThat(dao.getJarId()).isEqualTo(JAR_ENTRY.getJar().getId());
			assertThat(dao.getBalanceBefore())
					.isEqualByComparingTo(updatedBalanceBefore);
			assertThat(dao.getBalanceAfter())
					.isEqualByComparingTo(updatedBalanceAfter);
		}

		@Test
		void entryNull() {
			final var dao = new JarEntryDao(JAR_ENTRY.bdo());

			assertThatThrownBy(() -> dao.updateWith(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class ToBdo {

		@Test
		void succes() {
			final var dao = new JarEntryDao(JAR_ENTRY.bdo());
			final var bdo = dao.toBdo();

			assertThat(bdo.getId()).isNull();
			assertThat(bdo.getTransactionId())
					.isEqualTo(JAR_ENTRY.getTransaction().getId());
			assertThat(bdo.getJarId()).isEqualTo(JAR_ENTRY.getJar().getId());
			assertThat(bdo.getBalanceBefore())
					.isEqualByComparingTo(JAR_ENTRY.getBalanceBefore());
			assertThat(bdo.getBalanceAfter())
					.isEqualByComparingTo(JAR_ENTRY.getBalanceAfter());
		}
	}
}
