import type { JSX } from "react";
import { Link, useLocation } from "react-router-dom";
import IconPanelLeftOpen from "@assets/icons/IconPanelLeftOpen";
import IconPanelLeftClose from "@assets/icons/IconPanelLeftClose";
import IconChartArea from "@assets/icons/IconChartArea";
import IconJar from "@assets/icons/IconJar";
import IconSettings from "@assets/icons/IconSettings";
import "./Sidebar.css";

interface SidebarProps {
	isOpen: boolean;
	onToggle: () => void;
}

const Sidebar = ({ isOpen, onToggle }: SidebarProps): JSX.Element => {
	const location = useLocation();

	const isActive = (path: string): boolean => location.pathname === path;

	return (
		<>
			<aside className={`sidebar ${isOpen ? "open" : "closed"}`}>
				<nav className="sidebar-nav">
					<div className="sidebar-header">
						<button
							className="toggle-btn"
							onClick={onToggle}
							aria-label="Toggle sidebar">
							{isOpen ? (
								<IconPanelLeftClose />
							) : (
								<IconPanelLeftOpen />
							)}
						</button>
						<h1>Money Jars</h1>
					</div>
					<ul className="nav-menu">
						<li className="nav-item">
							<Link
								to="/"
								className={`nav-link ${isActive("/") ? "active" : ""}`}
								title="Dashboard">
								<IconChartArea className="nav-icon" />
								<span>Dashboard</span>
							</Link>
						</li>
						<li className="nav-item">
							<Link
								to="/jars"
								className={`nav-link ${isActive("/jars") ? "active" : ""}`}
								title="Jars">
								<IconJar className="nav-icon" />
								<span>Jars</span>
							</Link>
						</li>
						<li className="nav-item">
							<Link
								to="/reports"
								className={`nav-link ${isActive("/reports") ? "active" : ""}`}
								title="Reports">
								<IconChartArea className="nav-icon" />
								<span>Reports</span>
							</Link>
						</li>
						<li className="nav-item">
							<Link
								to="/settings"
								className={`nav-link ${isActive("/settings") ? "active" : ""}`}
								title="Settings">
								<IconSettings className="nav-icon" />
								<span>Settings</span>
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
