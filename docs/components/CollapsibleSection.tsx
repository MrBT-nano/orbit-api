"use client";

import { ChevronDown } from "lucide-react";
import Link from "next/link";
import { useState } from "react";
import { cn } from "@/lib/utils";

interface SidebarItem {
	method: string;
	label: string;
	href: string;
}

interface CollapsibleSectionProps {
	icon: React.ElementType;
	label: string;
	basePath: string;
	items: SidebarItem[];
	pathname: string;
	defaultOpen?: boolean;
}

const METHOD_COLORS: Record<string, string> = {
	GET: "bg-[#5DFDCB]/15 text-[#5DFDCB]",
	POST: "bg-[#B07AFF]/15 text-[#B07AFF]",
	PUT: "bg-[#FFB74D]/15 text-[#FFB74D]",
};

function MethodBadge({ method }: { method: string }) {
	return (
		<span
			className={cn(
				"font-mono text-[10px] px-1.5 py-0.5 rounded font-semibold",
				METHOD_COLORS[method],
			)}
		>
			{method}
		</span>
	);
}

export default function CollapsibleSection({
	icon: Icon,
	label,
	basePath,
	items,
	pathname,
	defaultOpen = true,
}: CollapsibleSectionProps) {
	const [open, setOpen] = useState(defaultOpen);
	const active = pathname.startsWith(basePath);

	return (
		<>
			<button
				type="button"
				onClick={() => setOpen(!open)}
				className={cn(
					"flex items-center gap-2.5 w-full px-3 py-1.5 text-sm rounded-lg transition-colors",
					active
						? "bg-[#5DFDCB]/10 text-[#5DFDCB]"
						: "text-[#9B8FB8] hover:bg-white/3 hover:text-[#F0EDF5]",
				)}
			>
				<Icon className="size-4" />
				<span>{label}</span>
				<ChevronDown
					className={cn("size-3 ml-auto transition-transform", open ? "" : "-rotate-90")}
				/>
			</button>
			{open && (
				<div className="ml-7 space-y-0.5 border-l border-white/6 pl-3">
					{items.map((item) => (
						<Link
							key={`${item.method}-${item.label}`}
							href={item.href}
							className="flex items-center gap-2 px-2 py-1 text-sm text-[#9B8FB8] hover:text-[#F0EDF5] rounded transition-colors"
						>
							<MethodBadge method={item.method} />
							<span>{item.label}</span>
						</Link>
					))}
				</div>
			)}
		</>
	);
}
