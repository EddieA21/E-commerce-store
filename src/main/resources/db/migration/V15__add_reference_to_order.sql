ALTER TABLE `ecommerce`.`orders`
ADD COLUMN `transaction_reference` VARCHAR(255) NOT NULL AFTER `total_price`;
