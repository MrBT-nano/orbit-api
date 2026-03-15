import type { Metadata } from "next";
import "./globals.css";
import { basePath } from "@/lib/basePath";

export const metadata: Metadata = {
  title: "Orbit Documentation",
  description: "Official documentation for the Orbit Personal Finance Management System",
  icons: {
    icon: [
      { url: `${basePath}/favicon.webp`, type: "image/webp" },
    ],
    apple: { url: `${basePath}/favicon.webp` },
  },
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return children;
}
