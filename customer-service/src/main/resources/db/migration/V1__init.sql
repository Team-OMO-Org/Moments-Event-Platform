CREATE TABLE customers
(
    id                  BIGINT AUTO_INCREMENT,
    keycloak_user_id    VARCHAR(255) NOT NULL,
    profile_name        VARCHAR(100)          DEFAULT NULL,
    profile_email       VARCHAR(100)          DEFAULT NULL,
    profile_picture     VARCHAR(255)          DEFAULT NULL,
    profile_site_url    VARCHAR(255)          DEFAULT NULL,
    profile_description TEXT                  DEFAULT NULL,
    active              BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP    NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_customers_id PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET latin1;

