-- public.coach definition

-- Drop table

-- DROP TABLE public.coach;

CREATE TABLE IF NOT EXISTS public.coach (
	id uuid NOT NULL,
	profile_url varchar(255) NULL,
	coach_name varchar(255) NOT NULL,
	email varchar(255) NOT NULL,
	phone_number varchar(255) NOT NULL,
	status bool NOT NULL,
	joining_date date NOT NULL,
	created_at timestamp(6) NOT NULL,
	updated_at timestamp(6) NOT NULL,
	CONSTRAINT coach_pkey PRIMARY KEY (id)
);