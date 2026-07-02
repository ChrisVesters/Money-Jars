import type { JSX } from "react";
import { useTranslation } from "react-i18next";

import type { Account } from "@gql/graphql";

import IconDelete from "@assets/icons/IconDelete";
import IconEdit from "@assets/icons/IconEdit";

export type AccountViewProps = {
	account: Account;
	isSelected: boolean;
	onSelect: () => void;
	onEdit: (account: Account) => void;
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
					{t("balance")}: {props.account.balance}
				</div>
			</div>

			{props.isSelected && (
				<div className="card-actions">
					<button className="icon-button" onClick={handleEditClick}>
						<IconEdit size="1rem" />
					</button>
					<button
						className="icon-button danger"
						onClick={handleDeleteClick}>
						<IconDelete size="1ren" />
					</button>
				</div>
			)}
		</div>
	);
};

export default AccountView;
