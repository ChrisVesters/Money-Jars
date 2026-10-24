import type { JSX } from "react";
import { useTranslation } from "react-i18next";

import TransactionRow from "./TransactionRow";
import "./transaction.css";
import type { TransactionItem } from "./transactionTypes";

export type TransactionTableProps = {
	transactions: TransactionItem[];
	selectedTransaction: TransactionItem | undefined;
	onSelect: (transaction: TransactionItem) => void;
};

const TransactionTable = (
	props: Readonly<TransactionTableProps>
): JSX.Element => {
	const { t } = useTranslation();

	return (
		<table className="transactions-table">
			<thead>
				<tr>
					<th className="transaction-date">{t("date")}</th>
					<th className="transaction-type">
						{t("transactionType")}
					</th>
					<th>{t("fromTo")}</th>
					<th>{t("description")}</th>
					<th className="transaction-amount">{t("amount")}</th>
				</tr>
			</thead>
			<tbody>
				{props.transactions.map(transaction => (
					<TransactionRow
						key={transaction.id}
						transaction={transaction}
						isSelected={
							props.selectedTransaction?.id === transaction.id
						}
						onSelect={() => props.onSelect(transaction)}
					/>
				))}
			</tbody>
		</table>
	);
};

export default TransactionTable;
