CREATE TABLE orders (
  id BIGINT NOT NULL,
  customer_id BIGINT NOT NULL,
  status VARCHAR(20) NOT NULL,
  created_at DATETIME NOT NULL,
  total_price DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (id),
  INDEX fk_orders_1_idx (customer_id ASC) VISIBLE,
  CONSTRAINT fk_orders_1
    FOREIGN KEY (customer_id)
    REFERENCES users (id)
    ON DELETE NO ACTION
    ON UPDATE CASCADE);

CREATE TABLE order_items (
  id BIGINT NOT NULL,
  order_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  unit_price DECIMAL(10,2) NOT NULL,
  quantity INT NOT NULL,
  total_price DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (id),
  INDEX fk_order_items_1_idx (product_id ASC) VISIBLE,
  CONSTRAINT fk_order_items_1
    FOREIGN KEY (product_id)
    REFERENCES products (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
