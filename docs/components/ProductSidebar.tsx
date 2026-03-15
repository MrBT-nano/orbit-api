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
  disabled,
  badge,
}: {
  href: string;
  icon: React.ElementType;
  label: string;
  active: boolean;
  disabled?: boolean;
  badge?: string;
}) {
  if (disabled) {
    return (
      <div className="flex items-center gap-2.5 px-3 py-1.5 text-sm text-[#9B8FB8]/50 rounded-lg cursor-not-allowed">
        <Icon className="size-4" />
        <span>{label}</span>
        {badge && (
          <span className="ml-auto text-[10px] font-mono bg-[#2A1F3D]/50 text-[#9B8FB8] px-1.5 py-0.5 rounded">
            {badge}
          </span>
        )}
      </div>
    );
  }

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
  const st = useTranslations("sidebar");
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
              href="#"
              icon={Map}
              label={t("roadmap")}
              active={false}
              disabled
              badge={st("soon")}
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
              href="#"
              icon={TrendingUp}
              label={t("budget_tracking")}
              active={false}
              disabled
              badge={st("soon")}
            />
            <SidebarLink
              href="#"
              icon={Bitcoin}
              label={t("crypto_support")}
              active={false}
              disabled
              badge={st("soon")}
            />
            <SidebarLink
              href="#"
              icon={PieChart}
              label={t("analytics")}
              active={false}
              disabled
              badge={st("soon")}
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
              href="#"
              icon={PlayCircle}
              label={t("getting_started")}
              active={false}
              disabled
              badge={st("soon")}
            />
            <SidebarLink
              href="#"
              icon={HelpCircle}
              label={t("faq")}
              active={false}
              disabled
              badge={st("soon")}
            />
          </div>
        </div>
      </div>
    </SidebarShell>
  );
}
