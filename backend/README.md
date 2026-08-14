=== Concept

Separation between transactions and ledger.
Transactions contain the information about what has happened:
- PaymentTransaction
- JarTransferTransaction
- AccountTransferTransaction

It becomes easy to find transactions belonging to a jar or account.
And at the same time, updating a transaction does not require to keep things in sync. Note: transactions don't have a sequence, that is part of the ledger.
We may want to have special DTO objects, such that each transaction type contains the same information.
When showing all transactions for a specific jar, there is no point in seeing from which account money was taken. It may still be relevant to see that money was moved from another jar?

There is no point in requesting just the transfer transactions.
We will have to see which changes we allow on a transaction. Changing from one type to another will not be allowed. Other changes may be allowed.

The Ledger contains the bookkeeping and points to the transaction.
- JarLedgerEntry
- AccountLedgerEntry

Updating a transaction amount, means we have to look for the ledger entries to are linked to it and update it (as well as any subsequent entry).
