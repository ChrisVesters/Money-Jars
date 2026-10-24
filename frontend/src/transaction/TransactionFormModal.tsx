import type { JSX } from "react";

import { useQuery } from "@apollo/client/react";
import { GetAccountsDocument, GetJarsDocument } from "@gql/graphql";

import Modal from "@src/common/Modal";

import AccountTransferTransactionForm from "./AccountTransferTransactionForm";
import CreateTransactionForm from "./CreateTransactionForm";
import JarTransferTransactionForm from "./JarTransferTransactionForm";
import PaymentTransactionForm from "./PaymentTransactionForm";
import type { TransactionItem } from "./transactionTypes";
import { TransactionType } from "./transactionTypes";

export type TransactionFormModalProps = {
	isOpen: boolean;
	transaction: TransactionItem | undefined;
	onClose: () => void;
};

const TransactionFormModal = (
	props: Readonly<TransactionFormModalProps>
): JSX.Element => {
	const jarsQuery = useQuery(GetJarsDocument);
	const accountsQuery = useQuery(GetAccountsDocument);

	const jars = jarsQuery.data?.getJars ?? [];
	const accounts = accountsQuery.data?.getAccounts ?? [];

	const renderForm = (): JSX.Element | false => {
		if (!props.transaction) {
			return (
				props.isOpen && (
					<CreateTransactionForm
						jars={jars}
						accounts={accounts}
						onClose={props.onClose}
					/>
				)
			);
		}

		switch (props.transaction.__typename) {
			case TransactionType.Payment:
				return (
					<PaymentTransactionForm
						transaction={props.transaction}
						jars={jars}
						accounts={accounts}
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

	return (
		<Modal isOpen={props.isOpen} onClose={props.onClose}>
			{renderForm()}
		</Modal>
	);
};

export default TransactionFormModal;
