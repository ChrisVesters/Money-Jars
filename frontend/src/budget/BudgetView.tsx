import type { JSX } from "react";
import { useTranslation } from "react-i18next";

import IconDelete from "@assets/icons/IconDelete";
import IconEdit from "@assets/icons/IconEdit";

import type { BudgetItem } from "./budgetTypes";

export type BudgetViewProps = {
	budget: BudgetItem;
	isCurrent: boolean;
	isSelected: boolean;
	onSelect: () => void;
	onEdit: (budget: BudgetItem) => void;
	onDelete: (budgetId: string) => void;
};

const BudgetView = (props: Readonly<BudgetViewProps>): JSX.Element => {
	const { t } = useTranslation();

	const handleEditClick = (
		event: React.MouseEvent<HTMLButtonElement>
	): void => {
		event.stopPropagation();

		props.onEdit(props.budget);
	};

	const handleDeleteClick = (
		event: React.MouseEvent<HTMLButtonElement>
	): void => {
		event.stopPropagation();

		props.onDelete(props.budget.id);
	};

	const className = [
		"jar-card",
		props.isCurrent ? "budget-current" : "",
		props.isSelected ? "selected" : ""
	]
		.filter(Boolean)
		.join(" ");

	return (
		<div className={className} onClick={props.onSelect}>
			<div className="card-content">
				<div className="card-title">{props.budget.name}</div>
				<div className="card-description">
					{props.budget.description}
				</div>
				<div className="card-footer">
					<div>
						{props.budget.startDate} → {props.budget.endDate}
					</div>
					{props.isCurrent && (
						<div className="budget-current-label">
							{t("currentBudget")}
						</div>
					)}
				</div>
			</div>

			{props.isSelected && (
				<div className="card-actions">
					<button
						className="icon-button"
						onClick={handleEditClick}
						title={t("editBudget")}>
						<IconEdit size="1rem" />
					</button>
					<button
						className="icon-button danger"
						onClick={handleDeleteClick}
						title={t("deleteBudget")}>
						<IconDelete size="1rem" />
					</button>
				</div>
			)}
		</div>
	);
};

export default BudgetView;
