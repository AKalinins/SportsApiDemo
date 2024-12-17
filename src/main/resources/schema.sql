CREATE SCHEMA IF NOT EXISTS sportsapi;
SET SCHEMA sportsapi;
CREATE TABLE events
(
    id   bigint PRIMARY KEY AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    type varchar(255) NOT NULL,
    status varchar(255) NOT NULL,
    start_time timestamp NOT NULL
);
