package com.cvesters.moneyjars.transaction.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.cvesters.moneyjars.transaction.bdo.Transaction;

@Getter
@Setter
@Entity
@Table(name = "transactions")
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class TransactionDao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(lombok.AccessLevel.NONE)
	private Long id;

	@Column(nullable = false)
	private LocalDate date;

	@Column(nullable = false)
	private BigDecimal amount;

	@Column(name = "jar_id", nullable = false)
	private long jarId;

	private String beneficiary;
	private String description;

	public TransactionDao(final Transaction bdo) {
		Objects.requireNonNull(bdo);

		this.date = bdo.getDate();
		this.amount = bdo.getAmount();
		this.jarId = bdo.getJarId();
		this.beneficiary = bdo.getBeneficiary();
		this.description = bdo.getDescription();
	}

	public void updateWith(final Transaction bdo) {
		Objects.requireNonNull(bdo);

		this.date = bdo.getDate();
		this.amount = bdo.getAmount();
		this.jarId = bdo.getJarId();
		this.beneficiary = bdo.getBeneficiary();
		this.description = bdo.getDescription();
	}

	public Transaction toBdo() {
		return new Transaction(id, date, amount, jarId, beneficiary,
				description);
	}
}
