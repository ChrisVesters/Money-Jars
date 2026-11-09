import type {
	BudgetLineDirection,
	GetBudgetLinesQuery,
	GetBudgetsQuery,
	GetPaymentTransactionCategoryTotalsQuery
} from "@gql/graphql";

export type BudgetItem = GetBudgetsQuery["getBudgets"][number];

export type BudgetLineItem =
	GetBudgetLinesQuery["getBudget"]["lines"][number];

export type CategoryTotalItem =
	GetPaymentTransactionCategoryTotalsQuery["getPaymentTransactionCategoryTotals"][number];

export type BudgetRow = {
	key: string;
	direction: BudgetLineDirection;
	category?: { id: string; name: string };
	line?: BudgetLineItem;
	planned: number;
	actual: number;
	remaining: number;
	isOver: boolean;
};
