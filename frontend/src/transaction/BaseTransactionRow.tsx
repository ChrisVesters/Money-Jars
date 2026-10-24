import type { JSX } from "react";

import { Direction } from "@gql/graphql";

import { Money } from "@src/common/money";

import "./transaction.css";
import type { TransactionItem } from "./transactionTypes";

export type BaseTransactionRowProps = {
	transaction: Pick<TransactionItem, "date" | "amount" | "description">;
	icon: JSX.Element;
	typeLabel: string;
	party: string;
	context: string;
	direction?: Direction;
	isSelected?: boolean;
	onSelect?: () => void;
};

type AmountStyle = {
	prefix: string;
	className: string;
};

const AMOUNT_STYLES: Record<Direction, AmountStyle> = {
	[Direction.Incoming]: { prefix: "+", className: "amount-incoming" },
	[Direction.Outgoing]: { prefix: "-", className: "amount-outgoing" }
};

const BaseTransactionRow = (
	props: Readonly<BaseTransactionRowProps>
): JSX.Element => {
	const amountStyle = props.direction && AMOUNT_STYLES[props.direction];

	return (
		<tr
			className={props.isSelected ? "selected" : ""}
			onClick={props.onSelect}>
			<td className="transaction-date">{props.transaction.date}</td>
			<td className="transaction-type" title={props.typeLabel}>
				{props.icon}
			</td>
			<td>
				<div>{props.party}</div>
				<div className="transaction-context">{props.context}</div>
			</td>
			<td>{props.transaction.description}</td>
			<td
				className={`transaction-amount ${amountStyle?.className ?? ""}`}>
				{amountStyle?.prefix}
				{Money.toLabel(props.transaction.amount)}
			</td>
		</tr>
	);
};

export default BaseTransactionRow;
