import { setRequestLocale } from "next-intl/server";
import FeaturesContent from "@/components/pages/FeaturesPage";

export default async function Features({
  params,
}: {
  params: Promise<{ locale: string }>;
}) {
  const { locale } = await params;
  setRequestLocale(locale);
  return <FeaturesContent />;
}
