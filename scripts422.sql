CREATE TABLE cars(
	id serial primary key,
	brand varchar,
	model varchar,
	sale numeric(10,2)
);

CREATE TABLE persons(
	id SERIAL,
	name varchar primary key,
	age integer,
	root boolean,
	car_id integer references cars(id)
);
