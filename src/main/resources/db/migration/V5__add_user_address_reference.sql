ALTER TABLE addresses
ADD INDEX fk_addresses_1_idx (user_id ASC) VISIBLE;
;
ALTER TABLE addresses
ADD CONSTRAINT fk_addresses_1
  FOREIGN KEY (user_id)
  REFERENCES users (id)
  ON DELETE NO ACTION
  ON UPDATE CASCADE;
