-- public.billing_info definition

-- Drop table

-- DROP TABLE public.billing_info;

CREATE TABLE IF NOT EXISTS public.billing_info (
	billing_id uuid NOT NULL,
	member_id uuid NOT NULL,
	joining_date date NOT NULL,
	due_date date NOT NULL,
	category varchar(255) NOT NULL,
	category_amount int4 NOT NULL,
	paid_amount int4 NOT NULL,
	balance_amount int4 NOT NULL,
	payment_status varchar(255) NOT NULL,
	created_at timestamp(6) NOT NULL,
	updated_at timestamp(6) NOT NULL,
	CONSTRAINT billing_info_pkey PRIMARY KEY (billing_id),
    CONSTRAINT fk_member_id FOREIGN KEY (member_id) REFERENCES public.members(id)
);