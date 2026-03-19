"use client";

import { useLocale, useTranslations } from "next-intl";
import ApiEndpoint from "@/components/ApiEndpoint";
import Breadcrumb from "@/components/Breadcrumb";
import CodeBlock from "@/components/CodeBlock";
import PageNav from "@/components/PageNav";
import ParamTable from "@/components/ParamTable";

export default function GoalsAPIPage() {
	const locale = useLocale();
	const t = useTranslations("pages");
	const ta = useTranslations("api");

	return (
		<div className="space-y-8">
			<Breadcrumb
				items={[
					{ label: "Develop", href: `/${locale}/develop/getting-started` },
					{ label: "API Reference" },
					{ label: t("goals_api_title").replace(" API", "").replace("API ", "") },
				]}
			/>

			<div>
				<h1 className="text-3xl font-bold text-[#F0EDF5] mb-2">{t("goals_api_title")}</h1>
				<p className="text-[#9B8FB8] leading-relaxed">{t("goals_api_desc")}</p>
			</div>

			{/* POST /api/v1/goals */}
			<div className="space-y-4">
				<h2 id="create-goal" className="text-xl font-semibold text-[#F0EDF5]">
					{t("create_goal")}
				</h2>
				<ApiEndpoint
					method="POST"
					path="/api/v1/goals"
					description="Creates a new savings goal, optionally linked to an account for automatic balance tracking"
				>
					<div className="space-y-6">
						<div>
							<h3
								id="create-goal-body"
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
										description: "The UUID of the user who owns the goal",
									},
									{
										name: "name",
										type: "string",
										required: true,
										description: "A descriptive name for the goal",
									},
									{
										name: "targetAmount",
										type: "number",
										required: true,
										description: "The target amount to save",
									},
									{
										name: "targetDate",
										type: "string (date)",
										required: false,
										description: "Optional target date to reach the goal (ISO 8601)",
									},
									{
										name: "linkedAccountId",
										type: "string (uuid)",
										required: false,
										description: "Optional account ID whose balance is used as current progress",
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="create-goal-example"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("example_request")}
							</h3>
							<CodeBlock
								tabs={[
									{
										label: "cURL",
										code: `curl -X POST http://localhost:8080/api/v1/goals \\
  -H "Content-Type: application/json" \\
  -d '{
    "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "name": "Emergency Fund",
    "targetAmount": 10000.00,
    "targetDate": "2026-12-31",
    "linkedAccountId": "acc-sav-0001-aaaa-bbbbbbbbbbbb"
  }'`,
									},
									{
										label: "JavaScript",
										code: `const response = await fetch("http://localhost:8080/api/v1/goals", {
  method: "POST",
  headers: { "Content-Type": "application/json" },
  body: JSON.stringify({
    userId: "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    name: "Emergency Fund",
    targetAmount: 10000.00,
    targetDate: "2026-12-31",
    linkedAccountId: "acc-sav-0001-aaaa-bbbbbbbbbbbb",
  }),
});

const data = await response.json();
console.log(data);`,
									},
									{
										label: "Python",
										code: `import requests

response = requests.post(
    "http://localhost:8080/api/v1/goals",
    json={
        "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
        "name": "Emergency Fund",
        "targetAmount": 10000.00,
        "targetDate": "2026-12-31",
        "linkedAccountId": "acc-sav-0001-aaaa-bbbbbbbbbbbb",
    },
)

print(response.json())`,
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="create-goal-response"
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
  "message": "Goal created successfully",
  "data": {
    "id": "g1a2b3c4-d5e6-7890-abcd-ef1234567890",
    "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "name": "Emergency Fund",
    "targetAmount": 10000.00,
    "currentAmount": 0.00,
    "targetDate": "2026-12-31",
    "linkedAccountId": "acc-sav-0001-aaaa-bbbbbbbbbbbb",
    "status": "IN_PROGRESS",
    "createdAt": "2026-03-18T10:00:00Z"
  },
  "timestamp": "2026-03-18T10:00:00Z"
}`,
									},
								]}
							/>
						</div>
					</div>
				</ApiEndpoint>
			</div>

			{/* GET /api/v1/goals/{goalId} */}
			<div className="space-y-4">
				<h2 id="get-goal-by-id" className="text-xl font-semibold text-[#F0EDF5]">
					{t("get_goal_by_id")}
				</h2>
				<ApiEndpoint
					method="GET"
					path="/api/v1/goals/{goalId}"
					description="Returns a single goal. If linked to an account, currentAmount reflects the live account balance."
				>
					<div className="space-y-6">
						<div>
							<h3
								id="get-goal-by-id-params"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("path_params")}
							</h3>
							<ParamTable
								params={[
									{
										name: "goalId",
										type: "string (uuid)",
										required: true,
										description: "The UUID of the goal to retrieve",
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="get-goal-by-id-example"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("example_request")}
							</h3>
							<CodeBlock
								tabs={[
									{
										label: "cURL",
										code: `curl http://localhost:8080/api/v1/goals/g1a2b3c4-d5e6-7890-abcd-ef1234567890`,
									},
									{
										label: "JavaScript",
										code: `const goalId = "g1a2b3c4-d5e6-7890-abcd-ef1234567890";
const response = await fetch(
  \`http://localhost:8080/api/v1/goals/\${goalId}\`
);

const data = await response.json();
console.log(data);`,
									},
									{
										label: "Python",
										code: `import requests

response = requests.get(
    "http://localhost:8080/api/v1/goals/g1a2b3c4-d5e6-7890-abcd-ef1234567890"
)

print(response.json())`,
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="get-goal-by-id-response"
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
    "id": "g1a2b3c4-d5e6-7890-abcd-ef1234567890",
    "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "name": "Emergency Fund",
    "targetAmount": 10000.00,
    "currentAmount": 3250.00,
    "targetDate": "2026-12-31",
    "linkedAccountId": "acc-sav-0001-aaaa-bbbbbbbbbbbb",
    "status": "IN_PROGRESS",
    "createdAt": "2026-03-18T10:00:00Z"
  },
  "timestamp": "2026-03-18T10:05:00Z"
}`,
									},
								]}
							/>
						</div>
					</div>
				</ApiEndpoint>
			</div>

			{/* GET /api/v1/goals/user/{userId} */}
			<div className="space-y-4">
				<h2 id="get-goals-by-user" className="text-xl font-semibold text-[#F0EDF5]">
					{t("get_goals_by_user")}
				</h2>
				<ApiEndpoint
					method="GET"
					path="/api/v1/goals/user/{userId}"
					description="Returns paginated goals for a user"
				>
					<div className="space-y-6">
						<div>
							<h3
								id="get-goals-by-user-params"
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
										description: "The UUID of the user whose goals to retrieve",
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="get-goals-by-user-query"
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
								id="get-goals-by-user-example"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("example_request")}
							</h3>
							<CodeBlock
								tabs={[
									{
										label: "cURL",
										code: `curl "http://localhost:8080/api/v1/goals/user/a1b2c3d4-e5f6-7890-abcd-ef1234567890?page=0&size=20"`,
									},
									{
										label: "JavaScript",
										code: `const userId = "a1b2c3d4-e5f6-7890-abcd-ef1234567890";
const response = await fetch(
  \`http://localhost:8080/api/v1/goals/user/\${userId}?page=0&size=20\`
);

const data = await response.json();
console.log(data);`,
									},
									{
										label: "Python",
										code: `import requests

response = requests.get(
    "http://localhost:8080/api/v1/goals/user/a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    params={"page": 0, "size": 20},
)

print(response.json())`,
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="get-goals-by-user-response"
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
        "id": "g1a2b3c4-d5e6-7890-abcd-ef1234567890",
        "name": "Emergency Fund",
        "targetAmount": 10000.00,
        "currentAmount": 3250.00,
        "targetDate": "2026-12-31",
        "status": "IN_PROGRESS",
        "createdAt": "2026-03-18T10:00:00Z"
      }
    ],
    "totalElements": 1,
    "totalPages": 1,
    "page": 0,
    "size": 20
  },
  "timestamp": "2026-03-18T10:10:00Z"
}`,
									},
								]}
							/>
						</div>
					</div>
				</ApiEndpoint>
			</div>

			{/* PATCH /api/v1/goals/{goalId}/contribute */}
			<div className="space-y-4">
				<h2 id="contribute-to-goal" className="text-xl font-semibold text-[#F0EDF5]">
					{t("contribute_to_goal")}
				</h2>
				<ApiEndpoint
					method="PATCH"
					path="/api/v1/goals/{goalId}/contribute"
					description="Records a manual contribution toward a goal. If currentAmount reaches targetAmount, the status transitions to ACHIEVED."
				>
					<div className="space-y-6">
						<div>
							<h3
								id="contribute-to-goal-params"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("path_params")}
							</h3>
							<ParamTable
								params={[
									{
										name: "goalId",
										type: "string (uuid)",
										required: true,
										description: "The UUID of the goal to contribute to",
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="contribute-to-goal-body"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("request_body")}
							</h3>
							<ParamTable
								params={[
									{
										name: "amount",
										type: "number",
										required: true,
										description: "The contribution amount (must be positive)",
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="contribute-to-goal-example"
								className="text-sm font-semibold text-[#9B8FB8] uppercase tracking-wider mb-3"
							>
								{ta("example_request")}
							</h3>
							<CodeBlock
								tabs={[
									{
										label: "cURL",
										code: `curl -X PATCH http://localhost:8080/api/v1/goals/g1a2b3c4-d5e6-7890-abcd-ef1234567890/contribute \\
  -H "Content-Type: application/json" \\
  -d '{ "amount": 500.00 }'`,
									},
									{
										label: "JavaScript",
										code: `const goalId = "g1a2b3c4-d5e6-7890-abcd-ef1234567890";
const response = await fetch(
  \`http://localhost:8080/api/v1/goals/\${goalId}/contribute\`,
  {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ amount: 500.00 }),
  }
);

const data = await response.json();
console.log(data);`,
									},
									{
										label: "Python",
										code: `import requests

response = requests.patch(
    "http://localhost:8080/api/v1/goals/g1a2b3c4-d5e6-7890-abcd-ef1234567890/contribute",
    json={"amount": 500.00},
)

print(response.json())`,
									},
								]}
							/>
						</div>

						<div>
							<h3
								id="contribute-to-goal-response"
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
  "message": "Contribution recorded successfully",
  "data": {
    "id": "g1a2b3c4-d5e6-7890-abcd-ef1234567890",
    "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "name": "Emergency Fund",
    "targetAmount": 10000.00,
    "currentAmount": 3750.00,
    "targetDate": "2026-12-31",
    "linkedAccountId": "acc-sav-0001-aaaa-bbbbbbbbbbbb",
    "status": "IN_PROGRESS",
    "createdAt": "2026-03-18T10:00:00Z"
  },
  "timestamp": "2026-03-18T10:20:00Z"
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
					label: "Budgets API",
					href: `/${locale}/develop/api/budgets`,
				}}
				next={{
					label: "Architecture",
					href: `/${locale}/develop/architecture`,
				}}
			/>
		</div>
	);
}
