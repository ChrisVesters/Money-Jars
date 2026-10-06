package com.cvesters.moneyjars.transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.cvesters.moneyjars.transaction.dao.TransactionDao;

public interface TransactionRepository
		extends Repository<TransactionDao, Long> {

	// TODO: should this not be descending?
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

	Optional<TransactionDao> findById(final long id);

	@Query("""
			SELECT t
			FROM TransactionDao t
			WHERE t.date = :date
			ORDER BY t.sequence
			LIMIT 1
			""")
	Optional<TransactionDao> findFirstForDate(final LocalDate date);

	@Query("""
			SELECT t
			FROM TransactionDao t
			WHERE t.date = :date
			ORDER BY t.sequence DESC
			LIMIT 1
			""")
	Optional<TransactionDao> findLastForDate(final LocalDate date);

	TransactionDao save(final TransactionDao transaction);

	void deleteById(final long id);
}
