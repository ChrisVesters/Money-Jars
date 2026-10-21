package com.cvesters.moneyjars.jar.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.jar.TestJar;
import com.cvesters.moneyjars.jar.TestJarEntry;
import com.cvesters.moneyjars.transaction.TestJarTransferTransaction;
import com.cvesters.moneyjars.transaction.TestPaymentTransaction;
import com.cvesters.moneyjars.transaction.bdo.JarTransferTransaction;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;

class JarEntryTest {

	@Nested
	class FirstPaymentTransaction {

		private static final TestJarEntry TEST_ENTRY = TestJarEntry.HOLIDAY_BONUS;

		@Test
		void postiveAmount() {
			final var testTransaction = TEST_ENTRY.getPaymentTransaction();
			final var transaction = testTransaction.bdo();

			final var entry = JarEntry.first(transaction);

			assertThat(entry.getId()).isNull();
			assertThat(entry.getTransactionId())
					.isEqualTo(testTransaction.getId());
			assertThat(entry.getJarId())
					.isEqualTo(testTransaction.getJar().getId());
			assertThat(entry.getBalanceBefore())
					.isEqualByComparingTo(BigDecimal.ZERO);
			assertThat(entry.getBalanceAfter())
					.isEqualByComparingTo(testTransaction.getAmount());
			assertThat(entry.getAmount())
					.isEqualByComparingTo(transaction.getAmount());
		}

		@Test
		void negativeAmount() {
			final TestJarEntry testEntry = TestJarEntry.HOLIDAY_FLIGHTS;
			final var testTransaction = testEntry.getPaymentTransaction();
			final var transaction = testTransaction.bdo();

			final var entry = JarEntry.first(transaction);

			assertThat(entry.getId()).isNull();
			assertThat(entry.getTransactionId())
					.isEqualTo(testTransaction.getId());
			assertThat(entry.getJarId())
					.isEqualTo(testTransaction.getJar().getId());
			assertThat(entry.getBalanceBefore())
					.isEqualByComparingTo(BigDecimal.ZERO);
			assertThat(entry.getBalanceAfter())
					.isEqualByComparingTo(testTransaction.getAmount().negate());
			assertThat(entry.getAmount())
					.isEqualByComparingTo(transaction.getAmount().negate());
		}

		@Test
		void transactionNull() {
			assertThatThrownBy(() -> JarEntry.first(null))
					.isInstanceOf(NullPointerException.class);
		}

	}

	@Nested
	class FirstJarTransferTransaction {

		@Test
		void targetJar() {
			final var testEntry = TestJarEntry.HOLIDAY_HOLIDAY_SAVINGS;

			final var testTransaction = testEntry.getJarTransferTransaction();
			final var jarId = testEntry.getJar().getId();
			final var transaction = testTransaction.bdo();

			final var entry = JarEntry.first(jarId, transaction);

			assertThat(entry.getId()).isNull();
			assertThat(entry.getTransactionId())
					.isEqualTo(testTransaction.getId());
			assertThat(entry.getJarId()).isEqualTo(testEntry.getJar().getId());
			assertThat(entry.getBalanceBefore())
					.isEqualByComparingTo(BigDecimal.ZERO);
			assertThat(entry.getBalanceAfter())
					.isEqualByComparingTo(testTransaction.getAmount());
			assertThat(entry.getAmount())
					.isEqualByComparingTo(transaction.getAmount());
		}

		@Test
		void sourceJar() {
			final var testEntry = TestJarEntry.HOUSEHOLD_HOLIDAY_SAVINGS;

			final var testTransaction = testEntry.getJarTransferTransaction();
			final var jarId = testEntry.getJar().getId();
			final var transaction = testTransaction.bdo();

			final var entry = JarEntry.first(jarId, transaction);

			assertThat(entry.getId()).isNull();
			assertThat(entry.getTransactionId())
					.isEqualTo(testTransaction.getId());
			assertThat(entry.getJarId()).isEqualTo(testEntry.getJar().getId());
			assertThat(entry.getBalanceBefore())
					.isEqualByComparingTo(BigDecimal.ZERO);
			assertThat(entry.getBalanceAfter())
					.isEqualByComparingTo(testTransaction.getAmount().negate());
			assertThat(entry.getAmount())
					.isEqualByComparingTo(transaction.getAmount().negate());
		}

		@Test
		void jarInvalid() {
			final var testEntry = TestJarEntry.HOUSEHOLD_HOLIDAY_SAVINGS;
			final var testJar = TestJar.CAR;

			final var testTransaction = testEntry.getJarTransferTransaction();
			final var jarId = testJar.getId();
			final var transaction = testTransaction.bdo();

			assertThatThrownBy(() -> JarEntry.first(jarId, transaction))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void transactionNull() {
			final TestJarEntry testEntry = TestJarEntry.HOLIDAY_HOLIDAY_SAVINGS;

			assertThatThrownBy(
					() -> JarEntry.first(testEntry.getJar().getId(), null))
							.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Constructor {

		private static final TestJarEntry TEST_ENTRY = TestJarEntry.HOLIDAY_BONUS;

		@Test
		void withoutId() {
			final long transactionId = TEST_ENTRY.getTransaction().getId();
			final long jarId = TEST_ENTRY.getJar().getId();
			final BigDecimal balanceBefore = TEST_ENTRY.getBalanceBefore();
			final BigDecimal balanceAfter = TEST_ENTRY.getBalanceAfter();
			final BigDecimal amount = balanceAfter.subtract(balanceBefore);

			final var entry = new JarEntry(null, transactionId, jarId,
					balanceBefore, balanceAfter);

			assertThat(entry.getId()).isNull();
			assertThat(entry.getTransactionId())
					.isEqualTo(TEST_ENTRY.getTransaction().getId());
			assertThat(entry.getJarId()).isEqualTo(TEST_ENTRY.getJar().getId());
			assertThat(entry.getBalanceBefore())
					.isEqualByComparingTo(TEST_ENTRY.getBalanceBefore());
			assertThat(entry.getBalanceAfter())
					.isEqualByComparingTo(TEST_ENTRY.getBalanceAfter());
			assertThat(entry.getAmount()).isEqualByComparingTo(amount);
		}

		@Test
		void withId() {
			final long id = TEST_ENTRY.getId();
			final long transactionId = TEST_ENTRY.getTransaction().getId();
			final long jarId = TEST_ENTRY.getJar().getId();
			final BigDecimal balanceBefore = TEST_ENTRY.getBalanceBefore();
			final BigDecimal balanceAfter = TEST_ENTRY.getBalanceAfter();
			final BigDecimal amount = balanceAfter.subtract(balanceBefore);

			final var entry = new JarEntry(id, transactionId, jarId,
					balanceBefore, balanceAfter);

			assertThat(entry.getId()).isEqualTo(TEST_ENTRY.getId());
			assertThat(entry.getTransactionId())
					.isEqualTo(TEST_ENTRY.getTransaction().getId());
			assertThat(entry.getJarId()).isEqualTo(TEST_ENTRY.getJar().getId());
			assertThat(entry.getBalanceBefore())
					.isEqualByComparingTo(TEST_ENTRY.getBalanceBefore());
			assertThat(entry.getBalanceAfter())
					.isEqualByComparingTo(TEST_ENTRY.getBalanceAfter());
			assertThat(entry.getAmount()).isEqualByComparingTo(amount);
		}

		@Test
		void balanceBeforeNull() {
			final long id = TEST_ENTRY.getId();
			final long transactionId = TEST_ENTRY.getTransaction().getId();
			final long jarId = TEST_ENTRY.getJar().getId();
			final BigDecimal balanceBefore = null;
			final BigDecimal balanceAfter = TEST_ENTRY.getBalanceAfter();

			assertThatThrownBy(() -> new JarEntry(id, transactionId, jarId,
					balanceBefore, balanceAfter))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void balanceAfterNull() {
			final long id = TEST_ENTRY.getId();
			final long transactionId = TEST_ENTRY.getTransaction().getId();
			final long jarId = TEST_ENTRY.getJar().getId();
			final BigDecimal balanceBefore = TEST_ENTRY.getBalanceBefore();
			final BigDecimal balanceAfter = null;

			assertThatThrownBy(() -> new JarEntry(id, transactionId, jarId,
					balanceBefore, balanceAfter))
							.isInstanceOf(NullPointerException.class);
		}

	}

	@Nested
	class NextPaymentTransaction {

		private static final TestJarEntry TEST_ENTRY = TestJarEntry.HOLIDAY_BONUS;

		@Test
		void positiveAmount() {
			final var entry = TEST_ENTRY.bdo();
			final var testTransaction = TestPaymentTransaction.BONUS;
			final var transaction = testTransaction.bdo();

			final BigDecimal balance = entry.getBalanceAfter()
					.add(testTransaction.getAmount());

			final JarEntry nextEntry = entry.next(transaction);

			assertThat(nextEntry.getId()).isNull();
			assertThat(nextEntry.getTransactionId())
					.isEqualTo(testTransaction.getId());
			assertThat(nextEntry.getJarId())
					.isEqualTo(TEST_ENTRY.getJar().getId());
			assertThat(nextEntry.getBalanceBefore())
					.isEqualByComparingTo(entry.getBalanceAfter());
			assertThat(nextEntry.getBalanceAfter())
					.isEqualByComparingTo(balance);
			assertThat(nextEntry.getAmount())
					.isEqualByComparingTo(transaction.getAmount());
		}

		@Test
		void negativeAmount() {
			final var entry = TEST_ENTRY.bdo();
			final var testTransaction = TestPaymentTransaction.FLIGHTS;
			final var transaction = testTransaction.bdo();

			final BigDecimal balance = entry.getBalanceAfter()
					.subtract(testTransaction.getAmount());

			final JarEntry nextEntry = entry.next(transaction);

			assertThat(nextEntry.getId()).isNull();
			assertThat(nextEntry.getTransactionId())
					.isEqualTo(testTransaction.getId());
			assertThat(nextEntry.getJarId())
					.isEqualTo(TEST_ENTRY.getJar().getId());
			assertThat(nextEntry.getBalanceBefore())
					.isEqualByComparingTo(entry.getBalanceAfter());
			assertThat(nextEntry.getBalanceAfter())
					.isEqualByComparingTo(balance);
			assertThat(nextEntry.getAmount())
					.isEqualByComparingTo(transaction.getAmount().negate());
		}

		@Test
		void wrongJar() {
			final var entry = TEST_ENTRY.bdo();
			final var testTransaction = TestPaymentTransaction.GROCERY;
			final var transaction = testTransaction.bdo();

			assertThatThrownBy(() -> entry.next(transaction))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void transactionNull() {
			final var entry = TEST_ENTRY.bdo();
			final PaymentTransaction transaction = null;

			assertThatThrownBy(() -> entry.next(transaction))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class NextJarTransferTransaction {

		@Test
		void targetJar() {
			final var testEntry = TestJarEntry.HOLIDAY_BONUS;
			final var testTransaction = TestJarTransferTransaction.HOLIDAY_SAVINGS;

			final var entry = testEntry.bdo();
			final var transaction = testTransaction.bdo();

			final BigDecimal balance = entry.getBalanceAfter()
					.add(testTransaction.getAmount());

			final JarEntry nextEntry = entry.next(transaction);

			assertThat(nextEntry.getId()).isNull();
			assertThat(nextEntry.getTransactionId())
					.isEqualTo(testTransaction.getId());
			assertThat(nextEntry.getJarId())
					.isEqualTo(testEntry.getJar().getId());
			assertThat(nextEntry.getBalanceBefore())
					.isEqualByComparingTo(entry.getBalanceAfter());
			assertThat(nextEntry.getBalanceAfter())
					.isEqualByComparingTo(balance);
			assertThat(nextEntry.getAmount())
					.isEqualByComparingTo(transaction.getAmount());
		}

		@Test
		void sourceJar() {
			final var testEntry = TestJarEntry.HOUSEHOLD_SALARY;
			final var testTransaction = TestJarTransferTransaction.HOLIDAY_SAVINGS;

			final var entry = testEntry.bdo();
			final var transaction = testTransaction.bdo();

			final BigDecimal balance = entry.getBalanceAfter()
					.subtract(testTransaction.getAmount());

			final JarEntry nextEntry = entry.next(transaction);

			assertThat(nextEntry.getId()).isNull();
			assertThat(nextEntry.getTransactionId())
					.isEqualTo(testTransaction.getId());
			assertThat(nextEntry.getJarId())
					.isEqualTo(testEntry.getJar().getId());
			assertThat(nextEntry.getBalanceBefore())
					.isEqualByComparingTo(entry.getBalanceAfter());
			assertThat(nextEntry.getBalanceAfter())
					.isEqualByComparingTo(balance);
			assertThat(nextEntry.getAmount())
					.isEqualByComparingTo(transaction.getAmount().negate());
		}

		@Test
		void jarInvalid() {
			final var testEntry = TestJarEntry.CAR_FUEL;
			final var testTransaction = TestJarTransferTransaction.HOLIDAY_SAVINGS;

			final var entry = testEntry.bdo();
			final var transaction = testTransaction.bdo();

			assertThatThrownBy(() -> entry.next(transaction))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void transactionNull() {
			final var testEntry = TestJarEntry.HOUSEHOLD_SALARY;

			final var entry = testEntry.bdo();
			final JarTransferTransaction transaction = null;

			assertThatThrownBy(() -> entry.next(transaction))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Shift {

		private static final TestJarEntry TEST_ENTRY = TestJarEntry.HOLIDAY_BONUS;
		private final JarEntry entry = TEST_ENTRY.bdo();

		@Test
		void amountPositive() {
			final BigDecimal before = entry.getBalanceBefore();
			final BigDecimal after = entry.getBalanceAfter();
			final BigDecimal amount = new BigDecimal("23.47");

			entry.shift(amount);

			final BigDecimal expectedBefore = before.add(amount);
			final BigDecimal expectedAfter = after.add(amount);
			final BigDecimal expectedAmount = after.subtract(before);

			assertThat(entry.getBalanceBefore())
					.isEqualByComparingTo(expectedBefore);
			assertThat(entry.getBalanceAfter())
					.isEqualByComparingTo(expectedAfter);
			assertThat(entry.getAmount()).isEqualByComparingTo(expectedAmount);
		}

		@Test
		void amountNegative() {
			final BigDecimal before = entry.getBalanceBefore();
			final BigDecimal after = entry.getBalanceAfter();
			final BigDecimal amount = new BigDecimal("23.47");

			entry.shift(amount.negate());

			final BigDecimal expectedBefore = before.subtract(amount);
			final BigDecimal expectedAfter = after.subtract(amount);
			final BigDecimal expectedAmount = after.subtract(before);

			assertThat(entry.getBalanceBefore())
					.isEqualByComparingTo(expectedBefore);
			assertThat(entry.getBalanceAfter())
					.isEqualByComparingTo(expectedAfter);
			assertThat(entry.getAmount()).isEqualByComparingTo(expectedAmount);

		}

		@Test
		void amountZero() {
			final BigDecimal before = entry.getBalanceBefore();
			final BigDecimal after = entry.getBalanceAfter();

			entry.shift(BigDecimal.ZERO);

			final BigDecimal expectedAmount = after.subtract(before);

			assertThat(entry.getBalanceBefore()).isEqualByComparingTo(before);
			assertThat(entry.getBalanceAfter()).isEqualByComparingTo(after);
			assertThat(entry.getAmount()).isEqualByComparingTo(expectedAmount);

		}

		@Test
		void amountNull() {
			assertThatThrownBy(() -> entry.shift(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
