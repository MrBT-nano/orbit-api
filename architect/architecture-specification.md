# Orbit Architecture Specification

> **Primary architecture reference** for all module contracts and conventions.
> This document supersedes `structure.md` and `clean_code_guidelines.md` where they conflict.
> Both `CLAUDE.md` and `GEMINI.md` should reference this file for architectural decisions.
> Column-level field definitions are in `architect/database/RDBMS/00_full_schema.mermaid`.
> Last updated: 2026-03-16

---

## Part 1: Global Conventions

All modules MUST follow these rules without exception.

### 1.1 Layer Dependency Rules

```
ALLOWED:
  api/             → core/              (controller calls service via use case interface)
  infrastructure/  → core/              (adapter implements port interface)
  core/            → common/            (domain uses shared base classes)

FORBIDDEN:
  api/             → infrastructure/    (controller must NOT touch mapper/entity/repo)
  core/            → infrastructure/    (domain must NOT know about JPA)
  core/            → api/               (domain must NOT know about HTTP)
  infrastructure/  → infrastructure/    (adapter A must NOT inject repo B directly)
                                         use core ports for cross-entity lookups
```

### 1.2 Data Flow Lifecycle

```
Client → JSON Request
  → Controller receives @Valid DTO (api/request/ record)
  → Controller converts request DTO → Domain Model via DtoMapper (api/mapper/)
  → Controller calls UseCase interface (core/port/in/) with Domain Model
  → Service implementation (core/service/):
      1. Executes business logic on Domain Model
      2. Calls RepositoryPort (core/port/out/)
      3. Returns Domain Model to controller
  → Controller converts Domain Model → response DTO via DtoMapper (api/mapper/)
  → Controller wraps response in ApiResponse<T> and returns ResponseEntity
  → RepositoryAdapter (infrastructure/repository/):
      1. Maps Domain → Entity via EntityMapper (infrastructure/mapper/)
      2. Saves via Spring Data JPA
      3. Maps Entity → Domain and returns
```

**Why this layering:**
- `core/service/` operates ONLY on Domain Models — never touches request/response DTOs.
- `api/mapper/` handles DTO ↔ Domain conversion (lives in `api/` layer, depends on `core/`).
- `infrastructure/mapper/` handles Domain ↔ Entity conversion (lives in `infrastructure/`, depends on `core/`).
- This respects the layer rule: `core/` depends on NOTHING except `common/`.

**Controller does exactly 4 things**: receive request, convert DTO→Domain, call service, convert Domain→response.
No `if/else` business logic in controllers. Mapping is mechanical, not business logic.

### 1.3 Mapper Contract

There are TWO types of mappers, living in different layers:

**A. Entity Mapper (infrastructure/mapper/) — Domain ↔ Entity**

MUST implement `EntityMapper<E, D>` from `common/infrastructure/mapper/` (all 4 methods).

```java
@Component
public class AccountEntityMapper implements EntityMapper<AccountEntity, Account> {
    @Override public Account toDomain(AccountEntity entity) { ... }
    @Override public AccountEntity toEntity(Account domain) { ... }
    @Override public List<Account> toDomainList(List<AccountEntity> entities) { ... }
    @Override public List<AccountEntity> toEntityList(List<Account> domains) { ... }
}
```

**B. DTO Mapper (api/mapper/) — Request DTO ↔ Domain, Domain → Response DTO**

Lives in `api/` layer. Called by controllers. Does NOT implement `EntityMapper`.

```java
@Component
public class AccountDtoMapper {
    public Account fromRequest(CreateAccountRequest request) { ... }
    public AccountResponse toResponse(Account domain) { ... }
}
```

**Rules:**
- Entity mappers live in `infrastructure/mapper/` — called by repository adapters ONLY.
- DTO mappers live in `api/mapper/` — called by controllers ONLY.
- Services in `core/` NEVER call either mapper. They receive and return Domain Models.
- Future migration path: replace manual mappers with MapStruct.

### 1.4 Concurrency & Data Integrity

Any entity with a mutable monetary field (`current_balance`, `spent_amount`, `current_amount`) MUST use one or both of:

**A. Optimistic Locking**
```java
@Version
private Long version;
```

**B. Atomic SQL Updates (preferred for balance fields)**
```java
@Modifying
@Query("UPDATE AccountEntity a SET a.currentBalance = a.currentBalance + :amount WHERE a.id = :id")
int updateBalance(@Param("id") UUID id, @Param("amount") BigDecimal amount);
```

**FORBIDDEN pattern (race condition):**
```java
// NEVER do this:
BigDecimal newBalance = account.getCurrentBalance().add(amount);
account.setCurrentBalance(newBalance);
repository.save(account);
```

The service layer MUST catch `OptimisticLockException` and return `409 Conflict` via `GlobalExceptionHandler`.

### 1.5 Pagination

Every "list" endpoint MUST use Spring `Pageable`:

```java
@GetMapping("/user/{userId}")
public ResponseEntity<ApiResponse<Page<AccountResponse>>> getAccountsByUser(
    @PathVariable UUID userId,
    Pageable pageable  // ?page=0&size=20&sort=createdAt,desc
) { ... }
```

**FORBIDDEN:** Returning unbounded `List<T>` from any list endpoint.

Repository ports define `Page<T> findByUserId(UUID userId, Pageable pageable)`.

### 1.6 Error Handling

**System-wide exceptions (common/exception/):**

| Exception | HTTP Status | When |
|---|---|---|
| `ResourceNotFoundException` | 404 | Entity not found by ID |
| `DuplicateResourceException` | 409 | Unique constraint violation |
| `BadRequestException` | 400 | Invalid business input |
| `OptimisticLockException` | 409 | Concurrent modification |

**Feature-specific exceptions ([feature]/exception/):**

| Exception | Module | HTTP Status | When |
|---|---|---|---|
| `InsufficientFundsException` | ledger | 422 | Balance too low |
| `AccountClosedException` | ledger | 422 | Operation on CLOSED/INACTIVE account |
| `InvalidTransferException` | ledger | 422 | Transfer validation failure |
| `UserAlreadyExistsException` | security | 409 | Clerk ID or email already registered |
| `PriceFetchException` | crypto | 502 | External API call failed |

**GlobalExceptionHandler MUST handle:**
- All custom exceptions above
- `MethodArgumentNotValidException` → 400 with field-level errors in `ApiResponse` format
- `HttpMessageNotReadableException` → 400 (malformed JSON)

All error responses use `ApiResponse<Void>` envelope — NEVER raw Spring error format.

### 1.7 Use Case Pattern (CQRS Lite)

We prefer Use Case isolation (Command/Query separation) as the default pattern to avoid "God Services."

```
DEFAULT:  Separate Use Case interfaces per operation group:
  - AccountCommandUseCase  → createAccount(), updateAccount(), deleteAccount()
  - AccountQueryUseCase    → getAccountById(), getAccountsByUser()

IMPLEMENTATION:
  - If total implementation is < 300 lines → ONE service implements BOTH interfaces:
      AccountService implements AccountCommandUseCase, AccountQueryUseCase
  - If total implementation exceeds ~300 lines → split into separate service classes:
      AccountCommandService implements AccountCommandUseCase
      AccountQueryService implements AccountQueryUseCase
```

**Key principle:** Interfaces are always split by Command/Query. Implementation classes consolidate when small, split when large. This gives clean contracts without premature class explosion.

### 1.8 Testing Convention

```
Unit Tests:        core/service/   → mock ports, test business logic only
Integration Tests: infrastructure/ → @DataJpaTest with H2, test queries and mappings
API Tests:         api/            → @WebMvcTest, mock service, test HTTP contract
Full Stack:        sparingly       → @SpringBootTest for critical end-to-end flows

File naming:       [ClassName]Test.java
Coverage target:   80% per module (JaCoCo enforced in CI)
Test profile:      application-test.properties (H2, Docker Compose disabled)
```

### 1.9 Package Template

Every feature module follows this exact structure:

```
[feature]/
├── api/
│   ├── [Feature]Controller.java
│   ├── mapper/         DTO ↔ Domain mappers (called by controller)
│   ├── request/        Java 25 records with @NotNull, @NotBlank, etc.
│   └── response/       Java 25 records (immutable, no Lombok @Data)
├── core/
│   ├── model/          Pure POJOs extending BaseDomainModel (no JPA annotations)
│   │   └── enums/      Feature-specific enums
│   ├── port/
│   │   ├── in/         Use case interfaces extending UseCase marker
│   │   └── out/        Repository port interfaces
│   └── service/        Implementations annotated with @Service, @Transactional
├── infrastructure/
│   ├── entity/         JPA @Entity classes extending BaseEntity/SoftDeletableEntity
│   ├── repository/     Spring Data JPA interfaces + Adapter classes implementing ports
│   ├── mapper/         Mappers implementing EntityMapper<E, D>
│   └── client/         External API clients (only if module calls external services)
└── exception/          Feature-specific exceptions
```

### 1.10 Cross-Module Communication

Modules may depend on other modules ONLY through their `core/port/` interfaces.

```
ALLOWED:
  budget/core/service/BudgetService → injects ledger/core/port/out/CategoryRepositoryPort
  payment/core/service/ → injects ledger/core/port/out/AccountRepositoryPort

FORBIDDEN:
  budget/core/service/ → imports ledger/infrastructure/repository/CategoryRepository
  payment/api/ → imports ledger/infrastructure/entity/AccountEntity
```

**Dependency graph (allowed directions):**
```
security ← ledger ← budget
                  ← payment
                  ← integrations
crypto (standalone, reads ledger accounts)
audit (observes all modules via AOP/listeners)
```

---

## Part 2: Module Specifications

### 2.1 Ledger Module (Core Financial Engine)

**Purpose:** Heart of the system. Manages accounts, transactions, categories, tags, and recurring rules.

**Tables (6):**

> Note: `@Version` column indicates REQUIRED state. If not yet implemented, it MUST be added.

| Table | Entity Base | Domain Model | Soft Delete | @Version (required) |
|---|---|---|---|---|
| `accounts` | `SoftDeletableEntity` | `Account` | Yes | Yes (balance mutation) |
| `categories` | `BaseEntity` | `Category` | No | No |
| `transactions` | `SoftDeletableEntity` | `Transaction` | Yes | No |
| `tags` | `CreatedOnlyEntity` | `Tag` | No | No |
| `transaction_tags` | `@ManyToMany` on TransactionEntity | — (no separate entity) | No | No |
| `recurring_transactions` | `BaseEntity` | `RecurringTransaction` | No | No |

**Key decision:** `transaction_tags` is a pure join table managed by `@ManyToMany` on `TransactionEntity`. Do NOT create a separate `TransactionTagEntity` — it conflicts with the `@ManyToMany` mapping.

**Endpoints:**

```
POST   /api/v1/accounts                              → Create account
GET    /api/v1/accounts/{accountId}                   → Get by ID
GET    /api/v1/accounts/user/{userId}                 → List by user (Pageable)
PUT    /api/v1/accounts/{accountId}                   → Update account
DELETE /api/v1/accounts/{accountId}                   → Soft delete

POST   /api/v1/categories                            → Create category
GET    /api/v1/categories/{categoryId}                → Get by ID
GET    /api/v1/categories/user/{userId}               → List user + system categories (Pageable)

POST   /api/v1/transactions                          → Create transaction (+ atomic balance update)
GET    /api/v1/transactions/{transactionId}           → Get by ID
GET    /api/v1/transactions/account/{accountId}       → List by account (Pageable)

POST   /api/v1/tags                                  → Create tag
GET    /api/v1/tags/user/{userId}                     → List user tags

POST   /api/v1/recurring-transactions                → Create recurring rule
GET    /api/v1/recurring-transactions/user/{userId}   → List by user (Pageable)
```

**Critical Business Rules:**

**Rule 1 — Transaction balance update MUST be atomic:**
```java
@Transactional
public TransactionResponse createTransaction(CreateTransactionRequest request) {
    Account account = accountPort.findById(request.accountId())
        .orElseThrow(() -> new ResourceNotFoundException("Account", "id", request.accountId()));

    if (account.getStatus() != AccountStatus.ACTIVE)
        throw new AccountClosedException(account.getId());

    Transaction tx = mapper.fromRequest(request);
    tx.setStatus(defaultIfNull(tx.getStatus(), TransactionStatus.COMPLETED));

    Transaction saved = transactionPort.save(tx);

    if (saved.getStatus() == TransactionStatus.COMPLETED) {
        accountPort.updateBalance(account.getId(), saved.getAmount());
        // MUST be: UPDATE accounts SET current_balance = current_balance + :amount WHERE id = :id
    }

    return mapper.toResponse(saved);
}
```

**Rule 2 — Category hierarchy (parentCategoryId):**
```java
// In CategoryRepositoryAdapter.save():
if (domain.getParentCategoryId() != null) {
    CategoryEntity parent = categoryRepository.findById(domain.getParentCategoryId())
        .orElseThrow(() -> new ResourceNotFoundException("Category", "id", domain.getParentCategoryId()));
    entity.setParentCategory(parent);
}
```

**Rule 3 — Transfer pairs (debit + credit):**
```
Transfer $100 from Account A to Account B creates TWO transactions in a single @Transactional:
  tx1: { accountId: A, amount: -100, transferPairId: tx2.id }
  tx2: { accountId: B, amount: +100, transferPairId: tx1.id }
Both balance updates are atomic.
```

---

### 2.2 Security Module (User & Auth — Clerk Integration)

**Purpose:** User management, Clerk SSO sync, addresses, and preferences.

**Tables (4):**

| Table | Entity Base | Domain Model | Soft Delete | @Version |
|---|---|---|---|---|
| `users` | `SoftDeletableEntity` | `User` | Yes | No |
| `user_addresses` | `BaseEntity` | `UserAddress` | No | No |
| `user_preferences` | `BaseEntity` | `UserPreference` | No | No |
| `notification_preferences` | `BaseEntity` | `NotificationPreference` | No | No |

**Endpoints:**

```
POST   /api/v1/users                                         → Register (Clerk sync)
GET    /api/v1/users/clerk/{clerkUserId}                     → Get by Clerk ID
GET    /api/v1/users/{userId}                                → Get by ID
PUT    /api/v1/users/{userId}                                → Update profile
DELETE /api/v1/users/{userId}                                → Soft delete (deactivate)

POST   /api/v1/users/{userId}/addresses                      → Add address
GET    /api/v1/users/{userId}/addresses                      → List addresses
PUT    /api/v1/addresses/{addressId}                         → Update address
DELETE /api/v1/addresses/{addressId}                         → Delete address

GET    /api/v1/users/{userId}/preferences                    → Get preferences
PUT    /api/v1/users/{userId}/preferences                    → Update preferences

GET    /api/v1/users/{userId}/notification-preferences       → Get notification prefs
PUT    /api/v1/users/{userId}/notification-preferences       → Update notification prefs
```

**Business Rules:**
- `clerkUserId` and `email` MUST be unique → `DuplicateResourceException` if exists.
- On register: auto-create `UserPreference` + `NotificationPreference` with defaults.
- Defaults: `baseCurrency = "USD"`, `timezone = "UTC"`, `status = ACTIVE`.
- Addresses and preferences use nested URLs (`/users/{userId}/addresses`) — no global endpoint.

---

### 2.3 Budget & Goals Module

**Purpose:** Budget planning per category, spending tracking, and savings goals.

**Tables (3):**

| Table | Entity Base | Domain Model | Soft Delete | @Version |
|---|---|---|---|---|
| `budgets` | `BaseEntity` | `Budget` | No | No |
| `budget_items` | `BaseEntity` | `BudgetItem` | No | Yes (spent_amount mutation) |
| `goals` | `BaseEntity` | `Goal` | No | Yes (current_amount mutation) |

**Endpoints:**

```
POST   /api/v1/budgets                                → Create budget
GET    /api/v1/budgets/{budgetId}                     → Get budget with items
GET    /api/v1/budgets/user/{userId}                  → List budgets (Pageable)
PUT    /api/v1/budgets/{budgetId}                     → Update budget
DELETE /api/v1/budgets/{budgetId}                     → Delete budget

POST   /api/v1/budgets/{budgetId}/items               → Add budget item
PUT    /api/v1/budget-items/{itemId}                   → Update allocated amount
DELETE /api/v1/budget-items/{itemId}                   → Delete item

POST   /api/v1/goals                                  → Create goal
GET    /api/v1/goals/user/{userId}                    → List goals (Pageable)
PUT    /api/v1/goals/{goalId}                         → Update goal
DELETE /api/v1/goals/{goalId}                         → Delete goal
```

**Business Rules:**
- `budget_items.spent_amount` is denormalized — updated when a matching transaction is created.
  - **Mechanism:** Ledger module publishes a Spring `ApplicationEvent` (`TransactionCreatedEvent`) after saving a transaction. Budget module listens via `@EventListener` and atomically updates `spent_amount`. This avoids circular dependency (ledger does NOT import budget).
- Alert trigger: when `spent_amount / allocated_amount >= alert_threshold_pct` → create notification.
- Goal with `linked_account_id`: `current_amount` derived live from `account.current_balance` on each GET request (not cached).
- Goal without link: `current_amount` updated manually by user.
- Status auto-update: `current_amount >= target_amount` → `ACHIEVED`.

**Cross-module dependencies:**
- `budget/core/` → `ledger/core/port/` (categories — for budget item categorization)
- `budget/core/` → `security/core/port/` (users)
- `budget/core/` ← `ledger/core/event/` (listens to `TransactionCreatedEvent` via Spring events — no direct import)

---

### 2.4 Payment & Subscription Module

**Purpose:** Payment method management and subscription tracking with billing reminders.

**Tables (2):**

| Table | Entity Base | Domain Model | Soft Delete | @Version |
|---|---|---|---|---|
| `payment_methods` | `SoftDeletableEntity` | `PaymentMethod` | Yes | No |
| `subscriptions` | `SoftDeletableEntity` | `Subscription` | Yes | No |

**Endpoints:**

```
POST   /api/v1/payment-methods                       → Add payment method
GET    /api/v1/payment-methods/user/{userId}          → List payment methods
PUT    /api/v1/payment-methods/{id}                   → Update (set default, etc.)
DELETE /api/v1/payment-methods/{id}                   → Soft delete

POST   /api/v1/subscriptions                         → Create subscription
GET    /api/v1/subscriptions/user/{userId}            → List subscriptions (Pageable)
PUT    /api/v1/subscriptions/{id}                     → Update subscription
DELETE /api/v1/subscriptions/{id}                     → Soft delete (cancel)
```

**Business Rules:**
- Only ONE default payment method per user — setting a new default unsets the previous.
- Subscription billing reminder: `next_billing_date - reminder_days_before` → trigger notification.
- Subscription auto-creates `recurring_transaction` in ledger module.
- Subscription has `category_id` FK → ledger categories (for expense categorization).
- Subscription has `account_id` FK → ledger accounts (charged account). If the linked account is CLOSED, billing MUST fail gracefully and create a `SYSTEM` notification.

**Cross-module dependencies:**
- `payment/core/` → `ledger/core/port/` (accounts, categories, recurring_transactions)
- `payment/core/` → `security/core/port/` (users)

---

### 2.5 Crypto Module

**Purpose:** Cryptocurrency asset tracking, portfolio snapshots, and price feeds.

**Tables (2):**

| Table | Entity Base | Domain Model | Soft Delete | @Version |
|---|---|---|---|---|
| `crypto_assets` | `BaseEntity` | `CryptoAsset` | No | No |
| `crypto_portfolio_snapshots` | `CreatedOnlyEntity` | `CryptoSnapshot` | No | No |

**Endpoints:**

```
GET    /api/v1/crypto/assets                          → List tracked assets (Pageable)
GET    /api/v1/crypto/assets/{symbol}                 → Get asset by symbol (BTC, ETH)
POST   /api/v1/crypto/assets/refresh                  → Trigger price refresh from CoinGecko

GET    /api/v1/crypto/portfolio/{userId}              → Get latest portfolio snapshot
GET    /api/v1/crypto/portfolio/{userId}/history       → Historical snapshots (Pageable)
POST   /api/v1/crypto/portfolio/{userId}/snapshot      → Create manual snapshot
```

**Business Rules:**
- `crypto_assets.current_price_usd` fetched from CoinGecko API via `infrastructure/client/CoinGeckoClient`.
- Portfolio snapshot: sum all user's `CRYPTO`-type accounts multiplied by current price.
- `holdings_json`: JSONB array — `[{symbol: "BTC", amount: 0.5, valueUsd: 35000}, ...]`.
- Price refresh runs on schedule (external cron), not per-request.

**Unique structure:** This module has `infrastructure/client/` for external API calls.

**Cross-module dependencies:**
- `crypto/core/` → `ledger/core/port/` (reads CRYPTO-type accounts for portfolio calculation)
- `crypto/core/` → `security/core/port/` (users)

---

### 2.6 Integrations Module

**Purpose:** Plaid bank connections, transaction sync, and exchange rate management.

**Tables (2):**

| Table | Entity Base | Domain Model | Soft Delete | @Version |
|---|---|---|---|---|
| `plaid_links` | `BaseEntity` | `PlaidLink` | No | No |
| `exchange_rates` | `CreatedOnlyEntity` | `ExchangeRate` | No | No |

**Endpoints:**

```
POST   /api/v1/plaid/link                             → Create Plaid link
GET    /api/v1/plaid/links/user/{userId}              → List connected banks
POST   /api/v1/plaid/links/{linkId}/sync              → Trigger transaction sync
DELETE /api/v1/plaid/links/{linkId}                   → Disconnect bank

GET    /api/v1/exchange-rates/{base}/{target}         → Get latest rate
POST   /api/v1/exchange-rates/refresh                 → Trigger rate refresh
```

**Business Rules:**
- `access_token_encrypted`: Jasypt encryption — NEVER store plaintext tokens.
- Plaid sync: fetches bank transactions → creates `Transaction` records in ledger module.
- Exchange rates: fetched from ECB/OpenExchangeRates, cached in DB.

**Cross-module dependencies:**
- `integrations/core/` → `ledger/core/port/` (creates transactions, links accounts)
- `integrations/core/` → `security/core/port/` (users)

---

### 2.7 Audit & Notifications Module

**Purpose:** Immutable audit trail and user notification system.

**Tables (2):**

| Table | Entity Base | Domain Model | Soft Delete | @Version |
|---|---|---|---|---|
| `audit_logs` | `CreatedOnlyEntity` | `AuditLog` | No (immutable) | No |
| `notifications` | `BaseEntity` | `Notification` | No | No |

**Endpoints:**

```
GET    /api/v1/audit-logs/user/{userId}               → List audit trail (Pageable, read-only)
GET    /api/v1/audit-logs/entity/{entityType}/{entityId} → Audit for specific entity

GET    /api/v1/notifications/user/{userId}            → List notifications (Pageable)
PUT    /api/v1/notifications/{id}/read                → Mark as read
PUT    /api/v1/notifications/user/{userId}/read-all   → Mark all as read
```

**Business Rules:**
- Audit logs are APPEND-ONLY — no update, no delete endpoints.
- `AuditLog` auto-created via Spring AOP or `@EntityListeners` on key entities.
- `changes_json`: JSONB storing before/after diff of entity state.
- Notifications created by: budget alerts, bill reminders, large transactions, goal milestones.
- Notification types: `BUDGET_ALERT`, `BILL_REMINDER`, `LARGE_TRANSACTION`, `GOAL_MILESTONE`, `SYSTEM`.

**Cross-module:** Audit observes ALL modules. Notifications triggered by budget, payment, and ledger events.

---

## Part 3: Module Dependency Map

```
                    ┌──────────┐
                    │ security │  (users, addresses, preferences)
                    └────┬─────┘
                         │ users
            ┌────────────┼────────────┬──────────────┐
            ▼            ▼            ▼              ▼
       ┌─────────┐ ┌─────────┐ ┌──────────┐  ┌──────────────┐
       │ ledger  │ │ crypto  │ │ payment  │  │ integrations │
       │         │ │         │ │          │  │              │
       │ accounts│ │ assets  │ │ methods  │  │ plaid links  │
       │ txns    │ │ snapshots│ │ subs     │  │ exchange     │
       │ cats    │ └─────────┘ └────┬─────┘  │ rates        │
       │ tags    │                  │         └──────┬───────┘
       │ recur   │◄─────────────────┘                │
       └────┬────┘◄──────────────────────────────────┘
            │
            ▼
       ┌─────────┐
       │ budget  │  (budgets, items, goals)
       └─────────┘

       ┌─────────┐
       │  audit  │  (observes all modules via AOP)
       └─────────┘
```

**Arrow = "depends on via core ports"**

---

## Part 4: Technology Decisions

| Concern | Decision | Rationale |
|---|---|---|
| Mapping | Manual mappers implementing `EntityMapper<E,D>` (MapStruct migration path) | Compile-time safety, no reflection overhead |
| Validation | JSR-303 on request DTOs + service-level business rules | Framework validates format, service validates logic |
| Documentation | springdoc-openapi with `@Operation`, `@Schema` annotations | Auto-generated Swagger UI at `/swagger-ui.html` |
| Formatting | Spotless (Eclipse JDT 4.32) — runs at compile phase | Builds fail on format violations |
| Coverage | JaCoCo — 80% minimum per module | Enforced in CI via Codecov |
| Database migration | `ddl-auto=update` (dev) → Flyway (production, future) | Current phase: rapid development |
| Encryption | Jasypt for sensitive fields (Plaid tokens) | At-rest encryption for compliance |
| Time types | `Instant` in entities, `OffsetDateTime` in domain/DTOs | JPA works best with Instant; API consumers prefer offset |
