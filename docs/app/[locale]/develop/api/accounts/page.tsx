import { setRequestLocale } from "next-intl/server";
import AccountsAPIContent from "@/components/pages/AccountsAPIPage";

export default async function AccountsAPI({
  params,
}: {
  params: Promise<{ locale: string }>;
}) {
  const { locale } = await params;
  setRequestLocale(locale);
  return <AccountsAPIContent />;
}
