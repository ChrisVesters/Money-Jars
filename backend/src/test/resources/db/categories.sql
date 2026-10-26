INSERT INTO categories(id, name, description)
OVERRIDING SYSTEM VALUE
VALUES 
	(1, 'Groceries', 'Food and drinks'),
	(2, 'Rent', 'Monthly rent of the apartment'),
	(3, 'Fuel', 'Fuel for the car');

SELECT setval('categories_id_seq', (SELECT MAX(id) from "categories"));