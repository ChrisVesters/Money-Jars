import type { QueryTransactionsQuery } from "@gql/graphql";

export type TransactionItem =
	QueryTransactionsQuery["queryTransactions"]["content"][number];
