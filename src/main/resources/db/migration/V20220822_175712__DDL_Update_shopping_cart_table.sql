DROP TABLE IF EXISTS shopping_cart;

-- Table: organizer_shopping_cart --

DROP TABLE IF EXISTS organizer_shopping_cart CASCADE;
CREATE TABLE organizer_shopping_cart (
  organizer_id BIGINT NOT NULL,
   package_id BIGINT NOT NULL,
   CONSTRAINT pk_organizer_shopping_cart PRIMARY KEY (organizer_id, package_id)
);

ALTER TABLE organizer_shopping_cart ADD CONSTRAINT fk_orgshocar_on_organizer FOREIGN KEY (organizer_id) REFERENCES organizer (id);
ALTER TABLE organizer_shopping_cart ADD CONSTRAINT fk_orgshocar_on_package FOREIGN KEY (package_id) REFERENCES package (id);