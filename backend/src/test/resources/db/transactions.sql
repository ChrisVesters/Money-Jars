INSERT INTO transactions(id, date, sequence, amount, beneficiary, description)
OVERRIDING SYSTEM VALUE
VALUES
  (1, '2026-01-10', 2, 1200.00, 'Landlord', 'January rent'),
  (2, '2026-01-10', 1, 134.50, 'Supermarket', 'Weekly groceries'),
  (3, '2026-01-15', 1, 15.00, 'Cafe', 'Coffee');

SELECT setval('transactions_id_seq', (SELECT MAX(id) from "transactions"));


INSERT INTO payment_transactions(transaction_id, jar_id, account_id, direction)
VALUES
  (1, 1, 1, 1),
  (2, 1, 1, 1),
  (3, 1, 2, 1);
