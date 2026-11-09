import type { JSX } from "react";
import { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";

import { useMutation, useQuery } from "@apollo/client/react";

import IconDelete from "@assets/icons/IconDelete";
import IconEdit from "@assets/icons/IconEdit";

import {
	BudgetLineDirection,
	CreateBudgetLineDocument,
	DeleteBudgetLineDocument,
	GetBudgetLinesDocument,
	GetCategoriesDocument,
	GetPaymentTransactionCategoryTotalsDocument,
	UpdateBudgetLineDocument
} from "@gql/graphql";

import Modal from "@src/common/Modal";
import { Money } from "@src/common/money";

import type { BudgetLineFormValues } from "./BudgetLineForm";
import BudgetLineForm from "./BudgetLineForm";
import BudgetLineSection from "./BudgetLineSection";
import { toBudgetRows } from "./budgetActuals";
import type { BudgetItem, BudgetRow } from "./budgetTypes";

export type BudgetLineTableProps = {
	budget: BudgetItem;
};

const sumPlanned = (rows: Array<BudgetRow>): number =>
	rows.reduce((total, row) => total + row.planned, 0);

const sumActual = (rows: Array<BudgetRow>): number =>
	rows.reduce((total, row) => total + row.actual, 0);

const toLabel = (amount: number): string => Money.toLabel(amount.toFixed(2));

const BudgetLineTable = (
	props: Readonly<BudgetLineTableProps>
): JSX.Element => {
	const { t } = useTranslation();

	const { data } = useQuery(GetBudgetLinesDocument, {
		variables: { budgetId: props.budget.id }
	});
	const { data: categoryData } = useQuery(GetCategoriesDocument);
	// Transactions can change on other pages, so the actual amounts are
	// always fetched again when the budget is shown.
	const { data: totalData } = useQuery(
		GetPaymentTransactionCategoryTotalsDocument,
		{
			variables: {
				from: props.budget.startDate,
				to: props.budget.endDate
			},
			fetchPolicy: "cache-and-network"
		}
	);

	const [createBudgetLine] = useMutation(CreateBudgetLineDocument);
	const [updateBudgetLine] = useMutation(UpdateBudgetLineDocument);
	const [deleteBudgetLine] = useMutation(DeleteBudgetLineDocument);

	const [lineFormVisible, setLineFormVisible] = useState(false);
	const [formDirection, setFormDirection] = useState<BudgetLineDirection>(
		BudgetLineDirection.Expense
	);
	// A row without a line can be selected too: editing it creates the line.
	const [selectedRow, setSelectedRow] = useState<BudgetRow | undefined>(
		undefined
	);
	const selectedLine = selectedRow?.line;

	const refetchQueries = [
		{
			query: GetBudgetLinesDocument,
			variables: { budgetId: props.budget.id }
		}
	];

	const lines = data?.getBudget.lines;
	const totals = totalData?.getPaymentTransactionCategoryTotals;
	const rows = useMemo(
		() => toBudgetRows(lines ?? [], totals ?? []),
		[lines, totals]
	);

	const incomeRows = rows.filter(
		row => row.direction === BudgetLineDirection.Income
	);
	const expenseRows = rows.filter(
		row => row.direction === BudgetLineDirection.Expense
	);

	// A category appears in at most one line of a budget.
	const availableCategories = useMemo(() => {
		const usedCategoryIds = new Set(
			(lines ?? []).map(line => line.category.id)
		);
		return [...(categoryData?.getCategories ?? [])]
			.filter(category => !usedCategoryIds.has(category.id))
			.sort((a, b) => a.name.localeCompare(b.name));
	}, [lines, categoryData?.getCategories]);

	const plannedDifference = sumPlanned(incomeRows) - sumPlanned(expenseRows);
	const actualDifference = sumActual(incomeRows) - sumActual(expenseRows);

	const handleSelectRow = (row: BudgetRow): void => {
		if (selectedRow?.key === row.key) {
			setSelectedRow(undefined);
		} else {
			setSelectedRow(row);
		}
	};

	const openCreateLineForm = (direction: BudgetLineDirection): void => {
		setSelectedRow(undefined);
		setFormDirection(direction);
		setLineFormVisible(true);
	};

	const openEditLineForm = (): void => {
		if (selectedRow) {
			setFormDirection(selectedRow.direction);
		}
		setLineFormVisible(true);
	};

	const closeLineForm = (): void => {
		setLineFormVisible(false);
		setSelectedRow(undefined);
	};

	const handleSubmitLine = async (
		values: BudgetLineFormValues
	): Promise<boolean> => {
		try {
			if (selectedLine) {
				await updateBudgetLine({
					variables: {
						id: selectedLine.id,
						req: {
							amount: values.amount,
							direction: values.direction
						}
					},
					refetchQueries
				});
			} else {
				await createBudgetLine({
					variables: {
						req: {
							budgetId: props.budget.id,
							categoryId: values.categoryId,
							amount: values.amount,
							direction: values.direction
						}
					},
					refetchQueries
				});
			}
		} catch (error) {
			console.info(t("errorSaveBudgetLine"), error);
			return false;
		}

		closeLineForm();
		return true;
	};

	const handleDeleteLine = async (): Promise<void> => {
		if (
			!selectedLine ||
			!globalThis.confirm(t("confirmDeleteBudgetLine"))
		) {
			return;
		}

		try {
			await deleteBudgetLine({
				variables: {
					id: selectedLine.id
				},
				refetchQueries
			});
		} catch (error) {
			console.info(t("errorDeleteBudgetLine"), error);
		}

		setSelectedRow(undefined);
	};

	const canCreate = availableCategories.length > 0;

	return (
		<div className="transactions-overview">
			<div className="transactions-header">
				<h2>{t("budgetLines")}</h2>
				{selectedRow && (
					<div className="transactions-actions">
						<button
							className="icon-button"
							onClick={openEditLineForm}
							title={t("editBudgetLine")}>
							<IconEdit />
						</button>
						{selectedLine && (
							<button
								className="icon-button"
								onClick={handleDeleteLine}
								title={t("deleteBudgetLine")}>
								<IconDelete />
							</button>
						)}
					</div>
				)}
			</div>

			<div className="budget-line-sections">
				<BudgetLineSection
					title={t("budgetIncome")}
					totalLabel={t("totalIncome")}
					createLabel={t("createIncomeLine")}
					direction={BudgetLineDirection.Income}
					rows={incomeRows}
					selectedRowKey={selectedRow?.key}
					canCreate={canCreate}
					onSelect={handleSelectRow}
					onCreate={() =>
						openCreateLineForm(BudgetLineDirection.Income)
					}
				/>
				<BudgetLineSection
					title={t("budgetExpenses")}
					totalLabel={t("totalExpenses")}
					createLabel={t("createExpenseLine")}
					direction={BudgetLineDirection.Expense}
					rows={expenseRows}
					selectedRowKey={selectedRow?.key}
					canCreate={canCreate}
					onSelect={handleSelectRow}
					onCreate={() =>
						openCreateLineForm(BudgetLineDirection.Expense)
					}
				/>
			</div>

			<div className="budget-difference">
				<span>{t("difference")}</span>
				<span className="budget-difference-amounts">
					<span>
						{t("planned")}{" "}
						<span className="budget-amount">
							{toLabel(plannedDifference)}
						</span>
					</span>
					<span>
						{t("actual")}{" "}
						<span className="budget-amount">
							{toLabel(actualDifference)}
						</span>
					</span>
				</span>
			</div>

			<Modal isOpen={lineFormVisible} onClose={closeLineForm}>
				<BudgetLineForm
					line={selectedLine}
					category={selectedLine ? undefined : selectedRow?.category}
					direction={formDirection}
					categories={availableCategories}
					onClose={closeLineForm}
					onConfirm={handleSubmitLine}
				/>
			</Modal>
		</div>
	);
};

export default BudgetLineTable;
