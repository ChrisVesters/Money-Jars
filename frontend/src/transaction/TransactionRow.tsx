import type { JSX } from "react";

import AccountTransferTransactionRow from "./AccountTransferTransactionRow";
import JarTransferTransactionRow from "./JarTransferTransactionRow";
import PaymentTransactionRow from "./PaymentTransactionRow";
import type { TransactionItem } from "./transactionTypes";
import { TransactionType } from "./transactionTypes";

export type TransactionRowProps = {
	transaction: TransactionItem;
	jarId?: string;
	accountId?: string;
	isSelected?: boolean;
	onSelect?: () => void;
};

const TransactionRow = (props: Readonly<TransactionRowProps>): JSX.Element => {
	const { transaction } = props;

	switch (transaction.__typename) {
		case TransactionType.Payment:
			return (
				<PaymentTransactionRow
					transaction={transaction}
					jarId={props.jarId}
					accountId={props.accountId}
					isSelected={props.isSelected}
					onSelect={props.onSelect}
				/>
			);
		case TransactionType.JarTransfer:
			return (
				<JarTransferTransactionRow
					transaction={transaction}
					jarId={props.jarId}
					isSelected={props.isSelected}
					onSelect={props.onSelect}
				/>
			);
		case TransactionType.AccountTransfer:
			return (
				<AccountTransferTransactionRow
					transaction={transaction}
					accountId={props.accountId}
					isSelected={props.isSelected}
					onSelect={props.onSelect}
				/>
			);
	}
};

export default TransactionRow;
