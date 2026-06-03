import type { JSX } from "react";
import TransactionsOverview from "../transaction/TransactionsOverview";

export default function TransactionsPage(): JSX.Element {
	return (
		<div>
			<h1>Transactions</h1>
			<TransactionsOverview />
		</div>
	);
}
