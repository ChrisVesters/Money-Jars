import type { JSX } from "react";
import { useTranslation } from "react-i18next";

import IconAccount from "@assets/icons/IconAccount";

import BaseTransactionRow from "../BaseTransactionRow";
import type { AccountTransferTransactionItem } from "../transactionTypes";
import { getTransferDirection } from "../transactionTypes";

export type AccountTransferTransactionRowProps = {
	transaction: AccountTransferTransactionItem;
	accountId?: string;
	isSelected?: boolean;
	onSelect?: () => void;
};

const AccountTransferTransactionRow = (
	props: Readonly<AccountTransferTransactionRowProps>
): JSX.Element => {
	const { t } = useTranslation();

	return (
		<BaseTransactionRow
			transaction={props.transaction}
			icon={<IconAccount />}
			typeLabel={t("accountTransferTransaction")}
			party={`${props.transaction.sourceAccount.name} → ${props.transaction.targetAccount.name}`}
			context={t("accountTransferTransaction")}
			direction={getTransferDirection(
				props.transaction.sourceAccount.id,
				props.accountId
			)}
			isSelected={props.isSelected}
			onSelect={props.onSelect}
		/>
	);
};

export default AccountTransferTransactionRow;
