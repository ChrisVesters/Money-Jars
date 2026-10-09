INSERT INTO accounts(id, name, description)
OVERRIDING SYSTEM VALUE
VALUES 
	(1, 'Checking', 'Everyday bank account'),
	(2, 'Wallet', 'Cash in pocket'),
	(3, 'Savings', 'High-interest savings account'),
	(4, 'Credit Card', 'Settled at the end of the month');

SELECT setval('accounts_id_seq', (SELECT MAX(id) from "accounts"));
