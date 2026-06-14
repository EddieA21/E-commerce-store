ALTER TABLE products
ADD INDEX fk_products_1_idx (category_id ASC) VISIBLE;
;
ALTER TABLE products
ADD CONSTRAINT fk_products_1
  FOREIGN KEY (category_id)
  REFERENCES categories (id)
  ON DELETE NO ACTION
  ON UPDATE CASCADE;
