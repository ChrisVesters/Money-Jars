import type { GetJarsQuery } from "@gql/graphql";

export type JarItem = GetJarsQuery["getJars"][number];
