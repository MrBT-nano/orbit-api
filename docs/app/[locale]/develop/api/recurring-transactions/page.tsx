import { setRequestLocale } from "next-intl/server";
import RecurringTransactionsAPIContent from "@/components/pages/RecurringTransactionsAPIPage";

export default async function RecurringTransactionsAPI({
	params,
}: {
	params: Promise<{ locale: string }>;
}) {
	const { locale } = await params;
	setRequestLocale(locale);
	return <RecurringTransactionsAPIContent />;
}
