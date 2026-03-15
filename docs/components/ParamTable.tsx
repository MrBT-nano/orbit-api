import { cn } from "@/lib/utils";

interface Param {
  name: string;
  type: string;
  required: boolean;
  description: string;
  enumValues?: string[];
}

export default function ParamTable({ params }: { params: Param[] }) {
  return (
    <div className="overflow-x-auto">
      <table className="w-full text-sm">
        <thead>
          <tr className="border-b border-white/6 text-left">
            <th className="pb-3 pr-4 text-xs font-semibold uppercase tracking-wider text-[#9B8FB8]">
              Field
            </th>
            <th className="pb-3 pr-4 text-xs font-semibold uppercase tracking-wider text-[#9B8FB8]">
              Type
            </th>
            <th className="pb-3 pr-4 text-xs font-semibold uppercase tracking-wider text-[#9B8FB8]">
              Required
            </th>
            <th className="pb-3 text-xs font-semibold uppercase tracking-wider text-[#9B8FB8]">
              Description
            </th>
          </tr>
        </thead>
        <tbody>
          {params.map((param) => (
            <tr key={param.name} className="border-b border-white/4">
              <td className="py-3 pr-4">
                <code className="font-mono text-[#F0EDF5]">{param.name}</code>
              </td>
              <td className="py-3 pr-4">
                <code className="font-mono text-[#B07AFF]">{param.type}</code>
              </td>
              <td className="py-3 pr-4">
                {param.required ? (
                  <span className="text-[#FF6B9D] bg-[#FF6B9D]/10 text-xs px-2 py-0.5 rounded font-medium">
                    required
                  </span>
                ) : (
                  <span className="text-[#9B8FB8] text-xs">optional</span>
                )}
              </td>
              <td className="py-3 text-[#9B8FB8]">
                <div>{param.description}</div>
                {param.enumValues && param.enumValues.length > 0 && (
                  <div className="flex flex-wrap gap-1.5 mt-2">
                    {param.enumValues.map((v) => (
                      <span
                        key={v}
                        className="font-mono text-xs bg-[#2A1F3D]/60 text-[#B07AFF] px-2 py-0.5 rounded border border-white/6"
                      >
                        {v}
                      </span>
                    ))}
                  </div>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
