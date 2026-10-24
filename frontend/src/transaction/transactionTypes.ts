import type { QueryTransactionsQuery } from "@gql/graphql";

export type TransactionItem =
	QueryTransactionsQuery["queryTransactions"]["content"][number];

export type TransactionType = TransactionItem["__typename"];

export const TransactionType = {
	Payment: "PaymentTransaction",
	JarTransfer: "JarTransferTransaction",
	AccountTransfer: "AccountTransferTransaction"
} as const satisfies Record<string, TransactionType>;

export type PaymentTransactionItem = Extract<
	TransactionItem,
	{ __typename: typeof TransactionType.Payment }
>;

export type JarTransferTransactionItem = Extract<
	TransactionItem,
	{ __typename: typeof TransactionType.JarTransfer }
>;

export type AccountTransferTransactionItem = Extract<
	TransactionItem,
	{ __typename: typeof TransactionType.AccountTransfer }
>;
