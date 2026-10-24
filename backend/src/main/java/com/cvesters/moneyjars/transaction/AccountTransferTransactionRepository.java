package com.cvesters.moneyjars.transaction;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.cvesters.moneyjars.transaction.dao.AccountTransferTransactionDao;

public interface AccountTransferTransactionRepository
		extends Repository<AccountTransferTransactionDao, Long> {

	Optional<AccountTransferTransactionDao> findById(final long id);

	AccountTransferTransactionDao save(
			final AccountTransferTransactionDao transaction);

	void delete(final AccountTransferTransactionDao transaction);
}
