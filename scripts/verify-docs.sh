#!/bin/bash

# Exit immediately if a command exits with a non-zero status
set -e

echo "======================================="
echo "🔍 Verifying API Documentation Coverage"
echo "======================================="

# Ensure the OpenAPI JSON file exists
if [ ! -f "docs/public/openapi.json" ]; then
  echo "❌ Error: openapi.json not found in docs/public/. The application may not have started correctly."
  exit 1
fi

# Find all Java files annotated with @RestController or @RestControllerAdvice
# We are looking for files that *should* be documented
echo "Scanning codebase for REST Controllers..."
CONTROLLERS=$(grep -rl "@RestController" src/main/java || true)

if [ -z "$CONTROLLERS" ]; then
  echo "⚠️ No REST Controllers found. Passing check."
  exit 0
fi

# Count the number of endpoints defined in the code vs OpenAPI
# This uses grep to find RequestMapping, GetMapping, PostMapping, etc.
CODE_ENDPOINTS=$(grep -hroE "@(Get|Post|Put|Delete|Patch|Request)Mapping" src/main/java | wc -l)

# Count documented paths in the OpenAPI spec
# Use non-greedy character class to correctly match each path on minified JSON
DOC_ENDPOINTS=$(grep -oE '"\/[^"]+"\s*:\s*\{' docs/public/openapi.json | wc -l || echo "0")

echo "📊 Metrics:"
echo "   - Controllers found in code: $(echo "$CONTROLLERS" | wc -l | tr -d ' ')"
echo "   - Endpoint mappings found in code: $CODE_ENDPOINTS"
echo "   - Endpoints documented in OpenAPI: $DOC_ENDPOINTS"

# Note: Since a single @RequestMapping at the class level combined with method-level mappings 
# can make the grep count inexact, we do a sanity check to make sure *something* is documented.
# If we have controllers but NO docs, we fail.
if [ "$CODE_ENDPOINTS" -gt 0 ] && [ "$DOC_ENDPOINTS" -eq 0 ]; then
  echo "❌ Error: You have $CODE_ENDPOINTS API endpoints in your code, but the OpenAPI JSON is empty."
  echo "   Did you forget to add springdoc annotations or is Swagger failing to scan the package?"
  exit 1
fi

echo "✅ API Documentation Check Passed."
exit 0
