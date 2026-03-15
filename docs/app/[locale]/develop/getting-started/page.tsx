import { setRequestLocale } from "next-intl/server";
import GettingStartedContent from "@/components/pages/GettingStartedPage";

export default async function GettingStarted({
  params,
}: {
  params: Promise<{ locale: string }>;
}) {
  const { locale } = await params;
  setRequestLocale(locale);
  return <GettingStartedContent />;
}
