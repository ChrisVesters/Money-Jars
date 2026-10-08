INSERT INTO jar_entries(id, transaction_id, jar_id, balance_before, balance_after)
OVERRIDING SYSTEM VALUE
VALUES
  (1, 1, 1, 2048.05, 848.05),
  (2, 2, 1, 2182.55, 2048.05),
  (3, 3, 1, 848.05, 833.05),
  (4, 4, 1, 0.00, 2182.55),
  (5, 5, 2, 0.00, 2500.00),
  (6, 6, 3, 0.00, 500.00),
  (7, 7, 2, 2500.00, 2300.00),
  (8, 8, 3, 500.00, 434.60),
  (9, 9, 1, 833.05, 734.85);

SELECT setval('jar_entries_id_seq', (SELECT MAX(id) from "jar_entries"));
