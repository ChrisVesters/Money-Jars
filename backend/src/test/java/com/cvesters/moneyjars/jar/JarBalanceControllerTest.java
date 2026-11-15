package com.cvesters.moneyjars.jar;

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
import com.cvesters.moneyjars.jar.bdo.JarBalanceHistory;

@GraphQlTest(JarBalanceController.class)
@Import(GraphqlExceptionResolver.class)
class JarBalanceControllerTest {

	@Autowired
	private GraphQlTester graphQlTester;

	@MockitoBean
	private JarBalanceService jarBalanceService;

	@Nested
	class QueryJarBalanceHistory {

		private static final TestJar HOUSEHOLD = TestJar.HOUSEHOLD;
		private static final TestJar HOLIDAY = TestJar.HOLIDAY;

		private static final String DOCUMENT = """
				query {
					queryJarBalanceHistory(req: { jarIds: [%s] }) {
						jarId
						snapshots {
							date
							balance
						}
					}
				}
				""";

		@Test
		void success() {
			final TestJarEntry first = TestJarEntry.HOUSEHOLD_SALARY;
			final TestJarEntry second = TestJarEntry.HOUSEHOLD_CAFE;
			final var householdHistory = new JarBalanceHistory(
					HOUSEHOLD.getId());
			householdHistory.addSnapshot(first.getTransaction().getDate(),
					first.getBalanceAfter());
			householdHistory.addSnapshot(second.getTransaction().getDate(),
					second.getBalanceAfter());
			final var holidayHistory = new JarBalanceHistory(HOLIDAY.getId());

			when(jarBalanceService
					.getHistory(List.of(HOUSEHOLD.getId(), HOLIDAY.getId())))
					.thenReturn(List.of(householdHistory, holidayHistory));

			final String document = DOCUMENT.formatted(
					HOUSEHOLD.getId() + ", " + HOLIDAY.getId());

			final Response response = graphQlTester.document(document)
					.execute();

			response.path("queryJarBalanceHistory").entityList(Object.class)
					.hasSize(2);
			assertHistory(response, "queryJarBalanceHistory[0]", HOUSEHOLD,
					first, second);
			assertHistory(response, "queryJarBalanceHistory[1]", HOLIDAY);
		}

		@Test
		void empty() {
			when(jarBalanceService.getHistory(Collections.emptyList()))
					.thenReturn(Collections.emptyList());

			final Response response = graphQlTester
					.document(DOCUMENT.formatted(""))
					.execute();

			response.path("queryJarBalanceHistory")
					.entityList(Object.class)
					.hasSize(0);
		}
	}

	private static void assertHistory(final Response response,
			final String prefix, final TestJar jar,
			final TestJarEntry... snapshots) {
		response.path(prefix + ".jarId")
				.entity(Long.class)
				.isEqualTo(jar.getId())
				.path(prefix + ".snapshots")
				.entityList(Object.class)
				.hasSize(snapshots.length);

		for (int i = 0; i < snapshots.length; i++) {
			final TestJarEntry snapshot = snapshots[i];
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
