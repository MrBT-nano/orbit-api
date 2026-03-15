import { NextIntlClientProvider, hasLocale } from "next-intl";
import { getMessages, setRequestLocale } from "next-intl/server";
import { notFound } from "next/navigation";
import { locales } from "@/i18n/request";
import Navbar from "@/components/Navbar";

export function generateStaticParams() {
  return locales.map((locale) => ({ locale }));
}

export default async function LocaleLayout({
  children,
  params,
}: {
  children: React.ReactNode;
  params: Promise<{ locale: string }>;
}) {
  const { locale } = await params;

  if (!hasLocale(locales, locale)) {
    notFound();
  }

  setRequestLocale(locale);
  const messages = await getMessages();

  return (
    <html lang={locale} data-theme="dark" className="scroll-smooth">
      <body className="antialiased min-h-screen bg-[#0D0B1A] text-[#F0EDF5]">
        <NextIntlClientProvider messages={messages}>
          <div className="flex flex-col min-h-screen">
            <Navbar locale={locale} />
            {children}
          </div>
        </NextIntlClientProvider>
      </body>
    </html>
  );
}
