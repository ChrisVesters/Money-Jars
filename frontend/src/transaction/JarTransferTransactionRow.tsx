import type { JSX } from "react";
import { useTranslation } from "react-i18next";

import IconJar from "@assets/icons/IconJar";

import BaseTransactionRow from "./BaseTransactionRow";
import type { JarTransferTransactionItem } from "./transactionTypes";
import { getTransferDirection } from "./transactionTypes";

export type JarTransferTransactionRowProps = {
	transaction: JarTransferTransactionItem;
	jarId?: string;
	isSelected?: boolean;
	onSelect?: () => void;
};

const JarTransferTransactionRow = (
	props: Readonly<JarTransferTransactionRowProps>
): JSX.Element => {
	const { t } = useTranslation();

	return (
		<BaseTransactionRow
			transaction={props.transaction}
			icon={<IconJar />}
			typeLabel={t("jarTransferTransaction")}
			party={`${props.transaction.sourceJar.name} → ${props.transaction.targetJar.name}`}
			context={t("jarTransferTransaction")}
			direction={getTransferDirection(
				props.transaction.sourceJar.id,
				props.jarId
			)}
			isSelected={props.isSelected}
			onSelect={props.onSelect}
		/>
	);
};

export default JarTransferTransactionRow;
