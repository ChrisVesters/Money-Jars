package com.cvesters.moneyjars.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.MockedStatic;
import org.springframework.data.jpa.domain.Specification;

import com.cvesters.moneyjars.category.TestCategory;
import com.cvesters.moneyjars.common.exceptions.MissingEntityException;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionCategoryTotal;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionFilter;
import com.cvesters.moneyjars.transaction.dao.PaymentTransactionCategoryTotalDao;
import com.cvesters.moneyjars.transaction.dao.PaymentTransactionDao;
import com.cvesters.moneyjars.transaction.dao.PaymentTransactionQueryDao;

class PaymentTransactionStorageGatewayTest {

	private static final TestPaymentTransaction TRANSACTION = TestPaymentTransaction.RENT;

	private final PaymentTransactionRepository repository = mock();
	private final PaymentTransactionStorageGateway gateway = new PaymentTransactionStorageGateway(
			repository);

	@Nested
	class Exists {

		private static final PaymentTransactionFilter FILTER = new PaymentTransactionFilter(
				TestCategory.GROCERIES.getId());

		private final Specification<PaymentTransactionDao> spec = mock();

		private MockedStatic<PaymentTransactionQueryDao> queryDao;

		@BeforeEach
		void setUp() {
			queryDao = mockStatic(PaymentTransactionQueryDao.class);
			queryDao.when(
					() -> PaymentTransactionQueryDao.toSpecification(FILTER))
					.thenReturn(spec);
		}

		@AfterEach
		void tearDown() {
			queryDao.close();
		}

		@Test
		void exists() {
			when(repository.exists(spec)).thenReturn(true);

			final boolean result = gateway.exists(FILTER);

			assertThat(result).isTrue();
		}

		@Test
		void notExists() {
			when(repository.exists(spec)).thenReturn(false);

			final boolean result = gateway.exists(FILTER);

			assertThat(result).isFalse();
		}

		@Test
		void filterNull() {
			assertThatThrownBy(() -> gateway.exists(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class GetCategoryTotalsInPeriod {

		private static final LocalDate START = LocalDate.of(2026, 1, 1);
		private static final LocalDate END = LocalDate.of(2026, 1, 31);

		private MockedStatic<PaymentTransactionCategoryTotalDao> totalDao;

		@BeforeEach
		void setUp() {
			totalDao = mockStatic(PaymentTransactionCategoryTotalDao.class);
		}

		@AfterEach
		void tearDown() {
			totalDao.close();
		}

		@Test
		void success() {
			final List<PaymentTransactionCategoryTotalDao> daos = List
					.of(mock(), mock());
			when(repository.findCategoryTotalsInPeriod(START, END))
					.thenReturn(daos);

			final List<PaymentTransactionCategoryTotal> bdos = List.of(
					TestPaymentTransactionCategoryTotal.JANUARY_GROCERIES.bdo(),
					TestPaymentTransactionCategoryTotal.JANUARY_UNCATEGORISED
							.bdo());
			totalDao.when(() -> PaymentTransactionCategoryTotalDao.toBdo(daos))
					.thenReturn(bdos);

			final List<PaymentTransactionCategoryTotal> result = gateway
					.getCategoryTotalsInPeriod(START, END);

			assertThat(result).isSameAs(bdos);
		}

		@Test
		void startNull() {
			assertThatThrownBy(
					() -> gateway.getCategoryTotalsInPeriod(null, END))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void endNull() {
			assertThatThrownBy(
					() -> gateway.getCategoryTotalsInPeriod(START, null))
							.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Get {

		@Test
		void success() {
			final PaymentTransactionDao dao = mock();
			final PaymentTransaction bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);

			when(repository.findById(TRANSACTION.getId()))
					.thenReturn(Optional.of(dao));

			final var result = gateway.get(TRANSACTION.getId());

			assertThat(result).isSameAs(bdo);
		}

		@Test
		void notFound() {
			final long id = TRANSACTION.getId();
			when(repository.findById(id)).thenReturn(Optional.empty());

			assertThatThrownBy(() -> gateway.get(id))
					.isInstanceOf(MissingEntityException.class);
		}
	}

	@Nested
	class Create {

		@Test
		void success() {
			final PaymentTransactionDao createdDao = mock();
			final PaymentTransaction createdBdo = mock();
			when(createdDao.toBdo()).thenReturn(createdBdo);

			final PaymentTransaction transaction = TRANSACTION.bdo();
			when(repository.save(argThat(dao -> {
				assertThat(dao.getId()).isNull();
				assertThat(dao.getDate()).isEqualTo(TRANSACTION.getDate());
				assertThat(dao.getSequence())
						.isEqualTo(TRANSACTION.getSequence());
				assertThat(dao.getAmount()).isEqualTo(TRANSACTION.getAmount());
				assertThat(dao.getDescription())
						.isEqualTo(TRANSACTION.getDescription());
				assertThat(dao.getJarId())
						.isEqualTo(TRANSACTION.getJar().getId());
				assertThat(dao.getAccountId())
						.isEqualTo(TRANSACTION.getAccount().getId());
				assertThat(dao.getCounterparty())
						.isEqualTo(TRANSACTION.getCounterparty());
				assertThat(dao.getDirection())
						.isEqualTo(TRANSACTION.getDirectionId());
				return true;
			}))).thenReturn(createdDao);

			final var result = gateway.create(transaction);

			assertThat(result).isSameAs(createdBdo);
		}

		@Test
		void transactionNull() {
			assertThatThrownBy(() -> gateway.create(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Update {

		@Test
		void success() {
			final PaymentTransactionDao updatedDao = mock();
			final PaymentTransaction updatedBdo = mock();
			when(updatedDao.toBdo()).thenReturn(updatedBdo);

			final PaymentTransaction update = mock();
			when(update.getId()).thenReturn(TRANSACTION.getId());

			final PaymentTransactionDao existing = mock();
			when(repository.findById(TRANSACTION.getId()))
					.thenReturn(Optional.of(existing));
			when(repository.save(existing)).thenReturn(updatedDao);

			final var result = gateway.update(update);

			assertThat(result).isSameAs(updatedBdo);

			final InOrder inOrder = inOrder(existing, repository);
			inOrder.verify(existing).updateWith(update);
			inOrder.verify(repository).save(existing);
		}

		@Test
		void notFound() {
			when(repository.findById(TRANSACTION.getId()))
					.thenReturn(Optional.empty());

			final PaymentTransaction update = mock();
			when(update.getId()).thenReturn(TRANSACTION.getId());

			assertThatThrownBy(() -> gateway.update(update))
					.isInstanceOf(MissingEntityException.class);
		}

		@Test
		void transactionNull() {
			assertThatThrownBy(() -> gateway.update(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Delete {

		@Test
		void success() {
			final PaymentTransaction transaction = mock();
			when(transaction.getId()).thenReturn(TRANSACTION.getId());

			final PaymentTransactionDao existing = mock();
			when(repository.findById(TRANSACTION.getId()))
					.thenReturn(Optional.of(existing));

			gateway.delete(transaction);

			verify(repository).delete(existing);
		}

		@Test
		void notFound() {
			when(repository.findById(TRANSACTION.getId()))
					.thenReturn(Optional.empty());

			final PaymentTransaction transaction = mock();
			when(transaction.getId()).thenReturn(TRANSACTION.getId());

			assertThatThrownBy(() -> gateway.delete(transaction))
					.isInstanceOf(MissingEntityException.class);

			verify(repository, never()).delete(any());
		}

		@Test
		void transactionNull() {
			assertThatThrownBy(() -> gateway.delete(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
