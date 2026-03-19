"use client";

import { useLocale, useTranslations } from "next-intl";
import ApiEndpoint from "@/components/ApiEndpoint";
import Breadcrumb from "@/components/Breadcrumb";
import CodeBlock from "@/components/CodeBlock";
import PageNav from "@/components/PageNav";
import ParamTable from "@/components/ParamTable";

export default function RecurringTransactionsAPIPage() {
	const locale = useLocale();
	const t = useTranslations("pages");
	const ta = useTranslations("api");

	return (
		<div className="space-y-8">
			<Breadcrumb
				items={[
					{ label: "Develop", href: `/${locale}/develop/getting-started` },
					{ label: "API Reference" },
					{ label: t("recurring_transactions_api_title").replace(" API", "").replace("API ", "") },
				]}
			/>

			<div>
				<h1 className="text-3xl font-bold text-[#F0EDF5] mb-2">
					{t("recurring_transactions_api_title")}
				</h1>
				<p className="text-[#9B8FB8] leading-relaxed">{t("recurring_transactions_api_desc")}</p>
			</div>

			{/* POST /api/v1/recurring-transactions */}
			<div className="space-y-4">
				<h2 id="create-recurring" className="text-xl font-semibold text-[#F0EDF5]">
					{t("create_recurring")}
				</h2>
				<ApiEndpoint
					method="POST"
					path="/api/v1/recurring-transactions"
					description="Creates a new recurring transaction rule that automatically generates transactions at the specified frequency"
				>
					<div className="space-y-6">
						<div>
							<h3
								id="create-recurring-body"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("request_body")}
							</h3>
							<ParamTable
								params={[
									{
										name: "userId",
										type: "string (uuid)",
										required: true,
										description: "The UUID of the user who owns the rule",
									},
									{
										name: "accountId",
										type: "string (uuid)",
										required: true,
										description: "The account to create transactions against",
									},
									{
										name: "categoryId",
										type: "string (uuid)",
										required: false,
										description: "Optional category to assign to generated transactions",
									},
									{
										name: "amount",
										type: "number",
										required: true,
										description: "Transaction amount (positive for income, negative for expense)",
									},
									{
										name: "currencyCode",
										type: "string",
										required: true,
										description: "ISO 4217 currency code (e.g. USD, THB)",
									},
									{
										name: "description",
										type: "string",
										required: false,
										description: "Optional description for generated transactions",
									},
									{
										name: "frequency",
										type: "string",
										required: true,
										description:
											"Recurrence frequency: DAILY, WEEKLY, BIWEEKLY, MONTHLY, QUARTERLY, or YEARLY",
									},
									{
										name: "startDate",
										type: "string (date)",
										required: true,
										description: "The date when the first transaction should be created (ISO 8601)",
									},
									{
										name: "autoConfirm",
										type: "boolean",
										required: false,
										description:
											"If true, generated transactions are auto-confirmed. Default: false",
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="create-recurring-example"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("example_request")}
							</h3>
							<CodeBlock
								tabs={[
									{
										label: "cURL",
										code: `curl -X POST http://localhost:8080/api/v1/recurring-transactions \\
  -H "Content-Type: application/json" \\
  -d '{
    "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "accountId": "acc-chk-0001-aaaa-bbbbbbbbbbbb",
    "categoryId": "cat-rent-0002",
    "amount": -1500.00,
    "currencyCode": "USD",
    "description": "Monthly rent payment",
    "frequency": "MONTHLY",
    "startDate": "2026-04-01",
    "autoConfirm": true
  }'`,
									},
									{
										label: "JavaScript",
										code: `const response = await fetch("http://localhost:8080/api/v1/recurring-transactions", {
  method: "POST",
  headers: { "Content-Type": "application/json" },
  body: JSON.stringify({
    userId: "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    accountId: "acc-chk-0001-aaaa-bbbbbbbbbbbb",
    categoryId: "cat-rent-0002",
    amount: -1500.00,
    currencyCode: "USD",
    description: "Monthly rent payment",
    frequency: "MONTHLY",
    startDate: "2026-04-01",
    autoConfirm: true,
  }),
});

const data = await response.json();
console.log(data);`,
									},
									{
										label: "Python",
										code: `import requests

response = requests.post(
    "http://localhost:8080/api/v1/recurring-transactions",
    json={
        "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
        "accountId": "acc-chk-0001-aaaa-bbbbbbbbbbbb",
        "categoryId": "cat-rent-0002",
        "amount": -1500.00,
        "currencyCode": "USD",
        "description": "Monthly rent payment",
        "frequency": "MONTHLY",
        "startDate": "2026-04-01",
        "autoConfirm": True,
    },
)

print(response.json())`,
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="create-recurring-response"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("response")}
							</h3>
							<CodeBlock
								response
								tabs={[
									{
										label: "JSON",
										code: `{
  "success": true,
  "message": "Recurring transaction rule created successfully",
  "data": {
    "id": "rt-a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "accountId": "acc-chk-0001-aaaa-bbbbbbbbbbbb",
    "categoryId": "cat-rent-0002",
    "amount": -1500.00,
    "currencyCode": "USD",
    "description": "Monthly rent payment",
    "frequency": "MONTHLY",
    "startDate": "2026-04-01",
    "nextExecutionDate": "2026-04-01",
    "autoConfirm": true,
    "status": "ACTIVE",
    "createdAt": "2026-03-19T10:00:00Z"
  },
  "timestamp": "2026-03-19T10:00:00Z"
}`,
									},
								]}
							/>
						</div>
					</div>
				</ApiEndpoint>
			</div>

			{/* GET /api/v1/recurring-transactions/{id} */}
			<div className="space-y-4">
				<h2 id="get-recurring-by-id" className="text-xl font-semibold text-[#F0EDF5]">
					{t("get_recurring_by_id")}
				</h2>
				<ApiEndpoint
					method="GET"
					path="/api/v1/recurring-transactions/{id}"
					description="Returns a single recurring transaction rule by its ID"
				>
					<div className="space-y-6">
						<div>
							<h3
								id="get-recurring-by-id-params"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("path_params")}
							</h3>
							<ParamTable
								params={[
									{
										name: "id",
										type: "string (uuid)",
										required: true,
										description: "The UUID of the recurring transaction rule to retrieve",
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="get-recurring-by-id-example"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("example_request")}
							</h3>
							<CodeBlock
								tabs={[
									{
										label: "cURL",
										code: `curl http://localhost:8080/api/v1/recurring-transactions/rt-a1b2c3d4-e5f6-7890-abcd-ef1234567890`,
									},
									{
										label: "JavaScript",
										code: `const id = "rt-a1b2c3d4-e5f6-7890-abcd-ef1234567890";
const response = await fetch(
  \`http://localhost:8080/api/v1/recurring-transactions/\${id}\`
);

const data = await response.json();
console.log(data);`,
									},
									{
										label: "Python",
										code: `import requests

response = requests.get(
    "http://localhost:8080/api/v1/recurring-transactions/rt-a1b2c3d4-e5f6-7890-abcd-ef1234567890"
)

print(response.json())`,
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="get-recurring-by-id-response"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("response")}
							</h3>
							<CodeBlock
								response
								tabs={[
									{
										label: "JSON",
										code: `{
  "success": true,
  "message": null,
  "data": {
    "id": "rt-a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "accountId": "acc-chk-0001-aaaa-bbbbbbbbbbbb",
    "categoryId": "cat-rent-0002",
    "amount": -1500.00,
    "currencyCode": "USD",
    "description": "Monthly rent payment",
    "frequency": "MONTHLY",
    "startDate": "2026-04-01",
    "nextExecutionDate": "2026-05-01",
    "autoConfirm": true,
    "status": "ACTIVE",
    "createdAt": "2026-03-19T10:00:00Z"
  },
  "timestamp": "2026-03-19T10:05:00Z"
}`,
									},
								]}
							/>
						</div>
					</div>
				</ApiEndpoint>
			</div>

			{/* GET /api/v1/recurring-transactions/user/{userId} */}
			<div className="space-y-4">
				<h2 id="get-recurring-by-user" className="text-xl font-semibold text-[#F0EDF5]">
					{t("get_recurring_by_user")}
				</h2>
				<ApiEndpoint
					method="GET"
					path="/api/v1/recurring-transactions/user/{userId}"
					description="Returns paginated recurring transaction rules for a user"
				>
					<div className="space-y-6">
						<div>
							<h3
								id="get-recurring-by-user-params"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("path_params")}
							</h3>
							<ParamTable
								params={[
									{
										name: "userId",
										type: "string (uuid)",
										required: true,
										description: "The UUID of the user whose rules to retrieve",
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="get-recurring-by-user-query"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("query_params")}
							</h3>
							<ParamTable
								params={[
									{
										name: "page",
										type: "integer",
										required: false,
										description: "Page number (zero-based). Default: 0",
									},
									{
										name: "size",
										type: "integer",
										required: false,
										description: "Number of items per page. Default: 20",
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="get-recurring-by-user-example"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("example_request")}
							</h3>
							<CodeBlock
								tabs={[
									{
										label: "cURL",
										code: `curl "http://localhost:8080/api/v1/recurring-transactions/user/a1b2c3d4-e5f6-7890-abcd-ef1234567890?page=0&size=20"`,
									},
									{
										label: "JavaScript",
										code: `const userId = "a1b2c3d4-e5f6-7890-abcd-ef1234567890";
const response = await fetch(
  \`http://localhost:8080/api/v1/recurring-transactions/user/\${userId}?page=0&size=20\`
);

const data = await response.json();
console.log(data);`,
									},
									{
										label: "Python",
										code: `import requests

response = requests.get(
    "http://localhost:8080/api/v1/recurring-transactions/user/a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    params={"page": 0, "size": 20},
)

print(response.json())`,
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="get-recurring-by-user-response"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("response")}
							</h3>
							<CodeBlock
								response
								tabs={[
									{
										label: "JSON",
										code: `{
  "success": true,
  "message": null,
  "data": {
    "content": [
      {
        "id": "rt-a1b2c3d4-e5f6-7890-abcd-ef1234567890",
        "accountId": "acc-chk-0001-aaaa-bbbbbbbbbbbb",
        "amount": -1500.00,
        "currencyCode": "USD",
        "description": "Monthly rent payment",
        "frequency": "MONTHLY",
        "nextExecutionDate": "2026-05-01",
        "autoConfirm": true,
        "status": "ACTIVE",
        "createdAt": "2026-03-19T10:00:00Z"
      }
    ],
    "totalElements": 1,
    "totalPages": 1,
    "page": 0,
    "size": 20
  },
  "timestamp": "2026-03-19T10:10:00Z"
}`,
									},
								]}
							/>
						</div>
					</div>
				</ApiEndpoint>
			</div>

			{/* PATCH /api/v1/recurring-transactions/{id} */}
			<div className="space-y-4">
				<h2 id="update-recurring" className="text-xl font-semibold text-[#F0EDF5]">
					{t("update_recurring")}
				</h2>
				<ApiEndpoint
					method="PATCH"
					path="/api/v1/recurring-transactions/{id}"
					description="Updates an existing recurring transaction rule's description, amount, or category"
				>
					<div className="space-y-6">
						<div>
							<h3
								id="update-recurring-params"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("path_params")}
							</h3>
							<ParamTable
								params={[
									{
										name: "id",
										type: "string (uuid)",
										required: true,
										description: "The UUID of the recurring transaction rule to update",
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="update-recurring-body"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("request_body")}
							</h3>
							<ParamTable
								params={[
									{
										name: "description",
										type: "string",
										required: false,
										description: "Updated description for generated transactions",
									},
									{
										name: "amount",
										type: "number",
										required: false,
										description: "Updated transaction amount",
									},
									{
										name: "categoryId",
										type: "string (uuid)",
										required: false,
										description: "Updated category ID",
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="update-recurring-example"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("example_request")}
							</h3>
							<CodeBlock
								tabs={[
									{
										label: "cURL",
										code: `curl -X PATCH http://localhost:8080/api/v1/recurring-transactions/rt-a1b2c3d4-e5f6-7890-abcd-ef1234567890 \\
  -H "Content-Type: application/json" \\
  -d '{
    "amount": -1600.00,
    "description": "Monthly rent payment (increased)"
  }'`,
									},
									{
										label: "JavaScript",
										code: `const id = "rt-a1b2c3d4-e5f6-7890-abcd-ef1234567890";
const response = await fetch(
  \`http://localhost:8080/api/v1/recurring-transactions/\${id}\`,
  {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      amount: -1600.00,
      description: "Monthly rent payment (increased)",
    }),
  }
);

const data = await response.json();
console.log(data);`,
									},
									{
										label: "Python",
										code: `import requests

response = requests.patch(
    "http://localhost:8080/api/v1/recurring-transactions/rt-a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    json={
        "amount": -1600.00,
        "description": "Monthly rent payment (increased)",
    },
)

print(response.json())`,
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="update-recurring-response"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("response")}
							</h3>
							<CodeBlock
								response
								tabs={[
									{
										label: "JSON",
										code: `{
  "success": true,
  "message": "Recurring transaction rule updated successfully",
  "data": {
    "id": "rt-a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "accountId": "acc-chk-0001-aaaa-bbbbbbbbbbbb",
    "categoryId": "cat-rent-0002",
    "amount": -1600.00,
    "currencyCode": "USD",
    "description": "Monthly rent payment (increased)",
    "frequency": "MONTHLY",
    "startDate": "2026-04-01",
    "nextExecutionDate": "2026-05-01",
    "autoConfirm": true,
    "status": "ACTIVE",
    "createdAt": "2026-03-19T10:00:00Z"
  },
  "timestamp": "2026-03-19T11:00:00Z"
}`,
									},
								]}
							/>
						</div>
					</div>
				</ApiEndpoint>
			</div>

			{/* PATCH /api/v1/recurring-transactions/{id}/pause */}
			<div className="space-y-4">
				<h2 id="toggle-pause-recurring" className="text-xl font-semibold text-[#F0EDF5]">
					{t("toggle_pause_recurring")}
				</h2>
				<ApiEndpoint
					method="PATCH"
					path="/api/v1/recurring-transactions/{id}/pause"
					description="Toggles a recurring transaction rule between ACTIVE and PAUSED status"
				>
					<div className="space-y-6">
						<div>
							<h3
								id="toggle-pause-recurring-params"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("path_params")}
							</h3>
							<ParamTable
								params={[
									{
										name: "id",
										type: "string (uuid)",
										required: true,
										description: "The UUID of the recurring transaction rule to pause or resume",
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="toggle-pause-recurring-example"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("example_request")}
							</h3>
							<CodeBlock
								tabs={[
									{
										label: "cURL",
										code: `curl -X PATCH http://localhost:8080/api/v1/recurring-transactions/rt-a1b2c3d4-e5f6-7890-abcd-ef1234567890/pause`,
									},
									{
										label: "JavaScript",
										code: `const id = "rt-a1b2c3d4-e5f6-7890-abcd-ef1234567890";
const response = await fetch(
  \`http://localhost:8080/api/v1/recurring-transactions/\${id}/pause\`,
  { method: "PATCH" }
);

const data = await response.json();
console.log(data);`,
									},
									{
										label: "Python",
										code: `import requests

response = requests.patch(
    "http://localhost:8080/api/v1/recurring-transactions/rt-a1b2c3d4-e5f6-7890-abcd-ef1234567890/pause"
)

print(response.json())`,
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="toggle-pause-recurring-response"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("response")}
							</h3>
							<CodeBlock
								response
								tabs={[
									{
										label: "JSON",
										code: `{
  "success": true,
  "message": "Recurring transaction rule paused successfully",
  "data": {
    "id": "rt-a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "accountId": "acc-chk-0001-aaaa-bbbbbbbbbbbb",
    "categoryId": "cat-rent-0002",
    "amount": -1600.00,
    "currencyCode": "USD",
    "description": "Monthly rent payment (increased)",
    "frequency": "MONTHLY",
    "startDate": "2026-04-01",
    "nextExecutionDate": "2026-05-01",
    "autoConfirm": true,
    "status": "PAUSED",
    "createdAt": "2026-03-19T10:00:00Z"
  },
  "timestamp": "2026-03-19T11:15:00Z"
}`,
									},
								]}
							/>
						</div>
					</div>
				</ApiEndpoint>
			</div>

			{/* DELETE /api/v1/recurring-transactions/{id} */}
			<div className="space-y-4">
				<h2 id="cancel-recurring" className="text-xl font-semibold text-[#F0EDF5]">
					{t("cancel_recurring")}
				</h2>
				<ApiEndpoint
					method="DELETE"
					path="/api/v1/recurring-transactions/{id}"
					description="Cancels a recurring transaction rule. No further transactions will be generated."
				>
					<div className="space-y-6">
						<div>
							<h3
								id="cancel-recurring-params"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("path_params")}
							</h3>
							<ParamTable
								params={[
									{
										name: "id",
										type: "string (uuid)",
										required: true,
										description: "The UUID of the recurring transaction rule to cancel",
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="cancel-recurring-example"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("example_request")}
							</h3>
							<CodeBlock
								tabs={[
									{
										label: "cURL",
										code: `curl -X DELETE http://localhost:8080/api/v1/recurring-transactions/rt-a1b2c3d4-e5f6-7890-abcd-ef1234567890`,
									},
									{
										label: "JavaScript",
										code: `const id = "rt-a1b2c3d4-e5f6-7890-abcd-ef1234567890";
const response = await fetch(
  \`http://localhost:8080/api/v1/recurring-transactions/\${id}\`,
  { method: "DELETE" }
);

const data = await response.json();
console.log(data);`,
									},
									{
										label: "Python",
										code: `import requests

response = requests.delete(
    "http://localhost:8080/api/v1/recurring-transactions/rt-a1b2c3d4-e5f6-7890-abcd-ef1234567890"
)

print(response.json())`,
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="cancel-recurring-response"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("response")}
							</h3>
							<CodeBlock
								response
								tabs={[
									{
										label: "JSON",
										code: `{
  "success": true,
  "message": "Recurring transaction rule cancelled successfully",
  "data": null,
  "timestamp": "2026-03-19T11:30:00Z"
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
					label: "Goals API",
					href: `/${locale}/develop/api/goals`,
				}}
				next={{
					label: "Architecture",
					href: `/${locale}/develop/architecture`,
				}}
			/>
		</div>
	);
}
