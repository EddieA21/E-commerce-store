CREATE TABLE `ecommerce`.`profiles` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `bio` VARCHAR(255) NULL DEFAULT NULL,
  `phone_number` VARCHAR(15) NULL DEFAULT NULL,
  `date_of_birth` DATE NULL DEFAULT NULL,
  `loyalty_points` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `ecommerce`.`tags` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `ecommerce`.`user_tags` (
  `user_id` BIGINT NOT NULL,
  `tag_id` BIGINT NOT NULL,
  PRIMARY KEY (`user_id`, `tag_id`));
