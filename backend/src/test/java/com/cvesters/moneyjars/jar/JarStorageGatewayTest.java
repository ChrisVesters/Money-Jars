package com.cvesters.moneyjars.jar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import com.cvesters.moneyjars.jar.bdo.Jar;
import com.cvesters.moneyjars.jar.dao.JarDao;

class JarStorageGatewayTest {

	private static final TestJar JAR = TestJar.HOUSEHOLD;

	private final JarRepository repository = mock();
	private final JarStorageGateway gateway = new JarStorageGateway(repository);

	@Nested
	class GetAll {

		@Test
		void success() {
			final JarDao dao = mock();
			final Jar bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);
			when(repository.findAll()).thenReturn(List.of(dao));

			final var jars = gateway.getAll();

			assertThat(jars).containsExactly(bdo);
		}
	}

	@Nested
	class Find {

		@Test
		void success() {
			final JarDao dao = mock();
			final Jar bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);

			when(repository.findById(JAR.getId())).thenReturn(Optional.of(dao));

			final var result = gateway.find(JAR.getId());

			assertThat(result).containsSame(bdo);
		}
	}

	@Nested
	class Create {

		@Test
		void success() {
			final JarDao createdDao = mock();
			final Jar createdBdo = mock();
			when(createdDao.toBdo()).thenReturn(createdBdo);

			final Jar jar = JAR.bdo();
			when(repository.save(argThat(v -> {
				assertThat(v.getId()).isNull();
				assertThat(v.getName()).isEqualTo(jar.getName());
				assertThat(v.getDescription()).isEqualTo(jar.getDescription());
				assertThat(v.getBalance()).isEqualTo(jar.getBalance());
				return true;
			}))).thenReturn(createdDao);

			final var result = gateway.create(jar);

			assertThat(result).isSameAs(createdBdo);
		}

		@Test
		void jarNull() {
			assertThatThrownBy(() -> gateway.create(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Update {

		@Test
		void success() {
			final JarDao updatedDao = mock();
			final Jar updatedBdo = mock();
			when(updatedDao.toBdo()).thenReturn(updatedBdo);

			final Jar update = mock();
			when(update.getId()).thenReturn(JAR.getId());
			
			final JarDao existing = mock();
			when(repository.findById(JAR.getId()))
					.thenReturn(Optional.of(existing));
			when(repository.save(existing)).thenReturn(updatedDao);

			final Jar result = gateway.update(update);

			assertThat(result).isSameAs(updatedBdo);

			final InOrder inOrder = inOrder(existing, repository);
			inOrder.verify(existing).updateWith(update);
			inOrder.verify(repository).save(existing);
		}

		@Test
		void notFound() {
			when(repository.findById(JAR.getId())).thenReturn(Optional.empty());
			
			final Jar update = mock();
			assertThatThrownBy(() -> gateway.update(update))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void jarNull() {
			assertThatThrownBy(() -> gateway.update(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Delete {

		@Test
		void success() {
			gateway.delete(JAR.getId());

			verify(repository).deleteById(JAR.getId());
		}
	}
}
