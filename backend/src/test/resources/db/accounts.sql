INSERT INTO accounts(id, name, description, balance)
OVERRIDING SYSTEM VALUE
VALUES 
	(1, 'Checking', 'Everyday bank account', 1245.30),
	(2, 'Wallet', 'Cash in pocket', 82.45);

SELECT setval('accounts_id_seq', (SELECT MAX(id) from "accounts"));
