"use client";

import { Database, FileCode2, Hexagon, Package, Split } from "lucide-react";
import { useLocale, useTranslations } from "next-intl";
import Breadcrumb from "@/components/Breadcrumb";
import Callout from "@/components/Callout";
import CodeBlock from "@/components/CodeBlock";
import PageNav from "@/components/PageNav";

export default function ArchitecturePage() {
	const locale = useLocale();
	const t = useTranslations("pages");

	const principles = [
		{
			icon: Package,
			color: "#5DFDCB",
			title: "Package by Feature",
			description:
				"Code is grouped by business domain, not by technical layer. Each feature module is a self-contained unit with its own API, core logic, and infrastructure.",
		},
		{
			icon: Hexagon,
			color: "#B07AFF",
			title: "Hexagonal Boundaries",
			description:
				"Strict separation between api/ (inbound adapters), core/ (business logic and ports), and infrastructure/ (outbound adapters). Dependencies always point inward.",
		},
		{
			icon: Split,
			color: "#FFB07A",
			title: "CQRS Lite",
			description:
				"Instead of monolithic service classes, complex features are split into focused use-case classes like CreateTransactionUseCase and CalculateBalanceQuery.",
		},
		{
			icon: FileCode2,
			color: "#FF6B9D",
			title: "Java 25 Records",
			description:
				"All DTOs and immutable data carriers use Java record types. No @Data annotations for DTOs, ensuring true immutability and concise code.",
		},
	];

	return (
		<div className="space-y-8">
			<Breadcrumb
				items={[
					{ label: "Develop", href: `/${locale}/develop/getting-started` },
					{ label: t("architecture_title") },
				]}
			/>

			<div>
				<h1 className="text-3xl font-bold text-[#F0EDF5] mb-2">{t("architecture_title")}</h1>
				<p className="text-[#9B8FB8] leading-relaxed">{t("architecture_desc")}</p>
			</div>

			{/* Core Principles */}
			<section>
				<h2 id="core-principles" className="text-xl font-semibold text-[#F0EDF5] mb-4">
					{t("core_principles")}
				</h2>
				<div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
					{principles.map((p) => (
						<div key={p.title} className="bg-[#2A1F3D]/30 border border-white/6 rounded-2xl p-5">
							<div
								className="size-10 rounded-xl flex items-center justify-center mb-3"
								style={{ backgroundColor: `${p.color}15` }}
							>
								<p.icon className="size-5" style={{ color: p.color }} />
							</div>
							<h3 className="text-[#F0EDF5] font-semibold mb-1.5">{p.title}</h3>
							<p className="text-sm text-[#9B8FB8] leading-relaxed">{p.description}</p>
						</div>
					))}
				</div>
			</section>

			{/* Database Strategy */}
			<section>
				<h2 id="database-strategy" className="text-xl font-semibold text-[#F0EDF5] mb-4">
					{t("database_strategy")}
				</h2>
				<div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
					<div className="bg-[#2A1F3D]/30 border border-white/6 rounded-2xl p-5">
						<div className="flex items-center gap-3 mb-3">
							<div className="size-10 rounded-xl flex items-center justify-center bg-[#5DFDCB]/10">
								<Database className="size-5 text-[#5DFDCB]" />
							</div>
							<div>
								<h3 className="text-[#F0EDF5] font-semibold">PostgreSQL</h3>
								<span className="text-xs text-[#5DFDCB] font-mono">Primary</span>
							</div>
						</div>
						<p className="text-sm text-[#9B8FB8] leading-relaxed">
							ACID-compliant relational database for all financial transactions, user accounts, and
							ledger entries. Managed via Spring Data JPA with automatic Docker Compose lifecycle.
						</p>
					</div>
					<div className="bg-[#2A1F3D]/30 border border-white/6 rounded-2xl p-5">
						<div className="flex items-center gap-3 mb-3">
							<div className="size-10 rounded-xl flex items-center justify-center bg-[#B07AFF]/10">
								<Database className="size-5 text-[#B07AFF]" />
							</div>
							<div>
								<h3 className="text-[#F0EDF5] font-semibold">MongoDB</h3>
								<span className="text-xs text-[#B07AFF] font-mono">Planned</span>
							</div>
						</div>
						<p className="text-sm text-[#9B8FB8] leading-relaxed">
							Document store for analytics data, spending insights, and time-series financial
							metrics. Optimized for read-heavy aggregation queries and dashboards.
						</p>
					</div>
				</div>
			</section>

			{/* Feature Module Structure */}
			<section>
				<h2 id="module-structure" className="text-xl font-semibold text-[#F0EDF5] mb-4">
					{t("module_structure")}
				</h2>
				<p className="text-sm text-[#9B8FB8] mb-4">
					Every feature module under{" "}
					<code className="text-[#B07AFF] bg-[#2A1F3D]/60 px-1.5 py-0.5 rounded text-xs">
						src/main/java/com/mrbt/orbit/
					</code>{" "}
					follows this hexagonal layout:
				</p>
				<CodeBlock
					tabs={[
						{
							label: "Structure",
							code: `[feature]/
├── api/              # Inbound: REST Controllers, Request/Response DTOs
│   ├── FeatureController.java
│   ├── CreateFeatureRequest.java    (record)
│   └── FeatureResponse.java         (record)
├── core/             # Business Logic: Services, Domain Models, Ports
│   ├── FeatureService.java
│   ├── Feature.java                 (domain model)
│   └── FeaturePort.java             (interface)
├── infrastructure/   # Outbound: JPA Entities, Repositories, Mappers
│   ├── FeatureEntity.java
│   ├── FeatureRepository.java
│   └── FeatureMapper.java
└── exception/        # Module-specific exceptions
    └── FeatureNotFoundException.java`,
						},
					]}
				/>
			</section>

			<Callout type="info">
				Orbit is currently at v1.0.0-SNAPSHOT. The <strong>security</strong> and{" "}
				<strong>ledger</strong> feature modules are fully implemented. Additional modules (payment,
				crypto, audit) are on the roadmap.
			</Callout>

			<PageNav
				prev={{
					label: "Subscriptions API",
					href: `/${locale}/develop/api/subscriptions`,
				}}
				next={{
					label: "Quick Start",
					href: `/${locale}/develop/getting-started`,
				}}
			/>
		</div>
	);
}
