# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
# Build the project (includes Spotless format check at compile phase)
./mvnw clean install

# Run the application (starts PostgreSQL via Docker Compose automatically)
./mvnw spring-boot:run

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=OrbitApplicationTests

# Run a single test method
./mvnw test -Dtest=OrbitApplicationTests#contextLoads

# Package without running tests
./mvnw package -DskipTests

# Fix code formatting (Eclipse JDT 4.32 formatter)
./mvnw spotless:apply

# Check formatting without fixing
./mvnw spotless:check

# Generate test coverage report (output: target/site/jacoco/)
./mvnw jacoco:report

# Verify API documentation coverage (requires running app + openapi.json generated)
./scripts/verify-docs.sh
```

## Architecture & Tech Stack

Orbit is a **Personal Finance Management System** — a Spring Boot 4.0.3 / Java 25 RESTful API for managing financial ledgers, multi-currency assets (including crypto), and payments.

**Key Dependencies:**
- Spring Web MVC + springdoc-openapi (Swagger UI at `/swagger-ui.html`)
- Spring Data JPA with PostgreSQL (prod) / H2 (test)
- Spring Security, Spring Validation, Spring Actuator
- Lombok for boilerplate reduction
- Docker Compose for local PostgreSQL (auto-managed by `spring-boot-docker-compose`)
- Spotless (Eclipse JDT 4.32) for formatting, JaCoCo for coverage

**Hexagonal "Package by Feature" Architecture:**

Each feature module under `src/main/java/com/mrbt/orbit/` follows this structure:
```
[feature]/
├── api/              # Inbound: REST Controllers, request/response DTOs
├── core/             # Business logic: Services, Domain Models, Ports (interfaces)
├── infrastructure/   # Outbound: JPA Entities, Repositories, Mappers
└── exception/        # Module-specific exceptions
```
Feature packages: `audit`, `common`, `config`, `crypto`, `ledger`, `payment`, `security`.

Cross-cutting concerns live in `common/` and `config/`.

**Database:**
- PostgreSQL via Docker Compose (`compose.yaml`) — DB: `finance`, User: `mrbt`, Password: `secret`, Port: `5432`
- Spring Boot Docker Compose support auto-manages the container lifecycle
- Tests use H2 in-memory database (no Docker needed for tests)

## Development Conventions

- **Records for DTOs:** Use Java `record` for all DTOs and immutable data carriers. Never use `@Data` for DTOs.
- **Constructor Injection:** Use `@RequiredArgsConstructor` + `private final` fields. NEVER use `@Autowired` on fields.
- **Thin Controllers:** Controllers do exactly 3 things: receive request, call service, return response. No `if/else` business logic.
- **CQRS at scale:** If a service exceeds ~300 lines, split into focused use-case classes (e.g., `CreateTransactionUseCase`, `CalculateBalanceQuery`).
- **Formatting:** Spotless runs at the `compile` phase — builds will fail on format violations. Run `./mvnw spotless:apply` to fix.
- **API Documentation:** All endpoints must have springdoc/OpenAPI annotations. CI runs `scripts/verify-docs.sh` to enforce this.
- **Package Manager:** Use `bun`/`bunx` instead of `npm`/`npx` for anything in the `docs/` Next.js app.

## CI/CD

- **Spotless:** Format check on every build
- **JaCoCo + Codecov:** Coverage must not drop below 80%
- **GitHub CodeQL:** Security vulnerability scanning
- **Documentation Verification:** `scripts/verify-docs.sh` ensures no endpoint is merged undocumented
- **Dependabot:** Weekly automated dependency updates

## AI Agent Hooks (Pre-Write Checks)

Before creating or modifying code, you MUST:

1. **Duplicate Check:** Search to ensure the class/function/interface doesn't already exist.
2. **Path Check:** Place files in the correct hexagonal layer (`api/`, `core/`, `infrastructure/`) as defined in `architect/architecture-specification.md`.
3. **Style Check:** Use Java 25 records for DTOs, constructor injection, thin controllers.
4. **Architecture Spec is Primary:** `architect/architecture-specification.md` is the single source of truth for all architectural decisions, module contracts, and conventions. It supersedes `structure.md` and `clean_code_guidelines.md` where they conflict. Keep `CLAUDE.md` and `GEMINI.md` in sync with each other.
