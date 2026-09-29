package com.cvesters.moneyjars.transaction;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import com.cvesters.moneyjars.transaction.dao.TransactionDao;

public interface TransactionRepository
		extends ListCrudRepository<TransactionDao, Long> {

	@Query("""
			SELECT t
			FROM TransactionDao t
			ORDER BY t.date, t.sequence
			""")
	List<TransactionDao> findAll();

	@Query("""
			SELECT t
			FROM TransactionDao t
			WHERE t.date
			BETWEEN :start AND :end
			ORDER BY t.date, t.sequence
			""")
	List<TransactionDao> findAllInPeriod(final LocalDate start,
			final LocalDate end);
}
