"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { useTranslations } from "next-intl";
import {
  Rocket,
  Key,
  User,
  Wallet,
  ArrowLeftRight,
  CreditCard,
  Hexagon,
  Database,
  ChevronDown,
} from "lucide-react";
import { cn } from "@/lib/utils";
import SidebarShell from "@/components/SidebarShell";
import { useState } from "react";

const methodBadge = (method: string) => {
  const colors: Record<string, string> = {
    GET: "bg-[#5DFDCB]/15 text-[#5DFDCB]",
    POST: "bg-[#B07AFF]/15 text-[#B07AFF]",
  };
  return (
    <span
      className={cn(
        "font-mono text-[10px] px-1.5 py-0.5 rounded font-semibold",
        colors[method]
      )}
    >
      {method}
    </span>
  );
};

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

export default function DevelopSidebar({
  locale,
  mobile,
}: {
  locale: string;
  mobile?: boolean;
}) {
  const t = useTranslations("sidebar");
  const pathname = usePathname();
  const [usersOpen, setUsersOpen] = useState(true);
  const [accountsOpen, setAccountsOpen] = useState(true);

  const isActive = (path: string) => pathname === path;
  const isSection = (path: string) => pathname.startsWith(path);

  return (
    <SidebarShell mobile={mobile}>
      <div className="space-y-6">
        {/* Getting Started */}
        <div>
          <p className="text-xs font-semibold uppercase tracking-wider text-[#9B8FB8] mb-2 px-3">
            {t("getting_started")}
          </p>
          <div className="space-y-0.5">
            <SidebarLink
              href={`/${locale}/develop/getting-started`}
              icon={Rocket}
              label={t("quick_start")}
              active={isActive(`/${locale}/develop/getting-started`)}
            />
            <SidebarLink
              href={`/${locale}/develop/getting-started`}
              icon={Key}
              label={t("authentication")}
              active={false}
            />
          </div>
        </div>

        {/* API Reference */}
        <div>
          <p className="text-xs font-semibold uppercase tracking-wider text-[#9B8FB8] mb-2 px-3">
            {t("api_reference")}
          </p>
          <div className="space-y-0.5">
            {/* Users - collapsible */}
            <button
              onClick={() => setUsersOpen(!usersOpen)}
              className={cn(
                "flex items-center gap-2.5 w-full px-3 py-1.5 text-sm rounded-lg transition-colors",
                isSection(`/${locale}/develop/api/users`)
                  ? "bg-[#5DFDCB]/10 text-[#5DFDCB]"
                  : "text-[#9B8FB8] hover:bg-white/3 hover:text-[#F0EDF5]"
              )}
            >
              <User className="size-4" />
              <span>{t("users")}</span>
              <ChevronDown
                className={cn(
                  "size-3 ml-auto transition-transform",
                  usersOpen ? "" : "-rotate-90"
                )}
              />
            </button>
            {usersOpen && (
              <div className="ml-7 space-y-0.5 border-l border-white/6 pl-3">
                <Link
                  href={`/${locale}/develop/api/users`}
                  className="flex items-center gap-2 px-2 py-1 text-sm text-[#9B8FB8] hover:text-[#F0EDF5] rounded transition-colors"
                >
                  {methodBadge("POST")}
                  <span>Register User</span>
                </Link>
                <Link
                  href={`/${locale}/develop/api/users`}
                  className="flex items-center gap-2 px-2 py-1 text-sm text-[#9B8FB8] hover:text-[#F0EDF5] rounded transition-colors"
                >
                  {methodBadge("GET")}
                  <span>Get by Clerk ID</span>
                </Link>
              </div>
            )}

            {/* Accounts - collapsible */}
            <button
              onClick={() => setAccountsOpen(!accountsOpen)}
              className={cn(
                "flex items-center gap-2.5 w-full px-3 py-1.5 text-sm rounded-lg transition-colors",
                isSection(`/${locale}/develop/api/accounts`)
                  ? "bg-[#5DFDCB]/10 text-[#5DFDCB]"
                  : "text-[#9B8FB8] hover:bg-white/3 hover:text-[#F0EDF5]"
              )}
            >
              <Wallet className="size-4" />
              <span>{t("accounts")}</span>
              <ChevronDown
                className={cn(
                  "size-3 ml-auto transition-transform",
                  accountsOpen ? "" : "-rotate-90"
                )}
              />
            </button>
            {accountsOpen && (
              <div className="ml-7 space-y-0.5 border-l border-white/6 pl-3">
                <Link
                  href={`/${locale}/develop/api/accounts`}
                  className="flex items-center gap-2 px-2 py-1 text-sm text-[#9B8FB8] hover:text-[#F0EDF5] rounded transition-colors"
                >
                  {methodBadge("POST")}
                  <span>Create Account</span>
                </Link>
                <Link
                  href={`/${locale}/develop/api/accounts`}
                  className="flex items-center gap-2 px-2 py-1 text-sm text-[#9B8FB8] hover:text-[#F0EDF5] rounded transition-colors"
                >
                  {methodBadge("GET")}
                  <span>Get Account</span>
                </Link>
                <Link
                  href={`/${locale}/develop/api/accounts`}
                  className="flex items-center gap-2 px-2 py-1 text-sm text-[#9B8FB8] hover:text-[#F0EDF5] rounded transition-colors"
                >
                  {methodBadge("GET")}
                  <span>Get User Accounts</span>
                </Link>
              </div>
            )}

            {/* Disabled items */}
            <SidebarLink
              href="#"
              icon={ArrowLeftRight}
              label={t("transactions")}
              active={false}
              disabled
              badge={t("soon")}
            />
            <SidebarLink
              href="#"
              icon={CreditCard}
              label={t("payments")}
              active={false}
              disabled
              badge={t("soon")}
            />
          </div>
        </div>

        {/* Architecture */}
        <div>
          <p className="text-xs font-semibold uppercase tracking-wider text-[#9B8FB8] mb-2 px-3">
            {t("architecture")}
          </p>
          <div className="space-y-0.5">
            <SidebarLink
              href={`/${locale}/develop/architecture`}
              icon={Hexagon}
              label={t("system_design")}
              active={isActive(`/${locale}/develop/architecture`)}
            />
            <SidebarLink
              href={`/${locale}/develop/architecture`}
              icon={Database}
              label={t("database")}
              active={false}
            />
          </div>
        </div>
      </div>
    </SidebarShell>
  );
}
