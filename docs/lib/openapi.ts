import spec from "@/public/openapi.json";

export function getEndpointsByTag(tag: string) {
  const endpoints: Array<{
    method: string;
    path: string;
    summary: string;
    description: string;
    operationId: string;
    requestBody?: any;
    parameters?: any[];
    responses?: any;
  }> = [];

  for (const [path, methods] of Object.entries(spec.paths)) {
    for (const [method, operation] of Object.entries(
      methods as Record<string, any>
    )) {
      if (operation.tags?.includes(tag)) {
        endpoints.push({
          method: method.toUpperCase(),
          path,
          summary: operation.summary || "",
          description: operation.description || "",
          operationId: operation.operationId || "",
          requestBody: operation.requestBody,
          parameters: operation.parameters,
          responses: operation.responses,
        });
      }
    }
  }
  return endpoints;
}

export function resolveSchema(ref: string) {
  const parts = ref.replace("#/", "").split("/");
  let current: any = spec;
  for (const part of parts) {
    current = current[part];
  }
  return current;
}

export { spec };
