# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Xinco DMS (eXtensible INformation COre) is a web-service-based Document Management System featuring ACLs, versioning, full-text search, and a tree-based data hierarchy. It is undergoing an active migration from a Vaadin 6 monolith to a Vaadin 25 / Spring Boot architecture.

## Build & Development Commands

### Build
```bash
# Full build (all modules)
mvn -B install -Dmaven.javadoc.skip=true

# Skip tests during build
mvn -B install -DskipTests -Dmaven.javadoc.skip=true
```

### Test
```bash
# Run unit tests only
mvn test

# Run unit + integration tests with coverage
mvn verify

# Run tests for a single module
mvn test -pl xinco-core

# Run a single test class
mvn test -pl xinco-core -Dtest=XincoActivityServiceTest

# Run integration tests only
mvn failsafe:integration-test failsafe:verify -pl xinco-core

# Generate JaCoCo coverage report
mvn verify && open xinco-core/target/site/jacoco/index.html
```

### Local Development (Docker)
```bash
# Start app + MySQL
docker-compose up --build

# App runs at http://localhost:8081
# MySQL exposed on port 3307
```

### New UI (Vaadin 25 / Spring Boot)
```bash
# Run the new UI module directly
mvn spring-boot:run -pl xinco-ui-v25
```

## Module Architecture

The project is a Maven multi-module build (`xinco-parent`):

| Module | Role |
|---|---|
| `xinco-core` | Business logic, JPA entities, JAX-WS web service, service layer |
| `Xinco` | Legacy WAR — Vaadin 6 UI, deployed to Tomcat (scheduled for removal) |
| `xinco-ui` | New UI — Vaadin 25 + Spring Boot 3, depends on `xinco-core` |

### `xinco-core` internals
- **`server/`** — server-side domain objects (`XincoCoreDataServer`, `XincoCoreUserServer`, `XincoCoreNodeServer`, etc.) and extracted service classes (`XincoActivityService`, `XincoFileService`, `XincoTreeService`)
- **`server/service/`** — JAX-WS web service endpoint (`XincoWebService`) and new REST-style services; CXF generates client stubs from `src/main/resources/wsdl/XincoWebService/Xinco.wsdl` at build time into `target/generated-sources/cxf`
- **`server/db/`** — `XincoDBManager`, Flyway migrations, H2 for tests, MySQL/PostgreSQL for production
- **`server/persistence/`** — Hibernate JPA entities with **Hibernate Envers** for audit history; legacy `*_T` shadow tables and hand-rolled audit controllers have been removed
- Tests use H2 in-memory; `AbstractXincoDataBaseTestCase` is the base class for DB-backed tests

### `xinco-ui` internals
- Entry point: `XincoApplication` (Spring Boot)
- Views: `LoginView` → `ViewerView` (primary landing), `ExplorerView` (management), `AdminView`, `MainLayout` (Vaadin `@Route`)
- Shared state: `UserSession`
- Components: `PropertyGrid`, `CheckinDialog`

## Active Work: Vaadin Migration

Track: `conductor/tracks/vaadin_migration/plan.md`

**Current phase status:**
- Phases 1–3 are largely complete (logic extracted, new module bootstrapped, core views implemented).
- Remaining tasks: enhance PropertyGrid metadata, verify admin feature parity, switch Docker to deploy `xinco-ui-v25`, remove legacy `Xinco` module.

The `Xinco` (Vaadin 6) module is legacy and will be deleted once feature parity is confirmed.

## Workflow & Conventions

### Task tracking
Tasks are tracked in `conductor/tracks/<track>/plan.md`. Mark tasks `[~]` when starting, `[x]` with a 7-char SHA when done. Attach a `git notes` summary to each task commit. See `conductor/workflow.md` for the full protocol.

### Commit messages
Follow conventional commits: `<type>(<scope>): <description>`
- Types: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`
- Conductor infrastructure commits use the prefix `conductor(...):`

### Code formatting
- Java uses Google Java Format, enforced via `git-code-format-maven-plugin` (pre-commit hook installed by `mvn install`).
- XML files are formatted with 2-space indent via `xml-format-maven-plugin`.
- Run `mvn validate` to trigger format checks without a full build.

### Testing requirements
- Target >80% coverage for new code (JaCoCo).
- Unit tests mock external dependencies; integration tests use H2 in-memory.
- Test naming follows `<ClassName>Test.java` convention.
- Use JUnit 5 (`junit-jupiter`) for new tests; some existing tests still use JUnit 4.

## Key Configuration

- **Java version:** 25 LTS (enforced in compiler plugin and maven-enforcer-plugin)
- **JAVA_HOME:** Set to JDK 25 before running Maven — Homebrew Java 26 breaks JaCoCo. Use: `export JAVA_HOME=/Users/javierortiz/Library/Java/JavaVirtualMachines/openjdk-25.0.2/Contents/Home`
- **Database:** MySQL 8 in production (via Docker), H2 for tests
- **Flyway** handles schema migrations in `xinco-core`
- **CI:** GitHub Actions runs `mvn install` then `mvn verify` on every push/PR; JaCoCo report uploaded as artifact


<!-- BEGIN BEADS INTEGRATION v:1 profile:minimal hash:6cd5cc61 -->
## Beads Issue Tracker

This project uses **bd (beads)** for issue tracking. Run `bd prime` to see full workflow context and commands.

### Quick Reference

```bash
bd ready              # Find available work
bd show <id>          # View issue details
bd update <id> --claim  # Claim work
bd close <id>         # Complete work
```

### Rules

- Use `bd` for ALL task tracking — do NOT use TodoWrite, TaskCreate, or markdown TODO lists
- Run `bd prime` for detailed command reference and session close protocol
- Use `bd remember` for persistent knowledge — do NOT use MEMORY.md files

**Architecture in one line:** issues live in a local Dolt DB; sync uses `refs/dolt/data` on your git remote; `.beads/issues.jsonl` is a passive export. See https://github.com/gastownhall/beads/blob/main/docs/SYNC_CONCEPTS.md for details and anti-patterns.

## Agent Context Profiles

The managed Beads block is task-tracking guidance, not permission to override repository, user, or orchestrator instructions.

- **Conservative (default)**: Use `bd` for task tracking. Do not run git commits, git pushes, or Dolt remote sync unless explicitly asked. At handoff, report changed files, validation, and suggested next commands.
- **Minimal**: Keep tool instruction files as pointers to `bd prime`; use the same conservative git policy unless active instructions say otherwise.
- **Team-maintainer**: Only when the repository explicitly opts in, agents may close beads, run quality gates, commit, and push as part of session close. A current "do not commit" or "do not push" instruction still wins.

## Session Completion

This protocol applies when ending a Beads implementation workflow. It is subordinate to explicit user, repository, and orchestrator instructions.

1. **File issues for remaining work** - Create beads for anything that needs follow-up
2. **Run quality gates** (if code changed) - Tests, linters, builds
3. **Update issue status** - Close finished work, update in-progress items
4. **Handle git/sync by active profile**:
```bash
# Conservative/minimal/default: report status and proposed commands; wait for approval.
git status

# Team-maintainer opt-in only, unless current instructions forbid it:
git pull --rebase
git push
git status
```
5. **Hand off** - Summarize changes, validation, issue status, and any blocked sync/commit/push step

**Critical rules:**
- Explicit user or orchestrator instructions override this Beads block.
- Do not commit or push without clear authority from the active profile or the current user request.
- If a required sync or push is blocked, stop and report the exact command and error.
<!-- END BEADS INTEGRATION -->
