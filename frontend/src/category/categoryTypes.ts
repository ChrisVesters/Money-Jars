import type { GetCategoriesQuery } from "@gql/graphql";

export type CategoryItem = GetCategoriesQuery["getCategorys"][number];
