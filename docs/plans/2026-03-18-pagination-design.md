# Pagination Design: Transactions + Notifications

**Date:** 2026-03-18
**Scope:** Add offset-based pagination to `GET /api/v1/transactions/account/{accountId}` and `GET /api/v1/notifications/user/{userId}`

---

## Goals

- Support paginated list responses for the two endpoints that grow unboundedly
- Keep Spring infrastructure (`Pageable`, `Page<T>`) isolated to `infrastructure/` layer
- Reuse existing `PageResult<T>` record from `common/core/model/`
- Enforce a max page size of 100 to prevent abuse

## Non-Goals

- Cursor-based pagination
- Paginating accounts or categories (small, bounded sets)
- Sorting parameters (use fixed defaults for now)

---

## Architecture

Spring's `Pageable` is created and destroyed within `infrastructure/` only. Core layer only sees primitive `int page, int size` going in and `PageResult<T>` coming out.

```
Controller (api/)
  receives: ?page=0&size=20
  validates: size capped at 100
  calls: service.findBy...(id, page, size)

Service (core/service/)
  receives: int page, int size
  calls: repositoryPort.findBy...(id, page, size)
  returns: PageResult<T>

Repository Adapter (infrastructure/)
  builds: PageRequest.of(page, size, Sort.by(...).descending())
  maps: Spring Page<Entity> → PageResult<DomainModel>
  returns: PageResult<T>
```

---

## Response Shape

```json
{
  "success": true,
  "data": {
    "content": [...],
    "totalElements": 150,
    "totalPages": 8,
    "page": 0,
    "size": 20
  }
}
```

---

## Query Parameters

| Param  | Default | Max | Notes         |
|--------|---------|-----|---------------|
| `page` | `0`     | —   | zero-indexed  |
| `size` | `20`    | `100` | enforced in controller |

---

## Default Sort

| Endpoint       | Sort Field        | Direction |
|----------------|-------------------|-----------|
| Transactions   | `transactionDate` | DESC      |
| Notifications  | `createdAt`       | DESC      |

---

## Files Changed

### Transactions

| Layer | File |
|-------|------|
| `core/port/out/` | `TransactionRepositoryPort.java` — change `findByAccountId` return to `PageResult<Transaction>`, add page/size params |
| `core/port/in/` | `GetTransactionUseCase.java` — update method signature |
| `core/service/` | `GetTransactionService.java` — pass page/size through |
| `infrastructure/repository/` | `TransactionRepository.java` — add `findByAccount_Id(UUID, Pageable)` |
| `infrastructure/repository/` | `TransactionRepositoryAdapter.java` — build PageRequest, map Page → PageResult |
| `api/` | `TransactionController.java` — add @RequestParam page/size |
| `test/` | `GetTransactionServiceTest.java`, `TransactionControllerTest.java` — update mocks/assertions |

### Notifications

| Layer | File |
|-------|------|
| `core/port/out/` | `NotificationRepositoryPort.java` — change `findByUserId` return to `PageResult<Notification>`, add page/size params |
| `core/port/in/` | `GetNotificationsUseCase.java` — update method signature |
| `core/service/` | `NotificationService.java` — pass page/size through |
| `infrastructure/repository/` | `NotificationRepository.java` — add `findByUserId(UUID, Pageable)` |
| `infrastructure/repository/` | `NotificationRepositoryAdapter.java` — build PageRequest, map Page → PageResult |
| `api/` | `NotificationController.java` — add @RequestParam page/size |
| `test/` | `NotificationServiceTest.java`, `NotificationControllerTest.java` — update mocks/assertions |
