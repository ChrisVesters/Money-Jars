# Money-Jars
Budgetting application

Money Jars is based on the jar system: instead of seeing your money as one pile, you split it over a few jars, each with its own purpose (Necessities, Savings, Play, ...). The application keeps track of where your money physically is (accounts), what it is set aside for (jars), what it was spent on (categories), and what you plan to earn and spend (budgets).

## Concepts

### Jars
A jar is a purpose that money is set aside for, such as Necessities or Savings. A jar has a name, a description and an optional goal (a positive target amount). Its balance is the sum of everything that went in and out of it.

Jars are virtual: they don't care which bank account the money is on. Keep them few and coarse, and use categories for the detail.

### Accounts
An account is where money actually is, such as a bank account or a wallet. Like a jar, it has a name, a description and a balance.

The total of all jar balances equals the total of all account balances, since every payment moves money in both a jar and an account.

### Categories
A category describes what money was earned from or spent on, such as Groceries or Salary. It is only a label on a payment and has no balance. A payment has at most one category, so totals per category always add up.

Categories don't belong to jars: a payment can combine any category with any jar.

### Transactions
A transaction moves money on a given date. Amounts are always positive; the type and direction decide which way the money goes. There are three types:

| Type | What it does | Affects |
| --- | --- | --- |
| **Payment** | Money coming in (`INCOMING`) or going out (`OUTGOING`), with a counterparty and an optional category. | One jar and one account |
| **Jar transfer** | Moves money from one jar to another. | Two jars |
| **Account transfer** | Moves money from one account to another, for example to a savings account. | Two accounts |

Transfers move money around without earning or spending it, so they have no category.

Transactions on the same date are ordered by a sequence number, so the order within a day is always known.

### Ledger
Every transaction creates an entry in the ledger of each jar and account it touches. An entry records the balance before and after the transaction. Balances are read from the latest entry, so the ledger shows how each jar and account got to its current balance.

When a transaction is added, changed or removed in the past, the entries after it are shifted to keep the balances correct.

A jar, account or category that is used by a transaction is **in use** and can't be deleted.

### Budgets
A budget is a plan of what you expect to earn and spend over a period, with an inclusive start and end date. It is made of budget lines, each with a category, a planned amount and a direction (`INCOME` or `EXPENSE`).

Budgets are separate from jars: a jar holds money you actually have, a budget is only a plan. Budgets span all jars, and creating, changing or deleting a budget never changes any balance.
