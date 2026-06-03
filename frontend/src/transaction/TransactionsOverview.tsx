import { useState, type JSX } from "react";

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

export default function TransactionsOverview(): JSX.Element {
	const transactions = useQuery(GetTransactionsDocument);
	const jars = useQuery(GetJarsDocument);

	const [createTransaction] = useMutation(CreateTransactionDocument);
	const [updateTransaction] = useMutation(UpdateTransactionDocument);
	const [deleteTransaction] = useMutation(DeleteTransactionDocument);

	const [transactionFormVisible, setTransactionFormVisible] = useState(false);
	const [selectedTransaction, setSelectedTransaction] = useState<
		TransactionItem | undefined
	>(undefined);

	function openCreateTransaction(): void {
		setSelectedTransaction(undefined);
		setTransactionFormVisible(true);
	}

	function openEditTransaction(): void {
		if (selectedTransaction) {
			setTransactionFormVisible(true);
		}
	}

	function closeTransactionForm(): void {
		setTransactionFormVisible(false);
		setSelectedTransaction(undefined);
	}

	function selectTransaction(transaction: TransactionItem): void {
		if (transaction === selectedTransaction) {
			setSelectedTransaction(undefined);
		} else {
			setSelectedTransaction(transaction);
		}
	}

	async function handleSubmitTransaction(
		data: CreateTransaction
	): Promise<void> {
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
	}

	async function handleDeleteTransaction(): Promise<void> {
		if (!selectedTransaction) return;

		if (
			!window.confirm("Are you sure you want to delete this transaction?")
		) {
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
	}

	return (
		<div className="transactions-overview">
			<div className="transactions-actions">
				{selectedTransaction && (
					<>
						<button
							className="icon-button"
							onClick={openEditTransaction}
							title="Edit transaction">
							<IconEdit />
						</button>
						<button
							className="icon-button"
							onClick={handleDeleteTransaction}
							title="Delete transaction">
							<IconDelete />
						</button>
					</>
				)}
				<button
					className="icon-button"
					onClick={openCreateTransaction}
					title="Create transaction">
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
}
