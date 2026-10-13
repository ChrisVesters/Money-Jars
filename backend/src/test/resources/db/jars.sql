INSERT INTO jars(id, name, description, goal)
OVERRIDING SYSTEM VALUE
VALUES 
	(1, 'Household', 'General expenses', NULL),
	(2, 'Holiday', 'We need some time off', 3000.00),
	(3, 'Car', 'Fuel and maintenance', 1500.00);

SELECT setval('jars_id_seq', (SELECT MAX(id) from "jars"));
