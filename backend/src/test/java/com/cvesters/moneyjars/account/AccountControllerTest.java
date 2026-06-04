package com.cvesters.moneyjars.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.GraphQlTester.Response;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cvesters.moneyjars.account.bdo.Account;

@GraphQlTest(AccountController.class)
class AccountControllerTest {

	@Autowired
	private GraphQlTester graphQlTester;

	@MockitoBean
	private AccountService accountService;

	@Nested
	class GetAccounts {

		private static final TestAccount CHECKING = TestAccount.CHECKING;
		private static final TestAccount WALLET = TestAccount.WALLET;

		@Test
		void success() {
			final List<Account> accounts = Stream.of(CHECKING, WALLET)
					.map(TestAccount::bdo)
					.toList();

			when(accountService.getAll()).thenReturn(accounts);

			final String document = """
					query {
						getAccounts {
							id
							name
							description
							balance
						}
					}
					""";

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, CHECKING, "getAccounts[0]");
			assertEquals(response, WALLET, "getAccounts[1]");
		}
	}

	@Nested
	class GetAccount {

		private static final TestAccount CHECKING = TestAccount.CHECKING;

		@Test
		void success() {
			final Account account = CHECKING.bdo();
			when(accountService.find(CHECKING.getId()))
					.thenReturn(Optional.of(account));

			final String document = """
					query {
					  getAccount(id: 1) {
					    id
					    name
					    description
					    balance
					  }
					}
					""";

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, CHECKING, "getAccount");
		}

		@Test
		void notFound() {
			when(accountService.find(CHECKING.getId()))
					.thenReturn(Optional.empty());

			final String document = """
					query {
						getAccount(id: 1) {
							id
							name
							description
							balance
						}
					}
					""";

			graphQlTester.document(document)
					.execute()
					.path("getAccount")
					.valueIsNull();
		}
	}

	@Nested
	class CreateAccount {

		private static final TestAccount CHECKING = TestAccount.CHECKING;

		@Test
		void success() {
			final Account createdAccount = CHECKING.bdo();
			when(accountService.create(argThat(account -> {
				assertThat(account.name()).isEqualTo(CHECKING.getName());
				assertThat(account.description())
						.isEqualTo(CHECKING.getDescription());
				return true;
			}))).thenReturn(createdAccount);

			final String document = """
					mutation {
						createAccount(req: {
							name: "%s",
							description: "%s"
						}) {
							id
							name
							description
							balance
						}
					}
					""".formatted(CHECKING.getName(),
					CHECKING.getDescription());

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, CHECKING, "createAccount");
		}
	}

	@Nested
	class UpdateAccount {

		private static final TestAccount CHECKING = TestAccount.CHECKING;

		@Test
		void success() {
			final Account updatedAccount = CHECKING.bdo();
			when(accountService.update(eq(CHECKING.getId()),
					argThat(account -> {
						assertThat(account.name())
								.isEqualTo(CHECKING.getName());
						assertThat(account.description())
								.isEqualTo(CHECKING.getDescription());
						return true;
					}))).thenReturn(updatedAccount);

			final String document = """
					mutation {
						updateAccount(id: %d, req: {
							name: "%s",
							description: "%s"
						}) {
							id
							name
							description
							balance
						}
					}
					""".formatted(CHECKING.getId(), CHECKING.getName(),
					CHECKING.getDescription());

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, CHECKING, "updateAccount");
		}
	}

	@Nested
	class DeleteAccount {

		@Test
		void success() {
			final String document = """
					mutation {
						deleteAccount(id: 1)
					}
					""";

			graphQlTester.document(document).execute();

			verify(accountService).delete(1L);
		}
	}

	void assertEquals(final Response response, final TestAccount expected,
			final String prefix) {
		response.path(prefix + ".id")
				.entity(Long.class)
				.isEqualTo(expected.getId())
				.path(prefix + ".name")
				.entity(String.class)
				.isEqualTo(expected.getName())
				.path(prefix + ".description")
				.entity(String.class)
				.isEqualTo(expected.getDescription())
				.path(prefix + ".balance")
				.entity(Float.class)
				.isEqualTo(expected.getBalance().floatValue());
	}
}
