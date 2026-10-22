package com.cvesters.moneyjars.transaction.dao;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.cvesters.moneyjars.transaction.bdo.JarTransferTransaction;

@Getter
@Setter
@Entity
@Table(name = "jar_transfer_transactions")
@PrimaryKeyJoinColumn(name = "transaction_id")
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class JarTransferTransactionDao extends TransactionDao {

	@Column(name = "source_jar_id", nullable = false)
	private long sourceJarId;

	@Column(name = "target_jar_id", nullable = false)
	private long targetJarId;

	public JarTransferTransactionDao(final JarTransferTransaction bdo) {
		super(bdo);

		this.sourceJarId = bdo.getSourceJarId();
		this.targetJarId = bdo.getTargetJarId();
	}

	public void updateWith(final JarTransferTransaction bdo) {
		Objects.requireNonNull(bdo);

		super.updateWith(bdo);

		this.sourceJarId = bdo.getSourceJarId();
		this.targetJarId = bdo.getTargetJarId();
	}

	@Override
	public JarTransferTransaction toBdo() {
		return new JarTransferTransaction(getId(), getDate(), getSequence(),
				getAmount(), getDescription(), getSourceJarId(),
				getTargetJarId());
	}
}
