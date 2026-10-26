package com.cvesters.moneyjars.category;

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

import com.cvesters.moneyjars.category.bdo.Category;
import com.cvesters.moneyjars.category.dao.CategoryDao;
import com.cvesters.moneyjars.common.exceptions.MissingEntityException;

class CategoryStorageGatewayTest {

	private static final TestCategory CATEGORY = TestCategory.GROCERIES;

	private final CategoryRepository repository = mock();
	private final CategoryStorageGateway gateway = new CategoryStorageGateway(
			repository);

	@Nested
	class GetAll {

		@Test
		void success() {
			final CategoryDao dao = mock();
			final Category bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);
			when(repository.findAll()).thenReturn(List.of(dao));

			final var categories = gateway.getAll();

			assertThat(categories).containsExactly(bdo);
		}
	}

	@Nested
	class Get {

		@Test
		void success() {
			final CategoryDao dao = mock();
			final Category bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);

			when(repository.findById(CATEGORY.getId()))
					.thenReturn(Optional.of(dao));

			final Category result = gateway.get(CATEGORY.getId());

			assertThat(result).isSameAs(bdo);
		}

		@Test
		void missingEntity() {
			when(repository.findById(CATEGORY.getId()))
					.thenReturn(Optional.empty());

			assertThatThrownBy(() -> gateway.get(CATEGORY.getId()))
					.isInstanceOf(MissingEntityException.class);
		}
	}

	@Nested
	class Create {

		@Test
		void success() {
			final CategoryDao createdDao = mock();
			final Category createdBdo = mock();
			when(createdDao.toBdo()).thenReturn(createdBdo);

			final Category category = CATEGORY.bdo();
			when(repository.save(argThat(v -> {
				assertThat(v.getId()).isNull();
				assertThat(v.getName()).isEqualTo(CATEGORY.getName());
				assertThat(v.getDescription())
						.isEqualTo(CATEGORY.getDescription());
				return true;
			}))).thenReturn(createdDao);

			final var result = gateway.create(category);

			assertThat(result).isSameAs(createdBdo);
		}

		@Test
		void categoryNull() {
			assertThatThrownBy(() -> gateway.create(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Update {

		@Test
		void success() {
			final CategoryDao updatedDao = mock();
			final Category updatedBdo = mock();
			when(updatedDao.toBdo()).thenReturn(updatedBdo);

			final Category update = mock();
			when(update.getId()).thenReturn(CATEGORY.getId());

			final CategoryDao existing = mock();
			when(repository.findById(CATEGORY.getId()))
					.thenReturn(Optional.of(existing));
			when(repository.save(existing)).thenReturn(updatedDao);

			final Category result = gateway.update(update);

			assertThat(result).isSameAs(updatedBdo);

			final InOrder inOrder = inOrder(existing, repository);
			inOrder.verify(existing).updateWith(update);
			inOrder.verify(repository).save(existing);
		}

		@Test
		void notFound() {
			when(repository.findById(CATEGORY.getId()))
					.thenReturn(Optional.empty());

			final Category update = mock();
			when(update.getId()).thenReturn(CATEGORY.getId());

			assertThatThrownBy(() -> gateway.update(update))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void categoryNull() {
			assertThatThrownBy(() -> gateway.update(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Delete {

		@Test
		void success() {
			gateway.delete(CATEGORY.getId());

			verify(repository).deleteById(CATEGORY.getId());
		}
	}
}
