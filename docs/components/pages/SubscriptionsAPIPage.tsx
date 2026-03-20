"use client";

import { useLocale, useTranslations } from "next-intl";
import ApiEndpoint from "@/components/ApiEndpoint";
import Breadcrumb from "@/components/Breadcrumb";
import CodeBlock from "@/components/CodeBlock";
import PageNav from "@/components/PageNav";
import ParamTable from "@/components/ParamTable";

export default function SubscriptionsAPIPage() {
	const locale = useLocale();
	const t = useTranslations("pages");
	const ta = useTranslations("api");

	return (
		<div className="space-y-8">
			<Breadcrumb
				items={[
					{ label: "Develop", href: `/${locale}/develop/getting-started` },
					{ label: "API Reference" },
					{ label: t("subscriptions_api_title").replace(" API", "").replace("API ", "") },
				]}
			/>

			<div>
				<h1 className="text-3xl font-bold text-[#F0EDF5] mb-2">{t("subscriptions_api_title")}</h1>
				<p className="text-[#9B8FB8] leading-relaxed">{t("subscriptions_api_desc")}</p>
			</div>

			{/* POST /api/v1/subscriptions */}
			<div className="space-y-4">
				<h2 id="create-subscription" className="text-xl font-semibold text-[#F0EDF5]">
					{t("create_subscription")}
				</h2>
				<ApiEndpoint
					method="POST"
					path="/api/v1/subscriptions"
					description="Creates a new subscription to track recurring payments with optional billing reminders"
				>
					<div className="space-y-6">
						<div>
							<h3
								id="create-subscription-body"
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
										description: "The UUID of the user who owns this subscription",
									},
									{
										name: "accountId",
										type: "string (uuid)",
										required: true,
										description: "The account to charge for this subscription",
									},
									{
										name: "categoryId",
										type: "string (uuid)",
										required: false,
										description: "Optional category to classify the subscription expense",
									},
									{
										name: "paymentMethodId",
										type: "string (uuid)",
										required: false,
										description: "Optional linked payment method for this subscription",
									},
									{
										name: "name",
										type: "string",
										required: true,
										description: "Name of the subscription (e.g. Netflix, Spotify)",
									},
									{
										name: "amount",
										type: "number",
										required: true,
										description: "Billing amount per cycle",
									},
									{
										name: "currencyCode",
										type: "string",
										required: true,
										description: "ISO 4217 currency code (e.g. USD, THB)",
									},
									{
										name: "billingCycle",
										type: "string",
										required: true,
										description: "Billing frequency: WEEKLY, MONTHLY, QUARTERLY, or YEARLY",
									},
									{
										name: "nextBillingDate",
										type: "string (date)",
										required: true,
										description: "The next billing date (ISO 8601)",
									},
									{
										name: "reminderDaysBefore",
										type: "integer",
										required: false,
										description:
											"Days before billing to send a reminder notification. Default: null (no reminder)",
									},
									{
										name: "trialEndDate",
										type: "string (date)",
										required: false,
										description: "End date of a free trial period, if applicable",
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="create-subscription-example"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("example_request")}
							</h3>
							<CodeBlock
								tabs={[
									{
										label: "cURL",
										code: `curl -X POST http://localhost:8080/api/v1/subscriptions \\
  -H "Content-Type: application/json" \\
  -d '{
    "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "accountId": "acc-chk-0001-aaaa-bbbbbbbbbbbb",
    "categoryId": "cat-ent-0003",
    "paymentMethodId": "pm-a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "name": "Netflix Premium",
    "amount": 22.99,
    "currencyCode": "USD",
    "billingCycle": "MONTHLY",
    "nextBillingDate": "2026-04-15",
    "reminderDaysBefore": 3,
    "trialEndDate": "2026-04-01"
  }'`,
									},
									{
										label: "JavaScript",
										code: `const response = await fetch("http://localhost:8080/api/v1/subscriptions", {
  method: "POST",
  headers: { "Content-Type": "application/json" },
  body: JSON.stringify({
    userId: "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    accountId: "acc-chk-0001-aaaa-bbbbbbbbbbbb",
    categoryId: "cat-ent-0003",
    paymentMethodId: "pm-a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    name: "Netflix Premium",
    amount: 22.99,
    currencyCode: "USD",
    billingCycle: "MONTHLY",
    nextBillingDate: "2026-04-15",
    reminderDaysBefore: 3,
    trialEndDate: "2026-04-01",
  }),
});

const data = await response.json();
console.log(data);`,
									},
									{
										label: "Python",
										code: `import requests

response = requests.post(
    "http://localhost:8080/api/v1/subscriptions",
    json={
        "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
        "accountId": "acc-chk-0001-aaaa-bbbbbbbbbbbb",
        "categoryId": "cat-ent-0003",
        "paymentMethodId": "pm-a1b2c3d4-e5f6-7890-abcd-ef1234567890",
        "name": "Netflix Premium",
        "amount": 22.99,
        "currencyCode": "USD",
        "billingCycle": "MONTHLY",
        "nextBillingDate": "2026-04-15",
        "reminderDaysBefore": 3,
        "trialEndDate": "2026-04-01",
    },
)

print(response.json())`,
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="create-subscription-response"
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
  "message": "Subscription created",
  "data": {
    "id": "sub-a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "accountId": "acc-chk-0001-aaaa-bbbbbbbbbbbb",
    "categoryId": "cat-ent-0003",
    "paymentMethodId": "pm-a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "name": "Netflix Premium",
    "amount": 22.99,
    "currencyCode": "USD",
    "billingCycle": "MONTHLY",
    "nextBillingDate": "2026-04-15",
    "reminderDaysBefore": 3,
    "status": "TRIAL",
    "trialEndDate": "2026-04-01",
    "createdAt": "2026-03-20T10:00:00Z",
    "updatedAt": "2026-03-20T10:00:00Z"
  },
  "timestamp": "2026-03-20T10:00:00Z"
}`,
									},
								]}
							/>
						</div>
					</div>
				</ApiEndpoint>
			</div>

			{/* GET /api/v1/subscriptions/{id} */}
			<div className="space-y-4">
				<h2 id="get-subscription-by-id" className="text-xl font-semibold text-[#F0EDF5]">
					{t("get_subscription_by_id")}
				</h2>
				<ApiEndpoint
					method="GET"
					path="/api/v1/subscriptions/{id}"
					description="Returns a single subscription by its ID"
				>
					<div className="space-y-6">
						<div>
							<h3
								id="get-subscription-by-id-params"
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
										description: "The UUID of the subscription to retrieve",
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="get-subscription-by-id-example"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("example_request")}
							</h3>
							<CodeBlock
								tabs={[
									{
										label: "cURL",
										code: `curl http://localhost:8080/api/v1/subscriptions/sub-a1b2c3d4-e5f6-7890-abcd-ef1234567890`,
									},
									{
										label: "JavaScript",
										code: `const id = "sub-a1b2c3d4-e5f6-7890-abcd-ef1234567890";
const response = await fetch(
  \`http://localhost:8080/api/v1/subscriptions/\${id}\`
);

const data = await response.json();
console.log(data);`,
									},
									{
										label: "Python",
										code: `import requests

response = requests.get(
    "http://localhost:8080/api/v1/subscriptions/sub-a1b2c3d4-e5f6-7890-abcd-ef1234567890"
)

print(response.json())`,
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="get-subscription-by-id-response"
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
    "id": "sub-a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "accountId": "acc-chk-0001-aaaa-bbbbbbbbbbbb",
    "categoryId": "cat-ent-0003",
    "paymentMethodId": "pm-a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "name": "Netflix Premium",
    "amount": 22.99,
    "currencyCode": "USD",
    "billingCycle": "MONTHLY",
    "nextBillingDate": "2026-04-15",
    "reminderDaysBefore": 3,
    "status": "ACTIVE",
    "trialEndDate": "2026-04-01",
    "createdAt": "2026-03-20T10:00:00Z",
    "updatedAt": "2026-03-20T10:00:00Z"
  },
  "timestamp": "2026-03-20T10:05:00Z"
}`,
									},
								]}
							/>
						</div>
					</div>
				</ApiEndpoint>
			</div>

			{/* GET /api/v1/subscriptions/user/{userId} */}
			<div className="space-y-4">
				<h2 id="get-subscriptions-by-user" className="text-xl font-semibold text-[#F0EDF5]">
					{t("get_subscriptions_by_user")}
				</h2>
				<ApiEndpoint
					method="GET"
					path="/api/v1/subscriptions/user/{userId}"
					description="Returns paginated subscriptions for a user"
				>
					<div className="space-y-6">
						<div>
							<h3
								id="get-subscriptions-by-user-params"
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
										description: "The UUID of the user whose subscriptions to retrieve",
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="get-subscriptions-by-user-query"
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
								id="get-subscriptions-by-user-example"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("example_request")}
							</h3>
							<CodeBlock
								tabs={[
									{
										label: "cURL",
										code: `curl "http://localhost:8080/api/v1/subscriptions/user/a1b2c3d4-e5f6-7890-abcd-ef1234567890?page=0&size=20"`,
									},
									{
										label: "JavaScript",
										code: `const userId = "a1b2c3d4-e5f6-7890-abcd-ef1234567890";
const response = await fetch(
  \`http://localhost:8080/api/v1/subscriptions/user/\${userId}?page=0&size=20\`
);

const data = await response.json();
console.log(data);`,
									},
									{
										label: "Python",
										code: `import requests

response = requests.get(
    "http://localhost:8080/api/v1/subscriptions/user/a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    params={"page": 0, "size": 20},
)

print(response.json())`,
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="get-subscriptions-by-user-response"
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
        "id": "sub-a1b2c3d4-e5f6-7890-abcd-ef1234567890",
        "accountId": "acc-chk-0001-aaaa-bbbbbbbbbbbb",
        "name": "Netflix Premium",
        "amount": 22.99,
        "currencyCode": "USD",
        "billingCycle": "MONTHLY",
        "nextBillingDate": "2026-04-15",
        "reminderDaysBefore": 3,
        "status": "ACTIVE",
        "createdAt": "2026-03-20T10:00:00Z"
      }
    ],
    "totalElements": 1,
    "totalPages": 1,
    "page": 0,
    "size": 20
  },
  "timestamp": "2026-03-20T10:10:00Z"
}`,
									},
								]}
							/>
						</div>
					</div>
				</ApiEndpoint>
			</div>

			{/* PATCH /api/v1/subscriptions/{id} */}
			<div className="space-y-4">
				<h2 id="update-subscription" className="text-xl font-semibold text-[#F0EDF5]">
					{t("update_subscription")}
				</h2>
				<ApiEndpoint
					method="PATCH"
					path="/api/v1/subscriptions/{id}"
					description="Updates an existing subscription's name, amount, or reminder settings"
				>
					<div className="space-y-6">
						<div>
							<h3
								id="update-subscription-params"
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
										description: "The UUID of the subscription to update",
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="update-subscription-body"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("request_body")}
							</h3>
							<ParamTable
								params={[
									{
										name: "name",
										type: "string",
										required: false,
										description: "Updated subscription name",
									},
									{
										name: "amount",
										type: "number",
										required: false,
										description: "Updated billing amount",
									},
									{
										name: "reminderDaysBefore",
										type: "integer",
										required: false,
										description: "Updated number of days before billing to send a reminder",
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="update-subscription-example"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("example_request")}
							</h3>
							<CodeBlock
								tabs={[
									{
										label: "cURL",
										code: `curl -X PATCH http://localhost:8080/api/v1/subscriptions/sub-a1b2c3d4-e5f6-7890-abcd-ef1234567890 \\
  -H "Content-Type: application/json" \\
  -d '{
    "name": "Netflix Standard",
    "amount": 15.49,
    "reminderDaysBefore": 5
  }'`,
									},
									{
										label: "JavaScript",
										code: `const id = "sub-a1b2c3d4-e5f6-7890-abcd-ef1234567890";
const response = await fetch(
  \`http://localhost:8080/api/v1/subscriptions/\${id}\`,
  {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      name: "Netflix Standard",
      amount: 15.49,
      reminderDaysBefore: 5,
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
    "http://localhost:8080/api/v1/subscriptions/sub-a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    json={
        "name": "Netflix Standard",
        "amount": 15.49,
        "reminderDaysBefore": 5,
    },
)

print(response.json())`,
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="update-subscription-response"
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
  "message": "Subscription updated",
  "data": {
    "id": "sub-a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "accountId": "acc-chk-0001-aaaa-bbbbbbbbbbbb",
    "categoryId": "cat-ent-0003",
    "paymentMethodId": "pm-a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "name": "Netflix Standard",
    "amount": 15.49,
    "currencyCode": "USD",
    "billingCycle": "MONTHLY",
    "nextBillingDate": "2026-04-15",
    "reminderDaysBefore": 5,
    "status": "ACTIVE",
    "trialEndDate": "2026-04-01",
    "createdAt": "2026-03-20T10:00:00Z",
    "updatedAt": "2026-03-20T11:00:00Z"
  },
  "timestamp": "2026-03-20T11:00:00Z"
}`,
									},
								]}
							/>
						</div>
					</div>
				</ApiEndpoint>
			</div>

			{/* PATCH /api/v1/subscriptions/{id}/pause */}
			<div className="space-y-4">
				<h2 id="toggle-pause-subscription" className="text-xl font-semibold text-[#F0EDF5]">
					{t("toggle_pause_subscription")}
				</h2>
				<ApiEndpoint
					method="PATCH"
					path="/api/v1/subscriptions/{id}/pause"
					description="Toggles a subscription between ACTIVE and PAUSED status"
				>
					<div className="space-y-6">
						<div>
							<h3
								id="toggle-pause-subscription-params"
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
										description: "The UUID of the subscription to pause or resume",
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="toggle-pause-subscription-example"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("example_request")}
							</h3>
							<CodeBlock
								tabs={[
									{
										label: "cURL",
										code: `curl -X PATCH http://localhost:8080/api/v1/subscriptions/sub-a1b2c3d4-e5f6-7890-abcd-ef1234567890/pause`,
									},
									{
										label: "JavaScript",
										code: `const id = "sub-a1b2c3d4-e5f6-7890-abcd-ef1234567890";
const response = await fetch(
  \`http://localhost:8080/api/v1/subscriptions/\${id}/pause\`,
  { method: "PATCH" }
);

const data = await response.json();
console.log(data);`,
									},
									{
										label: "Python",
										code: `import requests

response = requests.patch(
    "http://localhost:8080/api/v1/subscriptions/sub-a1b2c3d4-e5f6-7890-abcd-ef1234567890/pause"
)

print(response.json())`,
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="toggle-pause-subscription-response"
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
  "message": "Subscription pause toggled",
  "data": {
    "id": "sub-a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "accountId": "acc-chk-0001-aaaa-bbbbbbbbbbbb",
    "categoryId": "cat-ent-0003",
    "paymentMethodId": "pm-a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "name": "Netflix Standard",
    "amount": 15.49,
    "currencyCode": "USD",
    "billingCycle": "MONTHLY",
    "nextBillingDate": "2026-04-15",
    "reminderDaysBefore": 5,
    "status": "PAUSED",
    "trialEndDate": "2026-04-01",
    "createdAt": "2026-03-20T10:00:00Z",
    "updatedAt": "2026-03-20T11:15:00Z"
  },
  "timestamp": "2026-03-20T11:15:00Z"
}`,
									},
								]}
							/>
						</div>
					</div>
				</ApiEndpoint>
			</div>

			{/* DELETE /api/v1/subscriptions/{id} */}
			<div className="space-y-4">
				<h2 id="delete-subscription" className="text-xl font-semibold text-[#F0EDF5]">
					{t("delete_subscription")}
				</h2>
				<ApiEndpoint
					method="DELETE"
					path="/api/v1/subscriptions/{id}"
					description="Cancels a subscription via soft delete, changing its status to CANCELLED"
				>
					<div className="space-y-6">
						<div>
							<h3
								id="delete-subscription-params"
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
										description: "The UUID of the subscription to cancel",
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="delete-subscription-example"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("example_request")}
							</h3>
							<CodeBlock
								tabs={[
									{
										label: "cURL",
										code: `curl -X DELETE http://localhost:8080/api/v1/subscriptions/sub-a1b2c3d4-e5f6-7890-abcd-ef1234567890`,
									},
									{
										label: "JavaScript",
										code: `const id = "sub-a1b2c3d4-e5f6-7890-abcd-ef1234567890";
const response = await fetch(
  \`http://localhost:8080/api/v1/subscriptions/\${id}\`,
  { method: "DELETE" }
);

const data = await response.json();
console.log(data);`,
									},
									{
										label: "Python",
										code: `import requests

response = requests.delete(
    "http://localhost:8080/api/v1/subscriptions/sub-a1b2c3d4-e5f6-7890-abcd-ef1234567890"
)

print(response.json())`,
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="delete-subscription-response"
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
  "message": "Subscription cancelled",
  "data": null,
  "timestamp": "2026-03-20T11:30:00Z"
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
					label: "Payment Methods API",
					href: `/${locale}/develop/api/payment-methods`,
				}}
				next={{
					label: "Architecture",
					href: `/${locale}/develop/architecture`,
				}}
			/>
		</div>
	);
}
