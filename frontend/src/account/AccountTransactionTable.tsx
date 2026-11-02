import type { JSX } from "react";
import { useState } from "react";
import { useTranslation } from "react-i18next";

import { useQuery } from "@apollo/client/react";
import { GetAccountDocument } from "@gql/graphql";

import Pagination from "@src/common/Pagination";
import type { TransactionFilterValue } from "@src/transaction/TransactionFilter";
import TransactionFilter from "@src/transaction/TransactionFilter";
import TransactionRow from "@src/transaction/TransactionRow";
import "@src/transaction/transaction.css";
import { TransactionType } from "@src/transaction/transactionTypes";

export type AccountTransactionTableProps = {
	accountId: string;
};

const AccountTransactionTable = (
	props: Readonly<AccountTransactionTableProps>
): JSX.Element => {
	const { t } = useTranslation();

	const [filter, setFilter] = useState<TransactionFilterValue>({
		from: "",
		to: ""
	});
	const [pageNumber, setPageNumber] = useState(0);
	const [pageSize, setPageSize] = useState(25);

	const page = useQuery(GetAccountDocument, {
		variables: {
			id: props.accountId,
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
	const pageData = (page.data ?? page.previousData)?.getAccount.transactions;

	const changeFilter = (value: TransactionFilterValue): void => {
		setFilter(value);
		setPageNumber(0);
	};

	const changePageSize = (value: number): void => {
		setPageSize(value);
		setPageNumber(0);
	};

	// TODO: this should actually not be possible
	// But I don't think we can enforce this?
	const transactions = (pageData?.content ?? []).filter(
		transaction => transaction.__typename !== TransactionType.JarTransfer
	);

	return (
		<>
			<TransactionFilter filter={filter} onChange={changeFilter} />

			<table className="transactions-table">
				<thead>
					<tr>
						<th className="transaction-date">{t("date")}</th>
						<th className="transaction-type">
							{t("transactionType")}
						</th>
						<th>{t("fromTo")}</th>
						<th>{t("description")}</th>
						<th className="transaction-amount">{t("amount")}</th>
					</tr>
				</thead>
				<tbody>
					{transactions.map(transaction => (
						<TransactionRow
							key={transaction.id}
							transaction={transaction}
							accountId={props.accountId}
							// isSelected={selectedTransaction?.id === transaction.id}
							// onSelect={() => selectTransaction(transaction)}
						/>
					))}
				</tbody>
			</table>
			<Pagination
				pageNumber={pageNumber}
				pageSize={pageSize}
				totalElements={pageData?.totalElements ?? 0}
				onPageChange={setPageNumber}
				onPageSizeChange={changePageSize}
			/>
		</>
	);
};

export default AccountTransactionTable;
