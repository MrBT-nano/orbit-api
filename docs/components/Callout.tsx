import { cn } from "@/lib/utils";
import { Info, AlertTriangle, CheckCircle } from "lucide-react";

const styles = {
  info: {
    border: "border-[#B07AFF]",
    bg: "bg-[#B07AFF]/8",
    icon: Info,
    iconColor: "text-[#B07AFF]",
  },
  warning: {
    border: "border-[#FFB07A]",
    bg: "bg-[#FFB07A]/8",
    icon: AlertTriangle,
    iconColor: "text-[#FFB07A]",
  },
  success: {
    border: "border-[#5DFDCB]",
    bg: "bg-[#5DFDCB]/8",
    icon: CheckCircle,
    iconColor: "text-[#5DFDCB]",
  },
};

export default function Callout({
  type,
  children,
}: {
  type: "info" | "warning" | "success";
  children: React.ReactNode;
}) {
  const s = styles[type];
  const Icon = s.icon;

  return (
    <div
      className={cn(
        "rounded-xl border-l-[3px] px-5 py-4 flex gap-3",
        s.border,
        s.bg
      )}
    >
      <Icon className={cn("size-5 shrink-0 mt-0.5", s.iconColor)} />
      <div className="text-sm text-[#F0EDF5] leading-relaxed">{children}</div>
    </div>
  );
}
