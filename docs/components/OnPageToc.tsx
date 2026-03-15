"use client";

import { useEffect, useState } from "react";
import { usePathname } from "next/navigation";
import { useTranslations } from "next-intl";
import { cn } from "@/lib/utils";

interface TocItem {
  id: string;
  text: string;
  level: number;
}

export default function OnPageToc() {
  const t = useTranslations("toc");
  const pathname = usePathname();
  const [headings, setHeadings] = useState<TocItem[]>([]);
  const [activeId, setActiveId] = useState<string>("");

  useEffect(() => {
    const collectHeadings = () => {
      const elements = document.querySelectorAll("h2, h3");
      const items: TocItem[] = [];
      elements.forEach((el) => {
        if (el.id) {
          items.push({
            id: el.id,
            text: el.textContent || "",
            level: el.tagName === "H2" ? 2 : 3,
          });
        }
      });
      setHeadings(items);
      setActiveId("");

      const observer = new IntersectionObserver(
        (entries) => {
          const visible = entries.filter((e) => e.isIntersecting);
          if (visible.length > 0) {
            setActiveId(visible[0].target.id);
          }
        },
        { rootMargin: "-80px 0px -60% 0px", threshold: 0 }
      );

      elements.forEach((el) => {
        if (el.id) observer.observe(el);
      });

      return () => observer.disconnect();
    };

    // Small delay to let page content render after client-side navigation
    const timeout = setTimeout(collectHeadings, 100);
    return () => clearTimeout(timeout);
  }, [pathname]);

  if (headings.length === 0) return null;

  return (
    <nav className="hidden xl:block w-48 shrink-0 sticky top-14 h-fit max-h-[calc(100vh-3.5rem)] overflow-y-auto py-8 pr-4">
      <p className="text-xs font-semibold uppercase tracking-wider text-[#9B8FB8] mb-3">
        {t("on_this_page")}
      </p>
      <ul className="space-y-1">
        {headings.map((heading) => (
          <li key={heading.id}>
            <a
              href={`#${heading.id}`}
              className={cn(
                "block text-sm py-1 transition-colors border-l-2",
                heading.level === 3 ? "pl-6" : "pl-3",
                activeId === heading.id
                  ? "text-[#5DFDCB] border-[#5DFDCB]"
                  : "text-[#9B8FB8] border-transparent hover:text-[#F0EDF5]"
              )}
            >
              {heading.text}
            </a>
          </li>
        ))}
      </ul>
    </nav>
  );
}
