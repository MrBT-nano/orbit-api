import { setRequestLocale } from "next-intl/server";
import SubscriptionsAPIContent from "@/components/pages/SubscriptionsAPIPage";

export default async function SubscriptionsAPI({
	params,
}: {
	params: Promise<{ locale: string }>;
}) {
	const { locale } = await params;
	setRequestLocale(locale);
	return <SubscriptionsAPIContent />;
}
