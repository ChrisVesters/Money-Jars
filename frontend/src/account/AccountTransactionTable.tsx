import type { JSX } from "react";
import { useTranslation } from "react-i18next";

import TransactionRow from "@src/transaction/TransactionRow";
import "@src/transaction/transaction.css";
import { TransactionType } from "@src/transaction/transactionTypes";

import type { AccountItem } from "./accountTypes";

export type AccountTransactionTableProps = {
	account: AccountItem | undefined;
};

const AccountTransactionTable = (
	props: Readonly<AccountTransactionTableProps>
): JSX.Element => {
	const { t } = useTranslation();

	// TODO: this should actually not be possible
	// But I don't think we can enforce this?
	const transactions = (props.account?.transactions ?? []).filter(
		transaction =>
			transaction.__typename !== TransactionType.JarTransfer
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
						accountId={props.account?.id}
						// isSelected={selectedTransaction?.id === transaction.id}
						// onSelect={() => selectTransaction(transaction)}
					/>
				))}
			</tbody>
		</table>
	);
};

export default AccountTransactionTable;
