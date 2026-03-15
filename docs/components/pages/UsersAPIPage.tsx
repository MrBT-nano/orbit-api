"use client";

import { useLocale, useTranslations } from "next-intl";
import Breadcrumb from "@/components/Breadcrumb";
import ApiEndpoint from "@/components/ApiEndpoint";
import ParamTable from "@/components/ParamTable";
import CodeBlock from "@/components/CodeBlock";
import PageNav from "@/components/PageNav";

export default function UsersAPIPage() {
  const locale = useLocale();
  const t = useTranslations("pages");
  const ta = useTranslations("api");

  return (
    <div className="space-y-8">
      <Breadcrumb
        items={[
          { label: "Develop", href: `/${locale}/develop/getting-started` },
          { label: ta("request_body").replace(" Body", " Reference").replace("เนื้อหาคำร้อง", "API อ้างอิง") || "API Reference" },
          { label: t("users_api_title").replace(" API", "").replace("API ", "") },
        ]}
      />

      <div>
        <h1 className="text-3xl font-bold text-[#F0EDF5] mb-2">{t("users_api_title")}</h1>
        <p className="text-[#9B8FB8] leading-relaxed">
          {t("users_api_desc")}
        </p>
      </div>

      {/* POST /api/v1/users */}
      <div className="space-y-4">
        <h2 id="register-user" className="text-xl font-semibold text-[#F0EDF5]">
          {t("register_user")}
        </h2>
        <ApiEndpoint
          method="POST"
          path="/api/v1/users"
          description="Syncs a newly registered Clerk user into the local database"
        >
          <div className="space-y-6">
            <div>
              <h3 id="register-request-body" className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3">
                {ta("request_body")}
              </h3>
              <ParamTable
                params={[
                  {
                    name: "clerkUserId",
                    type: "string",
                    required: true,
                    description: "The unique user ID from Clerk authentication",
                  },
                  {
                    name: "email",
                    type: "string (email)",
                    required: true,
                    description: "User email address",
                  },
                  {
                    name: "firstName",
                    type: "string",
                    required: false,
                    description: "User first name",
                  },
                  {
                    name: "lastName",
                    type: "string",
                    required: false,
                    description: "User last name",
                  },
                  {
                    name: "baseCurrency",
                    type: "string",
                    required: false,
                    description:
                      "Preferred base currency code (e.g., USD, THB)",
                  },
                  {
                    name: "timezone",
                    type: "string",
                    required: false,
                    description:
                      "User timezone (e.g., Asia/Bangkok)",
                  },
                ]}
              />
            </div>

            <div>
              <h3 id="register-example" className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3">
                {ta("example_request")}
              </h3>
              <CodeBlock
                tabs={[
                  {
                    label: "cURL",
                    code: `curl -X POST http://localhost:8080/api/v1/users \\
  -H "Content-Type: application/json" \\
  -d '{
    "clerkUserId": "user_2xK9mN3pQ7rT1wZ",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "baseCurrency": "USD",
    "timezone": "America/New_York"
  }'`,
                  },
                  {
                    label: "JavaScript",
                    code: `const response = await fetch("http://localhost:8080/api/v1/users", {
  method: "POST",
  headers: {
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    clerkUserId: "user_2xK9mN3pQ7rT1wZ",
    email: "john@example.com",
    firstName: "John",
    lastName: "Doe",
    baseCurrency: "USD",
    timezone: "America/New_York",
  }),
});

const data = await response.json();
console.log(data);`,
                  },
                  {
                    label: "Python",
                    code: `import requests

response = requests.post(
    "http://localhost:8080/api/v1/users",
    json={
        "clerkUserId": "user_2xK9mN3pQ7rT1wZ",
        "email": "john@example.com",
        "firstName": "John",
        "lastName": "Doe",
        "baseCurrency": "USD",
        "timezone": "America/New_York",
    },
)

print(response.json())`,
                  },
                ]}
              />
            </div>

            <div>
              <h3 id="register-response" className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3">
                {ta("response")}
              </h3>
              <CodeBlock
                response
                tabs={[
                  {
                    label: "JSON",
                    code: `{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "baseCurrency": "USD",
    "timezone": "America/New_York",
    "status": "ACTIVE",
    "createdAt": "2026-03-15T10:30:00Z"
  },
  "timestamp": "2026-03-15T10:30:00Z"
}`,
                  },
                ]}
              />
            </div>
          </div>
        </ApiEndpoint>
      </div>

      {/* GET /api/v1/users/clerk/{clerkUserId} */}
      <div className="space-y-4">
        <h2
          id="get-user-by-clerk-id"
          className="text-xl font-semibold text-[#F0EDF5]"
        >
          {t("get_by_clerk_id")}
        </h2>
        <ApiEndpoint
          method="GET"
          path="/api/v1/users/clerk/{clerkUserId}"
          description="Retrieves a local user profile using their external Clerk ID"
        >
          <div className="space-y-6">
            <div>
              <h3 id="get-user-path-params" className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3">
                {ta("path_params")}
              </h3>
              <ParamTable
                params={[
                  {
                    name: "clerkUserId",
                    type: "string",
                    required: true,
                    description:
                      "The Clerk user ID to look up",
                  },
                ]}
              />
            </div>

            <div>
              <h3 id="get-user-example" className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3">
                {ta("example_request")}
              </h3>
              <CodeBlock
                tabs={[
                  {
                    label: "cURL",
                    code: `curl http://localhost:8080/api/v1/users/clerk/user_2xK9mN3pQ7rT1wZ`,
                  },
                  {
                    label: "JavaScript",
                    code: `const response = await fetch(
  "http://localhost:8080/api/v1/users/clerk/user_2xK9mN3pQ7rT1wZ"
);

const data = await response.json();
console.log(data);`,
                  },
                  {
                    label: "Python",
                    code: `import requests

response = requests.get(
    "http://localhost:8080/api/v1/users/clerk/user_2xK9mN3pQ7rT1wZ"
)

print(response.json())`,
                  },
                ]}
              />
            </div>

            <div>
              <h3 id="get-user-response" className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3">
                {ta("response")}
              </h3>
              <CodeBlock
                response
                tabs={[
                  {
                    label: "JSON",
                    code: `{
  "success": true,
  "message": "User retrieved successfully",
  "data": {
    "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "baseCurrency": "USD",
    "timezone": "America/New_York",
    "status": "ACTIVE",
    "createdAt": "2026-03-15T10:30:00Z"
  },
  "timestamp": "2026-03-15T10:30:00Z"
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
          label: "Quick Start",
          href: `/${locale}/develop/getting-started`,
        }}
        next={{
          label: "Accounts API",
          href: `/${locale}/develop/api/accounts`,
        }}
      />
    </div>
  );
}
