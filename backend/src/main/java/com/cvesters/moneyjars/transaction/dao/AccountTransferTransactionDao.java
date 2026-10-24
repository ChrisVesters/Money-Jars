package com.cvesters.moneyjars.transaction.dao;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.cvesters.moneyjars.transaction.bdo.AccountTransferTransaction;

@Getter
@Setter
@Entity
@Table(name = "account_transfer_transactions")
@PrimaryKeyJoinColumn(name = "transaction_id")
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class AccountTransferTransactionDao extends TransactionDao {

	@Column(name = "source_account_id", nullable = false)
	private long sourceAccountId;

	@Column(name = "target_account_id", nullable = false)
	private long targetAccountId;

	public AccountTransferTransactionDao(final AccountTransferTransaction bdo) {
		super(bdo);

		this.sourceAccountId = bdo.getSourceAccountId();
		this.targetAccountId = bdo.getTargetAccountId();
	}

	public void updateWith(final AccountTransferTransaction bdo) {
		Objects.requireNonNull(bdo);

		super.updateWith(bdo);

		this.sourceAccountId = bdo.getSourceAccountId();
		this.targetAccountId = bdo.getTargetAccountId();
	}

	@Override
	public AccountTransferTransaction toBdo() {
		return new AccountTransferTransaction(getId(), getDate(), getSequence(),
				getAmount(), getDescription(), getSourceAccountId(),
				getTargetAccountId());
	}
}
