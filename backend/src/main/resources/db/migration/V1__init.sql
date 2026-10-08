CREATE TABLE jars(
	id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
	name TEXT NOT NULL,
	description TEXT NOT NULL,
	balance NUMERIC NOT NULL,

	PRIMARY KEY (id)
);


CREATE TABLE accounts(
	id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
	name TEXT NOT NULL,
	description TEXT NOT NULL,
	balance NUMERIC NOT NULL,

	PRIMARY KEY (id)
);


CREATE TABLE transactions(
	id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
	date DATE NOT NULL,
	sequence INTEGER NOT NULL,
	amount NUMERIC NOT NULL,
	description TEXT NOT NULL,

	PRIMARY KEY (id),
	UNIQUE (date, sequence)
);


CREATE TABLE payment_transactions(
	transaction_id BIGINT NOT NULL,
	jar_id BIGINT NOT NULL,
	account_id BIGINT NOT NULL,
	counterparty TEXT NOT NULL,
	direction SMALLINT NOT NULL,

	PRIMARY KEY (transaction_id),
	FOREIGN KEY (transaction_id) REFERENCES transactions(id),
	FOREIGN KEY (jar_id) REFERENCES jars(id),
	FOREIGN KEY (account_id) REFERENCES accounts(id)
);

CREATE INDEX ON payment_transactions(jar_id);
CREATE INDEX ON payment_transactions(account_id);


CREATE TABLE jar_entries(
	id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
	transaction_id BIGINT NOT NULL,
	jar_id BIGINT NOT NULL,
	balance_before NUMERIC NOT NULL,
	balance_after NUMERIC NOT NULL,

	PRIMARY KEY (id),
	UNIQUE (transaction_id, jar_id),
	FOREIGN KEY (jar_id) REFERENCES jars(id),
	FOREIGN KEY (transaction_id) REFERENCES transactions(id)
);

CREATE INDEX ON jar_entries(jar_id);
CREATE INDEX ON jar_entries(transaction_id);