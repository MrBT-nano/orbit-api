import Link from "next/link";
import { ArrowLeft, ArrowRight } from "lucide-react";

export default function PageNav({
  prev,
  next,
}: {
  prev?: { label: string; href: string };
  next?: { label: string; href: string };
}) {
  return (
    <div className="grid grid-cols-2 gap-4 mt-12">
      {prev ? (
        <Link
          href={prev.href}
          className="group border border-white/6 rounded-xl p-4 hover:border-white/12 transition-colors"
        >
          <div className="flex items-center gap-1.5 text-xs text-[#9B8FB8] mb-1">
            <ArrowLeft className="size-3" />
            <span>Previous</span>
          </div>
          <div className="text-sm text-[#F0EDF5] group-hover:text-[#F0EDF5] font-medium">
            {prev.label}
          </div>
        </Link>
      ) : (
        <div />
      )}
      {next ? (
        <Link
          href={next.href}
          className="group border border-[#5DFDCB]/15 rounded-xl p-4 hover:border-[#5DFDCB]/30 transition-colors text-right"
        >
          <div className="flex items-center justify-end gap-1.5 text-xs text-[#9B8FB8] mb-1">
            <span>Next</span>
            <ArrowRight className="size-3" />
          </div>
          <div className="text-sm text-[#5DFDCB] font-medium">
            {next.label}
          </div>
        </Link>
      ) : (
        <div />
      )}
    </div>
  );
}
