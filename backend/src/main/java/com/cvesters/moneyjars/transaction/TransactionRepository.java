package com.cvesters.moneyjars.transaction;

import org.springframework.data.repository.ListCrudRepository;

import com.cvesters.moneyjars.transaction.dao.TransactionDao;

public interface TransactionRepository
		extends ListCrudRepository<TransactionDao, Long> {

}
