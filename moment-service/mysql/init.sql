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
    recurrence   ENUM ('ONETIME', 'REGULAR')      NOT NULL DEFAULT 'ONETIME',
    price        DECIMAL(10, 2)                   NOT NULL,
    status       ENUM ('DRAFT', 'LIVE', 'PAUSED') NOT NULL DEFAULT 'DRAFT',
    ticket_count INT                              NOT NULL,
    created_at   TIMESTAMP                        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(100)                     NOT NULL,
    updated_at   TIMESTAMP                        NULL ON UPDATE CURRENT_TIMESTAMP,
    updated_by   VARCHAR(100)                     NULL
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET latin1;

INSERT INTO moments (host_id, category_id, location_id, title, start_date, recurrence, price, status, ticket_count, created_by, updated_by)
VALUES
    (1, 101, 201, 'Concert in the Park', '2025-06-01 19:00:00', 'ONETIME', 50.00, 'LIVE', 500, 'admin', 'admin'),
    (2, 102, 202, 'Art Exhibition', '2025-07-10 18:00:00', 'REGULAR', 25.00, 'LIVE', 200, 'admin', 'admin'),
    (3, 103, 203, 'Comedy Show', '2025-08-15 20:00:00', 'ONETIME', 30.00, 'DRAFT', 150, 'admin', 'admin'),
    (4, 104, 204, 'Food Festival', '2025-09-05 10:00:00', 'REGULAR', 15.00, 'LIVE', 800, 'admin', 'admin'),
    (5, 101, 205, 'Jazz Night', '2025-10-01 21:00:00', 'ONETIME', 40.00, 'PAUSED', 100, 'admin', 'admin'),
    (6, 102, 206, 'Science Fair', '2025-11-15 09:00:00', 'REGULAR', 20.00, 'DRAFT', 300, 'admin', 'admin'),
    (7, 105, 207, 'Tech Conference', '2025-12-01 09:00:00', 'REGULAR', 100.00, 'LIVE', 150, 'admin', 'admin'),
    (8, 106, 208, 'Yoga Retreat', '2025-01-10 08:00:00', 'ONETIME', 200.00, 'DRAFT', 50, 'admin', 'admin'),
    (9, 107, 209, 'Book Launch', '2025-02-20 18:00:00', 'ONETIME', 10.00, 'LIVE', 100, 'admin', 'admin'),
    (10, 108, 210, 'Music Festival', '2025-03-30 12:00:00', 'REGULAR', 70.00, 'PAUSED', 1200, 'admin', 'admin');