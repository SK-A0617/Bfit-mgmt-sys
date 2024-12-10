-- public."admin" definition

-- Drop table

-- DROP TABLE public."admin";

CREATE TABLE IF NOT EXISTS public."admin" (
	id uuid NOT NULL,
	first_name varchar(255) NOT NULL,
	last_name varchar(255) NOT NULL,
	email varchar(255) NOT NULL,
	"password" varchar(255) NOT NULL,
	phone_number varchar(255) NOT NULL,
	"role" varchar(255),
	starting_date date,
	created_at timestamp(6),
	updated_at timestamp(6),
	CONSTRAINT admin_pkey PRIMARY KEY (id),
	CONSTRAINT unique_email UNIQUE (email)
);