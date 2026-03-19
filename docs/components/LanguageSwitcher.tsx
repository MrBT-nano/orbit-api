"use client";

import { Globe } from "lucide-react";
import { usePathname, useRouter } from "next/navigation";

const localeLabels: Record<string, string> = {
	en: "EN",
	th: "TH",
};

export default function LanguageSwitcher({ locale }: { locale: string }) {
	const pathname = usePathname();
	const router = useRouter();

	const nextLocale = locale === "en" ? "th" : "en";

	const handleSwitch = () => {
		const newPath = pathname.replace(new RegExp(`^/${locale}(?=/|$)`), `/${nextLocale}`);
		router.push(newPath);
	};

	return (
		<button
			type="button"
			onClick={handleSwitch}
			className="flex items-center gap-1.5 bg-[#2A1F3D]/40 border border-white/8 rounded-lg px-3 py-1.5 text-sm text-[#9B8FB8] hover:text-[#F0EDF5] transition-colors"
		>
			<Globe className="size-3.5" />
			<span>{localeLabels[locale] || locale.toUpperCase()}</span>
		</button>
	);
}
