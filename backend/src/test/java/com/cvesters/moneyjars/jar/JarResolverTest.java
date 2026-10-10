package com.cvesters.moneyjars.jar;

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

@GraphQlTest({ JarController.class, JarResolver.class })
class JarResolverTest {

	@Autowired
	private GraphQlTester graphQlTester;

	@MockitoBean
	private JarService jarService;

	@MockitoBean
	private JarEntryService entryService;

	@Nested
	class ResolveBalance {

		private static final TestJar HOUSEHOLD = TestJar.HOUSEHOLD;
		private static final TestJarEntry ENTRY = TestJarEntry.HOUSEHOLD_MARKET;

		private static final String DOCUMENT = """
				query {
					getJar(id: %d) {
						balance
					}
				}
				""".formatted(HOUSEHOLD.getId());

		@Test
		void lastEntry() {
			when(jarService.find(HOUSEHOLD.getId()))
					.thenReturn(Optional.of(HOUSEHOLD.bdo()));
			when(entryService.findLast(HOUSEHOLD.getId()))
					.thenReturn(Optional.of(ENTRY.bdo()));

			graphQlTester.document(DOCUMENT)
					.execute()
					.path("getJar.balance")
					.entity(BigDecimal.class)
					.satisfies(balance -> assertThat(balance)
							.isEqualByComparingTo(ENTRY.getBalanceAfter()));
		}

		@Test
		void noEntries() {
			when(jarService.find(HOUSEHOLD.getId()))
					.thenReturn(Optional.of(HOUSEHOLD.bdo()));
			when(entryService.findLast(HOUSEHOLD.getId()))
					.thenReturn(Optional.empty());

			graphQlTester.document(DOCUMENT)
					.execute()
					.path("getJar.balance")
					.entity(BigDecimal.class)
					.satisfies(balance -> assertThat(balance)
							.isEqualByComparingTo(BigDecimal.ZERO));
		}
	}
}
