# RDBMS Architecture (PostgreSQL)

This directory contains the Relational Database Management System (RDBMS) architecture for Orbit, powered by **PostgreSQL**.

## Architectural Strategy
Orbit uses a centralized relational database to ensure **ACID compliance** (Atomicity, Consistency, Isolation, Durability) for all core financial and user data. This is critical for maintaining the integrity of ledgers, transactions, and balances.

The schema is designed using a **Modular Relational Pattern**, where tables are logically grouped into features but maintain strong relational integrity via Foreign Keys.

## Module Breakdown

### 1. [Full Schema (Single Source of Truth)](./00_full_schema.mermaid)
The master diagram illustrating all 21 tables and their interlocking relationships across the entire system.

### 2. [User & Authentication](./01_user_auth.mermaid)
*   **Purpose:** Manages core identity and personalized settings.
*   **Integration:** Designed to link with **Clerk Auth** via the `clerk_user_id` unique key.
*   **Key Tables:** `users`, `user_addresses`, `user_preferences`, `notification_preferences`.

### 3. [Ledger & Finance Engine](./02_ledger.mermaid)
*   **Purpose:** The core accounting system of the application.
*   **Features:** Multi-currency support, double-entry capabilities (via transfer pairs), and automated recurring transactions.
*   **Key Tables:** `accounts`, `categories`, `transactions`, `tags`, `recurring_transactions`.

### 4. [Budget & Goals](./03_budget_goals.mermaid)
*   **Purpose:** Financial planning and progress tracking.
*   **Features:** Category-based budget allocation and milestone-based goal tracking linked to specific savings accounts.
*   **Key Tables:** `budgets`, `budget_items`, `goals`.

### 5. [Payment & Subscriptions](./04_payment_subscription.mermaid)
*   **Purpose:** Orchestrating money movement and recurring bills.
*   **Features:** Integration with providers like Stripe/PayPal and automated tracking of periodic subscriptions (Netflix, AWS, etc.).
*   **Key Tables:** `payment_methods`, `subscriptions`.

### 6. [Crypto Portfolio](./05_crypto.mermaid)
*   **Purpose:** Tracking digital assets alongside fiat currency.
*   **Features:** Real-time price snapshots and historical portfolio value tracking.
*   **Key Tables:** `crypto_assets`, `crypto_portfolio_snapshots`.

### 7. [Integrations](./06_integrations.mermaid)
*   **Purpose:** Bridging Orbit with the external financial world.
*   **Features:** **Plaid** link management for automated bank syncing and live exchange rate fetching.
*   **Key Tables:** `plaid_links`, `exchange_rates`.

### 8. [Audit & Notifications](./07_audit_notifications.mermaid)
*   **Purpose:** Compliance, security, and user engagement.
*   **Features:** Immutable logs of every database change and a multi-channel notification system (In-app, Email, Push).
*   **Key Tables:** `audit_logs`, `notifications`.

## Data Integrity Standards
*   **UUIDs:** All primary keys use UUID v4 for better scalability and security.
*   **Precision:** Financial amounts are stored using `DECIMAL(19,4)` to prevent rounding errors.
*   **Soft Deletes:** Critical entities (Users, Transactions, Accounts) utilize a `deleted_at` timestamp for non-destructive data removal.
*   **Auditing:** Most tables are automatically tracked by the `audit_logs` module to maintain a "Who, What, When" record.
