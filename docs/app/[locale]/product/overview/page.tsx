import { setRequestLocale } from "next-intl/server";
import OverviewContent from "@/components/pages/OverviewPage";

export default async function Overview({
  params,
}: {
  params: Promise<{ locale: string }>;
}) {
  const { locale } = await params;
  setRequestLocale(locale);
  return <OverviewContent />;
}
