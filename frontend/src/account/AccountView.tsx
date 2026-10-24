import type { JSX } from "react";
import { useTranslation } from "react-i18next";

import IconDelete from "@assets/icons/IconDelete";
import IconEdit from "@assets/icons/IconEdit";

import { Money } from "@src/common/money";

import type { AccountItem } from "./accountTypes";

export type AccountViewProps = {
	account: AccountItem;
	isSelected: boolean;
	onSelect: () => void;
	onEdit: (account: AccountItem) => void;
	onDelete: (accountId: string) => void;
};

const AccountView = (props: Readonly<AccountViewProps>): JSX.Element => {
	const { t } = useTranslation();

	const handleEditClick = (
		event: React.MouseEvent<HTMLButtonElement>
	): void => {
		event.stopPropagation();

		props.onEdit(props.account);
	};

	const handleDeleteClick = (
		event: React.MouseEvent<HTMLButtonElement>
	): void => {
		event.stopPropagation();

		props.onDelete(props.account.id);
	};

	return (
		<div
			className={`jar-card ${props.isSelected ? "selected" : ""}`}
			onClick={props.onSelect}>
			<div className="card-content">
				<div className="card-title">{props.account.name}</div>
				<div className="card-description">
					{props.account.description}
				</div>
				<div className="card-footer">
					{t("balance")}: {Money.toLabel(props.account.balance)}
				</div>
			</div>

			{props.isSelected && (
				<div className="card-actions">
					<button className="icon-button" onClick={handleEditClick}>
						<IconEdit size="1rem" />
					</button>
					{props.account.transactions.length === 0 && (
						<button
							className="icon-button danger"
							onClick={handleDeleteClick}>
							<IconDelete size="1rem" />
						</button>
					)}
				</div>
			)}
		</div>
	);
};

export default AccountView;
