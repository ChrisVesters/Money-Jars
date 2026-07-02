import type { JSX } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import "./App.css";
import Sidebar from "./components/Sidebar";
import AccountsPage from "./pages/AccountsPage";
import DashboardPage from "./pages/DashboardPage";
import JarsPage from "./pages/JarsPage";
import ReportsPage from "./pages/ReportsPage";
import SettingsPage from "./pages/SettingsPage";
import TransactionsPage from "./pages/TransactionsPage";

const App = (): JSX.Element => {
	return (
		<BrowserRouter>
			<div className="app-container">
				<Sidebar />
				<main className="app-main">
					<Routes>
						<Route path="/" element={<DashboardPage />} />
						<Route path="/accounts" element={<AccountsPage />} />
						<Route path="/jars" element={<JarsPage />} />
						<Route
							path="/transactions"
							element={<TransactionsPage />}
						/>
						<Route path="/reports" element={<ReportsPage />} />
						<Route path="/settings" element={<SettingsPage />} />
					</Routes>
				</main>
			</div>
		</BrowserRouter>
	);
};

export default App;
