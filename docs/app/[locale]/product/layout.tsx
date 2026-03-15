import { setRequestLocale } from "next-intl/server";
import ProductSidebar from "@/components/ProductSidebar";
import OnPageToc from "@/components/OnPageToc";

export default async function ProductLayout({
  children,
  params,
}: {
  children: React.ReactNode;
  params: Promise<{ locale: string }>;
}) {
  const { locale } = await params;
  setRequestLocale(locale);

  return (
    <div className="flex flex-1 relative">
      <ProductSidebar locale={locale} />
      <main className="flex-1 min-w-0 px-4 sm:px-6 lg:px-10 py-8 max-w-4xl">
        {children}
      </main>
      <OnPageToc />
    </div>
  );
}
