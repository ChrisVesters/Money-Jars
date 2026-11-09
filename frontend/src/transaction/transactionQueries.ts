import {
	GetAccountDocument,
	GetAccountsDocument,
	GetCategoriesDocument,
	GetJarDocument,
	GetJarsDocument,
	GetPaymentTransactionCategoryTotalsDocument,
	QueryTransactionsDocument
} from "@gql/graphql";

export const TRANSACTION_REFETCH_QUERIES = [
	QueryTransactionsDocument,
	GetJarDocument,
	GetJarsDocument,
	GetAccountDocument,
	GetAccountsDocument,
	GetCategoriesDocument,
	GetPaymentTransactionCategoryTotalsDocument
];
