import type { JSX } from "react";
import { useTranslation } from "react-i18next";

import { Direction } from "@gql/graphql";

import IconIncoming from "@assets/icons/IconIncoming";
import IconOutgoing from "@assets/icons/IconOutgoing";

import BaseTransactionRow from "./BaseTransactionRow";
import type { PaymentTransactionItem } from "./transactionTypes";

export type PaymentTransactionRowProps = {
	transaction: PaymentTransactionItem;
	jarId?: string;
	accountId?: string;
	isSelected?: boolean;
	onSelect?: () => void;
};

const PaymentTransactionRow = (
	props: Readonly<PaymentTransactionRowProps>
): JSX.Element => {
	const { t } = useTranslation();

	const isOutgoing = props.transaction.direction === Direction.Outgoing;
	const context = [
		props.transaction.account.id === props.accountId
			? undefined
			: props.transaction.account.name,
		props.transaction.jar.id === props.jarId
			? undefined
			: props.transaction.jar.name
	]
		.filter(name => name !== undefined)
		.join(" · ");

	return (
		<BaseTransactionRow
			transaction={props.transaction}
			icon={isOutgoing ? <IconOutgoing /> : <IconIncoming />}
			typeLabel={t("paymentTransaction")}
			party={props.transaction.counterparty}
			context={context}
			direction={props.transaction.direction}
			isSelected={props.isSelected}
			onSelect={props.onSelect}
		/>
	);
};

export default PaymentTransactionRow;
