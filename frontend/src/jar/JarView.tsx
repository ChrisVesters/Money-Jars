import type { JSX } from "react";
import { useTranslation } from "react-i18next";

import IconDelete from "@assets/icons/IconDelete";
import IconEdit from "@assets/icons/IconEdit";

import { Money } from "@src/common/money";

import type { JarItem } from "./jarTypes";

export type JarViewProps = {
	jar: JarItem;
	isSelected: boolean;
	onSelect: () => void;
	onEdit: (jar: JarItem) => void;
	onDelete: () => void;
};

const JarView = (props: Readonly<JarViewProps>): JSX.Element => {
	const { t } = useTranslation();

	const handleEdit = (e: React.MouseEvent<HTMLButtonElement>) => {
		e.stopPropagation();

		props.onEdit(props.jar);
	};

	const handleDelete = (e: React.MouseEvent<HTMLButtonElement>) => {
		e.stopPropagation();

		props.onDelete();
	};

	return (
		<div
			className={`jar-card ${props.isSelected ? "selected" : ""}`}
			onClick={props.onSelect}>
			<div className="card-content">
				<div className="card-title">
					<div>{props.jar.name}</div>
				</div>
				<div className="card-description">
					<div>{props.jar.description}</div>
				</div>
				<div className="card-footer">
					<div>
						{t("balance")}: {Money.toLabel(props.jar.balance)}
					</div>
					{props.jar.goal && (
						<div>
							{t("goal")}: {Money.toLabel(props.jar.goal)}
						</div>
					)}
				</div>
			</div>

			{props.isSelected && (
				<div className="card-actions">
					<button className="icon-button" onClick={handleEdit}>
						<IconEdit size="1rem" />
					</button>
					{props.jar.transactions.length === 0 && (
						<button
							className="icon-button danger"
							onClick={handleDelete}>
							<IconDelete size="1rem" />
						</button>
					)}
				</div>
			)}
		</div>
	);
};

export default JarView;
