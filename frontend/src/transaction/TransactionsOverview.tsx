import type { JSX } from "react";
import { useState } from "react";
import { useTranslation } from "react-i18next";

import { useMutation, useQuery } from "@apollo/client/react";
import {
	DeleteAccountTransferTransactionDocument,
	DeleteJarTransferTransactionDocument,
	DeletePaymentTransactionDocument,
	GetAccountsDocument,
	GetJarsDocument,
	QueryTransactionsDocument
} from "@gql/graphql";

import IconDelete from "@assets/icons/IconDelete";
import IconEdit from "@assets/icons/IconEdit";
import IconPlus from "@assets/icons/IconPlus";

import Modal from "@src/common/Modal";
import Pagination from "@src/common/Pagination";

import AccountTransferTransactionForm from "./AccountTransferTransactionForm";
import CreateTransactionForm from "./CreateTransactionForm";
import JarTransferTransactionForm from "./JarTransferTransactionForm";
import PaymentTransactionForm from "./PaymentTransactionForm";
import TransactionTable from "./TransactionTable";
import "./transaction.css";
import { TRANSACTION_REFETCH_QUERIES } from "./transactionQueries";
import type { TransactionItem } from "./transactionTypes";
import { TransactionType } from "./transactionTypes";

const TransactionsOverview = (): JSX.Element => {
	const { t } = useTranslation();

	const [from, setFrom] = useState("");
	const [to, setTo] = useState("");
	const [pageNumber, setPageNumber] = useState(0);
	const [pageSize, setPageSize] = useState(25);

	const jars = useQuery(GetJarsDocument);
	const accounts = useQuery(GetAccountsDocument);

	const page = useQuery(QueryTransactionsDocument, {
		variables: {
			req: {
				filter: {
					from: from || null,
					to: to || null
				},
				page: pageNumber,
				size: pageSize
			}
		}
	});
	const pageData = (page.data ?? page.previousData)?.queryTransactions;

	const [deletePaymentTransaction] = useMutation(
		DeletePaymentTransactionDocument
	);
	const [deleteJarTransferTransaction] = useMutation(
		DeleteJarTransferTransactionDocument
	);
	const [deleteAccountTransferTransaction] = useMutation(
		DeleteAccountTransferTransactionDocument
	);

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

	const changeFrom = (value: string): void => {
		setFrom(value);
		setPageNumber(0);
	};

	const changeTo = (value: string): void => {
		setTo(value);
		setPageNumber(0);
	};

	const clearFilter = (): void => {
		setFrom("");
		setTo("");
		setPageNumber(0);
	};

	const changePageSize = (value: number): void => {
		setPageSize(value);
		setPageNumber(0);
	};

	const selectTransaction = (transaction: TransactionItem): void => {
		if (transaction === selectedTransaction) {
			setSelectedTransaction(undefined);
		} else {
			setSelectedTransaction(transaction);
		}
	};

	const handleDeleteTransaction = async (): Promise<void> => {
		if (!selectedTransaction) {
			return;
		}

		if (!globalThis.confirm(t("confirmDeleteTransaction"))) {
			return;
		}

		try {
			const deleteTransaction =
				selectedTransaction.__typename === TransactionType.Payment
					? deletePaymentTransaction
					: selectedTransaction.__typename ===
						  TransactionType.JarTransfer
						? deleteJarTransferTransaction
						: deleteAccountTransferTransaction;
			const result = await deleteTransaction({
				variables: {
					id: selectedTransaction.id
				},
				refetchQueries: TRANSACTION_REFETCH_QUERIES
			});
			console.log("Delete transaction result:", result);
			setSelectedTransaction(undefined);
			if (pageData?.content.length === 1 && pageNumber > 0) {
				setPageNumber(pageNumber - 1);
			}
		} catch (error) {
			console.error("Error deleting transaction:", error);
		}
	};

	return (
		<div className="transactions-overview">
			<div className="transactions-filters">
				<label className="transactions-filter">
					{t("from")}
					<input
						type="date"
						value={from}
						max={to || undefined}
						onChange={e => changeFrom(e.target.value)}
					/>
				</label>
				<label className="transactions-filter">
					{t("to")}
					<input
						type="date"
						value={to}
						min={from || undefined}
						onChange={e => changeTo(e.target.value)}
					/>
				</label>
				{(from || to) && (
					<button className="text-button" onClick={clearFilter}>
						{t("clearFilter")}
					</button>
				)}
			</div>

			<div className="transactions-actions">
				{selectedTransaction ? (
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
				) : (
					<button
						className="icon-button"
						onClick={openCreateTransaction}
						title={t("createTransaction")}>
						<IconPlus />
					</button>
				)}
			</div>

			<TransactionTable
				transactions={pageData?.content ?? []}
				selectedTransaction={selectedTransaction}
				onSelect={selectTransaction}
			/>
			<Pagination
				pageNumber={pageNumber}
				pageSize={pageSize}
				totalElements={pageData?.totalElements ?? 0}
				onPageChange={setPageNumber}
				onPageSizeChange={changePageSize}
			/>

			<Modal
				isOpen={transactionFormVisible}
				onClose={closeTransactionForm}>
				{!selectedTransaction ? (
					transactionFormVisible && (
						<CreateTransactionForm
							jars={jars.data?.getJars ?? []}
							accounts={accounts.data?.getAccounts ?? []}
							onClose={closeTransactionForm}
						/>
					)
				) : selectedTransaction.__typename ===
				  TransactionType.Payment ? (
					<PaymentTransactionForm
						transaction={selectedTransaction}
						jars={jars.data?.getJars ?? []}
						accounts={accounts.data?.getAccounts ?? []}
						onClose={closeTransactionForm}
					/>
				) : selectedTransaction.__typename ===
				  TransactionType.JarTransfer ? (
					<JarTransferTransactionForm
						transaction={selectedTransaction}
						jars={jars.data?.getJars ?? []}
						onClose={closeTransactionForm}
					/>
				) : (
					<AccountTransferTransactionForm
						transaction={selectedTransaction}
						accounts={accounts.data?.getAccounts ?? []}
						onClose={closeTransactionForm}
					/>
				)}
			</Modal>
		</div>
	);
};

export default TransactionsOverview;
