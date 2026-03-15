"use client";

import Link from "next/link";
import { useLocale, useTranslations } from "next-intl";
import {
  Terminal,
  Hexagon,
  Bitcoin,
  ShieldCheck,
} from "lucide-react";

export default function LandingPage() {
  const t = useTranslations("landing");
  const locale = useLocale();

  const features = [
    {
      icon: Terminal,
      color: "#5DFDCB",
      title: t("feature_rest_api"),
      description: t("feature_rest_api_desc"),
      href: `/${locale}/develop/api/users`,
    },
    {
      icon: Hexagon,
      color: "#B07AFF",
      title: t("feature_architecture"),
      description: t("feature_architecture_desc"),
      href: `/${locale}/develop/architecture`,
    },
    {
      icon: Bitcoin,
      color: "#FFB07A",
      title: t("feature_multi_currency"),
      description: t("feature_multi_currency_desc"),
      href: `/${locale}/product/features`,
    },
    {
      icon: ShieldCheck,
      color: "#FF6B9D",
      title: t("feature_security"),
      description: t("feature_security_desc"),
      href: `/${locale}/product/overview`,
    },
  ];

  return (
    <div className="flex-1">
      {/* Hero Section */}
      <section className="text-center pt-16 pb-12 px-6">
        {/* Status Badge */}
        <div className="inline-flex items-center gap-2 bg-[#5DFDCB]/10 border border-[#5DFDCB]/20 rounded-full px-4 py-1.5 mb-8">
          <span className="relative flex size-2">
            <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-[#5DFDCB] opacity-75" />
            <span className="relative inline-flex size-2 rounded-full bg-[#5DFDCB]" />
          </span>
          <span className="text-xs font-semibold text-[#5DFDCB]">
            {t("badge")}
          </span>
        </div>

        {/* Title */}
        <h1 className="text-5xl md:text-6xl font-bold mb-6">
          <span className="text-[#F0EDF5]">{t("title_line1")}</span>
          <br />
          <span className="bg-gradient-to-r from-[#5DFDCB] to-[#B07AFF] bg-clip-text text-transparent">
            {t("title_line2")}
          </span>
        </h1>

        {/* Description */}
        <p className="text-lg text-[#9B8FB8] max-w-2xl mx-auto mb-10">
          {t("description")}
        </p>

        {/* CTA Buttons */}
        <div className="flex items-center justify-center gap-4">
          <Link
            href={`/${locale}/develop/getting-started`}
            className="inline-flex items-center bg-gradient-to-br from-[#5DFDCB] to-[#4DE0B5] text-[#0D0B1A] rounded-xl px-7 py-3 font-semibold text-sm hover:opacity-90 transition-opacity"
          >
            {t("cta_build")}
          </Link>
          <Link
            href={`/${locale}/develop/api/users`}
            className="inline-flex items-center bg-transparent border border-white/12 text-[#F0EDF5] rounded-xl px-7 py-3 font-semibold text-sm hover:border-white/25 transition-colors"
          >
            {t("cta_docs")}
          </Link>
        </div>
      </section>

      {/* Feature Cards */}
      <section className="grid grid-cols-1 sm:grid-cols-2 gap-4 max-w-4xl mx-auto px-6">
        {features.map((feature) => (
          <Link
            key={feature.title}
            href={feature.href}
            className="group bg-[#2A1F3D]/30 border border-white/6 rounded-2xl p-6 hover:border-[#5DFDCB]/30 transition-colors"
          >
            <div
              className="size-10 rounded-xl flex items-center justify-center mb-4"
              style={{ backgroundColor: `${feature.color}15` }}
            >
              <feature.icon
                className="size-5"
                style={{ color: feature.color }}
              />
            </div>
            <h3 className="text-[#F0EDF5] font-semibold mb-1.5">
              {feature.title}
            </h3>
            <p className="text-sm text-[#9B8FB8] leading-relaxed">
              {feature.description}
            </p>
          </Link>
        ))}
      </section>

      {/* Quick Start */}
      <section className="max-w-4xl mx-auto px-6 mt-12 pb-16">
        <h2 className="text-2xl font-bold text-[#F0EDF5] mb-4">{t("quick_start")}</h2>
        <div className="bg-[#0D0B1A] border border-white/6 rounded-xl overflow-hidden">
          {/* macOS dots header */}
          <div className="flex items-center gap-2 px-4 py-3 border-b border-white/6">
            <span className="size-3 rounded-full bg-[#FF5F57]" />
            <span className="size-3 rounded-full bg-[#FEBC2E]" />
            <span className="size-3 rounded-full bg-[#28C840]" />
            <span className="ml-3 text-xs text-[#9B8FB8] font-mono">
              terminal
            </span>
          </div>
          <div className="p-4 overflow-x-auto">
            <pre className="font-mono text-sm leading-relaxed text-[#F0EDF5] whitespace-pre">
              <span className="text-[#9B8FB8]">$</span>{" "}
              <span className="text-[#5DFDCB]">git clone</span>{" "}
              https://github.com/MrBT-nano/orbit-api.git{"\n"}
              <span className="text-[#9B8FB8]">$</span>{" "}
              <span className="text-[#5DFDCB]">cd</span> orbit-api{"\n"}
              <span className="text-[#9B8FB8]">$</span>{" "}
              <span className="text-[#5DFDCB]">./mvnw spring-boot:run</span>
            </pre>
          </div>
        </div>
      </section>
    </div>
  );
}
