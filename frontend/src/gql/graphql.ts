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

export type Account = {
  __typename?: 'Account';
  balance: Scalars['String']['output'];
  description: Scalars['String']['output'];
  id: Scalars['ID']['output'];
  name: Scalars['String']['output'];
  transactions: Array<Transaction>;
};

export type AccountTransferTransaction = Transaction & {
  __typename?: 'AccountTransferTransaction';
  amount: Scalars['String']['output'];
  date: Scalars['String']['output'];
  description: Scalars['String']['output'];
  id: Scalars['ID']['output'];
  sourceAccount: Account;
  targetAccount: Account;
};

export type CreateAccountTransferTransaction = {
  amount: Scalars['String']['input'];
  date: Scalars['String']['input'];
  description: Scalars['String']['input'];
  sourceAccountId: Scalars['ID']['input'];
  targetAccountId: Scalars['ID']['input'];
};

export type CreateJarTransferTransaction = {
  amount: Scalars['String']['input'];
  date: Scalars['String']['input'];
  description: Scalars['String']['input'];
  sourceJarId: Scalars['ID']['input'];
  targetJarId: Scalars['ID']['input'];
};

export type CreatePaymentTransaction = {
  accountId: Scalars['ID']['input'];
  amount: Scalars['String']['input'];
  counterparty: Scalars['String']['input'];
  date: Scalars['String']['input'];
  description: Scalars['String']['input'];
  direction: Direction;
  jarId: Scalars['ID']['input'];
};

export enum Direction {
  Incoming = 'INCOMING',
  Outgoing = 'OUTGOING'
}

export type Jar = {
  __typename?: 'Jar';
  balance: Scalars['String']['output'];
  description: Scalars['String']['output'];
  goal?: Maybe<Scalars['String']['output']>;
  id: Scalars['ID']['output'];
  name: Scalars['String']['output'];
  transactions: Array<Transaction>;
};

export type JarTransferTransaction = Transaction & {
  __typename?: 'JarTransferTransaction';
  amount: Scalars['String']['output'];
  date: Scalars['String']['output'];
  description: Scalars['String']['output'];
  id: Scalars['ID']['output'];
  sourceJar: Jar;
  targetJar: Jar;
};

export type Mutation = {
  __typename?: 'Mutation';
  createAccount: Account;
  createAccountTransferTransaction: AccountTransferTransaction;
  createJar: Jar;
  createJarTransferTransaction: JarTransferTransaction;
  createPaymentTransaction: PaymentTransaction;
  deleteAccount: Scalars['Boolean']['output'];
  deleteAccountTransferTransaction: Scalars['Boolean']['output'];
  deleteJar: Scalars['Boolean']['output'];
  deleteJarTransferTransaction: Scalars['Boolean']['output'];
  deletePaymentTransaction: Scalars['Boolean']['output'];
  updateAccount: Account;
  updateAccountTransferTransaction: AccountTransferTransaction;
  updateJar: Jar;
  updateJarTransferTransaction: JarTransferTransaction;
  updatePaymentTransaction: PaymentTransaction;
};


export type MutationCreateAccountArgs = {
  req: UpdateAccount;
};


export type MutationCreateAccountTransferTransactionArgs = {
  req: CreateAccountTransferTransaction;
};


export type MutationCreateJarArgs = {
  req: UpdateJar;
};


export type MutationCreateJarTransferTransactionArgs = {
  req: CreateJarTransferTransaction;
};


export type MutationCreatePaymentTransactionArgs = {
  req: CreatePaymentTransaction;
};


export type MutationDeleteAccountArgs = {
  id: Scalars['ID']['input'];
};


export type MutationDeleteAccountTransferTransactionArgs = {
  id: Scalars['ID']['input'];
};


export type MutationDeleteJarArgs = {
  id: Scalars['ID']['input'];
};


export type MutationDeleteJarTransferTransactionArgs = {
  id: Scalars['ID']['input'];
};


export type MutationDeletePaymentTransactionArgs = {
  id: Scalars['ID']['input'];
};


export type MutationUpdateAccountArgs = {
  id: Scalars['ID']['input'];
  req: UpdateAccount;
};


export type MutationUpdateAccountTransferTransactionArgs = {
  id: Scalars['ID']['input'];
  req: CreateAccountTransferTransaction;
};


export type MutationUpdateJarArgs = {
  id: Scalars['ID']['input'];
  req: UpdateJar;
};


export type MutationUpdateJarTransferTransactionArgs = {
  id: Scalars['ID']['input'];
  req: CreateJarTransferTransaction;
};


export type MutationUpdatePaymentTransactionArgs = {
  id: Scalars['ID']['input'];
  req: CreatePaymentTransaction;
};

export type PaymentTransaction = Transaction & {
  __typename?: 'PaymentTransaction';
  account: Account;
  amount: Scalars['String']['output'];
  counterparty: Scalars['String']['output'];
  date: Scalars['String']['output'];
  description: Scalars['String']['output'];
  direction: Direction;
  id: Scalars['ID']['output'];
  jar: Jar;
};

export type Query = {
  __typename?: 'Query';
  getAccount: Account;
  getAccounts: Array<Account>;
  getJar: Jar;
  getJars: Array<Jar>;
  getTransaction: Transaction;
  getTransactions: Array<Transaction>;
  queryTransactions: TransactionPage;
};


export type QueryGetAccountArgs = {
  id: Scalars['ID']['input'];
};


export type QueryGetJarArgs = {
  id: Scalars['ID']['input'];
};


export type QueryGetTransactionArgs = {
  id: Scalars['ID']['input'];
};


export type QueryQueryTransactionsArgs = {
  query: TransactionQuery;
};

export type Transaction = {
  amount?: Maybe<Scalars['String']['output']>;
  date: Scalars['String']['output'];
  description: Scalars['String']['output'];
  id: Scalars['ID']['output'];
};

export type TransactionFilter = {
  from?: InputMaybe<Scalars['String']['input']>;
  to?: InputMaybe<Scalars['String']['input']>;
};

export type TransactionPage = {
  __typename?: 'TransactionPage';
  content: Array<Transaction>;
  page: Scalars['Int']['output'];
  size: Scalars['Int']['output'];
  totalElements: Scalars['Int']['output'];
};

export type TransactionQuery = {
  filter: TransactionFilter;
  page?: InputMaybe<Scalars['Int']['input']>;
  size?: InputMaybe<Scalars['Int']['input']>;
};

export type UpdateAccount = {
  description: Scalars['String']['input'];
  name: Scalars['String']['input'];
};

export type UpdateJar = {
  description: Scalars['String']['input'];
  goal?: InputMaybe<Scalars['String']['input']>;
  name: Scalars['String']['input'];
};

export type GetAccountsQueryVariables = Exact<{ [key: string]: never; }>;


export type GetAccountsQuery = { __typename?: 'Query', getAccounts: Array<{ __typename?: 'Account', id: string, name: string, description: string, balance: string, transactions: Array<
      | { __typename: 'AccountTransferTransaction', id: string, date: string, amount: string, description: string, sourceAccount: { __typename?: 'Account', id: string, name: string }, targetAccount: { __typename?: 'Account', id: string, name: string } }
      | { __typename: 'JarTransferTransaction', id: string, date: string, amount: string, description: string }
      | { __typename: 'PaymentTransaction', counterparty: string, direction: Direction, id: string, date: string, amount: string, description: string, jar: { __typename?: 'Jar', id: string, name: string }, account: { __typename?: 'Account', id: string, name: string } }
    > }> };

export type CreateAccountMutationVariables = Exact<{
  req: UpdateAccount;
}>;


export type CreateAccountMutation = { __typename?: 'Mutation', createAccount: { __typename?: 'Account', id: string, name: string, description: string, balance: string } };

export type UpdateAccountMutationVariables = Exact<{
  id: Scalars['ID']['input'];
  req: UpdateAccount;
}>;


export type UpdateAccountMutation = { __typename?: 'Mutation', updateAccount: { __typename?: 'Account', id: string, name: string, description: string, balance: string } };

export type DeleteAccountMutationVariables = Exact<{
  id: Scalars['ID']['input'];
}>;


export type DeleteAccountMutation = { __typename?: 'Mutation', deleteAccount: boolean };

export type GetJarsQueryVariables = Exact<{ [key: string]: never; }>;


export type GetJarsQuery = { __typename?: 'Query', getJars: Array<{ __typename?: 'Jar', id: string, name: string, description: string, balance: string, goal?: string | null, transactions: Array<
      | { __typename: 'AccountTransferTransaction', id: string, date: string, amount: string, description: string }
      | { __typename: 'JarTransferTransaction', id: string, date: string, amount: string, description: string, sourceJar: { __typename?: 'Jar', id: string, name: string }, targetJar: { __typename?: 'Jar', id: string, name: string } }
      | { __typename: 'PaymentTransaction', counterparty: string, direction: Direction, id: string, date: string, amount: string, description: string, jar: { __typename?: 'Jar', id: string, name: string }, account: { __typename?: 'Account', id: string, name: string } }
    > }> };

export type CreateJarMutationVariables = Exact<{
  req: UpdateJar;
}>;


export type CreateJarMutation = { __typename?: 'Mutation', createJar: { __typename?: 'Jar', id: string, name: string, description: string, balance: string } };

export type UpdateJarMutationVariables = Exact<{
  id: Scalars['ID']['input'];
  req: UpdateJar;
}>;


export type UpdateJarMutation = { __typename?: 'Mutation', updateJar: { __typename?: 'Jar', id: string, name: string, description: string, balance: string } };

export type DeleteJarMutationVariables = Exact<{
  id: Scalars['ID']['input'];
}>;


export type DeleteJarMutation = { __typename?: 'Mutation', deleteJar: boolean };

export type CreateAccountTransferTransactionMutationVariables = Exact<{
  req: CreateAccountTransferTransaction;
}>;


export type CreateAccountTransferTransactionMutation = { __typename?: 'Mutation', createAccountTransferTransaction: { __typename?: 'AccountTransferTransaction', id: string, date: string, amount: string, description: string } };

export type UpdateAccountTransferTransactionMutationVariables = Exact<{
  id: Scalars['ID']['input'];
  req: CreateAccountTransferTransaction;
}>;


export type UpdateAccountTransferTransactionMutation = { __typename?: 'Mutation', updateAccountTransferTransaction: { __typename?: 'AccountTransferTransaction', id: string, date: string, amount: string, description: string } };

export type DeleteAccountTransferTransactionMutationVariables = Exact<{
  id: Scalars['ID']['input'];
}>;


export type DeleteAccountTransferTransactionMutation = { __typename?: 'Mutation', deleteAccountTransferTransaction: boolean };

export type CreateJarTransferTransactionMutationVariables = Exact<{
  req: CreateJarTransferTransaction;
}>;


export type CreateJarTransferTransactionMutation = { __typename?: 'Mutation', createJarTransferTransaction: { __typename?: 'JarTransferTransaction', id: string, date: string, amount: string, description: string } };

export type UpdateJarTransferTransactionMutationVariables = Exact<{
  id: Scalars['ID']['input'];
  req: CreateJarTransferTransaction;
}>;


export type UpdateJarTransferTransactionMutation = { __typename?: 'Mutation', updateJarTransferTransaction: { __typename?: 'JarTransferTransaction', id: string, date: string, amount: string, description: string } };

export type DeleteJarTransferTransactionMutationVariables = Exact<{
  id: Scalars['ID']['input'];
}>;


export type DeleteJarTransferTransactionMutation = { __typename?: 'Mutation', deleteJarTransferTransaction: boolean };

export type CreatePaymentTransactionMutationVariables = Exact<{
  req: CreatePaymentTransaction;
}>;


export type CreatePaymentTransactionMutation = { __typename?: 'Mutation', createPaymentTransaction: { __typename?: 'PaymentTransaction', id: string, date: string, amount: string, description: string, counterparty: string, direction: Direction } };

export type UpdatePaymentTransactionMutationVariables = Exact<{
  id: Scalars['ID']['input'];
  req: CreatePaymentTransaction;
}>;


export type UpdatePaymentTransactionMutation = { __typename?: 'Mutation', updatePaymentTransaction: { __typename?: 'PaymentTransaction', id: string, date: string, amount: string, description: string, counterparty: string, direction: Direction } };

export type DeletePaymentTransactionMutationVariables = Exact<{
  id: Scalars['ID']['input'];
}>;


export type DeletePaymentTransactionMutation = { __typename?: 'Mutation', deletePaymentTransaction: boolean };

export type GetTransactionsQueryVariables = Exact<{ [key: string]: never; }>;


export type GetTransactionsQuery = { __typename?: 'Query', getTransactions: Array<
    | { __typename: 'AccountTransferTransaction', id: string, date: string, amount: string, description: string, sourceAccount: { __typename?: 'Account', id: string, name: string }, targetAccount: { __typename?: 'Account', id: string, name: string } }
    | { __typename: 'JarTransferTransaction', id: string, date: string, amount: string, description: string, sourceJar: { __typename?: 'Jar', id: string, name: string }, targetJar: { __typename?: 'Jar', id: string, name: string } }
    | { __typename: 'PaymentTransaction', counterparty: string, direction: Direction, id: string, date: string, amount: string, description: string, jar: { __typename?: 'Jar', id: string, name: string }, account: { __typename?: 'Account', id: string, name: string } }
  > };

export type QueryTransactionsQueryVariables = Exact<{
  req: TransactionQuery;
}>;


export type QueryTransactionsQuery = { __typename?: 'Query', queryTransactions: { __typename?: 'TransactionPage', page: number, size: number, totalElements: number, content: Array<
      | { __typename: 'AccountTransferTransaction', id: string, date: string, amount: string, description: string, sourceAccount: { __typename?: 'Account', id: string, name: string }, targetAccount: { __typename?: 'Account', id: string, name: string } }
      | { __typename: 'JarTransferTransaction', id: string, date: string, amount: string, description: string, sourceJar: { __typename?: 'Jar', id: string, name: string }, targetJar: { __typename?: 'Jar', id: string, name: string } }
      | { __typename: 'PaymentTransaction', counterparty: string, direction: Direction, id: string, date: string, amount: string, description: string, jar: { __typename?: 'Jar', id: string, name: string }, account: { __typename?: 'Account', id: string, name: string } }
    > } };


export const GetAccountsDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"query","name":{"kind":"Name","value":"GetAccounts"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"getAccounts"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}},{"kind":"Field","name":{"kind":"Name","value":"description"}},{"kind":"Field","name":{"kind":"Name","value":"balance"}},{"kind":"Field","name":{"kind":"Name","value":"transactions"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"__typename"}},{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"date"}},{"kind":"Field","name":{"kind":"Name","value":"amount"}},{"kind":"Field","name":{"kind":"Name","value":"description"}},{"kind":"InlineFragment","typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"PaymentTransaction"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"jar"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}}]}},{"kind":"Field","name":{"kind":"Name","value":"account"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}}]}},{"kind":"Field","name":{"kind":"Name","value":"counterparty"}},{"kind":"Field","name":{"kind":"Name","value":"direction"}}]}},{"kind":"InlineFragment","typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"AccountTransferTransaction"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"sourceAccount"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}}]}},{"kind":"Field","name":{"kind":"Name","value":"targetAccount"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}}]}}]}}]}}]}}]}}]} as unknown as DocumentNode<GetAccountsQuery, GetAccountsQueryVariables>;
export const CreateAccountDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"CreateAccount"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"req"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"UpdateAccount"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"createAccount"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"req"},"value":{"kind":"Variable","name":{"kind":"Name","value":"req"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}},{"kind":"Field","name":{"kind":"Name","value":"description"}},{"kind":"Field","name":{"kind":"Name","value":"balance"}}]}}]}}]} as unknown as DocumentNode<CreateAccountMutation, CreateAccountMutationVariables>;
export const UpdateAccountDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"UpdateAccount"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"id"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}},{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"req"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"UpdateAccount"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"updateAccount"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"id"},"value":{"kind":"Variable","name":{"kind":"Name","value":"id"}}},{"kind":"Argument","name":{"kind":"Name","value":"req"},"value":{"kind":"Variable","name":{"kind":"Name","value":"req"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}},{"kind":"Field","name":{"kind":"Name","value":"description"}},{"kind":"Field","name":{"kind":"Name","value":"balance"}}]}}]}}]} as unknown as DocumentNode<UpdateAccountMutation, UpdateAccountMutationVariables>;
export const DeleteAccountDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"DeleteAccount"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"id"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"deleteAccount"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"id"},"value":{"kind":"Variable","name":{"kind":"Name","value":"id"}}}]}]}}]} as unknown as DocumentNode<DeleteAccountMutation, DeleteAccountMutationVariables>;
export const GetJarsDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"query","name":{"kind":"Name","value":"GetJars"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"getJars"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}},{"kind":"Field","name":{"kind":"Name","value":"description"}},{"kind":"Field","name":{"kind":"Name","value":"balance"}},{"kind":"Field","name":{"kind":"Name","value":"goal"}},{"kind":"Field","name":{"kind":"Name","value":"transactions"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"__typename"}},{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"date"}},{"kind":"Field","name":{"kind":"Name","value":"amount"}},{"kind":"Field","name":{"kind":"Name","value":"description"}},{"kind":"InlineFragment","typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"PaymentTransaction"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"jar"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}}]}},{"kind":"Field","name":{"kind":"Name","value":"account"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}}]}},{"kind":"Field","name":{"kind":"Name","value":"counterparty"}},{"kind":"Field","name":{"kind":"Name","value":"direction"}}]}},{"kind":"InlineFragment","typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"JarTransferTransaction"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"sourceJar"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}}]}},{"kind":"Field","name":{"kind":"Name","value":"targetJar"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}}]}}]}}]}}]}}]}}]} as unknown as DocumentNode<GetJarsQuery, GetJarsQueryVariables>;
export const CreateJarDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"CreateJar"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"req"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"UpdateJar"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"createJar"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"req"},"value":{"kind":"Variable","name":{"kind":"Name","value":"req"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}},{"kind":"Field","name":{"kind":"Name","value":"description"}},{"kind":"Field","name":{"kind":"Name","value":"balance"}}]}}]}}]} as unknown as DocumentNode<CreateJarMutation, CreateJarMutationVariables>;
export const UpdateJarDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"UpdateJar"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"id"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}},{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"req"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"UpdateJar"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"updateJar"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"id"},"value":{"kind":"Variable","name":{"kind":"Name","value":"id"}}},{"kind":"Argument","name":{"kind":"Name","value":"req"},"value":{"kind":"Variable","name":{"kind":"Name","value":"req"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}},{"kind":"Field","name":{"kind":"Name","value":"description"}},{"kind":"Field","name":{"kind":"Name","value":"balance"}}]}}]}}]} as unknown as DocumentNode<UpdateJarMutation, UpdateJarMutationVariables>;
export const DeleteJarDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"DeleteJar"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"id"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"deleteJar"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"id"},"value":{"kind":"Variable","name":{"kind":"Name","value":"id"}}}]}]}}]} as unknown as DocumentNode<DeleteJarMutation, DeleteJarMutationVariables>;
export const CreateAccountTransferTransactionDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"CreateAccountTransferTransaction"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"req"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"CreateAccountTransferTransaction"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"createAccountTransferTransaction"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"req"},"value":{"kind":"Variable","name":{"kind":"Name","value":"req"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"date"}},{"kind":"Field","name":{"kind":"Name","value":"amount"}},{"kind":"Field","name":{"kind":"Name","value":"description"}}]}}]}}]} as unknown as DocumentNode<CreateAccountTransferTransactionMutation, CreateAccountTransferTransactionMutationVariables>;
export const UpdateAccountTransferTransactionDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"UpdateAccountTransferTransaction"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"id"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}},{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"req"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"CreateAccountTransferTransaction"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"updateAccountTransferTransaction"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"id"},"value":{"kind":"Variable","name":{"kind":"Name","value":"id"}}},{"kind":"Argument","name":{"kind":"Name","value":"req"},"value":{"kind":"Variable","name":{"kind":"Name","value":"req"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"date"}},{"kind":"Field","name":{"kind":"Name","value":"amount"}},{"kind":"Field","name":{"kind":"Name","value":"description"}}]}}]}}]} as unknown as DocumentNode<UpdateAccountTransferTransactionMutation, UpdateAccountTransferTransactionMutationVariables>;
export const DeleteAccountTransferTransactionDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"DeleteAccountTransferTransaction"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"id"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"deleteAccountTransferTransaction"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"id"},"value":{"kind":"Variable","name":{"kind":"Name","value":"id"}}}]}]}}]} as unknown as DocumentNode<DeleteAccountTransferTransactionMutation, DeleteAccountTransferTransactionMutationVariables>;
export const CreateJarTransferTransactionDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"CreateJarTransferTransaction"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"req"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"CreateJarTransferTransaction"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"createJarTransferTransaction"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"req"},"value":{"kind":"Variable","name":{"kind":"Name","value":"req"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"date"}},{"kind":"Field","name":{"kind":"Name","value":"amount"}},{"kind":"Field","name":{"kind":"Name","value":"description"}}]}}]}}]} as unknown as DocumentNode<CreateJarTransferTransactionMutation, CreateJarTransferTransactionMutationVariables>;
export const UpdateJarTransferTransactionDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"UpdateJarTransferTransaction"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"id"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}},{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"req"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"CreateJarTransferTransaction"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"updateJarTransferTransaction"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"id"},"value":{"kind":"Variable","name":{"kind":"Name","value":"id"}}},{"kind":"Argument","name":{"kind":"Name","value":"req"},"value":{"kind":"Variable","name":{"kind":"Name","value":"req"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"date"}},{"kind":"Field","name":{"kind":"Name","value":"amount"}},{"kind":"Field","name":{"kind":"Name","value":"description"}}]}}]}}]} as unknown as DocumentNode<UpdateJarTransferTransactionMutation, UpdateJarTransferTransactionMutationVariables>;
export const DeleteJarTransferTransactionDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"DeleteJarTransferTransaction"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"id"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"deleteJarTransferTransaction"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"id"},"value":{"kind":"Variable","name":{"kind":"Name","value":"id"}}}]}]}}]} as unknown as DocumentNode<DeleteJarTransferTransactionMutation, DeleteJarTransferTransactionMutationVariables>;
export const CreatePaymentTransactionDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"CreatePaymentTransaction"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"req"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"CreatePaymentTransaction"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"createPaymentTransaction"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"req"},"value":{"kind":"Variable","name":{"kind":"Name","value":"req"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"date"}},{"kind":"Field","name":{"kind":"Name","value":"amount"}},{"kind":"Field","name":{"kind":"Name","value":"description"}},{"kind":"Field","name":{"kind":"Name","value":"counterparty"}},{"kind":"Field","name":{"kind":"Name","value":"direction"}}]}}]}}]} as unknown as DocumentNode<CreatePaymentTransactionMutation, CreatePaymentTransactionMutationVariables>;
export const UpdatePaymentTransactionDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"UpdatePaymentTransaction"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"id"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}},{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"req"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"CreatePaymentTransaction"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"updatePaymentTransaction"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"id"},"value":{"kind":"Variable","name":{"kind":"Name","value":"id"}}},{"kind":"Argument","name":{"kind":"Name","value":"req"},"value":{"kind":"Variable","name":{"kind":"Name","value":"req"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"date"}},{"kind":"Field","name":{"kind":"Name","value":"amount"}},{"kind":"Field","name":{"kind":"Name","value":"description"}},{"kind":"Field","name":{"kind":"Name","value":"counterparty"}},{"kind":"Field","name":{"kind":"Name","value":"direction"}}]}}]}}]} as unknown as DocumentNode<UpdatePaymentTransactionMutation, UpdatePaymentTransactionMutationVariables>;
export const DeletePaymentTransactionDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"DeletePaymentTransaction"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"id"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"deletePaymentTransaction"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"id"},"value":{"kind":"Variable","name":{"kind":"Name","value":"id"}}}]}]}}]} as unknown as DocumentNode<DeletePaymentTransactionMutation, DeletePaymentTransactionMutationVariables>;
export const GetTransactionsDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"query","name":{"kind":"Name","value":"GetTransactions"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"getTransactions"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"__typename"}},{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"date"}},{"kind":"Field","name":{"kind":"Name","value":"amount"}},{"kind":"Field","name":{"kind":"Name","value":"description"}},{"kind":"InlineFragment","typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"PaymentTransaction"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"jar"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}}]}},{"kind":"Field","name":{"kind":"Name","value":"account"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}}]}},{"kind":"Field","name":{"kind":"Name","value":"counterparty"}},{"kind":"Field","name":{"kind":"Name","value":"direction"}}]}},{"kind":"InlineFragment","typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"JarTransferTransaction"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"sourceJar"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}}]}},{"kind":"Field","name":{"kind":"Name","value":"targetJar"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}}]}}]}},{"kind":"InlineFragment","typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"AccountTransferTransaction"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"sourceAccount"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}}]}},{"kind":"Field","name":{"kind":"Name","value":"targetAccount"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}}]}}]}}]}}]}}]} as unknown as DocumentNode<GetTransactionsQuery, GetTransactionsQueryVariables>;
export const QueryTransactionsDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"query","name":{"kind":"Name","value":"QueryTransactions"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"req"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"TransactionQuery"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"queryTransactions"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"query"},"value":{"kind":"Variable","name":{"kind":"Name","value":"req"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"content"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"__typename"}},{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"date"}},{"kind":"Field","name":{"kind":"Name","value":"amount"}},{"kind":"Field","name":{"kind":"Name","value":"description"}},{"kind":"InlineFragment","typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"PaymentTransaction"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"jar"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}}]}},{"kind":"Field","name":{"kind":"Name","value":"account"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}}]}},{"kind":"Field","name":{"kind":"Name","value":"counterparty"}},{"kind":"Field","name":{"kind":"Name","value":"direction"}}]}},{"kind":"InlineFragment","typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"JarTransferTransaction"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"sourceJar"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}}]}},{"kind":"Field","name":{"kind":"Name","value":"targetJar"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}}]}}]}},{"kind":"InlineFragment","typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"AccountTransferTransaction"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"sourceAccount"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}}]}},{"kind":"Field","name":{"kind":"Name","value":"targetAccount"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}}]}}]}}]}},{"kind":"Field","name":{"kind":"Name","value":"page"}},{"kind":"Field","name":{"kind":"Name","value":"size"}},{"kind":"Field","name":{"kind":"Name","value":"totalElements"}}]}}]}}]} as unknown as DocumentNode<QueryTransactionsQuery, QueryTransactionsQueryVariables>;