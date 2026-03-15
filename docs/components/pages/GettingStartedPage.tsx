"use client";

import Link from "next/link";
import { useLocale, useTranslations } from "next-intl";
import { ArrowRight } from "lucide-react";
import Breadcrumb from "@/components/Breadcrumb";
import CodeBlock from "@/components/CodeBlock";
import Callout from "@/components/Callout";
import PageNav from "@/components/PageNav";

export default function GettingStartedPage() {
  const locale = useLocale();
  const t = useTranslations("pages");

  const exploreLinks = [
    {
      label: "Users API",
      href: `/${locale}/develop/api/users`,
      description: "Register users and manage profiles",
    },
    {
      label: "Accounts API",
      href: `/${locale}/develop/api/accounts`,
      description: "Create and manage financial accounts",
    },
    {
      label: "Swagger UI",
      href: "http://localhost:8080/swagger-ui.html",
      description: "Interactive API explorer",
      external: true,
    },
  ];

  return (
    <div className="space-y-8">
      <Breadcrumb
        items={[
          { label: "Develop", href: `/${locale}/develop/getting-started` },
          { label: t("getting_started_title") },
        ]}
      />

      <div>
        <h1 className="text-3xl font-bold text-[#F0EDF5] mb-2">{t("getting_started_title")}</h1>
        <p className="text-[#9B8FB8] leading-relaxed">
          {t("getting_started_desc")}
        </p>
      </div>

      {/* Prerequisites */}
      <section>
        <h2
          id="prerequisites"
          className="text-xl font-semibold text-[#F0EDF5] mb-4"
        >
          {t("prerequisites")}
        </h2>
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div className="bg-[#2A1F3D]/30 border border-white/6 rounded-xl p-4">
            <div className="flex items-center gap-2 mb-2">
              <span className="size-2 rounded-full bg-[#5DFDCB]" />
              <span className="text-sm font-semibold text-[#F0EDF5]">
                Java 25
              </span>
            </div>
            <p className="text-xs text-[#9B8FB8]">
              Required for building and running the Spring Boot application.
              Install via SDKMAN or download from adoptium.net.
            </p>
          </div>
          <div className="bg-[#2A1F3D]/30 border border-white/6 rounded-xl p-4">
            <div className="flex items-center gap-2 mb-2">
              <span className="size-2 rounded-full bg-[#B07AFF]" />
              <span className="text-sm font-semibold text-[#F0EDF5]">
                Docker Desktop
              </span>
            </div>
            <p className="text-xs text-[#9B8FB8]">
              PostgreSQL runs in a Docker container, automatically managed by
              Spring Boot Docker Compose support.
            </p>
          </div>
        </div>
      </section>

      {/* Setup */}
      <section>
        <h2 id="setup" className="text-xl font-semibold text-[#F0EDF5] mb-4">
          {t("setup")}
        </h2>
        <CodeBlock
          tabs={[
            {
              label: "Terminal",
              code: `# Clone the repository
git clone https://github.com/MrBT-nano/orbit-api.git

# Navigate into the project
cd orbit-api

# Run the application (PostgreSQL starts automatically)
./mvnw spring-boot:run`,
            },
          ]}
        />
        <p className="text-sm text-[#9B8FB8] mt-3">
          The application will start on{" "}
          <code className="text-[#5DFDCB] bg-[#2A1F3D]/60 px-1.5 py-0.5 rounded text-xs">
            http://localhost:8080
          </code>
          . Docker Compose will automatically provision the PostgreSQL database.
        </p>
      </section>

      {/* Test the API */}
      <section>
        <h2
          id="test-the-api"
          className="text-xl font-semibold text-[#F0EDF5] mb-4"
        >
          {t("test_api")}
        </h2>
        <CodeBlock
          tabs={[
            {
              label: "Create User",
              code: `curl -X POST http://localhost:8080/api/v1/users \\
  -H "Content-Type: application/json" \\
  -d '{
    "clerkUserId": "user_test123",
    "email": "test@example.com",
    "firstName": "Test",
    "lastName": "User",
    "baseCurrency": "USD",
    "timezone": "America/New_York"
  }'`,
            },
            {
              label: "Get User",
              code: `curl http://localhost:8080/api/v1/users/clerk/user_test123`,
            },
          ]}
        />
      </section>

      {/* Explore */}
      <section>
        <h2
          id="explore"
          className="text-xl font-semibold text-[#F0EDF5] mb-4"
        >
          {t("explore")}
        </h2>
        <div className="space-y-3">
          {exploreLinks.map((link) => {
            const Wrapper = link.external ? "a" : Link;
            const extraProps = link.external
              ? { target: "_blank", rel: "noopener noreferrer" }
              : {};
            return (
              <Wrapper
                key={link.label}
                href={link.href}
                {...(extraProps as any)}
                className="group flex items-center justify-between bg-[#2A1F3D]/30 border border-white/6 rounded-xl p-4 hover:border-[#5DFDCB]/30 transition-colors"
              >
                <div>
                  <span className="text-sm font-semibold text-[#F0EDF5] group-hover:text-[#5DFDCB] transition-colors">
                    {link.label}
                  </span>
                  <p className="text-xs text-[#9B8FB8] mt-0.5">
                    {link.description}
                  </p>
                </div>
                <ArrowRight className="size-4 text-[#9B8FB8] group-hover:text-[#5DFDCB] transition-colors" />
              </Wrapper>
            );
          })}
        </div>
      </section>

      <Callout type="success">
        Swagger UI is available at{" "}
        <code className="font-mono text-xs">/swagger-ui.html</code> when the
        application is running. It provides an interactive explorer for all API
        endpoints.
      </Callout>

      <PageNav
        next={{
          label: "Users API",
          href: `/${locale}/develop/api/users`,
        }}
      />
    </div>
  );
}
