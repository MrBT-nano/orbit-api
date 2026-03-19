import spec from "@/public/openapi.json";

export function getEndpointsByTag(tag: string) {
	const endpoints: Array<{
		method: string;
		path: string;
		summary: string;
		description: string;
		operationId: string;
		requestBody?: Record<string, unknown>;
		parameters?: Record<string, unknown>[];
		responses?: Record<string, unknown>;
	}> = [];

	for (const [path, methods] of Object.entries(spec.paths)) {
		for (const [method, operation] of Object.entries(
			methods as Record<string, Record<string, unknown>>,
		)) {
			if (Array.isArray(operation.tags) && operation.tags.includes(tag)) {
				endpoints.push({
					method: method.toUpperCase(),
					path,
					summary: (operation.summary as string) || "",
					description: (operation.description as string) || "",
					operationId: (operation.operationId as string) || "",
					requestBody: operation.requestBody as Record<string, unknown> | undefined,
					parameters: operation.parameters as Record<string, unknown>[] | undefined,
					responses: operation.responses as Record<string, unknown> | undefined,
				});
			}
		}
	}
	return endpoints;
}

export function resolveSchema(ref: string) {
	const parts = ref.replace("#/", "").split("/");
	let current: Record<string, unknown> = spec as Record<string, unknown>;
	for (const part of parts) {
		if (current == null) return undefined;
		current = current[part] as Record<string, unknown>;
	}
	return current;
}

export { spec };
