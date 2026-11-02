package com.cvesters.moneyjars.account.dao;

import org.springframework.data.core.TypedPropertyPath;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.data.jpa.domain.Specification;

import com.cvesters.moneyjars.transaction.dao.TransactionDao;
import com.cvesters.moneyjars.transaction.dao.TransactionQueryDao;

public final class AccountEntryQueryDao {

	private static final TypedPropertyPath<AccountEntryDao, Long> TRANSACTION_ID = TypedPropertyPath
			.of(AccountEntryDao::getTransactionId);
	private static final TypedPropertyPath<AccountEntryDao, Long> ACCOUNT_ID = TypedPropertyPath
			.of(AccountEntryDao::getAccountId);

	private AccountEntryQueryDao() {
	}

	public static Specification<TransactionDao> transactionsForAccount(
			final long accountId) {
		return TransactionQueryDao.referencedBy(AccountEntryDao.class,
				TRANSACTION_ID, forAccount(accountId));
	}

	private static PredicateSpecification<AccountEntryDao> forAccount(
			final long accountId) {
		return (from, builder) -> builder
				.equal(from.get(ACCOUNT_ID.toDotPath()), accountId);
	}
}
