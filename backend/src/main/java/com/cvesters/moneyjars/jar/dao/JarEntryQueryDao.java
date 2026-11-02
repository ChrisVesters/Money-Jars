package com.cvesters.moneyjars.jar.dao;

import org.springframework.data.core.TypedPropertyPath;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.data.jpa.domain.Specification;

import com.cvesters.moneyjars.transaction.dao.TransactionDao;
import com.cvesters.moneyjars.transaction.dao.TransactionQueryDao;

public final class JarEntryQueryDao {

	private static final TypedPropertyPath<JarEntryDao, Long> TRANSACTION_ID = TypedPropertyPath
			.of(JarEntryDao::getTransactionId);
	private static final TypedPropertyPath<JarEntryDao, Long> JAR_ID = TypedPropertyPath
			.of(JarEntryDao::getJarId);

	private JarEntryQueryDao() {
	}

	public static Specification<TransactionDao> transactionsForJar(
			final long jarId) {
		return TransactionQueryDao.referencedBy(JarEntryDao.class,
				TRANSACTION_ID, forJar(jarId));
	}

	private static PredicateSpecification<JarEntryDao> forJar(
			final long jarId) {
		return (from, builder) -> builder
				.equal(from.get(JAR_ID.toDotPath()), jarId);
	}
}
