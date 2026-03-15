"use client";

import { useLocale } from "next-intl";
import Breadcrumb from "@/components/Breadcrumb";
import ApiEndpoint from "@/components/ApiEndpoint";
import ParamTable from "@/components/ParamTable";
import CodeBlock from "@/components/CodeBlock";
import PageNav from "@/components/PageNav";

export default function AccountsAPIPage() {
  const locale = useLocale();

  return (
    <div className="space-y-8">
      <Breadcrumb
        items={[
          { label: "Develop", href: `/${locale}/develop/getting-started` },
          { label: "API Reference" },
          { label: "Accounts" },
        ]}
      />

      <div>
        <h1 className="text-3xl font-bold text-[#F0EDF5] mb-2">
          Accounts API
        </h1>
        <p className="text-[#9B8FB8] leading-relaxed">
          Ledger account management for tracking fiat and crypto assets. Create
          accounts of various types and retrieve them by ID or user.
        </p>
      </div>

      {/* POST /api/v1/accounts */}
      <div className="space-y-4">
        <h2
          id="create-account"
          className="text-xl font-semibold text-[#F0EDF5]"
        >
          Create a New Account
        </h2>
        <ApiEndpoint
          method="POST"
          path="/api/v1/accounts"
          description="Creates a new financial account for a specific user"
        >
          <div className="space-y-6">
            <div>
              <h3
                id="create-account-body"
                className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
              >
                Request Body
              </h3>
              <ParamTable
                params={[
                  {
                    name: "userId",
                    type: "string (uuid)",
                    required: true,
                    description: "The UUID of the user who owns this account",
                  },
                  {
                    name: "name",
                    type: "string",
                    required: true,
                    description:
                      "Display name for the account (e.g., \"Main Checking\")",
                  },
                  {
                    name: "type",
                    type: "string (enum)",
                    required: true,
                    description: "The type of financial account",
                    enumValues: [
                      "BANK",
                      "CREDIT",
                      "CRYPTO",
                      "CASH",
                      "INVESTMENT",
                    ],
                  },
                  {
                    name: "currencyCode",
                    type: "string",
                    required: true,
                    description:
                      "ISO 4217 currency code or crypto symbol (e.g., USD, BTC)",
                  },
                  {
                    name: "initialBalance",
                    type: "number",
                    required: false,
                    description: "Starting balance for the account (default: 0)",
                  },
                  {
                    name: "plaidAccountId",
                    type: "string",
                    required: false,
                    description: "External Plaid account ID for linked accounts",
                  },
                ]}
              />
            </div>

            <div>
              <h3
                id="create-account-example"
                className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
              >
                Example Request
              </h3>
              <CodeBlock
                tabs={[
                  {
                    label: "cURL",
                    code: `curl -X POST http://localhost:8080/api/v1/accounts \\
  -H "Content-Type: application/json" \\
  -d '{
    "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "name": "Main Checking",
    "type": "BANK",
    "currencyCode": "USD",
    "initialBalance": 5000.00
  }'`,
                  },
                  {
                    label: "JavaScript",
                    code: `const response = await fetch("http://localhost:8080/api/v1/accounts", {
  method: "POST",
  headers: {
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    userId: "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    name: "Main Checking",
    type: "BANK",
    currencyCode: "USD",
    initialBalance: 5000.0,
  }),
});

const data = await response.json();
console.log(data);`,
                  },
                  {
                    label: "Python",
                    code: `import requests

response = requests.post(
    "http://localhost:8080/api/v1/accounts",
    json={
        "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
        "name": "Main Checking",
        "type": "BANK",
        "currencyCode": "USD",
        "initialBalance": 5000.00,
    },
)

print(response.json())`,
                  },
                ]}
              />
            </div>

            <div>
              <h3
                id="create-account-response"
                className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
              >
                Response
              </h3>
              <CodeBlock
                response
                tabs={[
                  {
                    label: "JSON",
                    code: `{
  "success": true,
  "message": "Account created successfully",
  "data": {
    "id": "f7e6d5c4-b3a2-1908-fedc-ba0987654321",
    "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "name": "Main Checking",
    "type": "BANK",
    "currencyCode": "USD",
    "currentBalance": 5000.00,
    "plaidAccountId": null,
    "status": "ACTIVE",
    "createdAt": "2026-03-15T10:35:00Z",
    "updatedAt": "2026-03-15T10:35:00Z"
  },
  "timestamp": "2026-03-15T10:35:00Z"
}`,
                  },
                ]}
              />
            </div>
          </div>
        </ApiEndpoint>
      </div>

      {/* GET /api/v1/accounts/{accountId} */}
      <div className="space-y-4">
        <h2
          id="get-account-by-id"
          className="text-xl font-semibold text-[#F0EDF5]"
        >
          Get Account by ID
        </h2>
        <ApiEndpoint
          method="GET"
          path="/api/v1/accounts/{accountId}"
          description="Retrieves a specific account by its ID"
        >
          <div className="space-y-6">
            <div>
              <h3
                id="get-account-params"
                className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
              >
                Path Parameters
              </h3>
              <ParamTable
                params={[
                  {
                    name: "accountId",
                    type: "string (uuid)",
                    required: true,
                    description: "The UUID of the account to retrieve",
                  },
                ]}
              />
            </div>

            <div>
              <h3
                id="get-account-example"
                className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
              >
                Example Request
              </h3>
              <CodeBlock
                tabs={[
                  {
                    label: "cURL",
                    code: `curl http://localhost:8080/api/v1/accounts/f7e6d5c4-b3a2-1908-fedc-ba0987654321`,
                  },
                  {
                    label: "JavaScript",
                    code: `const response = await fetch(
  "http://localhost:8080/api/v1/accounts/f7e6d5c4-b3a2-1908-fedc-ba0987654321"
);

const data = await response.json();
console.log(data);`,
                  },
                  {
                    label: "Python",
                    code: `import requests

response = requests.get(
    "http://localhost:8080/api/v1/accounts/f7e6d5c4-b3a2-1908-fedc-ba0987654321"
)

print(response.json())`,
                  },
                ]}
              />
            </div>

            <div>
              <h3
                id="get-account-response"
                className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
              >
                Response
              </h3>
              <CodeBlock
                response
                tabs={[
                  {
                    label: "JSON",
                    code: `{
  "success": true,
  "message": "Account retrieved successfully",
  "data": {
    "id": "f7e6d5c4-b3a2-1908-fedc-ba0987654321",
    "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "name": "Main Checking",
    "type": "BANK",
    "currencyCode": "USD",
    "currentBalance": 5000.00,
    "plaidAccountId": null,
    "status": "ACTIVE",
    "createdAt": "2026-03-15T10:35:00Z",
    "updatedAt": "2026-03-15T10:35:00Z"
  },
  "timestamp": "2026-03-15T10:35:00Z"
}`,
                  },
                ]}
              />
            </div>
          </div>
        </ApiEndpoint>
      </div>

      {/* GET /api/v1/accounts/user/{userId} */}
      <div className="space-y-4">
        <h2
          id="get-accounts-by-user"
          className="text-xl font-semibold text-[#F0EDF5]"
        >
          Get All Accounts for a User
        </h2>
        <ApiEndpoint
          method="GET"
          path="/api/v1/accounts/user/{userId}"
          description="Retrieves all accounts belonging to a specific user"
        >
          <div className="space-y-6">
            <div>
              <h3
                id="get-user-accounts-params"
                className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
              >
                Path Parameters
              </h3>
              <ParamTable
                params={[
                  {
                    name: "userId",
                    type: "string (uuid)",
                    required: true,
                    description:
                      "The UUID of the user whose accounts to retrieve",
                  },
                ]}
              />
            </div>

            <div>
              <h3
                id="get-user-accounts-example"
                className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
              >
                Example Request
              </h3>
              <CodeBlock
                tabs={[
                  {
                    label: "cURL",
                    code: `curl http://localhost:8080/api/v1/accounts/user/a1b2c3d4-e5f6-7890-abcd-ef1234567890`,
                  },
                  {
                    label: "JavaScript",
                    code: `const response = await fetch(
  "http://localhost:8080/api/v1/accounts/user/a1b2c3d4-e5f6-7890-abcd-ef1234567890"
);

const data = await response.json();
console.log(data);`,
                  },
                  {
                    label: "Python",
                    code: `import requests

response = requests.get(
    "http://localhost:8080/api/v1/accounts/user/a1b2c3d4-e5f6-7890-abcd-ef1234567890"
)

print(response.json())`,
                  },
                ]}
              />
            </div>

            <div>
              <h3
                id="get-user-accounts-response"
                className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
              >
                Response
              </h3>
              <CodeBlock
                response
                tabs={[
                  {
                    label: "JSON",
                    code: `{
  "success": true,
  "message": "Accounts retrieved successfully",
  "data": [
    {
      "id": "f7e6d5c4-b3a2-1908-fedc-ba0987654321",
      "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
      "name": "Main Checking",
      "type": "BANK",
      "currencyCode": "USD",
      "currentBalance": 5000.00,
      "plaidAccountId": null,
      "status": "ACTIVE",
      "createdAt": "2026-03-15T10:35:00Z",
      "updatedAt": "2026-03-15T10:35:00Z"
    },
    {
      "id": "e8d7c6b5-a4f3-2109-edcb-af0987654321",
      "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
      "name": "Bitcoin Wallet",
      "type": "CRYPTO",
      "currencyCode": "BTC",
      "currentBalance": 0.5,
      "plaidAccountId": null,
      "status": "ACTIVE",
      "createdAt": "2026-03-15T10:40:00Z",
      "updatedAt": "2026-03-15T10:40:00Z"
    }
  ],
  "timestamp": "2026-03-15T10:45:00Z"
}`,
                  },
                ]}
              />
            </div>
          </div>
        </ApiEndpoint>
      </div>

      <PageNav
        prev={{
          label: "Users API",
          href: `/${locale}/develop/api/users`,
        }}
        next={{
          label: "Architecture",
          href: `/${locale}/develop/architecture`,
        }}
      />
    </div>
  );
}
