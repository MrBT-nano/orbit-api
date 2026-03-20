"use client";

import { useLocale, useTranslations } from "next-intl";
import ApiEndpoint from "@/components/ApiEndpoint";
import Breadcrumb from "@/components/Breadcrumb";
import CodeBlock from "@/components/CodeBlock";
import PageNav from "@/components/PageNav";
import ParamTable from "@/components/ParamTable";

export default function PaymentMethodsAPIPage() {
	const locale = useLocale();
	const t = useTranslations("pages");
	const ta = useTranslations("api");

	return (
		<div className="space-y-8">
			<Breadcrumb
				items={[
					{ label: "Develop", href: `/${locale}/develop/getting-started` },
					{ label: "API Reference" },
					{ label: t("payment_methods_api_title").replace(" API", "").replace("API ", "") },
				]}
			/>

			<div>
				<h1 className="text-3xl font-bold text-[#F0EDF5] mb-2">{t("payment_methods_api_title")}</h1>
				<p className="text-[#9B8FB8] leading-relaxed">{t("payment_methods_api_desc")}</p>
			</div>

			{/* POST /api/v1/payment-methods */}
			<div className="space-y-4">
				<h2 id="create-payment-method" className="text-xl font-semibold text-[#F0EDF5]">
					{t("create_payment_method")}
				</h2>
				<ApiEndpoint
					method="POST"
					path="/api/v1/payment-methods"
					description="Registers a new payment method for a user, such as a credit card, bank transfer, or digital wallet"
				>
					<div className="space-y-6">
						<div>
							<h3
								id="create-payment-method-body"
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
										description: "The UUID of the user who owns this payment method",
									},
									{
										name: "provider",
										type: "string",
										required: true,
										description:
											"Payment provider: STRIPE, PAYPAL, BANK_TRANSFER, CRYPTO_WALLET, APPLE_PAY, or GOOGLE_PAY",
									},
									{
										name: "providerReferenceId",
										type: "string",
										required: false,
										description:
											"External reference ID from the payment provider (e.g. Stripe token)",
									},
									{
										name: "lastFourDigits",
										type: "string",
										required: false,
										description: "Last four digits of the card or account number for display",
									},
									{
										name: "isDefault",
										type: "boolean",
										required: false,
										description:
											"Whether this should be the user's default payment method. Default: false",
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="create-payment-method-example"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("example_request")}
							</h3>
							<CodeBlock
								tabs={[
									{
										label: "cURL",
										code: `curl -X POST http://localhost:8080/api/v1/payment-methods \\
  -H "Content-Type: application/json" \\
  -d '{
    "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "provider": "STRIPE",
    "providerReferenceId": "pm_1OxAbCdEfGhIjKlMnOpQrStU",
    "lastFourDigits": "4242",
    "isDefault": true
  }'`,
									},
									{
										label: "JavaScript",
										code: `const response = await fetch("http://localhost:8080/api/v1/payment-methods", {
  method: "POST",
  headers: { "Content-Type": "application/json" },
  body: JSON.stringify({
    userId: "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    provider: "STRIPE",
    providerReferenceId: "pm_1OxAbCdEfGhIjKlMnOpQrStU",
    lastFourDigits: "4242",
    isDefault: true,
  }),
});

const data = await response.json();
console.log(data);`,
									},
									{
										label: "Python",
										code: `import requests

response = requests.post(
    "http://localhost:8080/api/v1/payment-methods",
    json={
        "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
        "provider": "STRIPE",
        "providerReferenceId": "pm_1OxAbCdEfGhIjKlMnOpQrStU",
        "lastFourDigits": "4242",
        "isDefault": True,
    },
)

print(response.json())`,
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="create-payment-method-response"
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
  "message": "Payment method created",
  "data": {
    "id": "pm-a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "provider": "STRIPE",
    "providerReferenceId": "pm_1OxAbCdEfGhIjKlMnOpQrStU",
    "lastFourDigits": "4242",
    "isDefault": true,
    "status": "ACTIVE",
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

			{/* GET /api/v1/payment-methods/{id} */}
			<div className="space-y-4">
				<h2 id="get-payment-method-by-id" className="text-xl font-semibold text-[#F0EDF5]">
					{t("get_payment_method_by_id")}
				</h2>
				<ApiEndpoint
					method="GET"
					path="/api/v1/payment-methods/{id}"
					description="Returns a single payment method by its ID"
				>
					<div className="space-y-6">
						<div>
							<h3
								id="get-payment-method-by-id-params"
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
										description: "The UUID of the payment method to retrieve",
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="get-payment-method-by-id-example"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("example_request")}
							</h3>
							<CodeBlock
								tabs={[
									{
										label: "cURL",
										code: `curl http://localhost:8080/api/v1/payment-methods/pm-a1b2c3d4-e5f6-7890-abcd-ef1234567890`,
									},
									{
										label: "JavaScript",
										code: `const id = "pm-a1b2c3d4-e5f6-7890-abcd-ef1234567890";
const response = await fetch(
  \`http://localhost:8080/api/v1/payment-methods/\${id}\`
);

const data = await response.json();
console.log(data);`,
									},
									{
										label: "Python",
										code: `import requests

response = requests.get(
    "http://localhost:8080/api/v1/payment-methods/pm-a1b2c3d4-e5f6-7890-abcd-ef1234567890"
)

print(response.json())`,
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="get-payment-method-by-id-response"
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
    "id": "pm-a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "provider": "STRIPE",
    "providerReferenceId": "pm_1OxAbCdEfGhIjKlMnOpQrStU",
    "lastFourDigits": "4242",
    "isDefault": true,
    "status": "ACTIVE",
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

			{/* GET /api/v1/payment-methods/user/{userId} */}
			<div className="space-y-4">
				<h2 id="get-payment-methods-by-user" className="text-xl font-semibold text-[#F0EDF5]">
					{t("get_payment_methods_by_user")}
				</h2>
				<ApiEndpoint
					method="GET"
					path="/api/v1/payment-methods/user/{userId}"
					description="Returns paginated payment methods for a user"
				>
					<div className="space-y-6">
						<div>
							<h3
								id="get-payment-methods-by-user-params"
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
										description: "The UUID of the user whose payment methods to retrieve",
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="get-payment-methods-by-user-query"
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
								id="get-payment-methods-by-user-example"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("example_request")}
							</h3>
							<CodeBlock
								tabs={[
									{
										label: "cURL",
										code: `curl "http://localhost:8080/api/v1/payment-methods/user/a1b2c3d4-e5f6-7890-abcd-ef1234567890?page=0&size=20"`,
									},
									{
										label: "JavaScript",
										code: `const userId = "a1b2c3d4-e5f6-7890-abcd-ef1234567890";
const response = await fetch(
  \`http://localhost:8080/api/v1/payment-methods/user/\${userId}?page=0&size=20\`
);

const data = await response.json();
console.log(data);`,
									},
									{
										label: "Python",
										code: `import requests

response = requests.get(
    "http://localhost:8080/api/v1/payment-methods/user/a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    params={"page": 0, "size": 20},
)

print(response.json())`,
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="get-payment-methods-by-user-response"
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
        "id": "pm-a1b2c3d4-e5f6-7890-abcd-ef1234567890",
        "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
        "provider": "STRIPE",
        "providerReferenceId": "pm_1OxAbCdEfGhIjKlMnOpQrStU",
        "lastFourDigits": "4242",
        "isDefault": true,
        "status": "ACTIVE",
        "createdAt": "2026-03-20T10:00:00Z",
        "updatedAt": "2026-03-20T10:00:00Z"
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

			{/* PATCH /api/v1/payment-methods/{id} */}
			<div className="space-y-4">
				<h2 id="update-payment-method" className="text-xl font-semibold text-[#F0EDF5]">
					{t("update_payment_method")}
				</h2>
				<ApiEndpoint
					method="PATCH"
					path="/api/v1/payment-methods/{id}"
					description="Updates the default status of an existing payment method"
				>
					<div className="space-y-6">
						<div>
							<h3
								id="update-payment-method-params"
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
										description: "The UUID of the payment method to update",
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="update-payment-method-body"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("request_body")}
							</h3>
							<ParamTable
								params={[
									{
										name: "isDefault",
										type: "boolean",
										required: false,
										description: "Set to true to make this the user's default payment method",
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="update-payment-method-example"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("example_request")}
							</h3>
							<CodeBlock
								tabs={[
									{
										label: "cURL",
										code: `curl -X PATCH http://localhost:8080/api/v1/payment-methods/pm-a1b2c3d4-e5f6-7890-abcd-ef1234567890 \\
  -H "Content-Type: application/json" \\
  -d '{
    "isDefault": true
  }'`,
									},
									{
										label: "JavaScript",
										code: `const id = "pm-a1b2c3d4-e5f6-7890-abcd-ef1234567890";
const response = await fetch(
  \`http://localhost:8080/api/v1/payment-methods/\${id}\`,
  {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      isDefault: true,
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
    "http://localhost:8080/api/v1/payment-methods/pm-a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    json={
        "isDefault": True,
    },
)

print(response.json())`,
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="update-payment-method-response"
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
  "message": "Payment method updated",
  "data": {
    "id": "pm-a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "provider": "STRIPE",
    "providerReferenceId": "pm_1OxAbCdEfGhIjKlMnOpQrStU",
    "lastFourDigits": "4242",
    "isDefault": true,
    "status": "ACTIVE",
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

			{/* DELETE /api/v1/payment-methods/{id} */}
			<div className="space-y-4">
				<h2 id="delete-payment-method" className="text-xl font-semibold text-[#F0EDF5]">
					{t("delete_payment_method")}
				</h2>
				<ApiEndpoint
					method="DELETE"
					path="/api/v1/payment-methods/{id}"
					description="Soft deletes a payment method, changing its status to REVOKED"
				>
					<div className="space-y-6">
						<div>
							<h3
								id="delete-payment-method-params"
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
										description: "The UUID of the payment method to remove",
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="delete-payment-method-example"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("example_request")}
							</h3>
							<CodeBlock
								tabs={[
									{
										label: "cURL",
										code: `curl -X DELETE http://localhost:8080/api/v1/payment-methods/pm-a1b2c3d4-e5f6-7890-abcd-ef1234567890`,
									},
									{
										label: "JavaScript",
										code: `const id = "pm-a1b2c3d4-e5f6-7890-abcd-ef1234567890";
const response = await fetch(
  \`http://localhost:8080/api/v1/payment-methods/\${id}\`,
  { method: "DELETE" }
);

const data = await response.json();
console.log(data);`,
									},
									{
										label: "Python",
										code: `import requests

response = requests.delete(
    "http://localhost:8080/api/v1/payment-methods/pm-a1b2c3d4-e5f6-7890-abcd-ef1234567890"
)

print(response.json())`,
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="delete-payment-method-response"
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
  "message": "Payment method deleted",
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
					label: "Recurring Transactions API",
					href: `/${locale}/develop/api/recurring-transactions`,
				}}
				next={{
					label: "Subscriptions API",
					href: `/${locale}/develop/api/subscriptions`,
				}}
			/>
		</div>
	);
}
