package com.cvesters.moneyjars.jar;

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

import com.cvesters.moneyjars.jar.bdo.Jar;

@GraphQlTest(JarController.class)
class JarControllerTest {

	@Autowired
	private GraphQlTester graphQlTester;

	@MockitoBean
	private JarService jarService;

	@Nested
	class GetAll {

		private static final TestJar HOUSEHOLD = TestJar.HOUSEHOLD;
		private static final TestJar HOLIDAY = TestJar.HOLIDAY;

		@Test
		void success() {
			final List<Jar> jars = Stream.of(HOUSEHOLD, HOLIDAY)
					.map(TestJar::bdo)
					.toList();

			when(jarService.getAll()).thenReturn(jars);

			String document = """
					query {
						getJars {
							id
							name
							description
							balance
						}
					}
					""";

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, HOUSEHOLD, "getJars[0]");
			assertEquals(response, HOLIDAY, "getJars[1]");
		}

	}

	@Nested
	class Get {

		private static final TestJar HOUSEHOLD = TestJar.HOUSEHOLD;

		@Test
		void success() {
			final Jar jar = HOUSEHOLD.bdo();

			when(jarService.find(HOUSEHOLD.getId()))
					.thenReturn(Optional.of(jar));

			String document = """
					query {
						getJar(id: 1) {
							id
							name
							description
							balance
						}
					}
					""";

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, HOUSEHOLD, "getJar");
		}

		@Test
		void notFound() {
			when(jarService.find(HOUSEHOLD.getId()))
					.thenReturn(Optional.empty());

			String document = """
					query {
						getJar(id: 1) {
							id
							name
							description
							balance
						}
					}
					""";

			graphQlTester.document(document)
					.execute()
					.path("getJar")
					.valueIsNull();
		}

	}

	@Nested
	class Create {

		private static final TestJar HOUSEHOLD = TestJar.HOUSEHOLD;

		@Test
		void success() {
			final Jar createdJar = HOUSEHOLD.bdo();
			when(jarService.create(argThat(jar -> {
				assertThat(jar.name()).isEqualTo(HOUSEHOLD.getName());
				assertThat(jar.description())
						.isEqualTo(HOUSEHOLD.getDescription());
				return true;
			}))).thenReturn(createdJar);

			String document = """
					mutation {
						createJar(req: {
							name: "%s",
							description: "%s"
						}) {
							id
							name
							description
							balance
						}
					}
					""".formatted(HOUSEHOLD.getName(),
					HOUSEHOLD.getDescription());

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, HOUSEHOLD, "createJar");
		}

	}

	@Nested
	class Update {

		private static final TestJar HOUSEHOLD = TestJar.HOUSEHOLD;

		@Test
		void success() {
			final Jar updatedJar = HOUSEHOLD.bdo();
			when(jarService.update(eq(HOUSEHOLD.getId()), argThat(jar -> {
				assertThat(jar.name()).isEqualTo(HOUSEHOLD.getName());
				assertThat(jar.description())
						.isEqualTo(HOUSEHOLD.getDescription());
				return true;
			}))).thenReturn(updatedJar);

			String document = """
					mutation {
						updateJar(id: %d, req: {
							name: "%s",
							description: "%s"
						}) {
							id
							name
							description
							balance
						}
					}
					""".formatted(HOUSEHOLD.getId(), HOUSEHOLD.getName(),
					HOUSEHOLD.getDescription());

			final Response response = graphQlTester.document(document)
					.execute();

			assertEquals(response, HOUSEHOLD, "updateJar");
		}
	}

	@Nested
	class Delete {

		@Test
		void success() {
			String document = """
					mutation {
						deleteJar(id: 1)
					}
					""";

			graphQlTester.document(document).execute();

			verify(jarService).delete(1L);
		}
	}

	void assertEquals(final Response response, final TestJar expected,
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