package com.cvesters.moneyjars.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cvesters.moneyjars.accountentry.AccountEntryService;
import com.cvesters.moneyjars.accountentry.TestAccountEntry;

@GraphQlTest({ AccountController.class, AccountResolver.class })
class AccountResolverTest {

	@Autowired
	private GraphQlTester graphQlTester;

	@MockitoBean
	private AccountService accountService;

	@MockitoBean
	private AccountEntryService entryService;

	@Nested
	class ResolveBalance {

		private static final TestAccount CHECKING = TestAccount.CHECKING;
		private static final TestAccountEntry ENTRY = TestAccountEntry.CHECKING_RENT;

		private static final String DOCUMENT = """
				query {
					getAccount(id: %d) {
						balance
					}
				}
				""".formatted(CHECKING.getId());

		@Test
		void lastEntry() {
			when(accountService.find(CHECKING.getId()))
					.thenReturn(Optional.of(CHECKING.bdo()));
			when(entryService.findLast(CHECKING.getId()))
					.thenReturn(Optional.of(ENTRY.bdo()));

			graphQlTester.document(DOCUMENT)
					.execute()
					.path("getAccount.balance")
					.entity(BigDecimal.class)
					.satisfies(balance -> assertThat(balance)
							.isEqualByComparingTo(ENTRY.getBalanceAfter()));
		}

		@Test
		void noEntries() {
			when(accountService.find(CHECKING.getId()))
					.thenReturn(Optional.of(CHECKING.bdo()));
			when(entryService.findLast(CHECKING.getId()))
					.thenReturn(Optional.empty());

			graphQlTester.document(DOCUMENT)
					.execute()
					.path("getAccount.balance")
					.entity(BigDecimal.class)
					.satisfies(balance -> assertThat(balance)
							.isEqualByComparingTo(BigDecimal.ZERO));
		}
	}
}
