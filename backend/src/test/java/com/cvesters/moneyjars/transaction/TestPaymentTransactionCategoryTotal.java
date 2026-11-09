package com.cvesters.moneyjars.transaction;

import java.math.BigDecimal;
import java.util.Optional;

import lombok.Getter;

import com.cvesters.moneyjars.category.TestCategory;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionCategoryTotal;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionDirection;

@Getter
public enum TestPaymentTransactionCategoryTotal {

	JANUARY_GROCERIES(TestCategory.GROCERIES, BigDecimal.ZERO,
			TestPaymentTransaction.GROCERY.getAmount()),
	JANUARY_RENT(TestCategory.RENT, BigDecimal.ZERO,
			TestPaymentTransaction.RENT.getAmount()),
	JANUARY_UNCATEGORISED(null,
			TestPaymentTransaction.SALARY.getAmount()
					.add(TestPaymentTransaction.BONUS.getAmount())
					.add(TestPaymentTransaction.CAR_ALLOWANCE.getAmount()),
			TestPaymentTransaction.CAFE.getAmount()
					.add(TestPaymentTransaction.FLIGHTS.getAmount())
					.add(TestPaymentTransaction.HOTEL.getAmount())),
	JANUARY_10_TO_FEBRUARY_3_GROCERIES(TestCategory.GROCERIES,
			BigDecimal.ZERO,
			TestPaymentTransaction.GROCERY.getAmount()
					.add(TestPaymentTransaction.MARKET.getAmount())),
	JANUARY_10_TO_FEBRUARY_3_RENT(TestCategory.RENT, BigDecimal.ZERO,
			TestPaymentTransaction.RENT.getAmount()),
	JANUARY_10_TO_FEBRUARY_3_FUEL(TestCategory.FUEL, BigDecimal.ZERO,
			TestPaymentTransaction.FUEL.getAmount()),
	JANUARY_10_TO_FEBRUARY_3_UNCATEGORISED(null, BigDecimal.ZERO,
			TestPaymentTransaction.CAFE.getAmount()
					.add(TestPaymentTransaction.FLIGHTS.getAmount())
					.add(TestPaymentTransaction.HOTEL.getAmount()));

	private final TestCategory category;
	private final BigDecimal incoming;
	private final BigDecimal outgoing;

	TestPaymentTransactionCategoryTotal(final TestCategory category,
			final BigDecimal incoming, final BigDecimal outgoing) {
		this.category = category;
		this.incoming = incoming;
		this.outgoing = outgoing;
	}

	public Long getCategoryId() {
		return Optional.ofNullable(category)
				.map(TestCategory::getId)
				.orElse(null);
	}

	public BigDecimal getAmount(final PaymentTransactionDirection direction) {
		return switch (direction) {
			case INCOMING -> incoming;
			case OUTGOING -> outgoing;
		};
	}

	public PaymentTransactionCategoryTotal bdo() {
		return new PaymentTransactionCategoryTotal(getCategoryId(), incoming,
				outgoing);
	}
}
