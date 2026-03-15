import Link from "next/link";
import { ChevronRight } from "lucide-react";

export default function Breadcrumb({
  items,
}: {
  items: Array<{ label: string; href?: string }>;
}) {
  return (
    <nav className="flex items-center gap-1.5 text-sm text-[#9B8FB8]">
      {items.map((item, i) => (
        <span key={i} className="flex items-center gap-1.5">
          {i > 0 && <ChevronRight className="size-2.5 shrink-0" />}
          {item.href && i < items.length - 1 ? (
            <Link
              href={item.href}
              className="hover:text-[#F0EDF5] transition-colors"
            >
              {item.label}
            </Link>
          ) : (
            <span className={i === items.length - 1 ? "text-[#F0EDF5]" : ""}>
              {item.label}
            </span>
          )}
        </span>
      ))}
    </nav>
  );
}
