package com.cvesters.moneyjars.jar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import com.cvesters.moneyjars.jar.bdo.Jar;
import com.cvesters.moneyjars.jar.bdo.JarAction;

class JarServiceTest {

	private final JarStorageGateway storage = mock();
	private final JarService service = new JarService(storage);

	@Nested
	class GetAll {

		@Test
		void success() {
			final Jar expected = mock();
			when(storage.getAll()).thenReturn(List.of(expected));

			final List<Jar> result = service.getAll();

			assertThat(result).containsExactly(expected);
		}
	}

	@Nested
	class Get {

		private static final long JAR_ID = 2L;

		@Test
		void success() {
			final Jar expected = mock();
			when(storage.get(JAR_ID)).thenReturn(expected);

			final Jar result = service.get(JAR_ID);

			assertThat(result).isSameAs(expected);
		}
	}

	@Nested
	class Create {

		@Test
		void success() {
			final JarAction.Create action = mock();
			final Jar create = mock();
			when(action.toBdo()).thenReturn(create);

			final Jar created = mock();
			when(storage.create(create)).thenReturn(created);

			final Jar result = service.create(action);

			assertThat(result).isSameAs(created);
		}

		@Test
		void actionNull() {
			assertThatThrownBy(() -> service.create(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Update {

		private static final long JAR_ID = 2L;

		@Test
		void success() {
			final JarAction.Update action = mock();
			final Jar existing = mock();

			final Jar updated = mock();
			when(storage.get(JAR_ID)).thenReturn(existing);
			when(storage.update(existing)).thenReturn(updated);

			final Jar result = service.update(JAR_ID, action);

			assertThat(result).isSameAs(updated);

			final InOrder inOrder = inOrder(storage, action);
			inOrder.verify(storage).get(JAR_ID);
			inOrder.verify(action).applyOn(existing);
			inOrder.verify(storage).update(existing);
		}

		@Test
		void actionNull() {
			assertThatThrownBy(() -> service.update(JAR_ID, null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Delete {

		private static final long JAR_ID = 2L;

		@Test
		void success() {
			service.delete(JAR_ID);

			verify(storage).delete(JAR_ID);
		}
	}
}
