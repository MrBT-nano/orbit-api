"use client";

import { Globe, Landmark, ShieldCheck, Sparkles } from "lucide-react";
import Image from "next/image";
import { useLocale, useTranslations } from "next-intl";
import Breadcrumb from "@/components/Breadcrumb";
import Callout from "@/components/Callout";
import PageNav from "@/components/PageNav";
import { basePath } from "@/lib/basePath";

export default function OverviewPage() {
	const locale = useLocale();
	const t = useTranslations("pages");

	const capabilities = [
		{
			icon: Landmark,
			color: "#5DFDCB",
			title: "Multi-Account",
			description:
				"Track bank accounts, credit cards, crypto wallets, cash, and investment portfolios all in one place.",
		},
		{
			icon: Globe,
			color: "#B07AFF",
			title: "Multi-Currency",
			description:
				"Native support for any fiat currency (USD, THB, EUR) and cryptocurrency (BTC, ETH) with live exchange rates.",
		},
		{
			icon: ShieldCheck,
			color: "#FFB07A",
			title: "Secure Identity",
			description:
				"Enterprise-grade authentication via Clerk SSO with Spring Security integration and full audit trails.",
		},
		{
			icon: Sparkles,
			color: "#FF6B9D",
			title: "AI Insights",
			description:
				"Planned intelligent categorization, spending pattern analysis, and predictive budgeting powered by ML models.",
		},
	];

	return (
		<div className="space-y-8">
			<Breadcrumb
				items={[
					{ label: "Product", href: `/${locale}/product/overview` },
					{ label: t("overview_title") },
				]}
			/>

			<div>
				<h1 className="text-3xl font-bold text-[#F0EDF5] mb-2">{t("overview_title")}</h1>
				<p className="text-[#9B8FB8] leading-relaxed">{t("overview_desc")}</p>
			</div>

			{/* Hero Card */}
			<div className="bg-gradient-to-br from-[#5DFDCB]/8 to-[#B07AFF]/8 border border-[#5DFDCB]/12 rounded-2xl p-8 text-center">
				<Image
					src={`${basePath}/orbit-logo.svg`}
					alt="Orbit"
					width={64}
					height={64}
					className="size-16 mx-auto mb-4"
				/>
				<h2 className="text-2xl font-bold text-[#F0EDF5] mb-2">{t("hero_title")}</h2>
				<p className="text-[#9B8FB8] max-w-lg mx-auto text-sm leading-relaxed">{t("hero_desc")}</p>
			</div>

			{/* Key Capabilities */}
			<section>
				<h2 id="key-capabilities" className="text-xl font-semibold text-[#F0EDF5] mb-4">
					{t("key_capabilities")}
				</h2>
				<div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
					{capabilities.map((cap) => (
						<div key={cap.title} className="bg-[#2A1F3D]/30 border border-white/6 rounded-2xl p-5">
							<div
								className="size-10 rounded-xl flex items-center justify-center mb-3"
								style={{ backgroundColor: `${cap.color}15` }}
							>
								<cap.icon className="size-5" style={{ color: cap.color }} />
							</div>
							<h3 className="text-[#F0EDF5] font-semibold mb-1.5">{cap.title}</h3>
							<p className="text-sm text-[#9B8FB8] leading-relaxed">{cap.description}</p>
						</div>
					))}
				</div>
			</section>

			<Callout type="info">
				Orbit is currently in <strong>v1.0.0-SNAPSHOT</strong>. Core user management and account
				functionality are live. Transaction tracking, payment processing, and AI insights are on the
				roadmap.
			</Callout>

			<PageNav
				next={{
					label: "Features",
					href: `/${locale}/product/features`,
				}}
			/>
		</div>
	);
}
