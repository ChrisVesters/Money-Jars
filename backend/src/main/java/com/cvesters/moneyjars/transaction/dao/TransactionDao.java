package com.cvesters.moneyjars.transaction.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.cvesters.moneyjars.transaction.bdo.Transaction;

@Getter
@Setter
@Entity
@Table(name = "transactions")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public abstract class TransactionDao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(lombok.AccessLevel.NONE)
	private Long id;

	@Column(nullable = false)
	private LocalDate date;

	@Column(nullable = false)
	private int sequence;

	@Column(nullable = false)
	private BigDecimal amount;

	private String beneficiary;
	private String description;

	protected TransactionDao(final Transaction bdo) {
		Objects.requireNonNull(bdo);

		this.date = bdo.getDate();
		this.sequence = bdo.getSequence();
		this.amount = bdo.getAmount();
		this.beneficiary = bdo.getBeneficiary();
		this.description = bdo.getDescription();
	}

	public void updateWith(final Transaction bdo) {
		Objects.requireNonNull(bdo);

		this.date = bdo.getDate();
		this.sequence = bdo.getSequence();
		this.amount = bdo.getAmount();
		this.beneficiary = bdo.getBeneficiary();
		this.description = bdo.getDescription();
	}

	public abstract Transaction toBdo();
}
