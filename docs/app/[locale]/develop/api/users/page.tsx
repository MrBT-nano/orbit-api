import { setRequestLocale } from "next-intl/server";
import UsersAPIContent from "@/components/pages/UsersAPIPage";

export default async function UsersAPI({
  params,
}: {
  params: Promise<{ locale: string }>;
}) {
  const { locale } = await params;
  setRequestLocale(locale);
  return <UsersAPIContent />;
}
