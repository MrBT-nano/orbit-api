"use client";

import { useState } from "react";
import { ChevronDown, ChevronRight } from "lucide-react";
import MethodBadge from "@/components/MethodBadge";
import { cn } from "@/lib/utils";

export default function ApiEndpoint({
  method,
  path,
  description,
  children,
}: {
  method: string;
  path: string;
  description?: string;
  children?: React.ReactNode;
}) {
  const [open, setOpen] = useState(true);

  return (
    <div className="bg-[#12101E] border border-white/6 rounded-2xl overflow-hidden">
      <button
        onClick={() => setOpen(!open)}
        className="w-full flex items-center gap-3 px-5 py-4 hover:bg-white/2 transition-colors"
      >
        <MethodBadge method={method} />
        <code className="font-mono text-sm text-[#F0EDF5]">{path}</code>
        {description && (
          <span className="text-sm text-[#9B8FB8] ml-2 hidden sm:inline">
            {description}
          </span>
        )}
        <span className="ml-auto">
          {open ? (
            <ChevronDown className="size-4 text-[#9B8FB8]" />
          ) : (
            <ChevronRight className="size-4 text-[#9B8FB8]" />
          )}
        </span>
      </button>
      {open && children && (
        <div className="border-t border-white/4 px-5 py-5">{children}</div>
      )}
    </div>
  );
}
