"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { useTranslations } from "next-intl";
import {
  Sparkles,
  Map,
  Wallet,
  TrendingUp,
  Bitcoin,
  PieChart,
  PlayCircle,
  HelpCircle,
} from "lucide-react";
import { cn } from "@/lib/utils";
import SidebarShell from "@/components/SidebarShell";

function SidebarLink({
  href,
  icon: Icon,
  label,
  active,
}: {
  href: string;
  icon: React.ElementType;
  label: string;
  active: boolean;
}) {
  return (
    <Link
      href={href}
      className={cn(
        "flex items-center gap-2.5 px-3 py-1.5 text-sm rounded-lg transition-colors",
        active
          ? "bg-[#5DFDCB]/10 text-[#5DFDCB]"
          : "text-[#9B8FB8] hover:bg-white/3 hover:text-[#F0EDF5]"
      )}
    >
      <Icon className="size-4" />
      <span>{label}</span>
    </Link>
  );
}

export default function ProductSidebar({
  locale,
  mobile,
}: {
  locale: string;
  mobile?: boolean;
}) {
  const t = useTranslations("product_sidebar");
  const pathname = usePathname();

  const isActive = (path: string) => pathname === path;

  return (
    <SidebarShell mobile={mobile}>
      <div className="space-y-6">
        {/* Overview */}
        <div>
          <p className="text-xs font-semibold uppercase tracking-wider text-[#9B8FB8] mb-2 px-3">
            {t("overview")}
          </p>
          <div className="space-y-0.5">
            <SidebarLink
              href={`/${locale}/product/overview`}
              icon={Sparkles}
              label={t("what_is_orbit")}
              active={isActive(`/${locale}/product/overview`)}
            />
            <SidebarLink
              href={`/${locale}/product/overview`}
              icon={Map}
              label={t("roadmap")}
              active={false}
            />
          </div>
        </div>

        {/* Features */}
        <div>
          <p className="text-xs font-semibold uppercase tracking-wider text-[#9B8FB8] mb-2 px-3">
            {t("features")}
          </p>
          <div className="space-y-0.5">
            <SidebarLink
              href={`/${locale}/product/features`}
              icon={Wallet}
              label={t("account_management")}
              active={isActive(`/${locale}/product/features`)}
            />
            <SidebarLink
              href={`/${locale}/product/features`}
              icon={TrendingUp}
              label={t("budget_tracking")}
              active={false}
            />
            <SidebarLink
              href={`/${locale}/product/features`}
              icon={Bitcoin}
              label={t("crypto_support")}
              active={false}
            />
            <SidebarLink
              href={`/${locale}/product/features`}
              icon={PieChart}
              label={t("analytics")}
              active={false}
            />
          </div>
        </div>

        {/* Guides */}
        <div>
          <p className="text-xs font-semibold uppercase tracking-wider text-[#9B8FB8] mb-2 px-3">
            {t("guides")}
          </p>
          <div className="space-y-0.5">
            <SidebarLink
              href={`/${locale}/product/overview`}
              icon={PlayCircle}
              label={t("getting_started")}
              active={false}
            />
            <SidebarLink
              href={`/${locale}/product/overview`}
              icon={HelpCircle}
              label={t("faq")}
              active={false}
            />
          </div>
        </div>
      </div>
    </SidebarShell>
  );
}
