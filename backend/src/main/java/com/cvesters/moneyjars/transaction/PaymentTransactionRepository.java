package com.cvesters.moneyjars.transaction;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.cvesters.moneyjars.transaction.dao.PaymentTransactionDao;

public interface PaymentTransactionRepository
		extends Repository<PaymentTransactionDao, Long> {

	Optional<PaymentTransactionDao> findById(final long id);

	PaymentTransactionDao save(final PaymentTransactionDao transaction);

	void delete(final PaymentTransactionDao transaction);
}
