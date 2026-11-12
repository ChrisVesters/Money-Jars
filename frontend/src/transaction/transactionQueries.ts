import {
	GetAccountDocument,
	GetAccountsDocument,
	GetCategoriesDocument,
	GetJarDocument,
	GetJarsDocument,
	GetPaymentTransactionCategoryTotalsDocument,
	SearchTransactionsDocument
} from "@gql/graphql";

export const TRANSACTION_REFETCH_QUERIES = [
	SearchTransactionsDocument,
	GetJarDocument,
	GetJarsDocument,
	GetAccountDocument,
	GetAccountsDocument,
	GetCategoriesDocument,
	GetPaymentTransactionCategoryTotalsDocument
];
