import type { GetTransactionsQuery } from "@gql/graphql";

export type TransactionItem = GetTransactionsQuery["getTransactions"][number];
