"use client";

import { useState } from "react";
import { Check, Copy } from "lucide-react";
import { cn } from "@/lib/utils";

interface CodeTab {
  label: string;
  code: string;
}

export default function CodeBlock({
  tabs,
  response,
}: {
  tabs: CodeTab[];
  response?: boolean;
}) {
  const [activeTab, setActiveTab] = useState(0);
  const [copied, setCopied] = useState(false);

  const handleCopy = async () => {
    await navigator.clipboard.writeText(tabs[activeTab].code);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="bg-[#0D0B1A] border border-white/6 rounded-xl overflow-hidden">
      {response && (
        <div className="flex items-center gap-2 px-4 py-2 bg-[#5DFDCB]/8 border-b border-white/4">
          <span className="size-2 rounded-full bg-[#5DFDCB]" />
          <span className="text-xs font-mono font-medium text-[#5DFDCB]">
            200 Success
          </span>
        </div>
      )}
      <div className="flex items-center justify-between border-b border-white/4">
        <div className="flex">
          {tabs.map((tab, i) => (
            <button
              key={tab.label}
              onClick={() => setActiveTab(i)}
              className={cn(
                "px-4 py-2.5 text-xs font-medium transition-colors",
                i === activeTab
                  ? "bg-[#5DFDCB]/10 text-[#5DFDCB]"
                  : "text-[#9B8FB8] hover:text-[#F0EDF5]"
              )}
            >
              {tab.label}
            </button>
          ))}
        </div>
        <button
          onClick={handleCopy}
          className="px-3 py-2 text-[#9B8FB8] hover:text-[#F0EDF5] transition-colors"
        >
          {copied ? (
            <Check className="size-3.5" />
          ) : (
            <Copy className="size-3.5" />
          )}
        </button>
      </div>
      <div className="p-4 overflow-x-auto">
        <pre className="font-mono text-sm leading-relaxed text-[#F0EDF5] whitespace-pre">
          {tabs[activeTab].code}
        </pre>
      </div>
    </div>
  );
}
