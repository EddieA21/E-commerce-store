CREATE TABLE `ecommerce`.`carts` (
  `id` BINARY(16) NOT NULL DEFAULT (uuid_to_bin(uuid())),
  `date_created` DATE NOT NULL,
  PRIMARY KEY (`id`));
