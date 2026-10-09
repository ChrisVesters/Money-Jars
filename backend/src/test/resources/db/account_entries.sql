INSERT INTO account_entries(id, transaction_id, account_id, balance_before, balance_after)
OVERRIDING SYSTEM VALUE
VALUES
  (1, 1, 1, 2548.05, 1348.05),
  (2, 2, 1, 2682.55, 2548.05),
  (3, 3, 2, 0.00, -15.00),
  (4, 4, 1, 0.00, 2182.55),
  (5, 5, 3, 0.00, 2500.00),
  (6, 6, 1, 2182.55, 2682.55),
  (7, 7, 4, 0.00, -200.00),
  (8, 8, 4, -200.00, -265.40),
  (9, 9, 2, -15.00, -113.20);

SELECT setval('account_entries_id_seq', (SELECT MAX(id) from "account_entries"));
