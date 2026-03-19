"use client";

import {
	ArrowLeftRight,
	Bell,
	Database,
	Hexagon,
	Key,
	PiggyBank,
	Rocket,
	Tag,
	Target,
	User,
	Wallet,
} from "lucide-react";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { useTranslations } from "next-intl";
import CollapsibleSection from "@/components/CollapsibleSection";
import SidebarShell from "@/components/SidebarShell";
import { cn } from "@/lib/utils";

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
					: "text-[#9B8FB8] hover:bg-white/3 hover:text-[#F0EDF5]",
			)}
		>
			<Icon className="size-4" />
			<span>{label}</span>
		</Link>
	);
}

export default function DevelopSidebar({ locale, mobile }: { locale: string; mobile?: boolean }) {
	const t = useTranslations("sidebar");
	const pathname = usePathname();

	const isActive = (path: string) => pathname === path;

	const apiSections = [
		{
			icon: User,
			label: t("users"),
			basePath: `/${locale}/develop/api/users`,
			items: [
				{ method: "POST", label: "Register User", href: `/${locale}/develop/api/users` },
				{ method: "GET", label: "Get by Clerk ID", href: `/${locale}/develop/api/users` },
			],
		},
		{
			icon: Wallet,
			label: t("accounts"),
			basePath: `/${locale}/develop/api/accounts`,
			items: [
				{ method: "POST", label: "Create Account", href: `/${locale}/develop/api/accounts` },
				{ method: "GET", label: "Get Account", href: `/${locale}/develop/api/accounts` },
				{ method: "GET", label: "Get User Accounts", href: `/${locale}/develop/api/accounts` },
			],
		},
		{
			icon: ArrowLeftRight,
			label: t("transactions"),
			basePath: `/${locale}/develop/api/transactions`,
			items: [
				{
					method: "POST",
					label: "Create Transaction",
					href: `/${locale}/develop/api/transactions`,
				},
				{ method: "GET", label: "Get by ID", href: `/${locale}/develop/api/transactions` },
				{ method: "GET", label: "Get by Account", href: `/${locale}/develop/api/transactions` },
			],
		},
		{
			icon: Tag,
			label: t("categories"),
			basePath: `/${locale}/develop/api/categories`,
			items: [
				{ method: "POST", label: "Create Category", href: `/${locale}/develop/api/categories` },
				{ method: "GET", label: "System Categories", href: `/${locale}/develop/api/categories` },
				{ method: "GET", label: "User Categories", href: `/${locale}/develop/api/categories` },
			],
		},
		{
			icon: Bell,
			label: t("notifications"),
			basePath: `/${locale}/develop/api/notifications`,
			items: [
				{ method: "GET", label: "Get Notifications", href: `/${locale}/develop/api/notifications` },
				{ method: "GET", label: "Unread Count", href: `/${locale}/develop/api/notifications` },
				{ method: "PUT", label: "Mark as Read", href: `/${locale}/develop/api/notifications` },
				{ method: "PUT", label: "Mark All Read", href: `/${locale}/develop/api/notifications` },
			],
		},
		{
			icon: PiggyBank,
			label: t("budgets"),
			basePath: `/${locale}/develop/api/budgets`,
			items: [
				{ method: "POST", label: "Create Budget", href: `/${locale}/develop/api/budgets` },
				{ method: "GET", label: "Get Budget", href: `/${locale}/develop/api/budgets` },
				{ method: "GET", label: "User Budgets", href: `/${locale}/develop/api/budgets` },
				{ method: "PATCH", label: "Archive", href: `/${locale}/develop/api/budgets` },
			],
		},
		{
			icon: Target,
			label: t("goals"),
			basePath: `/${locale}/develop/api/goals`,
			items: [
				{ method: "POST", label: "Create Goal", href: `/${locale}/develop/api/goals` },
				{ method: "GET", label: "Get Goal", href: `/${locale}/develop/api/goals` },
				{ method: "GET", label: "User Goals", href: `/${locale}/develop/api/goals` },
				{ method: "PATCH", label: "Contribute", href: `/${locale}/develop/api/goals` },
			],
		},
	];

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
							href="#"
							icon={Key}
							label={t("authentication")}
							active={false}
							disabled
							badge={t("soon")}
						/>
					</div>
				</div>

				{/* API Reference */}
				<div>
					<p className="text-xs font-semibold uppercase tracking-wider text-[#9B8FB8] mb-2 px-3">
						{t("api_reference")}
					</p>
					<div className="space-y-0.5">
						{apiSections.map((section) => (
							<CollapsibleSection key={section.basePath} {...section} pathname={pathname} />
						))}
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
							href="#"
							icon={Database}
							label={t("database")}
							active={false}
							disabled
							badge={t("soon")}
						/>
					</div>
				</div>
			</div>
		</SidebarShell>
	);
}
