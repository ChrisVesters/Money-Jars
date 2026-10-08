package com.cvesters.moneyjars.jarentry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import com.cvesters.moneyjars.jarentry.bdo.JarEntry;
import com.cvesters.moneyjars.jarentry.dao.JarEntryDao;

class JarEntryStorageGatewayTest {

	private final JarEntryRepository repository = mock();
	private final JarEntryStorageGateway gateway = new JarEntryStorageGateway(
			repository);

	@Nested
	class FindByJarIdAndTransactionId {

		@Test
		public void found() {
			final long jarId = 1L;
			final long transactionId = 2L;

			final JarEntryDao dao = mock();
			final JarEntry bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);

			when(repository.findByJarIdAndTransactionId(jarId, transactionId))
					.thenReturn(Optional.of(dao));

			final Optional<JarEntry> result = gateway
					.findByJarIdAndTransactionId(jarId, transactionId);

			assertThat(result).contains(bdo);
		}

		@Test
		public void notFound() {
			final long jarId = 1L;
			final long transactionId = 2L;

			when(repository.findByJarIdAndTransactionId(jarId, transactionId))
					.thenReturn(Optional.empty());

			final Optional<JarEntry> result = gateway
					.findByJarIdAndTransactionId(jarId, transactionId);

			assertThat(result).isEmpty();
		}
	}

	@Nested
	class FindBefore {

		@Test
		public void found() {
			final long jarId = 1L;
			final LocalDate date = LocalDate.now();
			final int sequence = 0;

			final JarEntryDao dao = mock();
			final JarEntry bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);

			when(repository.findBefore(jarId, date, sequence))
					.thenReturn(Optional.of(dao));

			final Optional<JarEntry> result = gateway.findBefore(jarId, date,
					sequence);

			assertThat(result).contains(bdo);
		}

		@Test
		public void notFound() {
			final long jarId = 1L;
			final LocalDate date = LocalDate.now();
			final int sequence = 0;

			when(repository.findBefore(jarId, date, sequence))
					.thenReturn(Optional.empty());

			final Optional<JarEntry> result = gateway.findBefore(jarId, date,
					sequence);

			assertThat(result).isEmpty();
		}
	}

	@Nested
	class GetAllAfter {

		@Test
		public void single() {
			final long jarId = 1L;
			final LocalDate date = LocalDate.now();
			final int sequence = 0;

			final JarEntryDao dao = mock();
			final JarEntry bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);

			when(repository.findAllAfter(jarId, date, sequence))
					.thenReturn(List.of(dao));

			final List<JarEntry> result = gateway.getAllAfter(jarId, date,
					sequence);

			assertThat(result).containsExactly(bdo);
		}

		@Test
		public void multiple() {
			final long jarId = 1L;
			final LocalDate date = LocalDate.now();
			final int sequence = 0;

			final JarEntryDao dao1 = mock();
			final JarEntry bdo1 = mock();
			when(dao1.toBdo()).thenReturn(bdo1);

			final JarEntryDao dao2 = mock();
			final JarEntry bdo2 = mock();
			when(dao2.toBdo()).thenReturn(bdo2);

			when(repository.findAllAfter(jarId, date, sequence))
					.thenReturn(List.of(dao1, dao2));

			final List<JarEntry> result = gateway.getAllAfter(jarId, date,
					sequence);

			assertThat(result).containsExactly(bdo1, bdo2);
		}

		@Test
		public void empty() {
			final long jarId = 1L;
			final LocalDate date = LocalDate.now();
			final int sequence = 0;

			when(repository.findAllAfter(jarId, date, sequence))
					.thenReturn(Collections.emptyList());

			final List<JarEntry> result = gateway.getAllAfter(jarId, date,
					sequence);

			assertThat(result).isEmpty();
		}
	}

	@Nested
	class Create {

		private static final TestJarEntry JAR_ENTRY = TestJarEntry.HOUSEHOLD_CAFE;

		@Test
		void success() {
			final JarEntryDao createdDao = mock();
			final JarEntry createdBdo = mock();
			when(createdDao.toBdo()).thenReturn(createdBdo);

			final JarEntry entry = JAR_ENTRY.bdo();
			when(repository.save(argThat(v -> {
				assertThat(v.getId()).isNull();
				assertThat(v.getTransactionId())
						.isEqualTo(JAR_ENTRY.getTransaction().getId());
				assertThat(v.getJarId()).isEqualTo(JAR_ENTRY.getJar().getId());
				assertThat(v.getBalanceBefore())
						.isEqualByComparingTo(JAR_ENTRY.getBalanceBefore());
				assertThat(v.getBalanceAfter())
						.isEqualByComparingTo(JAR_ENTRY.getBalanceAfter());
				return true;
			}))).thenReturn(createdDao);

			final JarEntry result = gateway.create(entry);

			assertThat(result).isSameAs(createdBdo);
		}

		@Test
		void entryNull() {
			assertThatThrownBy(() -> gateway.create(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Update {

		private static final TestJarEntry JAR_ENTRY = TestJarEntry.HOUSEHOLD_CAFE;

		@Test
		void success() {
			final JarEntryDao updatedDao = mock();
			final JarEntry updatedBdo = mock();
			when(updatedDao.toBdo()).thenReturn(updatedBdo);

			final JarEntry update = mock();
			when(update.getId()).thenReturn(JAR_ENTRY.getId());

			final JarEntryDao existing = mock();
			when(repository.findById(JAR_ENTRY.getId()))
					.thenReturn(Optional.of(existing));
			when(repository.save(existing)).thenReturn(updatedDao);

			final JarEntry result = gateway.update(update);

			assertThat(result).isSameAs(updatedBdo);

			final InOrder inOrder = inOrder(existing, repository);
			inOrder.verify(existing).updateWith(update);
			inOrder.verify(repository).save(existing);
		}

		@Test
		void notFound() {
			when(repository.findById(JAR_ENTRY.getId()))
					.thenReturn(Optional.empty());

			final JarEntry update = mock();
			when(update.getId()).thenReturn(JAR_ENTRY.getId());

			assertThatThrownBy(() -> gateway.update(update))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void entryNull() {
			assertThatThrownBy(() -> gateway.update(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class UpdateAll {

		private static final TestJarEntry JAR_ENTRY_1 = TestJarEntry.HOUSEHOLD_CAFE;
		private static final TestJarEntry JAR_ENTRY_2 = TestJarEntry.HOUSEHOLD_MARKET;

		@Test
		void single() {
			final JarEntryDao updatedDao = mock();
			final JarEntry updatedBdo = mock();
			when(updatedDao.toBdo()).thenReturn(updatedBdo);

			final JarEntry update = mock();
			when(update.getId()).thenReturn(JAR_ENTRY_1.getId());

			final JarEntryDao existing = mock();
			when(repository.findById(JAR_ENTRY_1.getId()))
					.thenReturn(Optional.of(existing));
			when(repository.save(existing)).thenReturn(updatedDao);

			final List<JarEntry> result = gateway.updateAll(List.of(update));

			assertThat(result).containsExactly(updatedBdo);

			final InOrder inOrder = inOrder(existing, repository);
			inOrder.verify(existing).updateWith(update);
			inOrder.verify(repository).save(existing);
		}

		@Test
		void multiple() {
			final JarEntryDao updatedDao1 = mock();
			final JarEntry updatedBdo1 = mock();
			when(updatedDao1.toBdo()).thenReturn(updatedBdo1);

			final JarEntryDao updatedDao2 = mock();
			final JarEntry updatedBdo2 = mock();
			when(updatedDao2.toBdo()).thenReturn(updatedBdo2);

			final JarEntry update1 = mock();
			when(update1.getId()).thenReturn(JAR_ENTRY_1.getId());

			final JarEntry update2 = mock();
			when(update2.getId()).thenReturn(JAR_ENTRY_2.getId());

			final JarEntryDao existing1 = mock();
			when(repository.findById(JAR_ENTRY_1.getId()))
					.thenReturn(Optional.of(existing1));
			when(repository.save(existing1)).thenReturn(updatedDao1);

			final JarEntryDao existing2 = mock();
			when(repository.findById(JAR_ENTRY_2.getId()))
					.thenReturn(Optional.of(existing2));
			when(repository.save(existing2)).thenReturn(updatedDao2);

			final List<JarEntry> result = gateway
					.updateAll(List.of(update1, update2));

			assertThat(result).containsExactly(updatedBdo1, updatedBdo2);

			final InOrder inOrder = inOrder(existing1, existing2, repository);
			inOrder.verify(existing1).updateWith(update1);
			inOrder.verify(repository).save(existing1);
			inOrder.verify(existing2).updateWith(update2);
			inOrder.verify(repository).save(existing2);
		}

		@Test
		void empty() {
			final List<JarEntry> result = gateway
					.updateAll(Collections.emptyList());

			assertThat(result).isEmpty();
			verify(repository, never()).save(any());
		}

		@Test
		void notFound() {
			final JarEntry update1 = mock();
			when(update1.getId()).thenReturn(JAR_ENTRY_1.getId());

			final JarEntry update2 = mock();
			when(update2.getId()).thenReturn(JAR_ENTRY_2.getId());

			final JarEntryDao existing1 = mock();
			when(repository.findById(JAR_ENTRY_1.getId()))
					.thenReturn(Optional.of(existing1));
			when(repository.save(existing1)).thenReturn(mock());
			when(repository.findById(JAR_ENTRY_2.getId()))
					.thenReturn(Optional.empty());

			assertThatThrownBy(
					() -> gateway.updateAll(List.of(update1, update2)))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void entriesNull() {
			assertThatThrownBy(() -> gateway.updateAll(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Delete {

		private static final TestJarEntry JAR_ENTRY = TestJarEntry.HOUSEHOLD_CAFE;

		@Test
		void success() {
			final JarEntry entry = mock();
			when(entry.getId()).thenReturn(JAR_ENTRY.getId());

			final JarEntryDao existing = mock();
			when(repository.findById(JAR_ENTRY.getId()))
					.thenReturn(Optional.of(existing));

			gateway.delete(entry);

			verify(repository).delete(existing);
		}

		@Test
		void notFound() {
			when(repository.findById(JAR_ENTRY.getId()))
					.thenReturn(Optional.empty());

			final JarEntry entry = mock();
			when(entry.getId()).thenReturn(JAR_ENTRY.getId());

			assertThatThrownBy(() -> gateway.delete(entry))
					.isInstanceOf(IllegalArgumentException.class);

			verify(repository, never()).delete(any());
		}

		@Test
		void entryNull() {
			assertThatThrownBy(() -> gateway.delete(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
