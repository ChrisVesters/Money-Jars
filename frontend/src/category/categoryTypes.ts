import type { GetCategoriesQuery } from "@gql/graphql";

export type CategoryItem = GetCategoriesQuery["getCategories"][number];
