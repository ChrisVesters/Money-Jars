package com.cvesters.moneyjars.account;

import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.GraphQlTester.Response;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cvesters.moneyjars.config.GraphqlExceptionResolver;
import com.cvesters.moneyjars.account.bdo.AccountBalanceHistory;

@GraphQlTest(AccountBalanceController.class)
@Import(GraphqlExceptionResolver.class)
class AccountBalanceControllerTest {

	@Autowired
	private GraphQlTester graphQlTester;

	@MockitoBean
	private AccountBalanceService accountBalanceService;

	@Nested
	class QueryAccountBalanceHistory {

		private static final TestAccount CHECKING = TestAccount.CHECKING;
		private static final TestAccount WALLET = TestAccount.WALLET;

		private static final String DOCUMENT = """
				query {
					queryAccountBalanceHistory(req: { accountIds: [%s] }) {
						accountId
						snapshots {
							date
							balance
						}
					}
				}
				""";

		@Test
		void success() {
			final TestAccountEntry first = TestAccountEntry.CHECKING_SALARY;
			final TestAccountEntry second = TestAccountEntry.CHECKING_RENT;
			final var householdHistory = new AccountBalanceHistory(
					CHECKING.getId());
			householdHistory.addSnapshot(first.getTransaction().getDate(),
					first.getBalanceAfter());
			householdHistory.addSnapshot(second.getTransaction().getDate(),
					second.getBalanceAfter());
			final var holidayHistory = new AccountBalanceHistory(WALLET.getId());

			when(accountBalanceService
					.getHistory(List.of(CHECKING.getId(), WALLET.getId())))
					.thenReturn(List.of(householdHistory, holidayHistory));

			final String document = DOCUMENT.formatted(
					CHECKING.getId() + ", " + WALLET.getId());

			final Response response = graphQlTester.document(document)
					.execute();

			response.path("queryAccountBalanceHistory").entityList(Object.class)
					.hasSize(2);
			assertHistory(response, "queryAccountBalanceHistory[0]", CHECKING,
					first, second);
			assertHistory(response, "queryAccountBalanceHistory[1]", WALLET);
		}

		@Test
		void empty() {
			when(accountBalanceService.getHistory(Collections.emptyList()))
					.thenReturn(Collections.emptyList());

			final Response response = graphQlTester
					.document(DOCUMENT.formatted(""))
					.execute();

			response.path("queryAccountBalanceHistory")
					.entityList(Object.class)
					.hasSize(0);
		}
	}

	private static void assertHistory(final Response response,
			final String prefix, final TestAccount account,
			final TestAccountEntry... snapshots) {
		response.path(prefix + ".accountId")
				.entity(Long.class)
				.isEqualTo(account.getId())
				.path(prefix + ".snapshots")
				.entityList(Object.class)
				.hasSize(snapshots.length);

		for (int i = 0; i < snapshots.length; i++) {
			final TestAccountEntry snapshot = snapshots[i];
			final String snapshotPrefix = prefix + ".snapshots[" + i + "]";
			response.path(snapshotPrefix + ".date")
					.entity(String.class)
					.isEqualTo(snapshot.getTransaction().getDate().toString())
					.path(snapshotPrefix + ".balance")
					.entity(String.class)
					.isEqualTo(snapshot.getBalanceAfter().toPlainString());
		}
	}
}
