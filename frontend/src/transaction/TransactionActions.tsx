import type { JSX } from "react";
import { useTranslation } from "react-i18next";

import IconDelete from "@assets/icons/IconDelete";
import IconEdit from "@assets/icons/IconEdit";
import IconPlus from "@assets/icons/IconPlus";

import "./transaction.css";

export type TransactionActionsProps = {
	hasSelection: boolean;
	onCreate: () => void;
	onEdit: () => void;
	onDelete: () => void;
};

const TransactionActions = (
	props: Readonly<TransactionActionsProps>
): JSX.Element => {
	const { t } = useTranslation();

	return (
		<div className="transactions-actions">
			{props.hasSelection ? (
				<>
					<button
						className="icon-button"
						onClick={props.onEdit}
						title={t("editTransaction")}>
						<IconEdit />
					</button>
					<button
						className="icon-button"
						onClick={props.onDelete}
						title={t("deleteTransaction")}>
						<IconDelete />
					</button>
				</>
			) : (
				<button
					className="icon-button"
					onClick={props.onCreate}
					title={t("createTransaction")}>
					<IconPlus />
				</button>
			)}
		</div>
	);
};

export default TransactionActions;
