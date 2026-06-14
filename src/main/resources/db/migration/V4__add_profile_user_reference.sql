ALTER TABLE `ecommerce`.`profiles`
ADD CONSTRAINT `fk_profiles_1`
  FOREIGN KEY (`id`)
  REFERENCES `ecommerce`.`users` (`id`)
  ON DELETE NO ACTION
  ON UPDATE CASCADE;
