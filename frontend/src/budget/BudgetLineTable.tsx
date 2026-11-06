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
	UpdateBudgetLineDocument
} from "@gql/graphql";

import Modal from "@src/common/Modal";
import { Money } from "@src/common/money";

import type { BudgetLineFormValues } from "./BudgetLineForm";
import BudgetLineForm from "./BudgetLineForm";
import BudgetLineSection from "./BudgetLineSection";
import type { BudgetLineItem } from "./budgetTypes";

export type BudgetLineTableProps = {
	budgetId: string;
};

const sumAmounts = (lines: Array<BudgetLineItem>): number =>
	lines.reduce((total, line) => total + Number.parseFloat(line.amount), 0);

const BudgetLineTable = (
	props: Readonly<BudgetLineTableProps>
): JSX.Element => {
	const { t } = useTranslation();

	const { data } = useQuery(GetBudgetLinesDocument, {
		variables: { budgetId: props.budgetId }
	});
	const { data: categoryData } = useQuery(GetCategoriesDocument);

	const [createBudgetLine] = useMutation(CreateBudgetLineDocument);
	const [updateBudgetLine] = useMutation(UpdateBudgetLineDocument);
	const [deleteBudgetLine] = useMutation(DeleteBudgetLineDocument);

	const [lineFormVisible, setLineFormVisible] = useState(false);
	const [formDirection, setFormDirection] = useState<BudgetLineDirection>(
		BudgetLineDirection.Expense
	);
	const [selectedLine, setSelectedLine] = useState<
		BudgetLineItem | undefined
	>(undefined);

	const refetchQueries = [
		{
			query: GetBudgetLinesDocument,
			variables: { budgetId: props.budgetId }
		}
	];

	const lines = useMemo(
		() =>
			[...(data?.getBudget.lines ?? [])].sort((a, b) =>
				a.category.name.localeCompare(b.category.name)
			),
		[data?.getBudget.lines]
	);

	const incomeLines = lines.filter(
		line => line.direction === BudgetLineDirection.Income
	);
	const expenseLines = lines.filter(
		line => line.direction === BudgetLineDirection.Expense
	);

	// A category appears in at most one line of a budget.
	const availableCategories = useMemo(() => {
		const usedCategoryIds = new Set(lines.map(line => line.category.id));
		return [...(categoryData?.getCategories ?? [])]
			.filter(category => !usedCategoryIds.has(category.id))
			.sort((a, b) => a.name.localeCompare(b.name));
	}, [lines, categoryData?.getCategories]);

	const plannedIncome = sumAmounts(incomeLines);
	const plannedExpenses = sumAmounts(expenseLines);
	const difference = plannedIncome - plannedExpenses;

	const handleSelectLine = (line: BudgetLineItem): void => {
		if (selectedLine?.id === line.id) {
			setSelectedLine(undefined);
		} else {
			setSelectedLine(line);
		}
	};

	const openCreateLineForm = (direction: BudgetLineDirection): void => {
		setSelectedLine(undefined);
		setFormDirection(direction);
		setLineFormVisible(true);
	};

	const openEditLineForm = (): void => {
		setLineFormVisible(true);
	};

	const closeLineForm = (): void => {
		setLineFormVisible(false);
		setSelectedLine(undefined);
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
							budgetId: props.budgetId,
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

		setSelectedLine(undefined);
	};

	const canCreate = availableCategories.length > 0;

	return (
		<div className="transactions-overview">
			<div className="transactions-header">
				<h2>{t("budgetLines")}</h2>
				{selectedLine && (
					<div className="transactions-actions">
						<button
							className="icon-button"
							onClick={openEditLineForm}
							title={t("editBudgetLine")}>
							<IconEdit />
						</button>
						<button
							className="icon-button"
							onClick={handleDeleteLine}
							title={t("deleteBudgetLine")}>
							<IconDelete />
						</button>
					</div>
				)}
			</div>

			<div className="budget-line-sections">
				<BudgetLineSection
					title={t("budgetIncome")}
					totalLabel={t("plannedIncome")}
					createLabel={t("createIncomeLine")}
					lines={incomeLines}
					total={plannedIncome}
					selectedLineId={selectedLine?.id}
					canCreate={canCreate}
					onSelect={handleSelectLine}
					onCreate={() =>
						openCreateLineForm(BudgetLineDirection.Income)
					}
				/>
				<BudgetLineSection
					title={t("budgetExpenses")}
					totalLabel={t("plannedExpenses")}
					createLabel={t("createExpenseLine")}
					lines={expenseLines}
					total={plannedExpenses}
					selectedLineId={selectedLine?.id}
					canCreate={canCreate}
					onSelect={handleSelectLine}
					onCreate={() =>
						openCreateLineForm(BudgetLineDirection.Expense)
					}
				/>
			</div>

			<div className="budget-difference">
				<span>{t("difference")}</span>
				<span className="budget-amount">
					{Money.toLabel(difference.toFixed(2))}
				</span>
			</div>

			<Modal isOpen={lineFormVisible} onClose={closeLineForm}>
				<BudgetLineForm
					line={selectedLine}
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
