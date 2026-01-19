# Specification: Modern Development Environment & Baseline

## Context
The Xinco DMS project requires a modernized foundation to support future development (UI migration, backend updates). Currently, the project uses older tooling (Travis CI, AppVeyor) and may lack a consistent containerized environment and comprehensive test coverage.

## Goals
1.  **Modernize Build:** Ensure the project compiles successfully with modern JDK versions (aiming for compatibility with JDK 17/21).
2.  **Containerize:** Refine the Docker setup to provide a consistent development and testing environment.
3.  **CI/CD Migration:** Migrate the CI/CD pipeline to GitHub Actions for better integration and maintenance.
4.  **Test Baseline:** Establish a baseline suite of unit tests for core logic and ensure coverage reporting is active.

## Requirements
- **Build System:** Maven configuration must be updated to handle modern Java versions.
- **Docker:** A `Dockerfile` and `docker-compose.yml` (if needed) must be created/updated to spin up the application and its dependencies (MySQL/PostgreSQL) easily.
- **CI/CD:** Create a GitHub Actions workflow (`.github/workflows/maven.yml`) that:
    -   Builds the project with Maven.
    -   Runs unit tests.
    -   Uploads coverage reports (e.g., using JaCoCo and Codecov).
- **Testing:** Add/verify JUnit tests for critical core components. Ensure a coverage tool (JaCoCo) is configured in `pom.xml`.

## Non-Goals
- Full migration to Vaadin 24 (this is a separate track).
- Complete refactoring of legacy code (focus is on environment first).
