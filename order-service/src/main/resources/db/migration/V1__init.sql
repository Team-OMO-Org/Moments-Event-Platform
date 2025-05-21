SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE carts
(
    id          BIGINT AUTO_INCREMENT,
    customer_id BIGINT    NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_carts_id PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET latin1;

CREATE TABLE cart_items
(
    id         BIGINT AUTO_INCREMENT,
    cart_id    BIGINT    NOT NULL,
    moment_id  BIGINT    NOT NULL,
    quantity   INT       NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_cart_items_id PRIMARY KEY (id),
    CONSTRAINT fk_cart_items_cart_id FOREIGN KEY (cart_id) REFERENCES carts (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET latin1;

CREATE TABLE orders
(
    id           BIGINT AUTO_INCREMENT,
    customer_id  BIGINT                                     NOT NULL,
    total_price  DECIMAL(10, 2)                             NOT NULL,
    order_status ENUM ('PENDING', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    created_at   TIMESTAMP                                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP                                  NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_orders_id PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET latin1;

CREATE TABLE order_items
(
    id         BIGINT AUTO_INCREMENT,
    order_id   BIGINT         NOT NULL,
    moment_id  BIGINT         NOT NULL,
    quantity   INT            NOT NULL,
    price      DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP      NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_order_items_id PRIMARY KEY (id),
    CONSTRAINT fk_order_items_order_id FOREIGN KEY (order_id) REFERENCES orders (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET latin1;

CREATE TABLE payments
(
    id                BIGINT AUTO_INCREMENT,
    order_id          BIGINT                                  NOT NULL,
    stripe_payment_id VARCHAR(100),
    amount            DECIMAL(10, 2)                          NOT NULL,
    payment_status    ENUM ('PENDING', 'SUCCEEDED', 'FAILED') NOT NULL DEFAULT 'PENDING',
    processed_at      TIMESTAMP,
    created_at        TIMESTAMP                               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP                               NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_payments_id PRIMARY KEY (id),
    CONSTRAINT fk_payments_order_id FOREIGN KEY (order_id) REFERENCES orders (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET latin1;

SET FOREIGN_KEY_CHECKS = 1;