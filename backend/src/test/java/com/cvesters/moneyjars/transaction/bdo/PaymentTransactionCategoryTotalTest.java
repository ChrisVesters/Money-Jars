package com.cvesters.moneyjars.transaction.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.transaction.TestPaymentTransactionCategoryTotal;

class PaymentTransactionCategoryTotalTest {

	private static final TestPaymentTransactionCategoryTotal TOTAL = TestPaymentTransactionCategoryTotal.JANUARY_UNCATEGORISED;

	@Nested
	class Constructor {

		@Test
		void success() {
			final Long categoryId = TOTAL.getCategoryId();
			final BigDecimal incoming = TOTAL.getIncoming();
			final BigDecimal outgoing = TOTAL.getOutgoing();

			final var total = new PaymentTransactionCategoryTotal(categoryId,
					incoming, outgoing);

			assertThat(total.categoryId()).isEqualTo(categoryId);
			assertThat(total.incoming()).isEqualTo(incoming);
			assertThat(total.outgoing()).isEqualTo(outgoing);
		}

		@Test
		void categoryNull() {
			final BigDecimal incoming = TOTAL.getIncoming();
			final BigDecimal outgoing = TOTAL.getOutgoing();

			final var total = new PaymentTransactionCategoryTotal(null,
					incoming, outgoing);

			assertThat(total.categoryId()).isNull();
		}

		@Test
		void zero() {
			final Long categoryId = TOTAL.getCategoryId();
			final BigDecimal incoming = BigDecimal.ZERO;
			final BigDecimal outgoing = BigDecimal.ZERO;

			final var total = new PaymentTransactionCategoryTotal(categoryId,
					incoming, outgoing);

			assertThat(total.incoming()).isEqualTo(incoming);
			assertThat(total.outgoing()).isEqualTo(outgoing);
		}

		@Test
		void incomingNull() {
			final Long categoryId = TOTAL.getCategoryId();
			final BigDecimal outgoing = TOTAL.getOutgoing();

			assertThatThrownBy(
					() -> new PaymentTransactionCategoryTotal(categoryId, null,
							outgoing)).isInstanceOf(NullPointerException.class);
		}

		@Test
		void incomingNegative() {
			final Long categoryId = TOTAL.getCategoryId();
			final BigDecimal incoming = TOTAL.getIncoming().negate();
			final BigDecimal outgoing = TOTAL.getOutgoing();

			assertThatThrownBy(() -> new PaymentTransactionCategoryTotal(
					categoryId, incoming, outgoing))
							.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void outgoingNull() {
			final Long categoryId = TOTAL.getCategoryId();
			final BigDecimal incoming = TOTAL.getIncoming();

			assertThatThrownBy(() -> new PaymentTransactionCategoryTotal(
					categoryId, incoming, null))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void outgoingNegative() {
			final Long categoryId = TOTAL.getCategoryId();
			final BigDecimal incoming = TOTAL.getIncoming();
			final BigDecimal outgoing = TOTAL.getOutgoing().negate();

			assertThatThrownBy(() -> new PaymentTransactionCategoryTotal(
					categoryId, incoming, outgoing))
							.isInstanceOf(IllegalArgumentException.class);
		}
	}

	@Nested
	class Of {

		@Test
		void incoming() {
			final Long categoryId = TOTAL.getCategoryId();
			final PaymentTransactionDirection direction = PaymentTransactionDirection.INCOMING;
			final BigDecimal amount = TOTAL.getIncoming();

			final var total = PaymentTransactionCategoryTotal.of(categoryId,
					direction, amount);

			assertThat(total).isEqualTo(new PaymentTransactionCategoryTotal(
					categoryId, amount, BigDecimal.ZERO));
		}

		@Test
		void outgoing() {
			final Long categoryId = TOTAL.getCategoryId();
			final PaymentTransactionDirection direction = PaymentTransactionDirection.OUTGOING;
			final BigDecimal amount = TOTAL.getOutgoing();

			final var total = PaymentTransactionCategoryTotal.of(categoryId,
					direction, amount);

			assertThat(total).isEqualTo(new PaymentTransactionCategoryTotal(
					categoryId, BigDecimal.ZERO, amount));
		}

		@Test
		void directionNull() {
			final Long categoryId = TOTAL.getCategoryId();
			final BigDecimal amount = TOTAL.getIncoming();

			assertThatThrownBy(() -> PaymentTransactionCategoryTotal
					.of(categoryId, null, amount))
							.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Add {

		@Test
		void success() {
			final var incoming = new PaymentTransactionCategoryTotal(
					TOTAL.getCategoryId(), TOTAL.getIncoming(),
					BigDecimal.ZERO);
			final var outgoing = new PaymentTransactionCategoryTotal(
					TOTAL.getCategoryId(), BigDecimal.ZERO,
					TOTAL.getOutgoing());

			final PaymentTransactionCategoryTotal total = incoming
					.add(outgoing);

			assertThat(total).isEqualTo(TOTAL.bdo());
		}

		@Test
		void otherCategory() {
			final PaymentTransactionCategoryTotal total = TOTAL.bdo();
			final PaymentTransactionCategoryTotal other = TestPaymentTransactionCategoryTotal.JANUARY_RENT
					.bdo();

			assertThatThrownBy(() -> total.add(other))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void otherNull() {
			final PaymentTransactionCategoryTotal total = TOTAL.bdo();

			assertThatThrownBy(() -> total.add(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
