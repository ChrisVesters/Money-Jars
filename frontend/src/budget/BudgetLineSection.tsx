import type { JSX } from "react";
import { useTranslation } from "react-i18next";

import IconPlus from "@assets/icons/IconPlus";

import { BudgetLineDirection } from "@gql/graphql";

import { Money } from "@src/common/money";

import type { BudgetRow } from "./budgetTypes";

export type BudgetLineSectionProps = {
	title: string;
	totalLabel: string;
	createLabel: string;
	direction: BudgetLineDirection;
	rows: Array<BudgetRow>;
	selectedRowKey?: string;
	canCreate: boolean;
	onSelect: (row: BudgetRow) => void;
	onCreate: () => void;
};

const toLabel = (amount: number): string => Money.toLabel(amount.toFixed(2));

const BudgetLineSection = (
	props: Readonly<BudgetLineSectionProps>
): JSX.Element => {
	const { t } = useTranslation();

	// Earning more than planned is good news, spending more is not.
	const overClassName =
		props.direction === BudgetLineDirection.Income
			? "budget-over-income"
			: "budget-over-expense";

	const planned = props.rows.reduce((total, row) => total + row.planned, 0);
	const actual = props.rows.reduce((total, row) => total + row.actual, 0);

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
						<th className="budget-amount">{t("actualAmount")}</th>
						<th className="budget-amount">
							{t("remainingAmount")}
						</th>
					</tr>
				</thead>
				<tbody>
					{props.rows.map(row => (
						<tr
							key={row.key}
							className={[
								props.selectedRowKey === row.key ? "selected" : "",
								row.line ? "" : "budget-unplanned",
								row.category ? "" : "budget-unselectable",
								row.isOver ? overClassName : ""
							]
								.filter(Boolean)
								.join(" ")}
							onClick={() => {
								// Uncategorised payments can't get a line.
								if (row.category) {
									props.onSelect(row);
								}
							}}>
							<td>{row.category?.name ?? t("uncategorised")}</td>
							<td className="budget-amount">
								{toLabel(row.planned)}
							</td>
							<td className="budget-amount budget-actual">
								{toLabel(row.actual)}
							</td>
							<td className="budget-amount">
								{toLabel(row.remaining)}
							</td>
						</tr>
					))}
				</tbody>
				<tfoot className="budget-totals">
					<tr>
						<td>{props.totalLabel}</td>
						<td className="budget-amount">{toLabel(planned)}</td>
						<td className="budget-amount">{toLabel(actual)}</td>
						<td className="budget-amount">
							{toLabel(planned - actual)}
						</td>
					</tr>
				</tfoot>
			</table>
		</div>
	);
};

export default BudgetLineSection;
