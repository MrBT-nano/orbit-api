<p align="center">
  <img src="assets/orbit-logo.svg" alt="Orbit" width="120" height="120" />
</p>

<h1 align="center">Orbit Personal Finance API</h1>

![CI Pipeline](https://github.com/MrBT-nano/orbit-api/actions/workflows/pre-commit-ci.yml/badge.svg)
![Docs & Test Coverage](https://github.com/MrBT-nano/orbit-api/actions/workflows/docs-and-tests-cd.yml/badge.svg)
![CodeQL](https://github.com/MrBT-nano/orbit-api/actions/workflows/codeql.yml/badge.svg)
[![codecov](https://codecov.io/gh/MrBT-nano/orbit-api/graph/badge.svg?token=p8pQDKGOWm)](https://codecov.io/gh/MrBT-nano/orbit-api)
![Dependabot](https://img.shields.io/badge/Dependabot-Enabled-blue?logo=dependabot)
![Java Version](https://img.shields.io/badge/Java-25-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.3-brightgreen?logo=springboot)

Welcome to the **Orbit Personal Finance Management System**. This repository contains the core API (backend engine) responsible for managing financial ledgers, tracking multi-currency assets (including crypto), and orchestrating payments.

It is built to strict, top 1% engineering standards utilizing a Hexagonal "Package by Feature" Architecture.

---

## 📚 Interactive API Documentation
This project automatically generates and publishes its Swagger OpenAPI specification to a custom interactive Next.js website. 

👉 **[View Live API Documentation](https://MrBT-nano.github.io/orbit-api/)** *(Link active after first successful GitHub Action deployment)*

## 🚀 Quick Start (Local Development)

### Prerequisites
*   Java 25
*   Docker (for the local PostgreSQL database)

### Building & Running
Thanks to `spring-boot-docker-compose`, you do not need to manually configure a database. Running the app will automatically spin up the required PostgreSQL container.

```bash
# Clone the repository
git clone https://github.com/MrBT-nano/orbit-api.git
cd orbit-api

# Run the application
./mvnw spring-boot:run
```
The API will be available at: `http://localhost:8080`

### Running Tests
To run the full test suite and automatically generate a local JaCoCo coverage report:
```bash
./mvnw clean test
```

## 📐 Architecture & Standards

This project adheres to elite coding standards. For detailed rules on how to write code, structure folders, and use AI agents within this repository, please read the following:

1.  **[Architecture Specification](./architect/architecture-specification.md)** - Primary source of truth for all module contracts, conventions, and dependency rules.
2.  **[GEMINI.md](./GEMINI.md)** - AI hooks and workspace context for Gemini.
3.  **[CLAUDE.md](./CLAUDE.md)** - AI agent guidelines for Claude.
4.  **[Clean Code Guidelines](./architect/clean_code_guidelines.md)** - Hexagonal Architecture, DTOs, and clean code principles.

## 🛡️ Security & CI/CD

Every Pull Request and commit to the `main` branch is subjected to a rigorous gauntlet of automated checks:
*   **Spotless Formatting:** Code must comply with Google's Java Style Guide.
*   **Unit Tests & Codecov:** Code coverage must not drop below 80%.
*   **GitHub CodeQL:** Continuous scanning for security vulnerabilities (e.g., SQL Injection, XSS).
*   **Dependabot:** Weekly automated dependency updates.
*   **Documentation Verification:** A custom bash script (`scripts/verify-docs.sh`) ensures no API endpoint is merged without proper Swagger/OpenAPI documentation annotations.
