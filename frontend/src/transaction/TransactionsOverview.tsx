import type { JSX } from "react";
import { useState } from "react";
import { useTranslation } from "react-i18next";

import { useMutation, useQuery } from "@apollo/client/react";
import type { CreatePaymentTransaction } from "@gql/graphql";
import {
	CreatePaymentTransactionDocument,
	DeletePaymentTransactionDocument,
	Direction,
	GetAccountsDocument,
	GetJarsDocument,
	QueryTransactionsDocument,
	UpdatePaymentTransactionDocument
} from "@gql/graphql";

import IconChevronLeft from "@assets/icons/IconChevronLeft";
import IconChevronRight from "@assets/icons/IconChevronRight";
import IconDelete from "@assets/icons/IconDelete";
import IconEdit from "@assets/icons/IconEdit";
import IconIncoming from "@assets/icons/IconIncoming";
import IconOutgoing from "@assets/icons/IconOutgoing";
import IconPlus from "@assets/icons/IconPlus";

import Modal from "@src/common/Modal";
import { Money } from "@src/common/money";

import TransactionForm from "./TransactionForm";
import "./transaction.css";
import type { TransactionItem } from "./transactionTypes";

const PAGE_SIZES = [10, 25, 50, 100];

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
	const totalElements = pageData?.totalElements ?? 0;
	const totalPages = Math.max(1, Math.ceil(totalElements / pageSize));
	const firstElement = totalElements === 0 ? 0 : pageNumber * pageSize + 1;
	const lastElement = Math.min((pageNumber + 1) * pageSize, totalElements);

	const [createTransaction] = useMutation(CreatePaymentTransactionDocument);
	const [updateTransaction] = useMutation(UpdatePaymentTransactionDocument);
	const [deleteTransaction] = useMutation(DeletePaymentTransactionDocument);

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

	const handleSubmitTransaction = async (
		data: CreatePaymentTransaction
	): Promise<void> => {
		console.log("Submit transaction data:", data);
		try {
			if (selectedTransaction) {
				const result = await updateTransaction({
					variables: {
						id: selectedTransaction.id,
						req: data
					},
					refetchQueries: [
						{ query: QueryTransactionsDocument },
						{ query: GetJarsDocument },
						{ query: GetAccountsDocument }
					]
				});
				console.log("Update transaction result:", result);
			} else {
				const result = await createTransaction({
					variables: {
						req: data
					},
					refetchQueries: [
						{ query: QueryTransactionsDocument },
						{ query: GetJarsDocument },
						{ query: GetAccountsDocument }
					]
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
				refetchQueries: [
					{ query: QueryTransactionsDocument },
					{ query: GetJarsDocument },
					{ query: GetAccountsDocument }
				]
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
					<button
						className="text-button"
						onClick={clearFilter}>
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

			<table className="transactions-table">
				<thead>
					<tr>
						<th>{t("date")}</th>
						<th>{t("amount")}</th>
						<th>{t("direction")}</th>
						<th>{t("counterparty")}</th>
						<th>{t("description")}</th>
						<th>{t("jar")}</th>
						<th>{t("account")}</th>
					</tr>
				</thead>
				<tbody>
					{pageData?.content.map(transaction => (
						<tr
							key={transaction.id}
							className={`${selectedTransaction?.id === transaction.id ? "selected" : ""}`}
							onClick={() => selectTransaction(transaction)}>
							<td>{transaction.date}</td>
							<td>{Money.toLabel(transaction.amount)}</td>
							<td>
								{transaction.direction ===
								Direction.Outgoing ? (
									<IconOutgoing />
								) : (
									<IconIncoming />
								)}
							</td>
							<td>{transaction.counterparty}</td>
							<td>{transaction.description}</td>
							<td>{transaction.jar.name}</td>
							<td>{transaction.account.name}</td>
						</tr>
					))}
				</tbody>
			</table>
			<div className="transactions-paging">
				<span>
					{t("pagingRange", {
						first: firstElement,
						last: lastElement,
						total: totalElements
					})}
				</span>
				<div className="transactions-paging-controls">
					<label className="transactions-page-size">
						{t("pageSize")}
						<select
							value={pageSize}
							onChange={e =>
								changePageSize(Number(e.target.value))
							}>
							{PAGE_SIZES.map(size => (
								<option key={size} value={size}>
									{size}
								</option>
							))}
						</select>
					</label>
					<button
						className="icon-button"
						onClick={() => setPageNumber(pageNumber - 1)}
						disabled={pageNumber === 0}
						title={t("previousPage")}>
						<IconChevronLeft />
					</button>
					<span>
						{t("pageOf", {
							page: pageNumber + 1,
							total: totalPages
						})}
					</span>
					<button
						className="icon-button"
						onClick={() => setPageNumber(pageNumber + 1)}
						disabled={pageNumber >= totalPages - 1}
						title={t("nextPage")}>
						<IconChevronRight />
					</button>
				</div>
			</div>

			<Modal
				isOpen={transactionFormVisible}
				onClose={closeTransactionForm}>
				<TransactionForm
					transaction={selectedTransaction}
					jars={jars.data?.getJars ?? []}
					accounts={accounts.data?.getAccounts ?? []}
					onClose={closeTransactionForm}
					onConfirm={handleSubmitTransaction}
				/>
			</Modal>
		</div>
	);
};

export default TransactionsOverview;
