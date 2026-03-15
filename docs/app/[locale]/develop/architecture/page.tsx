import { setRequestLocale } from "next-intl/server";
import ArchitectureContent from "@/components/pages/ArchitecturePage";

export default async function Architecture({
  params,
}: {
  params: Promise<{ locale: string }>;
}) {
  const { locale } = await params;
  setRequestLocale(locale);
  return <ArchitectureContent />;
}
