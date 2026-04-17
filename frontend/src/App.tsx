import type { JSX } from "react";
import { useState } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import "./App.css";
import Sidebar from "./components/Sidebar";
import DashboardPage from "./pages/DashboardPage";
import JarsPage from "./pages/JarsPage";
import ReportsPage from "./pages/ReportsPage";
import SettingsPage from "./pages/SettingsPage";

function App(): JSX.Element {
	const [sidebarOpen, setSidebarOpen] = useState(true);

	const toggleSidebar = () => {
		setSidebarOpen(!sidebarOpen);
	};

	return (
		<BrowserRouter>
			<div className="app-container">
				<Sidebar isOpen={sidebarOpen} onToggle={toggleSidebar} />
				<main className={`app-main ${sidebarOpen ? "" : "collapsed"}`}>
					<Routes>
						<Route path="/" element={<DashboardPage />} />
						<Route path="/jars" element={<JarsPage />} />
						<Route path="/reports" element={<ReportsPage />} />
						<Route path="/settings" element={<SettingsPage />} />
					</Routes>
				</main>
			</div>
		</BrowserRouter>
	);
}

export default App;
