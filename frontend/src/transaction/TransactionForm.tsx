import type { JSX } from "react";

import { useQuery } from "@apollo/client/react";
import {
	GetAccountsDocument,
	GetCategoriesDocument,
	GetJarsDocument
} from "@gql/graphql";

import AccountTransferTransactionForm from "./accounttransfer/AccountTransferTransactionForm";
import CreateTransactionForm from "./CreateTransactionForm";
import JarTransferTransactionForm from "./jartransfer/JarTransferTransactionForm";
import PaymentTransactionForm from "./payment/PaymentTransactionForm";
import type { TransactionItem } from "./transactionTypes";
import { TransactionType } from "./transactionTypes";

export type TransactionFormProps = {
	transaction: TransactionItem | undefined;
	onClose: () => void;
};

const TransactionForm = (
	props: Readonly<TransactionFormProps>
): JSX.Element => {
	const jarsQuery = useQuery(GetJarsDocument);
	const accountsQuery = useQuery(GetAccountsDocument);
	const categoryQuery = useQuery(GetCategoriesDocument);

	const jars = jarsQuery.data?.getJars ?? [];
	const accounts = accountsQuery.data?.getAccounts ?? [];
	const categories = categoryQuery.data?.getCategories ?? [];

	if (!props.transaction) {
		return (
			<CreateTransactionForm
				jars={jars}
				accounts={accounts}
				categories={categories}
				onClose={props.onClose}
			/>
		);
	}

	switch (props.transaction.__typename) {
		case TransactionType.Payment:
			return (
				<PaymentTransactionForm
					transaction={props.transaction}
					jars={jars}
					accounts={accounts}
					categories={categories}
					onClose={props.onClose}
				/>
			);
		case TransactionType.JarTransfer:
			return (
				<JarTransferTransactionForm
					transaction={props.transaction}
					jars={jars}
					onClose={props.onClose}
				/>
			);
		case TransactionType.AccountTransfer:
			return (
				<AccountTransferTransactionForm
					transaction={props.transaction}
					accounts={accounts}
					onClose={props.onClose}
				/>
			);
	}
};

export default TransactionForm;
