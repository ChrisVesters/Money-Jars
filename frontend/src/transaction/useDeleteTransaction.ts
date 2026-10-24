import { useMutation } from "@apollo/client/react";
import {
	DeleteAccountTransferTransactionDocument,
	DeleteJarTransferTransactionDocument,
	DeletePaymentTransactionDocument
} from "@gql/graphql";

import { TRANSACTION_REFETCH_QUERIES } from "./transactionQueries";
import type { TransactionItem } from "./transactionTypes";
import { TransactionType } from "./transactionTypes";

const useDeleteTransaction = (): ((
	transaction: TransactionItem
) => Promise<void>) => {
	const [deletePaymentTransaction] = useMutation(
		DeletePaymentTransactionDocument
	);
	const [deleteJarTransferTransaction] = useMutation(
		DeleteJarTransferTransactionDocument
	);
	const [deleteAccountTransferTransaction] = useMutation(
		DeleteAccountTransferTransactionDocument
	);

	const getDeleteMutation = (transaction: TransactionItem) => {
		switch (transaction.__typename) {
			case TransactionType.Payment:
				return deletePaymentTransaction;
			case TransactionType.JarTransfer:
				return deleteJarTransferTransaction;
			case TransactionType.AccountTransfer:
				return deleteAccountTransferTransaction;
		}
	};

	return async (transaction: TransactionItem): Promise<void> => {
		const deleteMutation = getDeleteMutation(transaction);
		const result = await deleteMutation({
			variables: {
				id: transaction.id
			},
			refetchQueries: TRANSACTION_REFETCH_QUERIES
		});
		console.log("Delete transaction result:", result);
	};
};

export default useDeleteTransaction;
