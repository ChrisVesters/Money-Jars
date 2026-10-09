INSERT INTO jars(id, name, description)
OVERRIDING SYSTEM VALUE
VALUES 
	(1, 'Household', 'General expenses'),
	(2, 'Holiday', 'We need some time off'),
	(3, 'Car', 'Fuel and maintenance');

SELECT setval('jars_id_seq', (SELECT MAX(id) from "jars"));
