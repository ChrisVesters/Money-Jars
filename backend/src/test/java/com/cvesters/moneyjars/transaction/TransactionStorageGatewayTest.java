package com.cvesters.moneyjars.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.cvesters.moneyjars.common.bdo.PagedResult;
import com.cvesters.moneyjars.common.bdo.Pagination;
import com.cvesters.moneyjars.common.exceptions.MissingEntityException;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;
import com.cvesters.moneyjars.transaction.bdo.Transaction;
import com.cvesters.moneyjars.transaction.bdo.TransactionFilter;
import com.cvesters.moneyjars.transaction.bdo.TransactionQuery;
import com.cvesters.moneyjars.transaction.dao.TransactionDao;
import com.cvesters.moneyjars.transaction.dao.TransactionQueryDao;

class TransactionStorageGatewayTest {

	private static final TestPaymentTransaction TRANSACTION = TestPaymentTransaction.RENT;

	private final TransactionRepository repository = mock();
	private final TransactionStorageGateway gateway = new TransactionStorageGateway(
			repository);

	@Nested
	class GetAll {

		@Test
		void success() {
			final TransactionDao dao = mock();
			final PaymentTransaction bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);
			when(repository.findAll()).thenReturn(List.of(dao));

			final var result = gateway.getAll();

			assertThat(result).containsExactly(bdo);
		}
	}

	@Nested
	class GetAllInPeriod {

		@Test
		void success() {
			final LocalDate start = LocalDate.of(2026, 1, 1);
			final LocalDate end = LocalDate.of(2026, 1, 31);

			final TransactionDao dao = mock();
			final PaymentTransaction bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);
			when(repository.findAllInPeriod(start, end))
					.thenReturn(List.of(dao));

			final var result = gateway.getAllInPeriod(start, end);

			assertThat(result).containsExactly(bdo);
		}

		@Test
		void startNull() {
			final LocalDate start = null;
			final LocalDate end = LocalDate.of(2026, 1, 31);

			assertThatThrownBy(() -> gateway.getAllInPeriod(start, end))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void endNull() {
			final LocalDate start = LocalDate.of(2026, 1, 1);
			final LocalDate end = null;

			assertThatThrownBy(() -> gateway.getAllInPeriod(start, end))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Get {

		@Test
		void success() {
			final TransactionDao dao = mock();
			final PaymentTransaction bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);

			when(repository.findById(TRANSACTION.getId()))
					.thenReturn(Optional.of(dao));

			final var result = gateway.get(TRANSACTION.getId());

			assertThat(result).isSameAs(bdo);
		}

		@Test
		void notFound() {
			when(repository.findById(TRANSACTION.getId()))
					.thenReturn(Optional.empty());

			assertThatThrownBy(() -> gateway.get(TRANSACTION.getId()))
					.isInstanceOf(MissingEntityException.class);
		}
	}

	@Nested
	class FindFirstForDate {

		@Test
		void success() {
			final TransactionDao dao = mock();
			final PaymentTransaction bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);

			when(repository.findFirstForDate(TRANSACTION.getDate()))
					.thenReturn(Optional.of(dao));

			final var result = gateway.findFirstForDate(TRANSACTION.getDate());

			assertThat(result).containsSame(bdo);
		}

		@Test
		void notFound() {
			when(repository.findFirstForDate(TRANSACTION.getDate()))
					.thenReturn(Optional.empty());

			final var result = gateway.findFirstForDate(TRANSACTION.getDate());

			assertThat(result).isEmpty();
		}

		@Test
		void dateNull() {
			assertThatThrownBy(() -> gateway.findFirstForDate(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class FindLastForDate {

		@Test
		void success() {
			final TransactionDao dao = mock();
			final PaymentTransaction bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);

			when(repository.findLastForDate(TRANSACTION.getDate()))
					.thenReturn(Optional.of(dao));

			final var result = gateway.findLastForDate(TRANSACTION.getDate());

			assertThat(result).containsSame(bdo);
		}

		@Test
		void notFound() {
			when(repository.findLastForDate(TRANSACTION.getDate()))
					.thenReturn(Optional.empty());

			final var result = gateway.findLastForDate(TRANSACTION.getDate());

			assertThat(result).isEmpty();
		}

		@Test
		void dateNull() {
			assertThatThrownBy(() -> gateway.findLastForDate(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class FindAll {

		private static final TransactionQuery QUERY = new TransactionQuery(
				new TransactionFilter(LocalDate.of(2026, 1, 1),
						LocalDate.of(2026, 1, 31)),
				new Pagination(1, 10));

		private final Specification<TransactionDao> spec = mock();
		private final Pageable pageable = mock();

		private MockedStatic<TransactionQueryDao> queryDao;

		@BeforeEach
		void setUp() {
			queryDao = mockStatic(TransactionQueryDao.class);
			queryDao.when(
					() -> TransactionQueryDao.toSpecification(QUERY.filter()))
					.thenReturn(spec);
			queryDao.when(
					() -> TransactionQueryDao.toPageable(QUERY.pagination()))
					.thenReturn(pageable);
		}

		@AfterEach
		void tearDown() {
			queryDao.close();
		}

		@Test
		void success() {
			final TransactionDao dao = mock();
			final PaymentTransaction bdo = mock();
			when(dao.toBdo()).thenReturn(bdo);

			final Page<TransactionDao> page = mock();
			when(page.getContent()).thenReturn(List.of(dao));
			when(page.getNumber()).thenReturn(1);
			when(page.getSize()).thenReturn(10);
			when(page.getTotalElements()).thenReturn(11L);
			when(repository.findAll(spec, pageable)).thenReturn(page);

			final var result = gateway.findAll(QUERY);

			final var expected = new PagedResult<Transaction>(List.of(bdo), 1,
					10, 11);
			assertThat(result).isEqualTo(expected);
		}

		@Test
		void empty() {
			final Page<TransactionDao> page = mock();
			when(page.getContent()).thenReturn(Collections.emptyList());
			when(page.getNumber()).thenReturn(1);
			when(page.getSize()).thenReturn(10);
			when(page.getTotalElements()).thenReturn(0L);
			when(repository.findAll(spec, pageable)).thenReturn(page);

			final var result = gateway.findAll(QUERY);

			final var expected = new PagedResult<Transaction>(
					Collections.emptyList(), 1, 10, 0);
			assertThat(result).isEqualTo(expected);
		}

		@Test
		void queryNull() {
			assertThatThrownBy(() -> gateway.findAll(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
