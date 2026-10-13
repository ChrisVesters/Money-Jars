import type { GetAccountsQuery } from "@gql/graphql";

export type AccountItem = GetAccountsQuery["getAccounts"][number];
