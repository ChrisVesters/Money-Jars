import {
	GetAccountDocument,
	GetAccountsDocument,
	GetCategoriesDocument,
	GetJarsDocument,
	QueryTransactionsDocument
} from "@gql/graphql";

export const TRANSACTION_REFETCH_QUERIES = [
	QueryTransactionsDocument,
	GetJarsDocument,
	GetAccountDocument,
	GetAccountsDocument,
	GetCategoriesDocument
];
