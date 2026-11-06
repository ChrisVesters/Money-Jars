import type { JSX } from "react";
import { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";

import { useMutation, useQuery } from "@apollo/client/react";

import IconPlus from "@assets/icons/IconPlus";

import type { UpdateBudget } from "@gql/graphql";
import {
	CreateBudgetDocument,
	DeleteBudgetDocument,
	GetBudgetsDocument,
	UpdateBudgetDocument
} from "@gql/graphql";

import Modal from "@src/common/Modal";

import "../jar/jar.css";
import "../transaction/transaction.css";
import "./budget.css";
import BudgetForm from "./BudgetForm";
import BudgetLineTable from "./BudgetLineTable";
import BudgetView from "./BudgetView";
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

	const [selectedCardId, setSelectedCardId] = useState<string | undefined>(
		undefined
	);

	const today = toIsoDate(new Date());

	const budgets = useMemo(
		() =>
			[...(data?.getBudgets ?? [])].sort((a, b) =>
				b.startDate.localeCompare(a.startDate)
			),
		[data?.getBudgets]
	);

	const handleSelectBudget = (budgetId: string): void => {
		if (selectedCardId === budgetId) {
			setSelectedCardId(undefined);
		} else {
			setSelectedCardId(budgetId);
		}
	};

	const openCreateBudgetForm = (): void => {
		setSelectedBudget(undefined);
		setSelectedCardId(undefined);
		setBudgetFormVisible(true);
	};

	const openEditBudgetForm = (budget: BudgetItem): void => {
		setSelectedBudget(budget);
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

	const handleDeleteBudget = async (budgetId: string): Promise<void> => {
		if (!globalThis.confirm(t("confirmDeleteBudget"))) {
			return;
		}

		try {
			await deleteBudget({
				variables: {
					id: budgetId
				},
				refetchQueries: [{ query: GetBudgetsDocument }]
			});
		} catch (error) {
			console.info(t("errorDeleteBudget"), error);
		}

		setSelectedCardId(undefined);
	};

	return (
		<>
			<div className="jar-grid">
				{budgets.map(budget => (
					<BudgetView
						key={budget.id}
						budget={budget}
						isCurrent={isCurrent(budget, today)}
						isSelected={selectedCardId === budget.id}
						onSelect={() => handleSelectBudget(budget.id)}
						onEdit={openEditBudgetForm}
						onDelete={handleDeleteBudget}
					/>
				))}
				<button
					type="button"
					className="jar-card jar-add-card"
					onClick={openCreateBudgetForm}
					title={t("createBudget")}>
					<IconPlus />
				</button>
			</div>

			{selectedCardId && (
				<BudgetLineTable
					key={selectedCardId}
					budgetId={selectedCardId}
				/>
			)}

			<Modal isOpen={budgetFormVisible} onClose={closeBudgetForm}>
				<BudgetForm
					budget={selectedBudget}
					onClose={closeBudgetForm}
					onConfirm={handleSubmitBudget}
				/>
			</Modal>
		</>
	);
};

export default Overview;
