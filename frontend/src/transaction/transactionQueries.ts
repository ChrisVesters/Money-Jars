import {
	GetAccountsDocument,
	GetJarsDocument,
	QueryTransactionsDocument
} from "@gql/graphql";

export const TRANSACTION_REFETCH_QUERIES = [
	QueryTransactionsDocument,
	GetJarsDocument,
	GetAccountsDocument
];
