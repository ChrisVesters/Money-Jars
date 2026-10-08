package com.cvesters.moneyjars.accountentry;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.cvesters.moneyjars.accountentry.dao.AccountEntryDao;

public interface AccountEntryRepository
		extends Repository<AccountEntryDao, Long> {

	Optional<AccountEntryDao> findById(Long id);

	Optional<AccountEntryDao> findByAccountIdAndTransactionId(long accountId,
			long transactionId);

	@Query("""
			SELECT e
			FROM AccountEntryDao e
			JOIN TransactionDao t ON t.id = e.transactionId
			WHERE e.accountId = :accountId
				AND (t.date < :date
					OR (t.date = :date AND t.sequence < :sequence))
			ORDER BY t.date DESC, t.sequence DESC
			LIMIT 1
			""")
	Optional<AccountEntryDao> findBefore(long accountId, LocalDate date,
			int sequence);

	@Query("""
			SELECT e
			FROM AccountEntryDao e
			JOIN TransactionDao t ON t.id = e.transactionId
			WHERE e.accountId = :accountId
				AND (t.date > :date
					OR (t.date = :date AND t.sequence > :sequence))
			ORDER BY t.date ASC, t.sequence ASC
			""")
	List<AccountEntryDao> findAllAfter(long accountId, LocalDate date,
			int sequence);

	AccountEntryDao save(AccountEntryDao entry);

	void delete(AccountEntryDao entry);

}
