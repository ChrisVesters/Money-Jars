import type { JSX } from "react";
import { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";

import { useMutation, useQuery } from "@apollo/client/react";

import IconDelete from "@assets/icons/IconDelete";
import IconEdit from "@assets/icons/IconEdit";
import IconPlus from "@assets/icons/IconPlus";

import type { UpdateBudget } from "@gql/graphql";
import {
	CreateBudgetDocument,
	DeleteBudgetDocument,
	GetBudgetsDocument,
	UpdateBudgetDocument
} from "@gql/graphql";

import Modal from "@src/common/Modal";

import "../transaction/transaction.css";
import "./budget.css";
import BudgetForm from "./BudgetForm";
import { isCurrent, toIsoDate } from "./budgetDates";
import type { BudgetItem } from "./budgetTypes";

const Overview = (): JSX.Element => {
	const { t } = useTranslation();
	const { data } = useQuery(GetBudgetsDocument);

	const [createBudget] = useMutation(CreateBudgetDocument);
	const [updateBudget] = useMutation(UpdateBudgetDocument);
	const [deleteBudget] = useMutation(DeleteBudgetDocument);

	const [budgetFormVisible, setBudgetFormVisible] = useState(false);
	const [selectedBudget, setSelectedBudget] = useState<
		BudgetItem | undefined
	>(undefined);

	const today = toIsoDate(new Date());

	const budgets = useMemo(
		() =>
			[...(data?.getBudgets ?? [])].sort((a, b) =>
				b.startDate.localeCompare(a.startDate)
			),
		[data?.getBudgets]
	);

	const handleSelectBudget = (budget: BudgetItem): void => {
		if (selectedBudget?.id === budget.id) {
			setSelectedBudget(undefined);
		} else {
			setSelectedBudget(budget);
		}
	};

	const openCreateBudgetForm = (): void => {
		setSelectedBudget(undefined);
		setBudgetFormVisible(true);
	};

	const openEditBudgetForm = (): void => {
		setBudgetFormVisible(true);
	};

	const closeBudgetForm = (): void => {
		setBudgetFormVisible(false);
		setSelectedBudget(undefined);
	};

	const handleSubmitBudget = async (
		budget: UpdateBudget
	): Promise<boolean> => {
		try {
			if (selectedBudget) {
				await updateBudget({
					variables: {
						id: selectedBudget.id,
						req: budget
					},
					refetchQueries: [{ query: GetBudgetsDocument }]
				});
			} else {
				await createBudget({
					variables: {
						req: budget
					},
					refetchQueries: [{ query: GetBudgetsDocument }]
				});
			}
		} catch (error) {
			console.info(t("errorSaveBudget"), error);
			return false;
		}

		closeBudgetForm();
		return true;
	};

	const handleDeleteBudget = async (): Promise<void> => {
		if (
			!selectedBudget ||
			!globalThis.confirm(t("confirmDeleteBudget"))
		) {
			return;
		}

		try {
			await deleteBudget({
				variables: {
					id: selectedBudget.id
				},
				refetchQueries: [{ query: GetBudgetsDocument }]
			});
		} catch (error) {
			console.info(t("errorDeleteBudget"), error);
		}

		setSelectedBudget(undefined);
	};

	const rowClassName = (budget: BudgetItem): string =>
		[
			isCurrent(budget, today) ? "budget-current" : "",
			selectedBudget?.id === budget.id ? "selected" : ""
		]
			.filter(Boolean)
			.join(" ");

	return (
		<div className="transactions-overview">
			<div className="transactions-actions">
				{selectedBudget ? (
					<>
						<button
							className="icon-button"
							onClick={openEditBudgetForm}
							title={t("editBudget")}>
							<IconEdit />
						</button>
						<button
							className="icon-button"
							onClick={handleDeleteBudget}
							title={t("deleteBudget")}>
							<IconDelete />
						</button>
					</>
				) : (
					<button
						className="icon-button"
						onClick={openCreateBudgetForm}
						title={t("createBudget")}>
						<IconPlus />
					</button>
				)}
			</div>

			<table className="transactions-table">
				<thead>
					<tr>
						<th>{t("name")}</th>
						<th>{t("startDate")}</th>
						<th>{t("endDate")}</th>
						<th>{t("description")}</th>
					</tr>
				</thead>
				<tbody>
					{budgets.map(budget => (
						<tr
							key={budget.id}
							className={rowClassName(budget)}
							onClick={() => handleSelectBudget(budget)}>
							<td>{budget.name}</td>
							<td className="transaction-date">
								{budget.startDate}
							</td>
							<td className="transaction-date">
								{budget.endDate}
							</td>
							<td>{budget.description}</td>
						</tr>
					))}
				</tbody>
			</table>

			<Modal isOpen={budgetFormVisible} onClose={closeBudgetForm}>
				<BudgetForm
					budget={selectedBudget}
					onClose={closeBudgetForm}
					onConfirm={handleSubmitBudget}
				/>
			</Modal>
		</div>
	);
};

export default Overview;
