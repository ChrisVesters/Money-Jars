import type { JSX } from "react";
import { useState } from "react";

import "./App.css";
import Sidebar from "./components/Sidebar";
import Overview from "./jar/Overview";

function App(): JSX.Element {
	const [sidebarOpen, setSidebarOpen] = useState(true);

	const toggleSidebar = () => {
		setSidebarOpen(!sidebarOpen);
	};

	return (
		<div className="app-container">
			<Sidebar isOpen={sidebarOpen} onToggle={toggleSidebar} />
			<main className={`app-main ${sidebarOpen ? "" : "collapsed"}`}>
				<Overview />
			</main>
		</div>
	);
}

export default App;
