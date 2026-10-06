INSERT INTO transactions(id, date, sequence, amount, description)
OVERRIDING SYSTEM VALUE
VALUES
  (1, '2026-01-10', 1, 1200.00, 'January rent'),
  (2, '2026-01-10', 0, 134.50, 'Weekly groceries'),
  (3, '2026-01-15', 0, 15.00, 'Coffee'),
  (4, '2026-01-01', 0, 2182.55, 'January salary'),
  (5, '2026-01-01', 1, 2500.00, 'Year-end bonus'),
  (6, '2026-01-01', 2, 500.00, 'Car allowance'),
  (7, '2026-01-20', 0, 200.00, 'Flight tickets'),
  (8, '2026-02-03', 0, 65.40, 'Fuel'),
  (9, '2026-02-03', 1, 98.20, 'Fresh produce');

SELECT setval('transactions_id_seq', (SELECT MAX(id) from "transactions"));


INSERT INTO payment_transactions(transaction_id, jar_id, account_id, counterparty, direction)
VALUES
  (1, 1, 1, 'Landlord', 1),
  (2, 1, 1, 'Supermarket', 1),
  (3, 1, 2, 'Cafe', 1),
  (4, 1, 1, 'Employer', 0),
  (5, 2, 3, 'Employer', 0),
  (6, 3, 1, 'Employer', 0),
  (7, 2, 4, 'Airline', 1),
  (8, 3, 4, 'Gas station', 1),
  (9, 1, 2, 'Farmers market', 1);
