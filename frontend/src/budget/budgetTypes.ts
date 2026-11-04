import type { GetBudgetsQuery } from "@gql/graphql";

export type BudgetItem = GetBudgetsQuery["getBudgets"][number];
