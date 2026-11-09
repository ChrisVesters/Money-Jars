package com.cvesters.moneyjars.transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.cvesters.moneyjars.transaction.dao.PaymentTransactionCategoryTotalDao;
import com.cvesters.moneyjars.transaction.dao.PaymentTransactionDao;

public interface PaymentTransactionRepository
		extends Repository<PaymentTransactionDao, Long> {

	boolean exists(final Specification<PaymentTransactionDao> spec);

	Optional<PaymentTransactionDao> findById(final long id);

	@Query("""
			SELECT p.categoryId AS categoryId,
				p.direction AS direction,
				SUM(p.amount) AS amount
			FROM PaymentTransactionDao p
			WHERE p.date BETWEEN :start AND :end
			GROUP BY p.categoryId, p.direction
			ORDER BY p.categoryId NULLS LAST, p.direction
			""")
	List<PaymentTransactionCategoryTotalDao> findCategoryTotalsInPeriod(
			final LocalDate start, final LocalDate end);

	PaymentTransactionDao save(final PaymentTransactionDao transaction);

	void delete(final PaymentTransactionDao transaction);
}
