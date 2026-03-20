import { setRequestLocale } from "next-intl/server";
import PaymentMethodsAPIContent from "@/components/pages/PaymentMethodsAPIPage";

export default async function PaymentMethodsAPI({
	params,
}: {
	params: Promise<{ locale: string }>;
}) {
	const { locale } = await params;
	setRequestLocale(locale);
	return <PaymentMethodsAPIContent />;
}
