--DROPPING foreign keys
ALTER TABLE IF EXISTS trackers
    DROP CONSTRAINT IF EXISTS fk_trackers_to_users;

ALTER TABLE IF EXISTS data
    DROP CONSTRAINT IF EXISTS fk_data_to_trackers;

ALTER TABLE IF EXISTS parameters
    DROP CONSTRAINT IF EXISTS fk_parameters_to_data;

ALTER TABLE IF EXISTS trackers_last_data
    DROP CONSTRAINT IF EXISTS fk_trackers_last_data_to_trackers;

ALTER TABLE IF EXISTS trackers_last_data
    DROP CONSTRAINT IF EXISTS fk_trackers_last_data_to_data;

--DROPPING tables
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS trackers;
DROP TABLE IF EXISTS data;
DROP TABLE IF EXISTS parameters;
DROP TABLE IF EXISTS trackers_last_data;

CREATE TABLE users
(
    id                 SERIAL       NOT NULL PRIMARY KEY,
    email              VARCHAR(256) NOT NULL,
    encrypted_password VARCHAR(256) NOT NULL,
    role               VARCHAR(16)  NOT NULL
);

ALTER TABLE users
    ADD CONSTRAINT correct_email CHECK (email ~ '[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+');

ALTER TABLE users
    ADD CONSTRAINT correct_role CHECK (role IN ('USER', 'ADMIN'));

CREATE TABLE trackers
(
    id                 SERIAL       NOT NULL PRIMARY KEY,
    imei               CHAR(20)     NOT NULL,
    encrypted_password VARCHAR(256) NOT NULL,
    phone_number       CHAR(9)      NOT NULL,
    user_id            INTEGER      NOT NULL
);

ALTER TABLE trackers
    ADD CONSTRAINT fk_trackers_to_users FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE;

ALTER TABLE trackers
    ADD CONSTRAINT correct_imei CHECK (imei ~ '[0-9]{20}');

ALTER TABLE trackers
    ADD CONSTRAINT correct_phone_number CHECK (phone_number ~ '[0-9]{9}');

CREATE TABLE data
(
    id                     BIGSERIAL NOT NULL PRIMARY KEY,
    date                   DATE      NOT NULL,
    time                   TIME      NOT NULL,

    latitude_degrees       INTEGER   NOT NULL,
    latitude_minutes       INTEGER   NOT NULL,
    latitude_minute_share  INTEGER   NOT NULL,
    latitude_type          CHAR(1)   NOT NULL,

    longitude_degrees      INTEGER   NOT NULL,
    longitude_minutes      INTEGER   NOT NULL,
    longitude_minute_share INTEGER   NOT NULL,
    longitude_type         CHAR(1)   NOT NULL,

    speed                  INTEGER   NOT NULL,
    course                 INTEGER   NOT NULL,
    altitude                 INTEGER   NOT NULL,
    amount_of_satellites   INTEGER   NOT NULL,

	reduction_precision DECIMAL      NOT NULL,
    inputs              INTEGER      NOT NULL,
    outputs             INTEGER      NOT NULL,
    analog_inputs       DOUBLE PRECISION[] NOT NULL,
    driver_key_code     VARCHAR(256) NOT NULL,

    tracker_id             INTEGER   NOT NULL
);

ALTER SEQUENCE data_id_seq INCREMENT 50;

ALTER TABLE data
    ADD CONSTRAINT fk_data_to_trackers FOREIGN KEY (tracker_id) REFERENCES trackers (id)
        ON DELETE CASCADE;

ALTER TABLE data
    ADD CONSTRAINT latitude_type_should_be_correct
        CHECK (data.latitude_type IN ('N', 'S'));

ALTER TABLE data
    ADD CONSTRAINT longitude_type_should_be_correct
        CHECK (data.longitude_type IN ('E', 'W'));

CREATE TABLE parameters
(
    id               BIGSERIAL    NOT NULL PRIMARY KEY,
    name             VARCHAR(256) NOT NULL,
    type             VARCHAR(64)  NOT NULL,
    value            VARCHAR(256) NOT NULL,
    data_id BIGINT       NOT NULL
);

ALTER SEQUENCE parameters_id_seq INCREMENT 50;

ALTER TABLE parameters
    ADD CONSTRAINT fk_parameters_to_data
        FOREIGN KEY (data_id) REFERENCES data (id)
            ON DELETE CASCADE;

ALTER TABLE parameters
    ADD CONSTRAINT correct_name CHECK (char_length(name) != 0);

ALTER TABLE parameters
    ADD CONSTRAINT correct_type CHECK (type IN ('INTEGER', 'DOUBLE', 'STRING'));

CREATE TABLE trackers_last_data(
	id SERIAL NOT NULL PRIMARY KEY,
	tracker_id INTEGER NOT NULL UNIQUE,
	data_id BIGINT UNIQUE
);

ALTER TABLE trackers_last_data
	ADD CONSTRAINT fk_trackers_last_data_to_trackers FOREIGN KEY(tracker_id)
		REFERENCES trackers(id)
			ON DELETE CASCADE;

ALTER TABLE trackers_last_data
	ADD CONSTRAINT fk_trackers_last_data_to_data FOREIGN KEY(data_id)
		REFERENCES data(id);

CREATE OR REPLACE FUNCTION on_insert_tracker() RETURNS TRIGGER AS
'
    BEGIN
        INSERT INTO trackers_last_data(tracker_id)
        VALUES (NEW.id);
        RETURN NEW;
    END;
' LANGUAGE plpgsql;

CREATE TRIGGER tr_on_insert_tracker
    AFTER INSERT
    ON trackers
    FOR EACH ROW
EXECUTE PROCEDURE on_insert_tracker();

CREATE OR REPLACE FUNCTION on_insert_data() RETURNS TRIGGER AS
'
    BEGIN
		UPDATE trackers_last_data
        SET data_id = NEW.id
        WHERE trackers_last_data.tracker_id = NEW.tracker_id;
        RETURN NEW;
    END;
' LANGUAGE plpgsql;

CREATE TRIGGER tr_on_insert_data
    AFTER INSERT
    ON data
    FOR EACH ROW
EXECUTE PROCEDURE on_insert_data();

