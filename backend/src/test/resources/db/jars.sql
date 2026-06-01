INSERT INTO jars(id, name, description, balance)
OVERRIDING SYSTEM VALUE
VALUES 
	(1, 'Household', 'General expenses', 734.85),
	(2, 'Holiday', 'We need some time off', 2300.00);

SELECT setval('jars_id_seq', (SELECT MAX(id) from "jars"));
