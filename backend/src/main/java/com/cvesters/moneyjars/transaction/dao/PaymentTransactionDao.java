package com.cvesters.moneyjars.transaction.dao;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;

@Getter
@Setter
@Entity
@Table(name = "payment_transactions")
@PrimaryKeyJoinColumn(name = "transaction_id")
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PaymentTransactionDao extends TransactionDao {

	@Column(name = "jar_id", nullable = false)
	private long jarId;

	@Column(name = "account_id", nullable = false)
	private long accountId;

	@Column(name = "counterparty", nullable = false)
	private String counterparty;

	@Column(name = "direction", nullable = false)
	private short direction;

	public PaymentTransactionDao(final PaymentTransaction bdo) {
		super(bdo);

		this.jarId = bdo.getJarId();
		this.accountId = bdo.getAccountId();
		this.counterparty = bdo.getCounterparty();
		this.direction = PaymentTransactionDirectionDao
				.toDao(bdo.getDirection());
	}

	public void updateWith(final PaymentTransaction bdo) {
		Objects.requireNonNull(bdo);

		super.updateWith(bdo);
		this.jarId = bdo.getJarId();
		this.accountId = bdo.getAccountId();
		this.counterparty = bdo.getCounterparty();
		this.direction = PaymentTransactionDirectionDao
				.toDao(bdo.getDirection());
	}

	@Override
	public PaymentTransaction toBdo() {
		return new PaymentTransaction(getId(), getDate(), getSequence(),
				getAmount(), getDescription(), jarId, accountId,
				counterparty, PaymentTransactionDirectionDao.toBdo(direction));
	}
}
