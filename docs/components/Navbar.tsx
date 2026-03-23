"use client";

import { Menu } from "lucide-react";
import Image from "next/image";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { useTranslations } from "next-intl";
import DevelopSidebar from "@/components/DevelopSidebar";
import LanguageSwitcher from "@/components/LanguageSwitcher";
import ProductSidebar from "@/components/ProductSidebar";
import SearchDialog from "@/components/SearchDialog";
import { Sheet, SheetContent, SheetTitle, SheetTrigger } from "@/components/ui/sheet";
import { basePath } from "@/lib/basePath";
import { cn } from "@/lib/utils";

export default function Navbar({ locale }: { locale: string }) {
	const t = useTranslations("nav");
	const pathname = usePathname();

	const isDevelop = pathname.includes("/develop");
	const isProduct = pathname.includes("/product");

	return (
		<header className="sticky top-0 z-50 h-14 bg-[#1A1528]/95 backdrop-blur-xl border-b border-white/5 flex items-center justify-between px-4 lg:px-6">
			{/* Left */}
			<div className="flex items-center gap-6">
				<Link href={`/${locale}`} className="flex items-center gap-2.5">
					<Image
						src={`${basePath}/orbit-logo.svg`}
						alt="Orbit"
						width={32}
						height={32}
						className="size-8"
					/>
					<span className="font-bold text-[#F0EDF5]">Orbit</span>
				</Link>

				{/* Section Switcher */}
				<div className="hidden sm:flex gap-1 bg-[#2A1F3D]/50 rounded-xl p-1">
					<Link
						href={`/${locale}/develop/getting-started`}
						className={cn(
							"px-4 py-1.5 rounded-lg text-[13px] font-medium transition-colors",
							isDevelop ? "bg-[#5DFDCB]/12 text-[#5DFDCB]" : "text-[#9B8FB8] hover:text-[#F0EDF5]",
						)}
					>
						{t("develop")}
					</Link>
					<Link
						href={`/${locale}/product/overview`}
						className={cn(
							"px-4 py-1.5 rounded-lg text-[13px] font-medium transition-colors",
							isProduct ? "bg-[#5DFDCB]/12 text-[#5DFDCB]" : "text-[#9B8FB8] hover:text-[#F0EDF5]",
						)}
					>
						{t("product")}
					</Link>
				</div>
			</div>

			{/* Right */}
			<div className="flex items-center gap-3">
				<div className="hidden sm:block">
					<SearchDialog locale={locale} />
				</div>

				<LanguageSwitcher locale={locale} />

				<a
					href="https://github.com/cntm-labs/orbit-api"
					target="_blank"
					rel="noopener noreferrer"
					className="text-[#9B8FB8] hover:text-[#F0EDF5] transition-colors"
				>
					<svg
						className="size-5"
						viewBox="0 0 24 24"
						fill="currentColor"
						role="img"
						aria-label="GitHub"
					>
						<title>GitHub</title>
						<path d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z" />
					</svg>
				</a>

				{/* Mobile menu */}
				<Sheet>
					<SheetTrigger className="lg:hidden text-[#9B8FB8] hover:text-[#F0EDF5]">
						<Menu className="size-5" />
					</SheetTrigger>
					<SheetContent side="left" className="bg-[#12101E] border-white/6 p-0 w-72">
						<SheetTitle className="sr-only">Navigation</SheetTitle>
						<div className="p-4 border-b border-white/6">
							<div className="flex gap-1 bg-[#2A1F3D]/50 rounded-xl p-1">
								<Link
									href={`/${locale}/develop/getting-started`}
									className={cn(
										"flex-1 text-center px-3 py-1.5 rounded-lg text-[13px] font-medium transition-colors",
										isDevelop ? "bg-[#5DFDCB]/12 text-[#5DFDCB]" : "text-[#9B8FB8]",
									)}
								>
									{t("develop")}
								</Link>
								<Link
									href={`/${locale}/product/overview`}
									className={cn(
										"flex-1 text-center px-3 py-1.5 rounded-lg text-[13px] font-medium transition-colors",
										isProduct ? "bg-[#5DFDCB]/12 text-[#5DFDCB]" : "text-[#9B8FB8]",
									)}
								>
									{t("product")}
								</Link>
							</div>
						</div>
						{isDevelop ? (
							<DevelopSidebar locale={locale} mobile />
						) : (
							<ProductSidebar locale={locale} mobile />
						)}
					</SheetContent>
				</Sheet>
			</div>
		</header>
	);
}
