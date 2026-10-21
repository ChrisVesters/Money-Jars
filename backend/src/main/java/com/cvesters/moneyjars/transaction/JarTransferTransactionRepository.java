package com.cvesters.moneyjars.transaction;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.cvesters.moneyjars.transaction.dao.JarTransferTransactionDao;

public interface JarTransferTransactionRepository
		extends Repository<JarTransferTransactionDao, Long> {

	Optional<JarTransferTransactionDao> findById(final long id);

	JarTransferTransactionDao save(final JarTransferTransactionDao transaction);

	void delete(final JarTransferTransactionDao transaction);
}
