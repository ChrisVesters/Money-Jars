import type { JSX } from "react";
import Overview from "../jar/Overview";

export default function JarsPage(): JSX.Element {
	return (
		<div>
			<h1>Jars</h1>
			<Overview />
		</div>
	);
}
