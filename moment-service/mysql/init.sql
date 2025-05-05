CREATE DATABASE IF NOT EXISTS moment_service_db;

USE moment_service_db;

DROP TABLE IF EXISTS moments;
CREATE TABLE moments
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    host_id      BIGINT                           NOT NULL,
    category_id  BIGINT                           NOT NULL,
    location_id  BIGINT                           NOT NULL,
    title        VARCHAR(100)                     NOT NULL,
    thumbnail    MEDIUMBLOB                                DEFAULT NULL,
    start_date   TIMESTAMP                        NOT NULL,
    recurrence   ENUM ('onetime', 'regular')      NOT NULL DEFAULT 'onetime',
    price        DECIMAL(10, 2)                   NOT NULL,
    status       ENUM ('draft', 'live', 'paused') NOT NULL DEFAULT 'draft',
    ticket_count INT                              NOT NULL,
    created_at   TIMESTAMP                        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(100)                     NOT NULL,
    updated_at   TIMESTAMP                        NULL ON UPDATE CURRENT_TIMESTAMP,
    updated_by   VARCHAR(100)                     NULL
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET latin1;