# Orbit Documentation Website Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Rebuild the Orbit docs site into a dual-section (Developer Portal + Product Docs) documentation website with i18n support (EN/TH), shadcn/ui components, and the Orbit Design System theme.

**Architecture:** Next.js 16 App Router with `[locale]` dynamic segment for i18n via `next-intl`. Two main sections (`develop/` and `product/`) with distinct sidebar navigation. Static export preserved for GitHub Pages deployment. OpenAPI spec parsed at build time for API reference pages.

**Tech Stack:** Next.js 16, React 19, Tailwind CSS v4, DaisyUI 5, shadcn/ui, next-intl, Lucide React, JetBrains Mono + DM Sans fonts

**Current State:** Flat route structure (`/`, `/api-reference`, `/architecture`) with basic Sidebar component, Swagger UI for API docs, `next-intl` installed but unused, no shadcn/ui, no i18n, `output: "export"` for GitHub Pages.

**IMPORTANT:** Use `bun`/`bunx` instead of `npm`/`npx` per CLAUDE.md.

---

## Task 1: Install & Configure shadcn/ui

**Files:**
- Create: `docs/components.json`
- Create: `docs/lib/utils.ts`
- Modify: `docs/package.json`
- Modify: `docs/app/globals.css`
- Modify: `docs/tsconfig.json`

**Step 1: Install shadcn/ui dependencies**

```bash
cd /Users/mrbt/Desktop/repository/orbit/docs
bun add class-variance-authority clsx tailwind-merge
```

**Step 2: Create utility file**

Create `docs/lib/utils.ts`:
```ts
import { type ClassValue, clsx } from "clsx";
import { twMerge } from "tailwind-merge";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}
```

**Step 3: Create components.json for shadcn**

Create `docs/components.json`:
```json
{
  "$schema": "https://ui.shadcn.com/schema.json",
  "style": "new-york",
  "rsc": true,
  "tsx": true,
  "tailwind": {
    "config": "",
    "css": "app/globals.css",
    "baseColor": "zinc",
    "cssVariables": true
  },
  "aliases": {
    "components": "@/components",
    "utils": "@/lib/utils",
    "ui": "@/components/ui",
    "lib": "@/lib",
    "hooks": "@/hooks"
  },
  "iconLibrary": "lucide"
}
```

**Step 4: Install shadcn components we need**

```bash
cd /Users/mrbt/Desktop/repository/orbit/docs
bunx shadcn@latest add badge button card dialog input separator sheet tabs -y
```

If `bunx shadcn` prompts for confirmation or fails, install individually or create manually.

**Step 5: Verify tsconfig has path alias**

Ensure `docs/tsconfig.json` has:
```json
{
  "compilerOptions": {
    "paths": {
      "@/*": ["./*"]
    }
  }
}
```

**Step 6: Commit**

```bash
git add docs/components.json docs/lib/ docs/components/ui/ docs/package.json docs/bun.lock docs/tsconfig.json
git commit -m "feat(docs): install shadcn/ui with base components"
```

---

## Task 2: Configure next-intl with [locale] Routing

**Files:**
- Create: `docs/i18n/request.ts`
- Create: `docs/i18n/routing.ts`
- Create: `docs/messages/en.json`
- Create: `docs/messages/th.json`
- Modify: `docs/next.config.ts`
- Create: `docs/middleware.ts` (if not using static export — see note)

**IMPORTANT NOTE:** The current config uses `output: "export"` for GitHub Pages. `next-intl` with middleware requires a server. For static export, use the **without-i18n-routing** approach from next-intl docs — locale is determined by `[locale]` param + `generateStaticParams`.

**Step 1: Create i18n request configuration**

Create `docs/i18n/request.ts`:
```ts
import { getRequestConfig } from "next-intl/server";

export const locales = ["en", "th"] as const;
export type Locale = (typeof locales)[number];
export const defaultLocale: Locale = "en";

export default getRequestConfig(async ({ requestLocale }) => {
  let locale = await requestLocale;

  if (!locale || !locales.includes(locale as Locale)) {
    locale = defaultLocale;
  }

  return {
    locale,
    messages: (await import(`../messages/${locale}.json`)).default,
  };
});
```

**Step 2: Create English messages**

Create `docs/messages/en.json`:
```json
{
  "nav": {
    "develop": "Develop",
    "product": "Product",
    "search": "Search documentation...",
    "shortcut": "⌘K"
  },
  "landing": {
    "badge": "v1.0.0 — NOW LIVE",
    "title_line1": "Build the future of",
    "title_line2": "Personal Finance",
    "description": "The Orbit Platform provides a rigorous, Hexagonal Architecture-based API for ledgers, crypto tracking, and smart financial rules.",
    "cta_build": "Start Building",
    "cta_docs": "Read Docs"
  },
  "sidebar": {
    "getting_started": "Getting Started",
    "quick_start": "Quick Start",
    "authentication": "Authentication",
    "api_reference": "API Reference",
    "users": "Users",
    "accounts": "Accounts",
    "transactions": "Transactions",
    "payments": "Payments",
    "architecture": "Architecture",
    "system_design": "System Design",
    "database": "Database Strategy",
    "soon": "Soon"
  },
  "product_sidebar": {
    "overview": "Overview",
    "what_is_orbit": "What is Orbit?",
    "roadmap": "Roadmap",
    "features": "Features",
    "account_management": "Account Management",
    "budget_tracking": "Budget Tracking",
    "crypto_support": "Crypto Support",
    "analytics": "Analytics",
    "guides": "Guides",
    "getting_started": "Getting Started",
    "faq": "FAQ"
  },
  "toc": {
    "on_this_page": "On this page"
  },
  "api": {
    "request_body": "Request Body",
    "path_params": "Path Parameters",
    "query_params": "Query Parameters",
    "response": "Response",
    "example_request": "Example Request",
    "field": "Field",
    "type": "Type",
    "required": "required",
    "optional": "optional",
    "description": "Description",
    "try_it": "Try it"
  },
  "common": {
    "previous": "Previous",
    "next": "Next"
  }
}
```

**Step 3: Create Thai messages**

Create `docs/messages/th.json`:
```json
{
  "nav": {
    "develop": "สำหรับนักพัฒนา",
    "product": "ผลิตภัณฑ์",
    "search": "ค้นหาเอกสาร...",
    "shortcut": "⌘K"
  },
  "landing": {
    "badge": "v1.0.0 — เปิดให้บริการแล้ว",
    "title_line1": "สร้างอนาคตของ",
    "title_line2": "การเงินส่วนบุคคล",
    "description": "แพลตฟอร์ม Orbit มอบ API ระดับ Hexagonal Architecture สำหรับบัญชีแยกประเภท การติดตามคริปโต และกฎทางการเงินอัจฉริยะ",
    "cta_build": "เริ่มพัฒนา",
    "cta_docs": "อ่านเอกสาร"
  },
  "sidebar": {
    "getting_started": "เริ่มต้นใช้งาน",
    "quick_start": "เริ่มต้นด่วน",
    "authentication": "การยืนยันตัวตน",
    "api_reference": "API อ้างอิง",
    "users": "ผู้ใช้งาน",
    "accounts": "บัญชี",
    "transactions": "ธุรกรรม",
    "payments": "การชำระเงิน",
    "architecture": "สถาปัตยกรรม",
    "system_design": "การออกแบบระบบ",
    "database": "กลยุทธ์ฐานข้อมูล",
    "soon": "เร็วๆ นี้"
  },
  "product_sidebar": {
    "overview": "ภาพรวม",
    "what_is_orbit": "Orbit คืออะไร?",
    "roadmap": "แผนงาน",
    "features": "ฟีเจอร์",
    "account_management": "จัดการบัญชี",
    "budget_tracking": "ติดตามงบประมาณ",
    "crypto_support": "รองรับคริปโต",
    "analytics": "วิเคราะห์ข้อมูล",
    "guides": "คู่มือ",
    "getting_started": "เริ่มต้นใช้งาน",
    "faq": "คำถามที่พบบ่อย"
  },
  "toc": {
    "on_this_page": "ในหน้านี้"
  },
  "api": {
    "request_body": "เนื้อหาคำร้อง",
    "path_params": "พารามิเตอร์เส้นทาง",
    "query_params": "พารามิเตอร์การค้นหา",
    "response": "การตอบกลับ",
    "example_request": "ตัวอย่างคำร้อง",
    "field": "ฟิลด์",
    "type": "ชนิด",
    "required": "จำเป็น",
    "optional": "ไม่บังคับ",
    "description": "คำอธิบาย",
    "try_it": "ทดลองใช้"
  },
  "common": {
    "previous": "ก่อนหน้า",
    "next": "ถัดไป"
  }
}
```

**Step 4: Update next.config.ts for next-intl plugin**

Modify `docs/next.config.ts`:
```ts
import type { NextConfig } from "next";
import createNextIntlPlugin from "next-intl/plugin";

const withNextIntl = createNextIntlPlugin("./i18n/request.ts");

const nextConfig: NextConfig = {
  output: "export",
  basePath: process.env.NODE_ENV === "production" ? "/orbit-api" : "",
  eslint: {
    ignoreDuringBuilds: true,
  },
  typescript: {
    ignoreBuildErrors: true,
  },
};

export default withNextIntl(nextConfig);
```

**Step 5: Run `bun install` to verify deps**

```bash
cd /Users/mrbt/Desktop/repository/orbit/docs
bun install
```

**Step 6: Commit**

```bash
git add docs/i18n/ docs/messages/ docs/next.config.ts
git commit -m "feat(docs): configure next-intl with EN/TH locale support"
```

---

## Task 3: Restructure Routes to [locale] Layout

**Files:**
- Create: `docs/app/[locale]/layout.tsx`
- Create: `docs/app/[locale]/page.tsx`
- Create: `docs/app/[locale]/develop/layout.tsx`
- Create: `docs/app/[locale]/develop/page.tsx`
- Create: `docs/app/[locale]/develop/api/users/page.tsx` (placeholder)
- Create: `docs/app/[locale]/develop/api/accounts/page.tsx` (placeholder)
- Create: `docs/app/[locale]/develop/architecture/page.tsx` (placeholder)
- Create: `docs/app/[locale]/develop/getting-started/page.tsx` (placeholder)
- Create: `docs/app/[locale]/product/layout.tsx`
- Create: `docs/app/[locale]/product/page.tsx`
- Create: `docs/app/[locale]/product/overview/page.tsx` (placeholder)
- Create: `docs/app/[locale]/product/features/page.tsx` (placeholder)
- Delete: `docs/app/page.tsx` (old root page)
- Delete: `docs/app/api-reference/page.tsx`
- Delete: `docs/app/architecture/page.tsx`
- Delete: `docs/app/components/Sidebar.tsx` (old sidebar)
- Modify: `docs/app/layout.tsx` (strip to minimal root, keep globals.css import)

**Step 1: Create root layout (minimal — just html/body + globals.css)**

Modify `docs/app/layout.tsx` to be a minimal shell:
```tsx
import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "Orbit Documentation",
  description: "Official documentation for the Orbit Personal Finance Management System",
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return children;
}
```

**Step 2: Create [locale] layout**

Create `docs/app/[locale]/layout.tsx`:
```tsx
import { NextIntlClientProvider, hasLocale } from "next-intl";
import { getMessages, setRequestLocale } from "next-intl/server";
import { notFound } from "next/navigation";
import { locales } from "@/i18n/request";
import Navbar from "@/components/Navbar";

export function generateStaticParams() {
  return locales.map((locale) => ({ locale }));
}

export default async function LocaleLayout({
  children,
  params,
}: {
  children: React.ReactNode;
  params: Promise<{ locale: string }>;
}) {
  const { locale } = await params;

  if (!hasLocale(locales, locale)) {
    notFound();
  }

  setRequestLocale(locale);
  const messages = await getMessages();

  return (
    <html lang={locale} data-theme="dark" className="scroll-smooth">
      <body className="antialiased min-h-screen bg-background text-foreground">
        <NextIntlClientProvider messages={messages}>
          <Navbar locale={locale} />
          {children}
        </NextIntlClientProvider>
      </body>
    </html>
  );
}
```

**Step 3: Create [locale] landing page (placeholder)**

Create `docs/app/[locale]/page.tsx`:
```tsx
import { setRequestLocale } from "next-intl/server";
import LandingPage from "@/components/pages/LandingPage";

export default async function Home({ params }: { params: Promise<{ locale: string }> }) {
  const { locale } = await params;
  setRequestLocale(locale);
  return <LandingPage />;
}
```

**Step 4: Create develop section layout**

Create `docs/app/[locale]/develop/layout.tsx`:
```tsx
import { setRequestLocale } from "next-intl/server";
import DevelopSidebar from "@/components/DevelopSidebar";
import OnPageToc from "@/components/OnPageToc";

export default async function DevelopLayout({
  children,
  params,
}: {
  children: React.ReactNode;
  params: Promise<{ locale: string }>;
}) {
  const { locale } = await params;
  setRequestLocale(locale);

  return (
    <div className="flex flex-1 relative">
      <DevelopSidebar locale={locale} />
      <main className="flex-1 min-w-0 px-4 sm:px-6 lg:px-10 py-8 max-w-4xl">
        {children}
      </main>
      <OnPageToc />
    </div>
  );
}
```

**Step 5: Create develop hub page + placeholder route pages**

Create `docs/app/[locale]/develop/page.tsx`:
```tsx
import { setRequestLocale } from "next-intl/server";
import { redirect } from "next/navigation";

export default async function DevelopHub({ params }: { params: Promise<{ locale: string }> }) {
  const { locale } = await params;
  setRequestLocale(locale);
  redirect(`/${locale}/develop/getting-started`);
}
```

Create placeholder pages for each develop sub-route:
- `docs/app/[locale]/develop/getting-started/page.tsx`
- `docs/app/[locale]/develop/api/users/page.tsx`
- `docs/app/[locale]/develop/api/accounts/page.tsx`
- `docs/app/[locale]/develop/architecture/page.tsx`

Each placeholder follows this pattern (example for getting-started):
```tsx
import { setRequestLocale } from "next-intl/server";

export default async function GettingStartedPage({ params }: { params: Promise<{ locale: string }> }) {
  const { locale } = await params;
  setRequestLocale(locale);
  return <div><h1 className="text-3xl font-bold">Quick Start</h1></div>;
}
```

**Step 6: Create product section layout + pages (same pattern)**

Create `docs/app/[locale]/product/layout.tsx`:
```tsx
import { setRequestLocale } from "next-intl/server";
import ProductSidebar from "@/components/ProductSidebar";
import OnPageToc from "@/components/OnPageToc";

export default async function ProductLayout({
  children,
  params,
}: {
  children: React.ReactNode;
  params: Promise<{ locale: string }>;
}) {
  const { locale } = await params;
  setRequestLocale(locale);

  return (
    <div className="flex flex-1 relative">
      <ProductSidebar locale={locale} />
      <main className="flex-1 min-w-0 px-4 sm:px-6 lg:px-10 py-8 max-w-4xl">
        {children}
      </main>
      <OnPageToc />
    </div>
  );
}
```

Create `docs/app/[locale]/product/page.tsx` — redirect to overview.
Create `docs/app/[locale]/product/overview/page.tsx` — placeholder.
Create `docs/app/[locale]/product/features/page.tsx` — placeholder.

**Step 7: Delete old files**

```bash
rm docs/app/page.tsx  # replaced by [locale]/page.tsx
rm -rf docs/app/api-reference/  # replaced by [locale]/develop/api/
rm -rf docs/app/architecture/  # replaced by [locale]/develop/architecture/
rm docs/app/components/Sidebar.tsx  # replaced by new sidebar components
rmdir docs/app/components/  # if empty
```

**Step 8: Commit**

```bash
git add -A docs/app/
git commit -m "feat(docs): restructure routes with [locale] segment and develop/product sections"
```

---

## Task 4: Update globals.css for New Theme System

**Files:**
- Modify: `docs/app/globals.css`

**Step 1: Update globals.css**

Rewrite `docs/app/globals.css` to include shadcn-compatible variables alongside the existing Orbit Design System tokens. Keep DaisyUI plugin. Add font imports. Add `[data-theme="dark"]` selector instead of `prefers-color-scheme` for explicit control.

Key changes:
- Add Google Fonts import for DM Sans, JetBrains Mono, Noto Sans Thai
- Keep `@plugin "daisyui"` and `@plugin "@tailwindcss/typography"`
- Add `--font-sans` and `--font-mono` to `@theme`
- Add dark/light theme via `[data-theme]` attribute
- Add utility classes for API method colors
- Remove old Swagger UI custom styles (no longer needed)

**Step 2: Verify build**

```bash
cd /Users/mrbt/Desktop/repository/orbit/docs
bun run build
```

Fix any build errors.

**Step 3: Commit**

```bash
git add docs/app/globals.css
git commit -m "feat(docs): update theme system with Orbit design tokens and font imports"
```

---

## Task 5: Build Navbar Component

**Files:**
- Create: `docs/components/Navbar.tsx`
- Create: `docs/components/LanguageSwitcher.tsx`
- Create: `docs/components/SearchDialog.tsx`

**Step 1: Create Navbar**

Create `docs/components/Navbar.tsx` — a client component with:
- Logo (Orbit icon + text)
- Section switcher (Develop / Product) — highlights based on current path
- Search trigger (opens SearchDialog)
- LanguageSwitcher
- GitHub link
- Mobile hamburger (uses shadcn Sheet for sidebar drawer)

Navigation behavior:
- Clicking "Develop" navigates to `/{locale}/develop/getting-started`
- Clicking "Product" navigates to `/{locale}/product/overview`
- Section switcher highlights based on `pathname.includes('/develop/')` or `/product/`

**Step 2: Create LanguageSwitcher**

Create `docs/components/LanguageSwitcher.tsx` — dropdown that switches locale in URL path:
- Shows current locale (EN/TH)
- On click, replaces `/{currentLocale}/...` with `/{newLocale}/...` in the URL

**Step 3: Create SearchDialog**

Create `docs/components/SearchDialog.tsx` — uses shadcn Dialog:
- Opens with ⌘K shortcut
- Search input
- Hardcoded results for now (Users API, Accounts API, Architecture, etc.)
- Results grouped by section (Develop / Product)

**Step 4: Verify dev server**

```bash
cd /Users/mrbt/Desktop/repository/orbit/docs
bun run dev
```

Open `http://localhost:3000/en/` — verify navbar renders.

**Step 5: Commit**

```bash
git add docs/components/Navbar.tsx docs/components/LanguageSwitcher.tsx docs/components/SearchDialog.tsx
git commit -m "feat(docs): add Navbar with section switcher, language toggle, and search"
```

---

## Task 6: Build Sidebar Components

**Files:**
- Create: `docs/components/DevelopSidebar.tsx`
- Create: `docs/components/ProductSidebar.tsx`
- Create: `docs/components/SidebarShell.tsx`

**Step 1: Create SidebarShell**

Create `docs/components/SidebarShell.tsx` — shared sidebar wrapper:
- Fixed left column on desktop (w-64), hidden on mobile (shown via Sheet from Navbar)
- Scroll area
- Version selector badge at top

**Step 2: Create DevelopSidebar**

Create `docs/components/DevelopSidebar.tsx`:
- Sections: Getting Started, API Reference, Architecture
- API Reference items show HTTP method badges (POST=purple, GET=cyan)
- Collapsible sections with ChevronDown/Right
- Active link highlighting based on pathname
- "Soon" badge for Transactions/Payments

Navigation links:
```
Getting Started → /{locale}/develop/getting-started
Authentication  → /{locale}/develop/getting-started (anchor or future page)
Users           → /{locale}/develop/api/users
Accounts        → /{locale}/develop/api/accounts
System Design   → /{locale}/develop/architecture
Database        → /{locale}/develop/architecture (anchor)
```

**Step 3: Create ProductSidebar**

Create `docs/components/ProductSidebar.tsx`:
- Sections: Overview, Features, Guides
- Links to product sub-pages

**Step 4: Verify navigation**

```bash
bun run dev
```

Navigate between `/en/develop/api/users` and `/en/product/overview` — verify sidebars switch.

**Step 5: Commit**

```bash
git add docs/components/DevelopSidebar.tsx docs/components/ProductSidebar.tsx docs/components/SidebarShell.tsx
git commit -m "feat(docs): add develop and product sidebars with navigation"
```

---

## Task 7: Build Content Components

**Files:**
- Create: `docs/components/ApiEndpoint.tsx`
- Create: `docs/components/CodeBlock.tsx`
- Create: `docs/components/ParamTable.tsx`
- Create: `docs/components/Callout.tsx`
- Create: `docs/components/Breadcrumb.tsx`
- Create: `docs/components/PageNav.tsx`
- Create: `docs/components/OnPageToc.tsx`
- Create: `docs/components/MethodBadge.tsx`

**Step 1: Create MethodBadge**

Create `docs/components/MethodBadge.tsx`:
```tsx
import { cn } from "@/lib/utils";

const methodColors = {
  GET: "bg-[#5DFDCB]/15 text-[#5DFDCB] border-[#5DFDCB]/30",
  POST: "bg-[#B07AFF]/15 text-[#B07AFF] border-[#B07AFF]/30",
  PUT: "bg-[#FFB07A]/15 text-[#FFB07A] border-[#FFB07A]/30",
  PATCH: "bg-[#FFB07A]/15 text-[#FFB07A] border-[#FFB07A]/30",
  DELETE: "bg-[#FF6B9D]/15 text-[#FF6B9D] border-[#FF6B9D]/30",
} as const;

type Method = keyof typeof methodColors;

export default function MethodBadge({ method }: { method: Method }) {
  return (
    <span className={cn("font-mono text-xs font-semibold px-2.5 py-1 rounded-md border", methodColors[method])}>
      {method}
    </span>
  );
}
```

**Step 2: Create ApiEndpoint**

Create `docs/components/ApiEndpoint.tsx`:
- Props: `method`, `path`, `description`, `children` (for body content)
- Header row with MethodBadge + path + description
- Collapsible body section
- Matches the design mockup Section 1

**Step 3: Create CodeBlock**

Create `docs/components/CodeBlock.tsx`:
- Props: `tabs` (array of `{label, code}`), `responseMode?` (boolean)
- Uses shadcn Tabs for language switching (cURL, JavaScript, Python)
- Dark background (#0D0B1A)
- Copy button
- Response variant with green 200 header

**Step 4: Create ParamTable**

Create `docs/components/ParamTable.tsx`:
- Props: `params` (array of `{name, type, required, description}`)
- Styled table matching mockup
- Required/optional badges

**Step 5: Create Callout**

Create `docs/components/Callout.tsx`:
- Props: `type` ("info" | "warning" | "success"), `children`
- Left border + background tint per type
- Icon per type

**Step 6: Create Breadcrumb**

Create `docs/components/Breadcrumb.tsx`:
- Props: `items` (array of `{label, href?}`)
- Last item is current (no link, white text)
- ChevronRight separators

**Step 7: Create PageNav (prev/next)**

Create `docs/components/PageNav.tsx`:
- Props: `prev?` and `next?` objects with `{label, href}`
- Two-column layout matching mockup

**Step 8: Create OnPageToc**

Create `docs/components/OnPageToc.tsx`:
- Client component
- Reads headings from the page DOM via `querySelectorAll("h2, h3")`
- Sticky positioning on desktop, hidden on mobile
- Active heading tracking via IntersectionObserver

**Step 9: Commit**

```bash
git add docs/components/ApiEndpoint.tsx docs/components/CodeBlock.tsx docs/components/ParamTable.tsx docs/components/Callout.tsx docs/components/Breadcrumb.tsx docs/components/PageNav.tsx docs/components/OnPageToc.tsx docs/components/MethodBadge.tsx
git commit -m "feat(docs): add content components — ApiEndpoint, CodeBlock, ParamTable, Callout, etc."
```

---

## Task 8: Build Landing Page

**Files:**
- Create: `docs/components/pages/LandingPage.tsx`

**Step 1: Create LandingPage component**

Create `docs/components/pages/LandingPage.tsx`:
- Full-width (no sidebar)
- Hero section with gradient title, badge, CTA buttons
- Feature cards grid (4 cards: REST API, Architecture, Multi-Currency, Security)
- Quick Start code block
- Uses `useTranslations` for all text
- Matches Section 2 of the design mockup

**Step 2: Verify**

```bash
bun run dev
```

Open `http://localhost:3000/en/` and `http://localhost:3000/th/` — verify landing renders with correct translations.

**Step 3: Commit**

```bash
git add docs/components/pages/LandingPage.tsx
git commit -m "feat(docs): add landing page with hero, features, and quick start"
```

---

## Task 9: Build Users API Page

**Files:**
- Modify: `docs/app/[locale]/develop/api/users/page.tsx`
- Create: `docs/lib/openapi.ts` (OpenAPI parser utility)

**Step 1: Create OpenAPI parser utility**

Create `docs/lib/openapi.ts`:
```ts
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
    for (const [method, operation] of Object.entries(methods as Record<string, any>)) {
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
```

**Step 2: Build Users API page**

Modify `docs/app/[locale]/develop/api/users/page.tsx`:
- Import `getEndpointsByTag("Users")`
- Render Breadcrumb
- Render page title + description
- For each endpoint: render ApiEndpoint with ParamTable, CodeBlock examples, response CodeBlock
- Render PageNav (prev: Getting Started, next: Accounts)
- Generate code examples (cURL, JavaScript fetch, Python requests)

**Step 3: Verify**

```bash
bun run dev
```

Open `http://localhost:3000/en/develop/api/users` — verify endpoints render from openapi.json.

**Step 4: Commit**

```bash
git add docs/lib/openapi.ts docs/app/\[locale\]/develop/api/users/page.tsx
git commit -m "feat(docs): add Users API page with auto-parsed OpenAPI endpoints"
```

---

## Task 10: Build Accounts API Page

**Files:**
- Modify: `docs/app/[locale]/develop/api/accounts/page.tsx`

**Step 1: Build Accounts API page**

Same pattern as Users API page but with `getEndpointsByTag("Accounts")`. Render:
- POST /api/v1/accounts
- GET /api/v1/accounts/{accountId}
- GET /api/v1/accounts/user/{userId}
- ParamTable showing enum values for `type` field (BANK, CREDIT, CRYPTO, CASH, INVESTMENT)
- PageNav (prev: Users, next: Architecture)

**Step 2: Verify**

```bash
bun run dev
```

**Step 3: Commit**

```bash
git add docs/app/\[locale\]/develop/api/accounts/page.tsx
git commit -m "feat(docs): add Accounts API page with endpoint documentation"
```

---

## Task 11: Build Architecture & Getting Started Pages

**Files:**
- Modify: `docs/app/[locale]/develop/architecture/page.tsx`
- Modify: `docs/app/[locale]/develop/getting-started/page.tsx`

**Step 1: Build Architecture page**

Port content from the old `docs/app/architecture/page.tsx` into the new route:
- Core Principles (Hexagonal, Package by Feature, CQRS Lite, Java 25 Records)
- Database Strategy (PostgreSQL + MongoDB)
- Use Callout components and styled cards
- Add Breadcrumb and PageNav

**Step 2: Build Getting Started page**

Create a quick-start guide page:
- Prerequisites (Java 25, Docker)
- Clone & run instructions with CodeBlock
- API testing example with CodeBlock
- Link to Swagger UI

**Step 3: Commit**

```bash
git add docs/app/\[locale\]/develop/architecture/page.tsx docs/app/\[locale\]/develop/getting-started/page.tsx
git commit -m "feat(docs): add Architecture and Getting Started pages"
```

---

## Task 12: Build Product Section Pages

**Files:**
- Modify: `docs/app/[locale]/product/overview/page.tsx`
- Modify: `docs/app/[locale]/product/features/page.tsx`

**Step 1: Build Product Overview page**

"What is Orbit?" page matching Section 3 of mockup:
- Hero card with gradient background
- Key Capabilities grid (Multi-Account, Multi-Currency, Secure Identity, AI Insights)
- Status callout (v1.0.0-SNAPSHOT)
- Use Breadcrumb, Callout, PageNav

**Step 2: Build Features page**

Feature breakdown page:
- Account types (BANK, CREDIT, CRYPTO, CASH, INVESTMENT) with icons
- Currency support description
- Security overview
- Each feature in a styled card

**Step 3: Commit**

```bash
git add docs/app/\[locale\]/product/
git commit -m "feat(docs): add Product overview and features pages"
```

---

## Task 13: Copy App Icon + Final Polish

**Files:**
- Copy: Orbit icon from `flutter/design/public/icon.webp` to `docs/public/orbit-icon.webp`
- Modify: `docs/app/[locale]/layout.tsx` (add favicon/icon metadata)
- Modify: All pages — final consistency check

**Step 1: Copy icon**

```bash
cp /Users/mrbt/Desktop/repository/flutter/design/public/icon.webp /Users/mrbt/Desktop/repository/orbit/docs/public/orbit-icon.webp
```

**Step 2: Add metadata to locale layout**

Add icon metadata to `docs/app/[locale]/layout.tsx`.

**Step 3: Verify full build**

```bash
cd /Users/mrbt/Desktop/repository/orbit/docs
bun run build
```

Fix any build errors. Ensure all routes generate static pages.

**Step 4: Test both locales**

```bash
bun run dev
```

Test:
- `/en/` — Landing page English
- `/th/` — Landing page Thai
- `/en/develop/api/users` — Users API
- `/en/develop/api/accounts` — Accounts API
- `/en/develop/architecture` — Architecture
- `/en/develop/getting-started` — Quick Start
- `/en/product/overview` — Product overview
- `/en/product/features` — Features
- Section switcher navigation
- Language switcher (EN ↔ TH)
- Mobile responsive (resize to 375px)
- Search dialog (⌘K)

**Step 5: Commit**

```bash
git add -A docs/
git commit -m "feat(docs): add orbit icon and final polish for docs site"
```

---

## Summary

| Task | What | Est. Files |
|------|------|------------|
| 1 | Install shadcn/ui | 3 + generated |
| 2 | Configure next-intl | 4 |
| 3 | Restructure routes | ~15 |
| 4 | Update globals.css | 1 |
| 5 | Navbar component | 3 |
| 6 | Sidebar components | 3 |
| 7 | Content components | 8 |
| 8 | Landing page | 1 |
| 9 | Users API page | 2 |
| 10 | Accounts API page | 1 |
| 11 | Architecture + Getting Started | 2 |
| 12 | Product pages | 2 |
| 13 | Icon + Polish | 2+ |
