# Budget Module Design

**Date:** 2026-03-18
**Scope:** Implement full Budget + Goal module — domain models, services, controllers, tests

---

## Goals

- CRUD for Budget with nested BudgetItems (create all in one request)
- CRUD for Goal with dual tracking: linked account balance OR manual contribution
- Auto-sync `spentAmount` on each completed transaction via Spring ApplicationEvent
- Alert notification when `spentAmount` crosses `alertThresholdPct`

## Non-Goals

- Budget vs actual reporting / analytics
- Recurring budget auto-renewal
- Multi-currency budget tracking
- Push/email notifications (in-app only via existing audit module)

---

## Architecture

### Cross-module Integration (Transactions → Budget)

`ledger` must not depend on `budget`. Decoupled via Spring `ApplicationEvent`:

```
CreateTransactionService (ledger)
  → publishes TransactionCreatedEvent(categoryId, amount, transactionDate)
    (only for COMPLETED transactions)

BudgetEventListener (budget)
  → @EventListener
  → finds BudgetItems where categoryId matches + budget date range contains transactionDate
  → item.spentAmount += amount
  → if spentAmount/allocatedAmount * 100 >= alertThresholdPct
       → CreateNotificationUseCase.createNotification(...)
```

`TransactionCreatedEvent` is a plain Java record in `ledger/core/` — ledger has no knowledge of the budget module.

### Goal Tracking (Hybrid)

```
if (goal.linkedAccountId != null)
  → currentAmount = AccountRepositoryPort.findById(linkedAccountId).currentBalance
else
  → currentAmount = goal.currentAmount (stored)

PATCH /goals/{id}/contribute
  → only allowed when linkedAccountId is null
  → goal.currentAmount += amount
  → if currentAmount >= targetAmount → status = ACHIEVED
```

---

## Domain Models

### Budget
```
id, userId, name
periodType: MONTHLY | QUARTERLY | YEARLY | CUSTOM
startDate, endDate, totalAmount
status: ACTIVE | ARCHIVED
items: List<BudgetItem>
```

### BudgetItem
```
id, budgetId, categoryId
allocatedAmount, spentAmount
alertThresholdPct (nullable)
```

### Goal
```
id, userId, name
targetAmount, currentAmount
targetDate (nullable), linkedAccountId (nullable)
status: IN_PROGRESS | ACHIEVED | CANCELLED
```

---

## API Endpoints

### Budgets

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/budgets` | Create budget with items |
| GET | `/api/v1/budgets/{budgetId}` | Get budget with items |
| GET | `/api/v1/budgets/user/{userId}?page=0&size=20` | List user budgets (paginated) |
| PATCH | `/api/v1/budgets/{budgetId}/archive` | Archive budget |

### Goals

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/goals` | Create goal |
| GET | `/api/v1/goals/{goalId}` | Get goal (resolves currentAmount) |
| GET | `/api/v1/goals/user/{userId}?page=0&size=20` | List user goals (paginated) |
| PATCH | `/api/v1/goals/{goalId}/contribute` | Add manual contribution |

---

## Data Flow

### Budget Creation
```
POST /api/v1/budgets
  body: { name, periodType, startDate, endDate,
          items: [{ categoryId, allocatedAmount, alertThresholdPct }] }

CreateBudgetService:
  1. validate endDate > startDate
  2. totalAmount = sum(items.allocatedAmount)
  3. save Budget with items (cascade)
  4. return BudgetResponse
```

### Auto-sync spentAmount
```
CreateTransactionService → publish TransactionCreatedEvent

BudgetEventListener:
  1. find BudgetItems: categoryId = event.categoryId
                       AND budget.startDate <= event.date <= budget.endDate
                       AND budget.status = ACTIVE
  2. item.spentAmount += event.amount
  3. pct = spentAmount / allocatedAmount * 100
  4. if alertThresholdPct != null AND pct >= alertThresholdPct
       → createNotification(userId, BUDGET_ALERT, "Budget alert: X% spent")
```

---

## Error Handling

| Case | Exception |
|------|-----------|
| endDate <= startDate | `BadRequestException` |
| Budget not found | `ResourceNotFoundException` |
| Goal not found | `ResourceNotFoundException` |
| Contribute on goal with linkedAccountId | `BadRequestException` |
| Contribute amount <= 0 | `BadRequestException` |

---

## Module Structure

```
budget/
├── api/
│   ├── BudgetController.java
│   ├── GoalController.java
│   ├── mapper/         BudgetDtoMapper.java, GoalDtoMapper.java
│   ├── request/        CreateBudgetRequest.java, CreateGoalRequest.java,
│   │                   ContributeGoalRequest.java
│   └── response/       BudgetResponse.java, BudgetItemResponse.java, GoalResponse.java
├── core/
│   ├── model/          Budget.java, BudgetItem.java, Goal.java
│   │   └── enums/      (BudgetPeriodType, BudgetStatus, GoalStatus — already exist)
│   ├── port/
│   │   ├── in/         CreateBudgetUseCase.java, GetBudgetUseCase.java,
│   │   │               CreateGoalUseCase.java, GetGoalUseCase.java,
│   │   │               ContributeGoalUseCase.java
│   │   └── out/        BudgetRepositoryPort.java, GoalRepositoryPort.java
│   └── service/        CreateBudgetService.java, GetBudgetService.java,
│                       CreateGoalService.java, GetGoalService.java,
│                       ContributeGoalService.java, BudgetEventListener.java
└── infrastructure/
    ├── entity/         (BudgetEntity, BudgetItemEntity, GoalEntity — already exist)
    ├── repository/     BudgetRepository.java (add Page methods),
    │                   BudgetItemRepository.java (add findByCategoryId query),
    │                   GoalRepository.java (add Page methods),
    │                   BudgetRepositoryAdapter.java, GoalRepositoryAdapter.java
    └── mapper/         BudgetEntityMapper.java, GoalEntityMapper.java
```

---

## Testing Plan

| Test Class | What It Tests |
|-----------|---------------|
| `CreateBudgetServiceTest` | totalAmount calculation, date validation, save delegation |
| `GetBudgetServiceTest` | findById, findByUserId paging |
| `BudgetEventListenerTest` | spentAmount update, alert threshold trigger, no-match (wrong category/date) |
| `CreateGoalServiceTest` | goal creation, validation |
| `GetGoalServiceTest` | linked account balance resolution vs stored amount |
| `ContributeGoalServiceTest` | add amount, ACHIEVED transition, reject if linkedAccountId set |
| `BudgetControllerTest` | all 4 endpoints, HTTP status codes |
| `GoalControllerTest` | all 4 endpoints, HTTP status codes |
| `BudgetRepositoryAdapterTest` | save, findById, findByUserId paged |
| `GoalRepositoryAdapterTest` | save, findById, findByUserId paged, contribute update |
