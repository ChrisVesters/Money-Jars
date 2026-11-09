package com.cvesters.moneyjars.transaction.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.transaction.TestPaymentTransactionCategoryTotal;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionCategoryTotal;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransactionDirection;

class PaymentTransactionCategoryTotalDaoTest {

	private static final TestPaymentTransactionCategoryTotal TOTAL = TestPaymentTransactionCategoryTotal.JANUARY_UNCATEGORISED;

	private static final short INCOMING = PaymentTransactionDirectionDao
			.toDao(PaymentTransactionDirection.INCOMING);
	private static final short OUTGOING = PaymentTransactionDirectionDao
			.toDao(PaymentTransactionDirection.OUTGOING);

	@Nested
	class ToBdo {

		@Test
		void incoming() {
			final var dao = new PaymentTransactionCategoryTotalDao(
					TOTAL.getCategoryId(), INCOMING, TOTAL.getIncoming());

			final PaymentTransactionCategoryTotal bdo = dao.toBdo();

			assertThat(bdo).isEqualTo(
					new PaymentTransactionCategoryTotal(TOTAL.getCategoryId(),
							TOTAL.getIncoming(), BigDecimal.ZERO));
		}

		@Test
		void outgoing() {
			final TestPaymentTransactionCategoryTotal total = TestPaymentTransactionCategoryTotal.JANUARY_RENT;
			final var dao = new PaymentTransactionCategoryTotalDao(
					total.getCategoryId(), OUTGOING, total.getOutgoing());

			final PaymentTransactionCategoryTotal bdo = dao.toBdo();

			assertThat(bdo).isEqualTo(total.bdo());
		}

		@Test
		void directionInvalid() {
			final var dao = new PaymentTransactionCategoryTotalDao(
					TOTAL.getCategoryId(), Short.MAX_VALUE,
					TOTAL.getIncoming());

			assertThatThrownBy(dao::toBdo)
					.isInstanceOf(IllegalArgumentException.class);
		}
	}

	@Nested
	class ToBdoList {

		@Test
		void mergesDirectionsPerCategory() {
			final List<PaymentTransactionCategoryTotalDao> daos = List.of(
					new PaymentTransactionCategoryTotalDao(
							TOTAL.getCategoryId(), INCOMING,
							TOTAL.getIncoming()),
					new PaymentTransactionCategoryTotalDao(
							TOTAL.getCategoryId(), OUTGOING,
							TOTAL.getOutgoing()));

			final List<PaymentTransactionCategoryTotal> bdos = PaymentTransactionCategoryTotalDao
					.toBdo(daos);

			assertThat(bdos).containsExactly(TOTAL.bdo());
		}

		@Test
		void multiple() {
			final TestPaymentTransactionCategoryTotal groceries = TestPaymentTransactionCategoryTotal.JANUARY_GROCERIES;
			final TestPaymentTransactionCategoryTotal rent = TestPaymentTransactionCategoryTotal.JANUARY_RENT;
			final List<PaymentTransactionCategoryTotalDao> daos = List.of(
					new PaymentTransactionCategoryTotalDao(
							groceries.getCategoryId(), OUTGOING,
							groceries.getOutgoing()),
					new PaymentTransactionCategoryTotalDao(rent.getCategoryId(),
							OUTGOING, rent.getOutgoing()),
					new PaymentTransactionCategoryTotalDao(
							TOTAL.getCategoryId(), INCOMING,
							TOTAL.getIncoming()),
					new PaymentTransactionCategoryTotalDao(
							TOTAL.getCategoryId(), OUTGOING,
							TOTAL.getOutgoing()));

			final List<PaymentTransactionCategoryTotal> bdos = PaymentTransactionCategoryTotalDao
					.toBdo(daos);

			assertThat(bdos).containsExactly(groceries.bdo(), rent.bdo(),
					TOTAL.bdo());
		}

		@Test
		void empty() {
			final List<PaymentTransactionCategoryTotal> bdos = PaymentTransactionCategoryTotalDao
					.toBdo(List.of());

			assertThat(bdos).isEmpty();
		}

		@Test
		void daosNull() {
			assertThatThrownBy(
					() -> PaymentTransactionCategoryTotalDao.toBdo(null))
							.isInstanceOf(NullPointerException.class);
		}
	}
}
