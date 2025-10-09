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
- Flyway migrations: place SQL migrations in `backend/src/main/resources/db/migration/` (standard Flyway path). There are currently no migrations in that folder.
- Testcontainers tests: see `backend/src/test/java/com/cvesters/moneyjars/TestcontainersConfiguration.java` — tests create a PostgreSQL container using `postgres:latest` and register it with `@ServiceConnection` so Spring Test recognizes it. Docker is required for these tests.
- Test runner helper: `backend/src/test/java/com/cvesters/moneyjars/TestMoneyJarsApplication.java` shows how to start the app with the test configuration applied.
- Application properties: `backend/src/main/resources/application.properties` contains basic app name; datasource and Flyway configs will go here if needed.
- Lombok: annotation processing is configured in `pom.xml`. Keep Lombok annotations (if added) and do not remove the compiler/plugin config unless you add a replacement.

Conventions and guardrails for code changes
- Do not change parent POM selection lightly — `backend/pom.xml` intentionally overrides `<licenses>` and `<developers>`; if you switch parent POMs, remove those overrides only when you understand inheritance effects (see `backend/HELP.md`).
- When adding DB migrations, use Flyway's naming conventions (V{version}__desc.sql) and commit them into `backend/src/main/resources/db/migration/`.
- Tests expect Docker available for Testcontainers. If adding CI jobs that run tests without Docker, provide an alternative profile or replace Testcontainers usage.
 - Do not add comments anywhere in source code. Do not add Javadoc, block comments (/* ... */) or line comments (//) in any source files. Comments in documentation (Markdown/README) or non-source assets are allowed.

Helpful files to inspect when you start working
- `backend/pom.xml` — dependencies, java version, build plugins (Lombok, spring-boot-plugin).
- `backend/HELP.md` — curated notes about Testcontainers, Flyway and parent-POM caveats.
- `backend/src/test/java/com/cvesters/moneyjars/TestcontainersConfiguration.java` — canonical Testcontainers wiring.
- `frontend/package.json` and `frontend/vite.config.ts` — frontend scripts and build details.

Tasks AI agents can do immediately
- Add a Flyway migration: create `backend/src/main/resources/db/migration/V1__initial.sql`.
- Implement a REST controller: create `backend/src/main/java/com/cvesters/moneyjars/web/` and follow Spring Boot conventions.
- Improve frontend dev DX: add a README in `frontend/` with Node version recommendation and `npm ci` vs `npm install` notes.

When in doubt, run the local build/test commands above and inspect failures; tests are the single-source-of-truth for runtime expectations.

If any of these assumptions are wrong (for example, a different JDK must be used, or migrations live elsewhere), ask a human maintainer and show the failing command output before making sweeping changes.
