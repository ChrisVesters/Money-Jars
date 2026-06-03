INSERT INTO transactions(id, date, amount, jar_id, beneficiary, description)
OVERRIDING SYSTEM VALUE
VALUES 
	(1, '2026-01-01', -1200.00, 1, 'Landlord', 'January rent'),
	(2, '2026-01-03', -134.50, 1, 'Supermarket', 'Weekly groceries');

SELECT setval('transactions_id_seq', (SELECT MAX(id) from "transactions"));
