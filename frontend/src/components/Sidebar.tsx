import type { JSX } from "react";
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
							<a href="#" className="nav-link active" title="Dashboard">
								<IconChartArea className="nav-icon" />
								<span>Dashboard</span>
							</a>
						</li>
						<li className="nav-item">
							<a href="#" className="nav-link" title="Jars">
								<IconJar className="nav-icon" />
								<span>Jars</span>
							</a>
						</li>
						<li className="nav-item">
							<a href="#" className="nav-link" title="Reports">
								<IconChartArea className="nav-icon" />
								<span>Reports</span>
							</a>
						</li>
						<li className="nav-item">
							<a href="#" className="nav-link" title="Settings">
								<IconSettings className="nav-icon" />
								<span>Settings</span>
							</a>
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
