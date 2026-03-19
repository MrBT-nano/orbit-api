# Complete CRUD Design: Update (PATCH) + Soft Delete

**Date:** 2026-03-19
**Scope:** Add PATCH update and soft DELETE to all 6 modules (12 new endpoints)

---

## Strategy

- **Update:** PATCH (partial) — only send fields to change, null = no change
- **Delete:** Soft delete — set status field, never hard delete
- **Transaction void:** Reverse account balance on VOIDED transition

---

## New Endpoints

| Module | Method | Path | Action |
|--------|--------|------|--------|
| Users | PATCH | `/api/v1/users/{userId}` | Update profile |
| Users | DELETE | `/api/v1/users/{userId}` | status=INACTIVE |
| Accounts | PATCH | `/api/v1/accounts/{accountId}` | Update name, status |
| Accounts | DELETE | `/api/v1/accounts/{accountId}` | status=CLOSED |
| Transactions | PATCH | `/api/v1/transactions/{id}` | Update description, categoryId, isReviewed |
| Transactions | DELETE | `/api/v1/transactions/{id}` | status=VOIDED + reverse balance |
| Categories | PATCH | `/api/v1/categories/{id}` | Update name, parentCategoryId |
| Categories | DELETE | `/api/v1/categories/{id}` | status=INACTIVE |
| Budgets | PATCH | `/api/v1/budgets/{budgetId}` | Update name, items |
| Budgets | DELETE | `/api/v1/budgets/{budgetId}` | status=ARCHIVED |
| Goals | PATCH | `/api/v1/goals/{goalId}` | Update name, targetAmount, targetDate |
| Goals | DELETE | `/api/v1/goals/{goalId}` | status=CANCELLED |

---

## Per-Module Details

### Users
- PATCH fields: firstName, lastName, baseCurrency, timezone
- Delete: status → INACTIVE
- New enum value needed: check if INACTIVE exists in UserStatus

### Accounts
- PATCH fields: name, status
- Delete: status → CLOSED
- New enum value needed: check if CLOSED exists in AccountStatus

### Transactions
- PATCH fields: description, categoryId, isReviewed
- Delete: status → VOIDED, reverse balance via accountRepositoryPort.updateBalance(accountId, -amount)
- Only COMPLETED transactions need balance reversal

### Categories
- PATCH fields: name, parentCategoryId
- Delete: status → INACTIVE
- New status field needed: CategoryEntity has no status — add CategoryStatus enum (ACTIVE, INACTIVE)

### Budgets
- PATCH fields: name (items update is complex — skip for now, use archive+recreate)
- Delete: status → ARCHIVED (reuse existing ArchiveBudgetUseCase)

### Goals
- PATCH fields: name, targetAmount, targetDate
- Delete: status → CANCELLED (already exists in GoalStatus)

---

## Files Per Module (pattern)

For each module, add:
1. `UpdateXxxRequest.java` — PATCH request DTO (all fields nullable)
2. `UpdateXxxUseCase.java` — port/in interface
3. `DeleteXxxUseCase.java` — port/in interface
4. `UpdateXxxService.java` — core service (find → patch fields → save)
5. Controller — add PATCH + DELETE methods
6. Tests — service test + controller test updates
