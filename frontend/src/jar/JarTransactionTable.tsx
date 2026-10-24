import type { JSX } from "react";
import { useTranslation } from "react-i18next";

import TransactionRow from "@src/transaction/TransactionRow";
import "@src/transaction/transaction.css";
import { TransactionType } from "@src/transaction/transactionTypes";

import type { JarItem } from "./jarTypes";

export type JarTransactionTableProps = {
	jar: JarItem | undefined;
};

const JarTransactionTable = (
	props: Readonly<JarTransactionTableProps>
): JSX.Element => {
	const { t } = useTranslation();

	// TODO: this should actually not be possible
	// But I don't think we can enforce this?
	const transactions = (props.jar?.transactions ?? []).filter(
		transaction =>
			transaction.__typename !== TransactionType.AccountTransfer
	);

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
				{transactions.map(transaction => (
					<TransactionRow
						key={transaction.id}
						transaction={transaction}
						jarId={props.jar?.id}
						// isSelected={selectedTransaction?.id === transaction.id}
						// onSelect={() => selectTransaction(transaction)}
					/>
				))}
			</tbody>
		</table>
	);
};

export default JarTransactionTable;
