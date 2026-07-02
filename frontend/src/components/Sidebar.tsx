import { useState } from "react";
import type { JSX } from "react";
import { useTranslation } from "react-i18next";
import { Link, useLocation } from "react-router-dom";

import IconAccount from "@assets/icons/IconAccount";
import IconChartArea from "@assets/icons/IconChartArea";
import IconJar from "@assets/icons/IconJar";
import IconPanelLeftClose from "@assets/icons/IconPanelLeftClose";
import IconPanelLeftOpen from "@assets/icons/IconPanelLeftOpen";
import IconSettings from "@assets/icons/IconSettings";
import IconTransaction from "@assets/icons/IconTransaction";

import "./Sidebar.css";

const Sidebar = (): JSX.Element => {
	const { t } = useTranslation();
	const location = useLocation();

	const [isOpen, setIsOpen] = useState(true);

	const onToggle = () => {
		setIsOpen(prev => !prev);
	};

	const isActive = (path: string): boolean => location.pathname === path;

	return (
		<>
			<aside className={`sidebar ${isOpen ? "open" : "closed"}`}>
				<nav className="sidebar-nav">
					<div className="sidebar-header">
						<button
							className="toggle-btn"
							onClick={onToggle}
							aria-label={t("toggleSidebar")}>
							{isOpen ? (
								<IconPanelLeftClose />
							) : (
								<IconPanelLeftOpen />
							)}
						</button>
						<h1>{t("appTitle")}</h1>
					</div>
					<ul className="nav-menu">
						<li className="nav-item">
							<Link
								to="/"
								className={`nav-link ${isActive("/") ? "active" : ""}`}
								title={t("dashboard")}>
								<IconChartArea className="nav-icon" />
								<span>{t("dashboard")}</span>
							</Link>
						</li>
						<li className="nav-item">
							<Link
								to="/accounts"
								className={`nav-link ${isActive("/accounts") ? "active" : ""}`}
								title={t("accounts")}>
								<IconAccount className="nav-icon" />
								<span>{t("accounts")}</span>
							</Link>
						</li>
						<li className="nav-item">
							<Link
								to="/jars"
								className={`nav-link ${isActive("/jars") ? "active" : ""}`}
								title={t("jars")}>
								<IconJar className="nav-icon" />
								<span>{t("jars")}</span>
							</Link>
						</li>
						<li className="nav-item">
							<Link
								to="/transactions"
								className={`nav-link ${isActive("/transactions") ? "active" : ""}`}
								title={t("transactions")}>
								<IconTransaction className="nav-icon" />
								<span>{t("transactions")}</span>
							</Link>
						</li>
						<li className="nav-item">
							<Link
								to="/reports"
								className={`nav-link ${isActive("/reports") ? "active" : ""}`}
								title={t("reports")}>
								<IconChartArea className="nav-icon" />
								<span>{t("reports")}</span>
							</Link>
						</li>
						<li className="nav-item">
							<Link
								to="/settings"
								className={`nav-link ${isActive("/settings") ? "active" : ""}`}
								title={t("settings")}>
								<IconSettings className="nav-icon" />
								<span>{t("settings")}</span>
							</Link>
						</li>
					</ul>
				</nav>
			</aside>
			<style>{`
				:root {
					--sidebar-width: ${isOpen ? "250px" : "80px"};
				}
			`}</style>
		</>
	);
};

export default Sidebar;
