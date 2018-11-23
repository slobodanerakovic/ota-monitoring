-- Sql to be executed from favorite db client (pgadmin for example)
-- Schema creation
CREATE SCHEMA IF NOT EXISTS monitoring AUTHORIZATION test;
GRANT ALL ON SCHEMA monitoring TO test;

--reset everything
--recreate dbs
DROP TABLE IF EXISTS monitoring.vehicle CASCADE;
DROP TABLE IF EXISTS monitoring.vehicle_package CASCADE;
DROP TABLE IF EXISTS monitoring.vehicle_geolocation CASCADE;
DROP TABLE IF EXISTS monitoring.feature CASCADE;
DROP TABLE IF EXISTS monitoring.vehicle_package_feature CASCADE;

--recreate sequences
DROP SEQUENCE IF EXISTS monitoring.seq_vehicle;
DROP SEQUENCE IF EXISTS monitoring.seq_vehicle_package;
DROP SEQUENCE IF EXISTS monitoring.seq_vehicle_geolocation;
DROP SEQUENCE IF EXISTS monitoring.seq_feature;
DROP SEQUENCE IF EXISTS monitoring.seq_vehicle_package_feature;

--Create table vehicle
CREATE TABLE monitoring.vehicle (
id bigint NOT NULL,
version integer NOT NULL DEFAULT 0,
creation_date timestamp without time zone NOT NULL,
modification_date timestamp without time zone NOT NULL,
vehicle_id character varying(50) NOT NULL,
model character varying(30) NOT NULL,
fleet_id character varying(30) NOT NULL,
CONSTRAINT vehicle_pkey PRIMARY KEY (id)
)
WITH (
OIDS=FALSE
);
ALTER TABLE monitoring.vehicle OWNER TO test;
GRANT ALL ON TABLE monitoring.vehicle TO test;

CREATE SEQUENCE monitoring.seq_vehicle START 1000;
ALTER TABLE monitoring.seq_vehicle OWNER TO test;


-- Create table vehicle_package
CREATE TABLE monitoring.vehicle_package (
id bigint NOT NULL,
version integer NOT NULL DEFAULT 0,
creation_date timestamp without time zone NOT NULL,
modification_date timestamp without time zone NOT NULL,
vehicle_id bigint NOT NULL,
package_name character varying(30) NOT NULL,
package_version double precision NOT NULL,
success_download boolean,
success_installment boolean,
CONSTRAINT vehicle_package_pkey PRIMARY KEY (id),
CONSTRAINT vehicle_package_vehicle_fkey FOREIGN KEY (vehicle_id)
REFERENCES monitoring.vehicle (id) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
OIDS=FALSE
);
ALTER TABLE monitoring.vehicle_package OWNER TO test;
GRANT ALL ON TABLE monitoring.vehicle_package TO test;

CREATE SEQUENCE monitoring.seq_vehicle_package START 1000;
ALTER TABLE monitoring.seq_vehicle_package OWNER TO test;


-- Create table vehicle_geolocation
CREATE TABLE monitoring.vehicle_geolocation (
id bigint NOT NULL,
version integer NOT NULL DEFAULT 0,
creation_date timestamp without time zone NOT NULL,
modification_date timestamp without time zone NOT NULL,
vehicle_id bigint NOT NULL,
country varchar(30) NOT NULL,
latitude double precision NOT NULL,
longitude double precision NOT NULL,
CONSTRAINT vehicle_geolocation_pkey PRIMARY KEY (id),
CONSTRAINT vehicle_fkey FOREIGN KEY (vehicle_id)
REFERENCES monitoring.vehicle (id) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
OIDS=FALSE
);
ALTER TABLE monitoring.vehicle_geolocation OWNER TO test;
GRANT ALL ON TABLE monitoring.vehicle_geolocation TO test;

CREATE SEQUENCE monitoring.seq_vehicle_geolocation START 1000;
ALTER TABLE monitoring.seq_vehicle_geolocation OWNER TO test;


-- Create table feature
CREATE TABLE monitoring.feature (
id bigint NOT NULL,
version integer NOT NULL DEFAULT 0,
creation_date timestamp without time zone NOT NULL,
modification_date timestamp without time zone NOT NULL,
name varchar(30) NOT NULL,
description text,
CONSTRAINT feature_pkey PRIMARY KEY (id)
)
WITH (
OIDS=FALSE
);
ALTER TABLE monitoring.feature OWNER TO test;
GRANT ALL ON TABLE monitoring.feature TO test;

CREATE SEQUENCE monitoring.seq_feature START 1000;
ALTER TABLE monitoring.seq_feature OWNER TO test;


-- Create table vehicle_package_feature
CREATE TABLE monitoring.vehicle_package_feature (
version integer NOT NULL DEFAULT 0,
creation_date timestamp without time zone NOT NULL DEFAULT now(),
vehicle_package_id bigint NOT NULL,
feature_id bigint NOT NULL,
CONSTRAINT vehicle_package_feature_pkey PRIMARY KEY (vehicle_package_id, feature_id),
CONSTRAINT vehicle_package_id_fkey FOREIGN KEY (vehicle_package_id)
REFERENCES monitoring.vehicle_package (id) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE NO ACTION,
CONSTRAINT feature_id_fkey FOREIGN KEY (feature_id)
REFERENCES monitoring.feature (id) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
OIDS=FALSE
);
ALTER TABLE monitoring.vehicle_package_feature OWNER TO test;
GRANT ALL ON TABLE monitoring.vehicle_package_feature TO test;

CREATE SEQUENCE monitoring.seq_vehicle_package_feature START 1000;
ALTER TABLE monitoring.seq_vehicle_package_feature OWNER TO test;