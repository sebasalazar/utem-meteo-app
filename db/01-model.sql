BEGIN TRANSACTION;

DROP TABLE IF EXISTS stations CASCADE;
CREATE TABLE stations (
    pk bigserial NOT NULL,
    code varchar(255) NOT NULL,
    name varchar(255) NOT NULL,
    latitude double precision NOT NULL,
    longitude double precision NOT NULL,
    altitude int NOT NULL,
    active boolean NOT NULL DEFAULT true,
    created timestamptz NOT NULL DEFAULT NOW(),
    updated timestamptz NOT NULL DEFAULT NOW(),
    PRIMARY KEY (pk)
);
CREATE UNIQUE INDEX ON stations(UPPER(code));



DROP TABLE IF EXISTS observations CASCADE;
CREATE TABLE observations (
    pk bigserial NOT NULL,
    station_fk bigint NOT NULL,
    code varchar(255) NOT NULL,
    date_time timestamptz NOT NULL,
    temperature double precision NOT NULL,
    humidity double precision NOT NULL,
    wind_speed double precision NOT NULL,
    wind_direction double precision NOT NULL,
    solar_radiation double precision NOT NULL,
    absolute_pressure double precision NOT NULL,
    precipitation double precision NOT NULL,
    dew_point double precision NOT NULL,
    wind_gust double precision NOT NULL,
    pressure double precision NOT NULL,
    rain_rate double precision NOT NULL,
    ultraviolet double precision NOT NULL,
    daily_rainfall double precision NOT NULL,
    created timestamptz NOT NULL DEFAULT NOW(),
    updated timestamptz NOT NULL DEFAULT NOW(),
    FOREIGN KEY (station_fk) REFERENCES stations(pk) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (pk)
);
CREATE UNIQUE INDEX ON observations(station_fk, UPPER(code));





COMMIT;



