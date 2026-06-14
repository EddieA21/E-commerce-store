CREATE TABLE `ecommerce`.`cart_items` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `cart_id` BINARY(16) NOT NULL DEFAULT (uuid_to_bin(uuid())),
  `product_id` BIGINT NOT NULL,
  `quantity` INT NOT NULL,
  PRIMARY KEY (`id`));
