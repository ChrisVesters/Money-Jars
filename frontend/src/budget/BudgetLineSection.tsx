import type { JSX } from "react";
import { useTranslation } from "react-i18next";

import IconPlus from "@assets/icons/IconPlus";

import { Money } from "@src/common/money";

import type { BudgetLineItem } from "./budgetTypes";

export type BudgetLineSectionProps = {
	title: string;
	totalLabel: string;
	createLabel: string;
	lines: Array<BudgetLineItem>;
	total: number;
	selectedLineId?: string;
	canCreate: boolean;
	onSelect: (line: BudgetLineItem) => void;
	onCreate: () => void;
};

const BudgetLineSection = (
	props: Readonly<BudgetLineSectionProps>
): JSX.Element => {
	const { t } = useTranslation();

	return (
		<div className="budget-line-section">
			<div className="transactions-header">
				<h3>{props.title}</h3>
				{props.canCreate && (
					<button
						className="icon-button"
						onClick={props.onCreate}
						title={props.createLabel}>
						<IconPlus />
					</button>
				)}
			</div>

			<table className="transactions-table">
				<thead>
					<tr>
						<th>{t("category")}</th>
						<th className="budget-amount">{t("plannedAmount")}</th>
					</tr>
				</thead>
				<tbody>
					{props.lines.map(line => (
						<tr
							key={line.id}
							className={
								props.selectedLineId === line.id
									? "selected"
									: ""
							}
							onClick={() => props.onSelect(line)}>
							<td>{line.category.name}</td>
							<td className="budget-amount">
								{Money.toLabel(line.amount)}
							</td>
						</tr>
					))}
				</tbody>
				<tfoot className="budget-totals">
					<tr>
						<td>{props.totalLabel}</td>
						<td className="budget-amount">
							{Money.toLabel(props.total.toFixed(2))}
						</td>
					</tr>
				</tfoot>
			</table>
		</div>
	);
};

export default BudgetLineSection;
