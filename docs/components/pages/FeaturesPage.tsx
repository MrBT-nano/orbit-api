"use client";

import { useLocale } from "next-intl";
import {
  Landmark,
  CreditCard,
  Bitcoin,
  Banknote,
  TrendingUp,
  Globe,
  ShieldCheck,
  KeyRound,
  ScrollText,
} from "lucide-react";
import Breadcrumb from "@/components/Breadcrumb";
import PageNav from "@/components/PageNav";

export default function FeaturesPage() {
  const locale = useLocale();

  const accountTypes = [
    {
      icon: Landmark,
      color: "#5DFDCB",
      type: "BANK",
      title: "Bank Accounts",
      description:
        "Checking and savings accounts with automatic balance tracking and Plaid integration support.",
    },
    {
      icon: CreditCard,
      color: "#B07AFF",
      type: "CREDIT",
      title: "Credit Cards",
      description:
        "Track credit card balances, spending limits, and payment due dates across multiple cards.",
    },
    {
      icon: Bitcoin,
      color: "#FFB07A",
      type: "CRYPTO",
      title: "Crypto Wallets",
      description:
        "Monitor cryptocurrency holdings with support for BTC, ETH, and other digital assets.",
    },
    {
      icon: Banknote,
      color: "#FF6B9D",
      type: "CASH",
      title: "Cash Accounts",
      description:
        "Track physical cash and petty cash funds for complete financial visibility.",
    },
    {
      icon: TrendingUp,
      color: "#5DFDCB",
      type: "INVESTMENT",
      title: "Investment Portfolios",
      description:
        "Monitor stocks, bonds, mutual funds, and other investment vehicles in one place.",
    },
  ];

  const currencies = [
    { code: "THB", name: "Thai Baht", symbol: "฿", color: "#5DFDCB" },
    { code: "USD", name: "US Dollar", symbol: "$", color: "#B07AFF" },
    { code: "EUR", name: "Euro", symbol: "€", color: "#FFB07A" },
    { code: "BTC", name: "Bitcoin", symbol: "₿", color: "#FF6B9D" },
    { code: "ETH", name: "Ethereum", symbol: "Ξ", color: "#5DFDCB" },
  ];

  const securityFeatures = [
    {
      icon: KeyRound,
      color: "#5DFDCB",
      title: "Clerk SSO",
      description:
        "Enterprise single sign-on with support for Google, GitHub, email/password, and more via Clerk authentication.",
    },
    {
      icon: ShieldCheck,
      color: "#B07AFF",
      title: "Spring Security",
      description:
        "Industry-standard security framework with CORS, CSRF protection, and role-based access control.",
    },
    {
      icon: ScrollText,
      color: "#FFB07A",
      title: "Audit Trail",
      description:
        "Complete audit logging of all financial operations for compliance and debugging purposes.",
    },
  ];

  return (
    <div className="space-y-8">
      <Breadcrumb
        items={[
          { label: "Product", href: `/${locale}/product/overview` },
          { label: "Features" },
        ]}
      />

      <div>
        <h1 className="text-3xl font-bold text-[#F0EDF5] mb-2">Features</h1>
        <p className="text-[#9B8FB8] leading-relaxed">
          A comprehensive look at what Orbit offers for personal finance
          management.
        </p>
      </div>

      {/* Account Types */}
      <section>
        <h2
          id="account-types"
          className="text-xl font-semibold text-[#F0EDF5] mb-4"
        >
          Account Types
        </h2>
        <p className="text-sm text-[#9B8FB8] mb-4">
          Orbit supports five account types to cover every aspect of your
          financial life.
        </p>
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          {accountTypes.map((account) => (
            <div
              key={account.type}
              className="bg-[#2A1F3D]/30 border border-white/6 rounded-2xl p-5"
            >
              <div className="flex items-center gap-3 mb-3">
                <div
                  className="size-10 rounded-xl flex items-center justify-center"
                  style={{ backgroundColor: `${account.color}15` }}
                >
                  <account.icon
                    className="size-5"
                    style={{ color: account.color }}
                  />
                </div>
                <div>
                  <h3 className="text-[#F0EDF5] font-semibold">
                    {account.title}
                  </h3>
                  <code className="text-xs text-[#B07AFF] font-mono">
                    {account.type}
                  </code>
                </div>
              </div>
              <p className="text-sm text-[#9B8FB8] leading-relaxed">
                {account.description}
              </p>
            </div>
          ))}
        </div>
      </section>

      {/* Multi-Currency */}
      <section>
        <h2
          id="multi-currency"
          className="text-xl font-semibold text-[#F0EDF5] mb-4"
        >
          Multi-Currency Support
        </h2>
        <p className="text-sm text-[#9B8FB8] mb-4">
          Track assets in any fiat currency or cryptocurrency. Orbit supports
          automatic conversion with live exchange rates.
        </p>
        <div className="flex flex-wrap gap-3">
          {currencies.map((currency) => (
            <div
              key={currency.code}
              className="bg-[#2A1F3D]/30 border border-white/6 rounded-xl px-4 py-3 flex items-center gap-3"
            >
              <span
                className="text-lg font-bold"
                style={{ color: currency.color }}
              >
                {currency.symbol}
              </span>
              <div>
                <span className="text-sm font-semibold text-[#F0EDF5]">
                  {currency.code}
                </span>
                <p className="text-xs text-[#9B8FB8]">{currency.name}</p>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* Security */}
      <section>
        <h2
          id="security"
          className="text-xl font-semibold text-[#F0EDF5] mb-4"
        >
          Security
        </h2>
        <p className="text-sm text-[#9B8FB8] mb-4">
          Enterprise-grade security baked into every layer of the stack.
        </p>
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
          {securityFeatures.map((feature) => (
            <div
              key={feature.title}
              className="bg-[#2A1F3D]/30 border border-white/6 rounded-2xl p-5"
            >
              <div
                className="size-10 rounded-xl flex items-center justify-center mb-3"
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
            </div>
          ))}
        </div>
      </section>

      <PageNav
        prev={{
          label: "What is Orbit?",
          href: `/${locale}/product/overview`,
        }}
      />
    </div>
  );
}
