"use client";

import { useEffect, useState } from "react";
import { useTranslations } from "next-intl";
import { useRouter } from "next/navigation";
import {
  Dialog,
  DialogContent,
  DialogTitle,
} from "@/components/ui/dialog";
import { Search, FileText, Code, Package } from "lucide-react";
import { cn } from "@/lib/utils";

interface SearchResult {
  name: string;
  path: string;
  href: string;
  icon: React.ReactNode;
}

const developResults: SearchResult[] = [
  {
    name: "Users API",
    path: "Develop / API Reference",
    href: "/develop/api/users",
    icon: <Code className="size-4" />,
  },
  {
    name: "Accounts API",
    path: "Develop / API Reference",
    href: "/develop/api/accounts",
    icon: <Code className="size-4" />,
  },
  {
    name: "Architecture",
    path: "Develop / Architecture",
    href: "/develop/architecture",
    icon: <FileText className="size-4" />,
  },
  {
    name: "Quick Start",
    path: "Develop / Getting Started",
    href: "/develop/getting-started",
    icon: <FileText className="size-4" />,
  },
];

const productResults: SearchResult[] = [
  {
    name: "What is Orbit?",
    path: "Product / Overview",
    href: "/product/overview",
    icon: <Package className="size-4" />,
  },
  {
    name: "Features",
    path: "Product / Features",
    href: "/product/features",
    icon: <Package className="size-4" />,
  },
];

export default function SearchDialog({ locale }: { locale: string }) {
  const t = useTranslations("nav");
  const ts = useTranslations("search");
  const router = useRouter();
  const [open, setOpen] = useState(false);
  const [query, setQuery] = useState("");
  const [activeIndex, setActiveIndex] = useState(0);

  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if ((e.metaKey || e.ctrlKey) && e.key === "k") {
        e.preventDefault();
        setOpen((prev) => !prev);
      }
    };
    document.addEventListener("keydown", handleKeyDown);
    return () => document.removeEventListener("keydown", handleKeyDown);
  }, []);

  const filterResults = (results: SearchResult[]) => {
    if (!query) return results;
    return results.filter(
      (r) =>
        r.name.toLowerCase().includes(query.toLowerCase()) ||
        r.path.toLowerCase().includes(query.toLowerCase())
    );
  };

  const filteredDevelop = filterResults(developResults);
  const filteredProduct = filterResults(productResults);
  const allResults = [...filteredDevelop, ...filteredProduct];

  const handleSelect = (href: string) => {
    router.push(`/${locale}${href}`);
    setOpen(false);
    setQuery("");
  };

  return (
    <>
      <button
        onClick={() => setOpen(true)}
        className="flex items-center gap-2 bg-[#2A1F3D]/40 border border-white/8 rounded-lg px-3 py-1.5 text-sm text-[#9B8FB8] hover:text-[#F0EDF5] transition-colors w-52 lg:w-64"
      >
        <Search className="size-3.5 shrink-0" />
        <span className="truncate">{t("search")}</span>
        <kbd className="ml-auto text-[10px] font-mono bg-white/6 px-1.5 py-0.5 rounded">
          {t("shortcut")}
        </kbd>
      </button>

      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent
          showCloseButton={false}
          className="bg-[#1A1528] border-white/8 p-0 max-w-lg gap-0"
        >
          <DialogTitle className="sr-only">{ts("title")}</DialogTitle>
          <div className="flex items-center gap-3 px-4 py-3 border-b border-white/6">
            <Search className="size-4 text-[#9B8FB8] shrink-0" />
            <input
              value={query}
              onChange={(e) => {
                setQuery(e.target.value);
                setActiveIndex(0);
              }}
              placeholder={t("search")}
              className="flex-1 bg-transparent text-sm text-[#F0EDF5] placeholder:text-[#9B8FB8] outline-none"
              autoFocus
            />
            <kbd className="text-[10px] font-mono text-[#9B8FB8] bg-white/6 px-1.5 py-0.5 rounded">
              ESC
            </kbd>
          </div>
          <div className="max-h-80 overflow-y-auto p-2">
            {filteredDevelop.length > 0 && (
              <div className="mb-2">
                <p className="text-xs font-semibold uppercase tracking-wider text-[#9B8FB8] px-3 py-2">
                  {ts("develop_section")}
                </p>
                {filteredDevelop.map((result, i) => (
                  <button
                    key={result.href}
                    onMouseEnter={() => setActiveIndex(i)}
                    onClick={() => handleSelect(result.href)}
                    className={cn(
                      "flex items-center gap-3 w-full px-3 py-2.5 rounded-lg text-left transition-colors",
                      activeIndex === i
                        ? "bg-[#5DFDCB]/6"
                        : "hover:bg-white/3"
                    )}
                  >
                    <span className="text-[#9B8FB8]">{result.icon}</span>
                    <div>
                      <div className="text-sm text-[#F0EDF5]">
                        {result.name}
                      </div>
                      <div className="text-xs text-[#9B8FB8]">
                        {result.path}
                      </div>
                    </div>
                  </button>
                ))}
              </div>
            )}
            {filteredProduct.length > 0 && (
              <div>
                <p className="text-xs font-semibold uppercase tracking-wider text-[#9B8FB8] px-3 py-2">
                  {ts("product_section")}
                </p>
                {filteredProduct.map((result, i) => {
                  const globalIndex = filteredDevelop.length + i;
                  return (
                    <button
                      key={result.href}
                      onMouseEnter={() => setActiveIndex(globalIndex)}
                      onClick={() => handleSelect(result.href)}
                      className={cn(
                        "flex items-center gap-3 w-full px-3 py-2.5 rounded-lg text-left transition-colors",
                        activeIndex === globalIndex
                          ? "bg-[#5DFDCB]/6"
                          : "hover:bg-white/3"
                      )}
                    >
                      <span className="text-[#9B8FB8]">{result.icon}</span>
                      <div>
                        <div className="text-sm text-[#F0EDF5]">
                          {result.name}
                        </div>
                        <div className="text-xs text-[#9B8FB8]">
                          {result.path}
                        </div>
                      </div>
                    </button>
                  );
                })}
              </div>
            )}
            {allResults.length === 0 && (
              <div className="text-center py-8 text-sm text-[#9B8FB8]">
                {ts("no_results")}
              </div>
            )}
          </div>
        </DialogContent>
      </Dialog>
    </>
  );
}
