# Gemini: Ledger Module Refactor Brief

> This document describes what needs to be fixed in the `ledger/` module you created.
> Before making ANY changes, read `architect/architecture-specification.md` — it is the single source of truth.

---

## Context

You built the `ledger/` module with 51 files across the hexagonal architecture. The `common/` package you created (base classes, `EntityMapper` interface, exceptions) is excellent — clean, well-designed, and correctly follows monolith patterns. However, the `ledger/` module does not follow the conventions you established in `common/`, nor does it fully align with the architecture defined in `architect/structure.md` and `architect/clean_code_guidelines.md`.

A comprehensive architecture specification has been written at `architect/architecture-specification.md`. Please read it fully before proceeding.

---

## Critical Bugs (Must Fix First)

### Bug 1: Race condition on balance update

**File:** `ledger/core/service/CreateTransactionService.java` (lines 54-57)

**Problem:** Balance update uses read-modify-write at the application layer:
```java
BigDecimal newBalance = account.getCurrentBalance().add(transaction.getAmount());
account.setCurrentBalance(newBalance);
accountRepositoryPort.save(account);
```
Two concurrent transactions on the same account will corrupt the balance (last-write-wins).

**Fix:** Use atomic SQL update in the repository:
```java
@Modifying
@Query("UPDATE AccountEntity a SET a.currentBalance = a.currentBalance + :amount WHERE a.id = :id")
int updateBalance(@Param("id") UUID id, @Param("amount") BigDecimal amount);
```
Add `@Version private Long version;` to `AccountEntity` for optimistic locking.

---

### Bug 2: `TransactionRepository.findByAccountId()` will fail at runtime

**File:** `ledger/infrastructure/repository/TransactionRepository.java` (line 12)

**Problem:** `TransactionEntity` has no `accountId` field — it has `@ManyToOne AccountEntity account`. Spring Data JPA derives queries from field names, so `findByAccountId()` will throw `PropertyReferenceException`.

**Fix:** Rename to `findByAccount_Id(UUID accountId)`.

---

### Bug 3: `parentCategoryId` silently dropped

**Files:** `ledger/infrastructure/mapper/CategoryMapper.java` (line 38), `ledger/infrastructure/repository/CategoryRepositoryAdapter.java` (lines 23-26)

**Problem:** The mapper acknowledges in a comment that it doesn't resolve `parentCategoryId` to a `CategoryEntity` reference, and the adapter never resolves it either. The entire sub-category hierarchy feature is silently broken.

**Fix:** In `CategoryRepositoryAdapter.save()`:
```java
if (domain.getParentCategoryId() != null) {
    CategoryEntity parent = categoryRepository.findById(domain.getParentCategoryId())
        .orElseThrow(() -> new ResourceNotFoundException("Category", "id", domain.getParentCategoryId()));
    entity.setParentCategory(parent);
}
```

---

### Bug 4: `@ManyToMany` and `TransactionTagEntity` conflict

**Files:** `ledger/infrastructure/entity/TransactionEntity.java` (lines 67-69), `TransactionTagEntity.java`, `TransactionTagId.java`

**Problem:** `TransactionEntity` uses `@ManyToMany @JoinTable("transaction_tags")` AND there's a separate `TransactionTagEntity` mapped to the same `transaction_tags` table. Two JPA entity graphs on one table = mapping conflict.

**Fix:** Delete `TransactionTagEntity.java`, `TransactionTagId.java`, and `TransactionTagRepository.java`. Keep only the `@ManyToMany` on `TransactionEntity`.

---

## Architecture Violations (Must Refactor)

### Violation 1: Controllers inject infrastructure mappers

**Files:** `AccountController.java:30`, `CategoryController.java:29`, `TransactionController.java:30`

**Problem:** Controllers directly inject mappers from `infrastructure/mapper/`. This violates `api/ → infrastructure/` being forbidden.

**Fix:** Move DTO conversion responsibility to the service layer:
- Service methods accept request DTOs and return response DTOs.
- Controller does: `service.createAccount(request)` → gets back `AccountResponse` directly.
- Mapper stays in `infrastructure/` but is called by the service, not the controller.

---

### Violation 2: `TransactionRepositoryAdapter` injects `AccountRepository` and `CategoryRepository` directly

**File:** `ledger/infrastructure/repository/TransactionRepositoryAdapter.java` (lines 23-24)

**Problem:** Cross-entity infrastructure coupling bypassing port abstraction.

**Fix:** If the adapter needs to resolve an `AccountEntity` from a UUID, inject `AccountRepositoryPort` (the core port interface), not `AccountRepository` (the Spring Data interface). Or better: have the service pass the already-resolved entity reference through the port method signature.

---

### Violation 3: `CategoryController` builds domain object inline

**File:** `ledger/api/CategoryController.java` (lines 36-38)

**Problem:** Manually constructs `Category` domain object in the controller instead of delegating to a mapper. This is inconsistent with `AccountController` and `TransactionController`.

**Fix:** Controller should call `categoryService.createCategory(request)` and let the service handle DTO→Domain conversion.

---

### Violation 4: Mappers don't implement `EntityMapper<E, D>`

**Files:** All 3 mappers in `ledger/infrastructure/mapper/`

**Problem:** You created the `EntityMapper<E, D>` generic interface in `common/infrastructure/mapper/EntityMapper.java` but none of the ledger mappers implement it.

**Fix:** Each mapper must `implements EntityMapper<XxxEntity, Xxx>` and override `toDomain()` and `toEntity()`.

---

### Violation 5: Over-split Use Cases

**Problem:** You created 6 separate UseCase interfaces and 6 separate Service classes (`CreateAccountUseCase` + `CreateAccountService`, `GetAccountUseCase` + `GetAccountService`, etc.) even though each service is only 30-70 lines.

The architecture spec says: split into Command/Query ONLY when a service exceeds ~300 lines.

**Fix:** Consolidate into one interface + one service per entity:
- `AccountUseCase` interface with `createAccount()`, `getAccountById()`, `getAccountsByUser()`
- `AccountService` implements `AccountUseCase`
- Delete the 6 separate files, create 3 (one per entity: Account, Category, Transaction)

---

## Missing Features (Must Add)

### Missing 1: Pagination on all list endpoints

All `GET /api/v1/.../user/{userId}` and `GET /api/v1/.../account/{accountId}` endpoints currently return unbounded `List<T>`. They must use Spring `Pageable` and return `Page<T>`.

### Missing 2: `exception/` package

Create `ledger/exception/` with:
- `AccountClosedException`
- `InsufficientFundsException`
- `InvalidTransferException`

Register these in `GlobalExceptionHandler` with appropriate HTTP status codes (422).

### Missing 3: `MethodArgumentNotValidException` handler

`GlobalExceptionHandler` only handles 3 custom exceptions. Add a handler for `MethodArgumentNotValidException` that returns field-level validation errors in `ApiResponse` format, not Spring's default error body.

### Missing 4: Orphan entities MUST be fully wired

`TagEntity`, `RecurringTransactionEntity` exist in infrastructure but have NO domain model, service, controller, or DTOs. The architecture spec (Section 2.1) mandates specific endpoints for both:

- Tags: `POST /api/v1/tags`, `GET /api/v1/tags/user/{userId}`
- Recurring Transactions: `POST /api/v1/recurring-transactions`, `GET /api/v1/recurring-transactions/user/{userId}`

**Wire them fully** (domain model → port → service → controller → DTOs). Do NOT delete them — they are required by the architecture spec.

Also: delete `TransactionTagEntity`, `TransactionTagId`, and `TransactionTagRepository` — the `transaction_tags` join table is managed by `@ManyToMany` on `TransactionEntity` only.

### Missing 5: `GET /api/v1/categories/{categoryId}` endpoint

`GetCategoryUseCase` defines `getCategoryById()` but there's no corresponding endpoint in `CategoryController`.

### Bug 5: Account status validation missing (functional bug)

`CreateTransactionService` does NOT check `account.getStatus()` before processing. Transactions can currently be created against `CLOSED` or `INACTIVE` accounts, corrupting business logic. This is a functional bug, not just a missing feature.

**Fix:** Add status check immediately after account lookup:
```java
if (account.getStatus() != AccountStatus.ACTIVE)
    throw new AccountClosedException(account.getId());
```

---

### Missing 7: Transfer pair logic (most complex ledger operation)

The architecture spec (Section 2.1, Rule 3) defines transfer creation as two paired transactions in a single `@Transactional`:
```
Transfer $100 from Account A to Account B:
  tx1: { accountId: A, amount: -100, transferPairId: tx2.id }
  tx2: { accountId: B, amount: +100, transferPairId: tx1.id }
```
Both balance updates must be atomic. The `transactions.transfer_pair_id` field exists in the schema but has no implementation whatsoever. This is the most complex operation in the ledger module and must be implemented.

### Missing 8: Mapper layer split (architecture change)

Per the updated architecture spec (Section 1.3), mappers must be split into TWO types:
- **Entity Mapper** in `infrastructure/mapper/` — Domain ↔ Entity (implements `EntityMapper<E,D>`)
- **DTO Mapper** in `api/mapper/` — Request DTO ↔ Domain, Domain → Response DTO

Controllers call DTO mappers. Services operate on Domain Models only. Repository adapters call Entity mappers.

---

## Duplicate Work (Must Remove)

### Duplicate 1: Account fetched twice per transaction creation

`CreateTransactionService` (line 29) fetches the account to validate it. Then `TransactionRepositoryAdapter.save()` (line 29) fetches the same account again to construct the `TransactionEntity`. Remove the duplicate lookup — have the service pass what the adapter needs.

---

## Summary Priority Order

```
1. Fix critical bugs (race condition, findByAccountId, parentCategory, ManyToMany conflict, account status)
2. Fix architecture violations (mapper layer split, controller→mapper, cross-repo injection, use case consolidation)
3. Add missing features (transfer pairs, pagination, exception package, validation handler, orphan wiring)
4. Remove duplicate work (double account fetch)
```

Read `architect/architecture-specification.md` for the full conventions and patterns. Every fix must comply with that document.

**Also update `Violation 5: Over-split Use Cases` note:** Per the updated spec, Use Case interfaces should still be split by Command/Query, but the implementation class can be ONE service that implements both interfaces when under 300 lines. So keep the separate `AccountCommandUseCase` and `AccountQueryUseCase` interfaces, but consolidate into a single `AccountService implements AccountCommandUseCase, AccountQueryUseCase`.
