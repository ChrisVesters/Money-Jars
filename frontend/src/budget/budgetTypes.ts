import type { GetBudgetLinesQuery, GetBudgetsQuery } from "@gql/graphql";

export type BudgetItem = GetBudgetsQuery["getBudgets"][number];

export type BudgetLineItem =
	GetBudgetLinesQuery["getBudget"]["lines"][number];
