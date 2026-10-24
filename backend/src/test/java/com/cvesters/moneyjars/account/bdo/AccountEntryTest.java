package com.cvesters.moneyjars.account.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.moneyjars.account.TestAccount;
import com.cvesters.moneyjars.account.TestAccountEntry;
import com.cvesters.moneyjars.transaction.TestAccountTransferTransaction;
import com.cvesters.moneyjars.transaction.TestPaymentTransaction;
import com.cvesters.moneyjars.transaction.bdo.AccountTransferTransaction;
import com.cvesters.moneyjars.transaction.bdo.PaymentTransaction;

class AccountEntryTest {

	@Nested
	class FirstPaymentTransaction {

		private static final TestAccountEntry TEST_ENTRY = TestAccountEntry.CHECKING_SALARY;

		@Test
		void postiveAmount() {
			final var testTransaction = TEST_ENTRY.getPaymentTransaction();
			final var transaction = testTransaction.bdo();

			final var entry = AccountEntry.first(transaction);

			assertThat(entry.getId()).isNull();
			assertThat(entry.getTransactionId())
					.isEqualTo(testTransaction.getId());
			assertThat(entry.getAccountId())
					.isEqualTo(testTransaction.getAccount().getId());
			assertThat(entry.getBalanceBefore())
					.isEqualByComparingTo(BigDecimal.ZERO);
			assertThat(entry.getBalanceAfter())
					.isEqualByComparingTo(testTransaction.getAmount());
			assertThat(entry.getAmount())
					.isEqualByComparingTo(transaction.getAmount());
		}

		@Test
		void negativeAmount() {
			final TestAccountEntry testEntry = TestAccountEntry.CREDIT_CARD_FLIGHTS;
			final var testTransaction = testEntry.getPaymentTransaction();
			final var transaction = testTransaction.bdo();

			final var entry = AccountEntry.first(transaction);

			assertThat(entry.getId()).isNull();
			assertThat(entry.getTransactionId())
					.isEqualTo(testTransaction.getId());
			assertThat(entry.getAccountId())
					.isEqualTo(testTransaction.getAccount().getId());
			assertThat(entry.getBalanceBefore())
					.isEqualByComparingTo(BigDecimal.ZERO);
			assertThat(entry.getBalanceAfter())
					.isEqualByComparingTo(testTransaction.getAmount().negate());
			assertThat(entry.getAmount())
					.isEqualByComparingTo(transaction.getAmount().negate());
		}

		@Test
		void transactionNull() {
			assertThatThrownBy(() -> AccountEntry.first(null))
					.isInstanceOf(NullPointerException.class);
		}

	}

	@Nested
	class FirstAccountTransferTransaction {

		@Test
		void targetAccount() {
			final var testEntry = TestAccountEntry.WALLET_CASH_WITHDRAWAL;

			final var testTransaction = testEntry
					.getAccountTransferTransaction();
			final var accountId = testEntry.getAccount().getId();
			final var transaction = testTransaction.bdo();

			final var entry = AccountEntry.first(accountId, transaction);

			assertThat(entry.getId()).isNull();
			assertThat(entry.getTransactionId())
					.isEqualTo(testTransaction.getId());
			assertThat(entry.getAccountId())
					.isEqualTo(testEntry.getAccount().getId());
			assertThat(entry.getBalanceBefore())
					.isEqualByComparingTo(BigDecimal.ZERO);
			assertThat(entry.getBalanceAfter())
					.isEqualByComparingTo(testTransaction.getAmount());
			assertThat(entry.getAmount())
					.isEqualByComparingTo(transaction.getAmount());
		}

		@Test
		void sourceAccount() {
			final var testEntry = TestAccountEntry.CHECKING_CASH_WITHDRAWAL;

			final var testTransaction = testEntry
					.getAccountTransferTransaction();
			final var accountId = testEntry.getAccount().getId();
			final var transaction = testTransaction.bdo();

			final var entry = AccountEntry.first(accountId, transaction);

			assertThat(entry.getId()).isNull();
			assertThat(entry.getTransactionId())
					.isEqualTo(testTransaction.getId());
			assertThat(entry.getAccountId())
					.isEqualTo(testEntry.getAccount().getId());
			assertThat(entry.getBalanceBefore())
					.isEqualByComparingTo(BigDecimal.ZERO);
			assertThat(entry.getBalanceAfter())
					.isEqualByComparingTo(testTransaction.getAmount().negate());
			assertThat(entry.getAmount())
					.isEqualByComparingTo(transaction.getAmount().negate());
		}

		@Test
		void accountInvalid() {
			final var testEntry = TestAccountEntry.CHECKING_CASH_WITHDRAWAL;
			final var testAccount = TestAccount.SAVINGS;

			final var testTransaction = testEntry
					.getAccountTransferTransaction();
			final var accountId = testAccount.getId();
			final var transaction = testTransaction.bdo();

			assertThatThrownBy(() -> AccountEntry.first(accountId, transaction))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void transactionNull() {
			final TestAccountEntry testEntry = TestAccountEntry.WALLET_CASH_WITHDRAWAL;

			assertThatThrownBy(() -> AccountEntry
					.first(testEntry.getAccount().getId(), null))
							.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Constructor {

		private static final TestAccountEntry TEST_ENTRY = TestAccountEntry.CHECKING_SALARY;

		@Test
		void withoutId() {
			final long transactionId = TEST_ENTRY.getTransaction().getId();
			final long accountId = TEST_ENTRY.getAccount().getId();
			final BigDecimal balanceBefore = TEST_ENTRY.getBalanceBefore();
			final BigDecimal balanceAfter = TEST_ENTRY.getBalanceAfter();
			final BigDecimal amount = balanceAfter.subtract(balanceBefore);

			final var entry = new AccountEntry(null, transactionId, accountId,
					balanceBefore, balanceAfter);

			assertThat(entry.getId()).isNull();
			assertThat(entry.getTransactionId())
					.isEqualTo(TEST_ENTRY.getTransaction().getId());
			assertThat(entry.getAccountId())
					.isEqualTo(TEST_ENTRY.getAccount().getId());
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
			final long accountId = TEST_ENTRY.getAccount().getId();
			final BigDecimal balanceBefore = TEST_ENTRY.getBalanceBefore();
			final BigDecimal balanceAfter = TEST_ENTRY.getBalanceAfter();
			final BigDecimal amount = balanceAfter.subtract(balanceBefore);

			final var entry = new AccountEntry(id, transactionId, accountId,
					balanceBefore, balanceAfter);

			assertThat(entry.getId()).isEqualTo(TEST_ENTRY.getId());
			assertThat(entry.getTransactionId())
					.isEqualTo(TEST_ENTRY.getTransaction().getId());
			assertThat(entry.getAccountId())
					.isEqualTo(TEST_ENTRY.getAccount().getId());
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
			final long accountId = TEST_ENTRY.getAccount().getId();
			final BigDecimal balanceBefore = null;
			final BigDecimal balanceAfter = TEST_ENTRY.getBalanceAfter();

			assertThatThrownBy(() -> new AccountEntry(id, transactionId,
					accountId, balanceBefore, balanceAfter))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void balanceAfterNull() {
			final long id = TEST_ENTRY.getId();
			final long transactionId = TEST_ENTRY.getTransaction().getId();
			final long accountId = TEST_ENTRY.getAccount().getId();
			final BigDecimal balanceBefore = TEST_ENTRY.getBalanceBefore();
			final BigDecimal balanceAfter = null;

			assertThatThrownBy(() -> new AccountEntry(id, transactionId,
					accountId, balanceBefore, balanceAfter))
							.isInstanceOf(NullPointerException.class);
		}

	}

	@Nested
	class NextPaymentTransaction {

		private static final TestAccountEntry TEST_ENTRY = TestAccountEntry.CHECKING_SALARY;

		@Test
		void positiveAmount() {
			final var entry = TEST_ENTRY.bdo();
			final var testTransaction = TestPaymentTransaction.CAR_ALLOWANCE;
			final var transaction = testTransaction.bdo();

			final BigDecimal balance = entry.getBalanceAfter()
					.add(testTransaction.getAmount());

			final AccountEntry nextEntry = entry.next(transaction);

			assertThat(nextEntry.getId()).isNull();
			assertThat(nextEntry.getTransactionId())
					.isEqualTo(testTransaction.getId());
			assertThat(nextEntry.getAccountId())
					.isEqualTo(TEST_ENTRY.getAccount().getId());
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
			final var testTransaction = TestPaymentTransaction.RENT;
			final var transaction = testTransaction.bdo();

			final BigDecimal balance = entry.getBalanceAfter()
					.subtract(testTransaction.getAmount());

			final AccountEntry nextEntry = entry.next(transaction);

			assertThat(nextEntry.getId()).isNull();
			assertThat(nextEntry.getTransactionId())
					.isEqualTo(testTransaction.getId());
			assertThat(nextEntry.getAccountId())
					.isEqualTo(TEST_ENTRY.getAccount().getId());
			assertThat(nextEntry.getBalanceBefore())
					.isEqualByComparingTo(entry.getBalanceAfter());
			assertThat(nextEntry.getBalanceAfter())
					.isEqualByComparingTo(balance);
			assertThat(nextEntry.getAmount())
					.isEqualByComparingTo(transaction.getAmount().negate());
		}

		@Test
		void wrongAccount() {
			final var entry = TEST_ENTRY.bdo();
			final var testTransaction = TestPaymentTransaction.CAFE;
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
	class NextAccountTransferTransaction {

		@Test
		void targetAccount() {
			final var testEntry = TestAccountEntry.WALLET_CAFE;
			final var testTransaction = TestAccountTransferTransaction.CASH_WITHDRAWAL;

			final var entry = testEntry.bdo();
			final var transaction = testTransaction.bdo();

			final BigDecimal balance = entry.getBalanceAfter()
					.add(testTransaction.getAmount());

			final AccountEntry nextEntry = entry.next(transaction);

			assertThat(nextEntry.getId()).isNull();
			assertThat(nextEntry.getTransactionId())
					.isEqualTo(testTransaction.getId());
			assertThat(nextEntry.getAccountId())
					.isEqualTo(testEntry.getAccount().getId());
			assertThat(nextEntry.getBalanceBefore())
					.isEqualByComparingTo(entry.getBalanceAfter());
			assertThat(nextEntry.getBalanceAfter())
					.isEqualByComparingTo(balance);
			assertThat(nextEntry.getAmount())
					.isEqualByComparingTo(transaction.getAmount());
		}

		@Test
		void sourceAccount() {
			final var testEntry = TestAccountEntry.CHECKING_SALARY;
			final var testTransaction = TestAccountTransferTransaction.CASH_WITHDRAWAL;

			final var entry = testEntry.bdo();
			final var transaction = testTransaction.bdo();

			final BigDecimal balance = entry.getBalanceAfter()
					.subtract(testTransaction.getAmount());

			final AccountEntry nextEntry = entry.next(transaction);

			assertThat(nextEntry.getId()).isNull();
			assertThat(nextEntry.getTransactionId())
					.isEqualTo(testTransaction.getId());
			assertThat(nextEntry.getAccountId())
					.isEqualTo(testEntry.getAccount().getId());
			assertThat(nextEntry.getBalanceBefore())
					.isEqualByComparingTo(entry.getBalanceAfter());
			assertThat(nextEntry.getBalanceAfter())
					.isEqualByComparingTo(balance);
			assertThat(nextEntry.getAmount())
					.isEqualByComparingTo(transaction.getAmount().negate());
		}

		@Test
		void accountInvalid() {
			final var testEntry = TestAccountEntry.CREDIT_CARD_FUEL;
			final var testTransaction = TestAccountTransferTransaction.CASH_WITHDRAWAL;

			final var entry = testEntry.bdo();
			final var transaction = testTransaction.bdo();

			assertThatThrownBy(() -> entry.next(transaction))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void transactionNull() {
			final var testEntry = TestAccountEntry.CHECKING_SALARY;

			final var entry = testEntry.bdo();
			final AccountTransferTransaction transaction = null;

			assertThatThrownBy(() -> entry.next(transaction))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Shift {

		private static final TestAccountEntry TEST_ENTRY = TestAccountEntry.CHECKING_SALARY;
		private final AccountEntry entry = TEST_ENTRY.bdo();

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
