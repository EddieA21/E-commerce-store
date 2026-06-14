ALTER TABLE `ecommerce`.`orders`
CHANGE COLUMN `id` `id` BIGINT NOT NULL AUTO_INCREMENT ;

ALTER TABLE `ecommerce`.`order_items`
CHANGE COLUMN `id` `id` BIGINT NOT NULL AUTO_INCREMENT ,
ADD INDEX `fk_order_items_2_idx` (`order_id` ASC) VISIBLE;
;
ALTER TABLE `ecommerce`.`order_items`
ADD CONSTRAINT `fk_order_items_2`
  FOREIGN KEY (`order_id`)
  REFERENCES `ecommerce`.`orders` (`id`)
  ON DELETE NO ACTION
  ON UPDATE CASCADE;
