package com.cvesters.moneyjars.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.cvesters.moneyjars.transaction.bdo.Transaction;

public interface TestTransaction {

	long getId();

	LocalDate getDate();

	int getSequence();

	BigDecimal getAmount();

	default String getAmountString() {
		return getAmount().toPlainString();
	}

	String getDescription();

	Transaction bdo();
}
