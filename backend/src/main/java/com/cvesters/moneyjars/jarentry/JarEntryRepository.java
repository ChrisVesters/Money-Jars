package com.cvesters.moneyjars.jarentry;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.cvesters.moneyjars.jarentry.dao.JarEntryDao;

public interface JarEntryRepository extends Repository<JarEntryDao, Long> {

	Optional<JarEntryDao> findById(Long id);

	Optional<JarEntryDao> findByJarIdAndTransactionId(long jarId,
			long transactionId);

	@Query("""
			SELECT e
			FROM JarEntryDao e
			JOIN TransactionDao t ON t.id = e.transactionId
			WHERE e.jarId = :jarId
				AND t.date < :date
				OR (t.date = :date AND t.sequence < :sequence)
			ORDER BY t.date DESC, t.sequence DESC
			LIMIT 1
			""")
	Optional<JarEntryDao> findBefore(long jarId, LocalDate date, int sequence);

	@Query("""
			SELECT e
			FROM JarEntryDao e
			JOIN TransactionDao t ON t.id = e.transactionId
			WHERE e.jarId = :jarId
				AND t.date > :date
				OR (t.date = :date AND t.sequence > :sequence)
			ORDER BY t.date ASC, t.sequence ASC
			""")
	List<JarEntryDao> findAllAfter(long jarId, LocalDate date, int sequence);

	JarEntryDao save(JarEntryDao entry);

	void delete(JarEntryDao entry);

}
