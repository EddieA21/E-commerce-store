CREATE TABLE `ecommerce`.`wish_list` (
  `user_id` BIGINT NOT NULL,
  `product_id` BIGINT NOT NULL,
  PRIMARY KEY (`user_id`, `product_id`),
  INDEX `fk_wish_list_2_idx` (`product_id` ASC) VISIBLE,
  CONSTRAINT `fk_wish_list_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `ecommerce`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  CONSTRAINT `fk_wish_list_2`
    FOREIGN KEY (`product_id`)
    REFERENCES `ecommerce`.`products` (`id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE);
