package com.cvesters.moneyjars.category;

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

import com.cvesters.moneyjars.category.bdo.Category;
import com.cvesters.moneyjars.category.bdo.CategoryAction;

class CategoryServiceTest {

	private final CategoryStorageGateway storage = mock();
	private final CategoryService service = new CategoryService(storage);

	@Nested
	class GetAll {

		@Test
		void success() {
			final Category expected = mock();
			when(storage.getAll()).thenReturn(List.of(expected));

			final List<Category> result = service.getAll();

			assertThat(result).containsExactly(expected);
		}
	}

	@Nested
	class Get {

		private static final long CATEGORY_ID = 2L;

		@Test
		void success() {
			final Category expected = mock();
			when(storage.get(CATEGORY_ID)).thenReturn(expected);

			final Category result = service.get(CATEGORY_ID);

			assertThat(result).isSameAs(expected);
		}
	}

	@Nested
	class Create {

		@Test
		void success() {
			final CategoryAction.Create action = mock();
			final Category create = mock();
			when(action.toBdo()).thenReturn(create);

			final Category created = mock();
			when(storage.create(create)).thenReturn(created);

			final Category result = service.create(action);

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

		private static final long CATEGORY_ID = 2L;

		@Test
		void success() {
			final CategoryAction.Update action = mock();
			final Category existing = mock();

			final Category updated = mock();
			when(storage.get(CATEGORY_ID)).thenReturn(existing);
			when(storage.update(existing)).thenReturn(updated);

			final Category result = service.update(CATEGORY_ID, action);

			assertThat(result).isSameAs(updated);

			final InOrder inOrder = inOrder(storage, action);
			inOrder.verify(storage).get(CATEGORY_ID);
			inOrder.verify(action).applyOn(existing);
			inOrder.verify(storage).update(existing);
		}

		@Test
		void actionNull() {
			assertThatThrownBy(() -> service.update(CATEGORY_ID, null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Delete {

		private static final long CATEGORY_ID = 2L;

		@Test
		void success() {
			service.delete(CATEGORY_ID);

			verify(storage).delete(CATEGORY_ID);
		}
	}
}
