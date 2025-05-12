SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE moments
(
    id                BIGINT AUTO_INCREMENT,
    host_id           BIGINT                           NOT NULL,
    category_id       BIGINT                           NOT NULL,
    location_id       BIGINT                           NOT NULL,
    title             VARCHAR(100)                     NOT NULL,
    short_description VARCHAR(255)                              DEFAULT NULL,
    thumbnail         VARCHAR(255)                              DEFAULT NULL,
    start_date        DATETIME                         NOT NULL,
    recurrence        ENUM ('ONETIME', 'REGULAR')      NOT NULL DEFAULT 'ONETIME',
    price             DECIMAL(10, 2)                   NOT NULL,
    status            ENUM ('DRAFT', 'LIVE', 'PAUSED') NOT NULL DEFAULT 'DRAFT',
    ticket_count      INT                              NOT NULL,
    created_at        TIMESTAMP                        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP                        NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_moments_id PRIMARY KEY (id),
    CONSTRAINT fk_moments_category_id FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT fk_moments_location_id FOREIGN KEY (location_id) REFERENCES locations (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET latin1;

CREATE TABLE moment_details
(
    id          BIGINT AUTO_INCREMENT,
    moment_id   BIGINT    NOT NULL,
    description TEXT,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_moments_id PRIMARY KEY (id),
    CONSTRAINT fk_moment_details_moment_id FOREIGN KEY (moment_id) REFERENCES moments (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET latin1;

CREATE TABLE categories
(
    id          BIGINT AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_categories_id PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET latin1;

CREATE TABLE locations
(
    id         BIGINT AUTO_INCREMENT,
    city       VARCHAR(50) NOT NULL,
    address    VARCHAR(100),
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP   NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_locations_id PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET latin1;

SET FOREIGN_KEY_CHECKS = 1;