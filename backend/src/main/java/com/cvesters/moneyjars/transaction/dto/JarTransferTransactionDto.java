package com.cvesters.moneyjars.transaction.dto;

import lombok.Getter;

import com.cvesters.moneyjars.transaction.bdo.JarTransferTransaction;

@Getter 
public final class JarTransferTransactionDto extends TransactionDto {

	private long sourceJarId;
	private long targetJarId;
	
	public JarTransferTransactionDto(final JarTransferTransaction bdo) {
		super(bdo);
		
		this.sourceJarId = bdo.getSourceJarId();
		this.targetJarId = bdo.getTargetJarId();
	}
}
