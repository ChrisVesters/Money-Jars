<!--
Guidance for AI coding agents working on the Money-Jars repository.
Keep these instructions short and actionable; reference concrete files and commands.
-->
# Money-Jars — Copilot instructions

This repository contains a split frontend + backend app:
- backend: Spring Boot (Maven) Java application in `backend/`.
- frontend: Vite + React + TypeScript app in `frontend/`.

Key facts (read these first)
- Java target: `java.version` is set to 25 in `backend/pom.xml` — ensure the environment has a matching JDK.
- The backend uses Spring Boot, Spring Data JPA, Flyway (database migrations), Lombok, and Testcontainers for tests.
- The frontend uses Vite + React (React 19) and TypeScript; scripts are in `frontend/package.json`.

How to run & build (developer workflows)
- Backend (recommended) — use the Maven wrapper from the `backend/` folder:
  - Run during development:
    ./mvnw spring-boot:run  (from backend/)
  - Build an executable jar:
    ./mvnw -f backend/ package
    java -jar backend/target/*.jar
  - Run tests (they use Testcontainers; Docker must be available):
    ./mvnw -f backend/ test

- Frontend:
  - Install deps and run dev server:
    cd frontend && npm install && npm run dev
  - Production build:
    cd frontend && npm run build
  - Linting: `cd frontend && npm run lint`

Important repo-specific patterns and examples
- Backend feature layout: keep new Spring Boot code in `backend/src/main/java/com/cvesters/moneyjars/<feature>/` and mirror the existing split between `controller`, `service`, `repository`, `storage gateway`, `bdo`, `dao`, and `dto` packages.
- Current backend feature folders are `jar/` and `transaction/`; add new features in the same style rather than flattening them into one package.
- Flyway migrations: place SQL migrations in `backend/src/main/resources/db/migration/` (standard Flyway path). There are currently no migrations in that folder.
- Testcontainers wiring: `backend/src/test/java/com/cvesters/moneyjars/TestcontainersConfiguration.java` is the canonical setup for PostgreSQL-backed tests. Keep Docker-based Testcontainers tests under `backend/src/test/java/com/cvesters/moneyjars/` and follow the same package naming as the production code it exercises.
- Application properties: `backend/src/main/resources/application.properties` contains the local datasource and server configuration. Keep environment-specific values there or in the existing Spring profile convention.
- Lombok: annotation processing is configured in `pom.xml`. Keep Lombok annotations (if added) and do not remove the compiler/plugin config unless you add a replacement.
- Frontend structure: keep UI code under `frontend/src/` in feature folders such as `jar/`, `transaction/`, and `pages/`; shared UI helpers belong in `common/` or `components/`.
- Generated GraphQL code lives in `frontend/src/gql/` and is produced from `frontend/src/**/*.graphql` by `frontend/codegen.ts`. Do not hand-edit generated files; update the GraphQL documents and regenerate with `npm run codegen`.

Conventions and guardrails for code changes
- Keep new backend and frontend files in the same structural style as the current project: feature folders, package-level separation, and small focused components/services.
- Do not change parent POM selection lightly — `backend/pom.xml` intentionally uses the Spring Boot parent and Lombok annotation processing. If you change build tooling, verify the effect on Java 25 compilation, Testcontainers, and generated artifacts.
- When adding DB migrations, use Flyway's naming conventions (`V{version}__desc.sql`) and commit them into `backend/src/main/resources/db/migration/`.
- When adding tests, mirror the production package path under `backend/src/test/java/`. Prefer the existing `com.cvesters.moneyjars.*` package structure and keep Testcontainers-based setup in `TestcontainersConfiguration.java`.
- The current backend test surface is intentionally minimal; if you add real tests, verify them with `./mvnw -f backend/ test` and ensure they are not dependent on hidden generated classes.
- Do not add comments anywhere in source code. Do not add Javadoc, block comments (`/* ... */`) or line comments (`//`) in any source files. Comments in documentation (Markdown/README) or non-source assets are allowed.
- Use tabs for indentation in all source files and new edits. Do not introduce spaces for code indentation in modified files.
- Do not hand-edit generated files in `frontend/src/gql/`; regenerate them from the GraphQL documents instead.

Helpful files to inspect when you start working
- `backend/pom.xml` — dependencies, java version, build plugins (Lombok, spring-boot-plugin).
- `backend/HELP.md` — curated notes about Testcontainers, Flyway and parent-POM caveats.
- `backend/src/test/java/com/cvesters/moneyjars/TestcontainersConfiguration.java` — canonical Testcontainers wiring.
- `frontend/package.json` and `frontend/vite.config.ts` — frontend scripts and build details.

Tasks AI agents can do immediately
- Add a Flyway migration: create `backend/src/main/resources/db/migration/V1__initial.sql`.
- Add a new backend feature: create `backend/src/main/java/com/cvesters/moneyjars/<feature>/` and mirror the current `controller`, `service`, `dto`, `bdo`, `dao` layout.
- Add or update a GraphQL document: create or edit `frontend/src/<feature>/<feature>.graphql`, then run `npm run codegen` to refresh `frontend/src/gql/`.

When in doubt, run the local build/test commands above and inspect failures; tests are the single-source-of-truth for runtime expectations.

If any of these assumptions are wrong (for example, a different JDK must be used, or migrations live elsewhere), ask a human maintainer and show the failing command output before making sweeping changes.
