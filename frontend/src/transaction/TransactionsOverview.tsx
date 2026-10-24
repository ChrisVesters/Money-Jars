import type { JSX } from "react";
import { useState } from "react";
import { useTranslation } from "react-i18next";

import { useQuery } from "@apollo/client/react";
import { QueryTransactionsDocument } from "@gql/graphql";

import Modal from "@src/common/Modal";
import Pagination from "@src/common/Pagination";

import TransactionActions from "./TransactionActions";
import type { TransactionFilterValue } from "./TransactionFilter";
import TransactionFilter from "./TransactionFilter";
import TransactionForm from "./TransactionForm";
import TransactionTable from "./TransactionTable";
import "./transaction.css";
import type { TransactionItem } from "./transactionTypes";
import useDeleteTransaction from "./useDeleteTransaction";

const TransactionsOverview = (): JSX.Element => {
	const { t } = useTranslation();

	const [filter, setFilter] = useState<TransactionFilterValue>({
		from: "",
		to: ""
	});
	const [pageNumber, setPageNumber] = useState(0);
	const [pageSize, setPageSize] = useState(25);

	const page = useQuery(QueryTransactionsDocument, {
		variables: {
			req: {
				filter: {
					from: filter.from || null,
					to: filter.to || null
				},
				page: pageNumber,
				size: pageSize
			}
		}
	});
	const pageData = (page.data ?? page.previousData)?.queryTransactions;

	const deleteTransaction = useDeleteTransaction();

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

	const changeFilter = (value: TransactionFilterValue): void => {
		setFilter(value);
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
			await deleteTransaction(selectedTransaction);
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
			<TransactionFilter filter={filter} onChange={changeFilter} />

			<TransactionActions
				hasSelection={selectedTransaction !== undefined}
				onCreate={openCreateTransaction}
				onEdit={openEditTransaction}
				onDelete={handleDeleteTransaction}
			/>

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
				{transactionFormVisible && (
					<TransactionForm
						transaction={selectedTransaction}
						onClose={closeTransactionForm}
					/>
				)}
			</Modal>
		</div>
	);
};

export default TransactionsOverview;
