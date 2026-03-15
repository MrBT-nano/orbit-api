import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "Orbit Documentation",
  description: "Official documentation for the Orbit Personal Finance Management System",
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return children;
}
