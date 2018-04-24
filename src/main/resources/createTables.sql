-- Table: public.users
-- DROP TABLE public.users;
CREATE TABLE public.users
(
  id integer NOT NULL,
  password character varying(255) COLLATE pg_catalog."default",
  role character varying(255) COLLATE pg_catalog."default",
  username character varying(255) COLLATE pg_catalog."default",
  CONSTRAINT users_pkey PRIMARY KEY (id),
  CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6 UNIQUE (username)
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.users
  OWNER to postgres;

-- Table: public.service
-- DROP TABLE public.service;
CREATE TABLE public.service
(
  id integer NOT NULL,
  description character varying(255) COLLATE pg_catalog."default",
  name character varying(255) COLLATE pg_catalog."default",
  price double precision,
  CONSTRAINT service_pkey PRIMARY KEY (id),
  CONSTRAINT uk_adgojnrwwx9c3y3qa2q08uuqp UNIQUE (name)
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.service
  OWNER to postgres;

-- Table: public.company
-- DROP TABLE public.company;
CREATE TABLE public.company
(
  id integer NOT NULL,
  account_number character varying(255) COLLATE pg_catalog."default",
  email character varying(255) COLLATE pg_catalog."default",
  name character varying(255) COLLATE pg_catalog."default",
  user_id integer,
  CONSTRAINT company_pkey PRIMARY KEY (id),
  CONSTRAINT uk_niu8sfil2gxywcru9ah3r4ec5 UNIQUE (name),
  CONSTRAINT fksxe9t9istcdt2mtdbvgh83a9g FOREIGN KEY (user_id)
  REFERENCES public.users (id) MATCH SIMPLE
  ON UPDATE NO ACTION
  ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.company
  OWNER to postgres;

-- Table: public.customer
-- DROP TABLE public.customer;
CREATE TABLE public.customer
(
  id integer NOT NULL,
  bank_account character varying(255) COLLATE pg_catalog."default",
  email character varying(255) COLLATE pg_catalog."default",
  first_name character varying(255) COLLATE pg_catalog."default",
  last_name character varying(255) COLLATE pg_catalog."default",
  user_id integer,
  CONSTRAINT customer_pkey PRIMARY KEY (id),
  CONSTRAINT fkra1cb3fu95r1a0m7aksow0nk4 FOREIGN KEY (user_id)
  REFERENCES public.users (id) MATCH SIMPLE
  ON UPDATE NO ACTION
  ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.customer
  OWNER to postgres;

-- Table: public.contract
-- DROP TABLE public.contract;
CREATE TABLE public.contract
(
  id integer NOT NULL,
  expire_date bytea,
  issue_date bytea,
  sum double precision,
  company_id integer,
  customer_id integer,
  service_id integer,
  CONSTRAINT contract_pkey PRIMARY KEY (id),
  CONSTRAINT fk38oaxtv1vfg7g4pclrar2adxq FOREIGN KEY (service_id)
  REFERENCES public.service (id) MATCH SIMPLE
  ON UPDATE NO ACTION
  ON DELETE NO ACTION,
  CONSTRAINT fkm8jvj0jm2ihmy0fvrupie0ndk FOREIGN KEY (company_id)
  REFERENCES public.company (id) MATCH SIMPLE
  ON UPDATE NO ACTION
  ON DELETE NO ACTION,
  CONSTRAINT fkq28qogy68douoc4gkgcy3ow9p FOREIGN KEY (customer_id)
  REFERENCES public.customer (id) MATCH SIMPLE
  ON UPDATE NO ACTION
  ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.contract
  OWNER to postgres;

-- Table: public.invoice
-- DROP TABLE public.invoice;
CREATE TABLE public.invoice
(
  id integer NOT NULL,
  due_date bytea,
  issue_date bytea,
  status character varying(255) COLLATE pg_catalog."default",
  sum double precision,
  contract_id integer,
  CONSTRAINT invoice_pkey PRIMARY KEY (id),
  CONSTRAINT fkqh9ibaacfusht7an2afwkrq5 FOREIGN KEY (contract_id)
  REFERENCES public.contract (id) MATCH SIMPLE
  ON UPDATE NO ACTION
  ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.invoice
  OWNER to postgres;

-- Table: public.company_services
-- DROP TABLE public.company_services;
CREATE TABLE public.company_services
(
  company_id integer NOT NULL,
  service_id integer NOT NULL,
  CONSTRAINT fkartm3ay3hwk592nr7hyf66err FOREIGN KEY (service_id)
  REFERENCES public.service (id) MATCH SIMPLE
  ON UPDATE NO ACTION
  ON DELETE NO ACTION,
  CONSTRAINT fkknxkm5akxjfhh3d1r3fk3vy5r FOREIGN KEY (company_id)
  REFERENCES public.company (id) MATCH SIMPLE
  ON UPDATE NO ACTION
  ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.company_services
  OWNER to postgres;