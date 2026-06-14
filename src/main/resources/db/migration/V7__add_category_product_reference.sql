ALTER TABLE `ecommerce`.`products`
ADD INDEX `fk_products_1_idx` (`category_id` ASC) VISIBLE;
;
ALTER TABLE `ecommerce`.`products`
ADD CONSTRAINT `fk_products_1`
  FOREIGN KEY (`category_id`)
  REFERENCES `ecommerce`.`categories` (`id`)
  ON DELETE NO ACTION
  ON UPDATE CASCADE;
