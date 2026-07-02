import { useState, type JSX } from "react";
import { useTranslation } from "react-i18next";

import { useMutation, useQuery } from "@apollo/client/react";
import {
	CreateTransactionDocument,
	DeleteTransactionDocument,
	GetJarsDocument,
	GetTransactionsDocument,
	UpdateTransactionDocument,
	type CreateTransaction
} from "@gql/graphql";

import IconPlus from "@assets/icons/IconPlus";
import IconEdit from "@assets/icons/IconEdit";
import IconDelete from "@assets/icons/IconDelete";

import Modal from "../common/Modal";
import TransactionForm from "./TransactionForm";
import "./transaction.css";
import type { TransactionItem } from "./transactionTypes";

const TransactionsOverview = (): JSX.Element => {
	const { t } = useTranslation();

	const transactions = useQuery(GetTransactionsDocument);
	const jars = useQuery(GetJarsDocument);

	const [createTransaction] = useMutation(CreateTransactionDocument);
	const [updateTransaction] = useMutation(UpdateTransactionDocument);
	const [deleteTransaction] = useMutation(DeleteTransactionDocument);

	const [transactionFormVisible, setTransactionFormVisible] = useState(false);
	const [selectedTransaction, setSelectedTransaction] = useState<
		TransactionItem | undefined
	>(undefined);

	const openCreateTransaction = (): void => {
		setSelectedTransaction(undefined);
		setTransactionFormVisible(true);
	};

	const openEditTransaction = (): void => {
		if (selectedTransaction) {
			setTransactionFormVisible(true);
		}
	};

	const closeTransactionForm = (): void => {
		setTransactionFormVisible(false);
		setSelectedTransaction(undefined);
	};

	const selectTransaction = (transaction: TransactionItem): void => {
		if (transaction === selectedTransaction) {
			setSelectedTransaction(undefined);
		} else {
			setSelectedTransaction(transaction);
		}
	};

	const handleSubmitTransaction = async (
		data: CreateTransaction
	): Promise<void> => {
		try {
			if (selectedTransaction) {
				const result = await updateTransaction({
					variables: {
						id: selectedTransaction.id,
						req: data
					},
					refetchQueries: [{ query: GetTransactionsDocument }]
				});
				console.log("Update transaction result:", result);
			} else {
				const result = await createTransaction({
					variables: {
						req: data
					},
					refetchQueries: [{ query: GetTransactionsDocument }]
				});
				console.log("Create transaction result:", result);
			}
		} catch (error) {
			console.error("Error creating transaction:", error);
		}

		closeTransactionForm();
	};

	const handleDeleteTransaction = async (): Promise<void> => {
		if (!selectedTransaction) {
			return;
		}

		if (!globalThis.confirm(t("confirmDeleteTransaction"))) {
			return;
		}

		try {
			const result = await deleteTransaction({
				variables: {
					id: selectedTransaction.id
				},
				refetchQueries: [{ query: GetTransactionsDocument }]
			});
			console.log("Delete transaction result:", result);
			setSelectedTransaction(undefined);
		} catch (error) {
			console.error("Error deleting transaction:", error);
		}
	};

	return (
		<div className="transactions-overview">
			<div className="transactions-actions">
				{selectedTransaction && (
					<>
						<button
							className="icon-button"
							onClick={openEditTransaction}
							title={t("editTransaction")}>
							<IconEdit />
						</button>
						<button
							className="icon-button"
							onClick={handleDeleteTransaction}
							title={t("deleteTransaction")}>
							<IconDelete />
						</button>
					</>
				)}
				<button
					className="icon-button"
					onClick={openCreateTransaction}
					title={t("createTransaction")}>
					<IconPlus />
				</button>
			</div>

			<table className="transactions-table">
				<thead>
					<tr>
						<th>Date</th>
						<th>Amount</th>
						<th>Beneficiary</th>
						<th>Description</th>
						<th>Jar</th>
					</tr>
				</thead>
				<tbody>
					{transactions.data?.getTransactions.map(transaction => (
						<tr
							key={transaction.id}
							className={`${selectedTransaction?.id === transaction.id ? "selected" : ""}`}
							onClick={() => selectTransaction(transaction)}>
							<td>{transaction.date}</td>
							<td>${transaction.amount.toFixed(2)}</td>
							<td>{transaction.beneficiary}</td>
							<td>{transaction.description}</td>
							<td>{transaction.jar.name}</td>
						</tr>
					))}
				</tbody>
			</table>

			<Modal
				isOpen={transactionFormVisible}
				onClose={closeTransactionForm}>
				<TransactionForm
					transaction={selectedTransaction}
					jars={jars.data?.getJars ?? []}
					onClose={closeTransactionForm}
					onConfirm={handleSubmitTransaction}
				/>
			</Modal>
		</div>
	);
};

export default TransactionsOverview;
