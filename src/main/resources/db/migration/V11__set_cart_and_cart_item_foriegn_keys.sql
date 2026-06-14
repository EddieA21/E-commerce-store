ALTER TABLE cart_items
ADD INDEX fk_cart_items_2_idx (product_id ASC) VISIBLE;

ALTER TABLE cart_items
ADD CONSTRAINT fk_cart_items_1
  FOREIGN KEY (cart_id)
  REFERENCES carts (id)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
ADD CONSTRAINT fk_cart_items_2
  FOREIGN KEY (product_id)
  REFERENCES products (id)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;
