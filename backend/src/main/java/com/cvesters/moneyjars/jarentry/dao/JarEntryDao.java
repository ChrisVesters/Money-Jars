package com.cvesters.moneyjars.jarentry.dao;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cvesters.moneyjars.jarentry.bdo.JarEntry;

// TODO: move to jar package?
@Getter
@Entity
@Table(name = "jar_entries", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "transaction_id", "jar_id" }) })
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class JarEntryDao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "transaction_id", nullable = false, updatable = false)
	private long transactionId;

	@Column(name = "jar_id", nullable = false, updatable = false)
	private long jarId;

	@Column(name = "balance_before", nullable = false)
	private BigDecimal balanceBefore;

	@Column(name = "balance_after", nullable = false)
	private BigDecimal balanceAfter;

	public JarEntryDao(final JarEntry jarEntry) {
		Objects.requireNonNull(jarEntry);

		this.transactionId = jarEntry.getTransactionId();
		this.jarId = jarEntry.getJarId();
		this.balanceBefore = jarEntry.getBalanceBefore();
		this.balanceAfter = jarEntry.getBalanceAfter();
	}

	public void updateWith(final JarEntry jarEntry) {
		Objects.requireNonNull(jarEntry);

		this.balanceBefore = jarEntry.getBalanceBefore();
		this.balanceAfter = jarEntry.getBalanceAfter();
	}

	public JarEntry toBdo() {
		return new JarEntry(id, transactionId, jarId, balanceBefore,
				balanceAfter);
	}
}
