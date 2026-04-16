/* eslint-disable */
import type { TypedDocumentNode as DocumentNode } from '@graphql-typed-document-node/core';
export type Maybe<T> = T | null;
export type InputMaybe<T> = T | null | undefined;
export type Exact<T extends { [key: string]: unknown }> = { [K in keyof T]: T[K] };
export type MakeOptional<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]?: Maybe<T[SubKey]> };
export type MakeMaybe<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]: Maybe<T[SubKey]> };
export type MakeEmpty<T extends { [key: string]: unknown }, K extends keyof T> = { [_ in K]?: never };
export type Incremental<T> = T | { [P in keyof T]?: P extends ' $fragmentName' | '__typename' ? T[P] : never };
/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
  ID: { input: string; output: string; }
  String: { input: string; output: string; }
  Boolean: { input: boolean; output: boolean; }
  Int: { input: number; output: number; }
  Float: { input: number; output: number; }
};

export type Jar = {
  __typename?: 'Jar';
  balance: Scalars['Float']['output'];
  description: Scalars['String']['output'];
  id: Scalars['ID']['output'];
  name: Scalars['String']['output'];
};

export type Mutation = {
  __typename?: 'Mutation';
  createJar?: Maybe<Jar>;
  deleteJar?: Maybe<Scalars['Boolean']['output']>;
  updateJar?: Maybe<Jar>;
};


export type MutationCreateJarArgs = {
  req: UpdateJar;
};


export type MutationDeleteJarArgs = {
  id: Scalars['ID']['input'];
};


export type MutationUpdateJarArgs = {
  id: Scalars['ID']['input'];
  req: UpdateJar;
};

export type Query = {
  __typename?: 'Query';
  getJar?: Maybe<Jar>;
  getJars: Array<Jar>;
};


export type QueryGetJarArgs = {
  id: Scalars['ID']['input'];
};

export type UpdateJar = {
  description: Scalars['String']['input'];
  name: Scalars['String']['input'];
};

export type GetJarsQueryVariables = Exact<{ [key: string]: never; }>;


export type GetJarsQuery = { __typename?: 'Query', getJars: Array<{ __typename?: 'Jar', id: string, name: string, description: string, balance: number }> };

export type GetJarQueryVariables = Exact<{
  id: Scalars['ID']['input'];
}>;


export type GetJarQuery = { __typename?: 'Query', getJar?: { __typename?: 'Jar', id: string, name: string, description: string, balance: number } | null };

export type CreateJarMutationVariables = Exact<{
  req: UpdateJar;
}>;


export type CreateJarMutation = { __typename?: 'Mutation', createJar?: { __typename?: 'Jar', id: string, name: string, description: string, balance: number } | null };

export type UpdateJarMutationVariables = Exact<{
  id: Scalars['ID']['input'];
  req: UpdateJar;
}>;


export type UpdateJarMutation = { __typename?: 'Mutation', updateJar?: { __typename?: 'Jar', id: string, name: string, description: string, balance: number } | null };


export const GetJarsDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"query","name":{"kind":"Name","value":"GetJars"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"getJars"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}},{"kind":"Field","name":{"kind":"Name","value":"description"}},{"kind":"Field","name":{"kind":"Name","value":"balance"}}]}}]}}]} as unknown as DocumentNode<GetJarsQuery, GetJarsQueryVariables>;
export const GetJarDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"query","name":{"kind":"Name","value":"GetJar"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"id"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"getJar"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"id"},"value":{"kind":"Variable","name":{"kind":"Name","value":"id"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}},{"kind":"Field","name":{"kind":"Name","value":"description"}},{"kind":"Field","name":{"kind":"Name","value":"balance"}}]}}]}}]} as unknown as DocumentNode<GetJarQuery, GetJarQueryVariables>;
export const CreateJarDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"CreateJar"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"req"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"UpdateJar"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"createJar"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"req"},"value":{"kind":"Variable","name":{"kind":"Name","value":"req"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}},{"kind":"Field","name":{"kind":"Name","value":"description"}},{"kind":"Field","name":{"kind":"Name","value":"balance"}}]}}]}}]} as unknown as DocumentNode<CreateJarMutation, CreateJarMutationVariables>;
export const UpdateJarDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"UpdateJar"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"id"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}},{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"req"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"UpdateJar"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"updateJar"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"id"},"value":{"kind":"Variable","name":{"kind":"Name","value":"id"}}},{"kind":"Argument","name":{"kind":"Name","value":"req"},"value":{"kind":"Variable","name":{"kind":"Name","value":"req"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}},{"kind":"Field","name":{"kind":"Name","value":"description"}},{"kind":"Field","name":{"kind":"Name","value":"balance"}}]}}]}}]} as unknown as DocumentNode<UpdateJarMutation, UpdateJarMutationVariables>;