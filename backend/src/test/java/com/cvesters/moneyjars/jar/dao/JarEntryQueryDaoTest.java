package com.cvesters.moneyjars.jar.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.springframework.data.core.TypedPropertyPath;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.data.jpa.domain.Specification;

import com.cvesters.moneyjars.transaction.dao.TransactionDao;
import com.cvesters.moneyjars.transaction.dao.TransactionQueryDao;

class JarEntryQueryDaoTest {

	@Nested
	class TransactionsForJar {

		private static final long JAR_ID = 1L;

		private final From<?, JarEntryDao> from = mock();
		private final CriteriaBuilder builder = mock();
		private final Path<Long> jarIdPath = mock();
		private final Specification<TransactionDao> scope = mock();

		@Test
		@SuppressWarnings("unchecked")
		void success() {
			final ArgumentCaptor<TypedPropertyPath<JarEntryDao, Long>> transactionId = ArgumentCaptor
					.forClass(TypedPropertyPath.class);
			final ArgumentCaptor<PredicateSpecification<JarEntryDao>> filter = ArgumentCaptor
					.forClass(PredicateSpecification.class);

			final Specification<TransactionDao> result;
			try (MockedStatic<TransactionQueryDao> queryDao = mockStatic(
					TransactionQueryDao.class)) {
				queryDao.when(() -> TransactionQueryDao.referencedBy(
						eq(JarEntryDao.class), transactionId.capture(),
						filter.capture())).thenReturn(scope);

				result = JarEntryQueryDao
						.transactionsForJar(JAR_ID);
			}

			assertThat(result).isSameAs(scope);
			assertThat(transactionId.getValue().toDotPath())
					.isEqualTo("transactionId");

			when(from.<Long>get("jarId")).thenReturn(jarIdPath);
			final Predicate jarPredicate = mock();
			when(builder.equal(jarIdPath, JAR_ID))
					.thenReturn(jarPredicate);
			assertThat(filter.getValue().toPredicate(from, builder))
					.isSameAs(jarPredicate);
		}
	}
}
