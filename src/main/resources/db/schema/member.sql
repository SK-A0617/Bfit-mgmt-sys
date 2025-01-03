-- public.members definition

-- Drop table

-- DROP TABLE public.members;

CREATE TABLE IF NOT EXISTS public.members (
	id uuid NOT NULL,
	profile_url varchar(255) NULL,
	member_name varchar(255) NOT NULL,
	email varchar(255) NOT NULL,
	phone_number varchar(255) NOT NULL,
	status bool NOT NULL,
	joining_date date NOT NULL,
	created_at timestamp(6) NOT NULL,
	updated_at timestamp(6) NOT NULL,
	CONSTRAINT members_pkey PRIMARY KEY (id)
);