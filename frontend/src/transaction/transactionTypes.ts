import type { QueryTransactionsQuery } from "@gql/graphql";

export type TransactionItem =
	QueryTransactionsQuery["queryTransactions"]["content"][number];

export type TransactionType = TransactionItem["__typename"];

export const TransactionType = {
	Payment: "PaymentTransaction",
	JarTransfer: "JarTransferTransaction"
} as const satisfies Record<string, TransactionType>;

export type PaymentTransactionItem = Extract<
	TransactionItem,
	{ __typename: typeof TransactionType.Payment }
>;

export type JarTransferTransactionItem = Extract<
	TransactionItem,
	{ __typename: typeof TransactionType.JarTransfer }
>;
