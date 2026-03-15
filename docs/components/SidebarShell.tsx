import { cn } from "@/lib/utils";

export default function SidebarShell({
  children,
  mobile,
}: {
  children: React.ReactNode;
  mobile?: boolean;
}) {
  if (mobile) {
    return (
      <div className="flex-1 overflow-y-auto p-4">
        <div className="mb-4">
          <span className="font-mono text-xs bg-[#5DFDCB]/8 border border-[#5DFDCB]/15 rounded-lg px-2.5 py-1 text-[#5DFDCB]">
            v1.0.0
          </span>
        </div>
        {children}
      </div>
    );
  }

  return (
    <aside className="hidden lg:block w-64 shrink-0 border-r border-white/4 bg-[#12101E]">
      <nav className="sticky top-14 h-[calc(100vh-3.5rem)] overflow-y-auto p-4">
        <div className="mb-4">
          <span className="font-mono text-xs bg-[#5DFDCB]/8 border border-[#5DFDCB]/15 rounded-lg px-2.5 py-1 text-[#5DFDCB]">
            v1.0.0
          </span>
        </div>
        {children}
      </nav>
    </aside>
  );
}
