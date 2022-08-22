DROP TABLE IF EXISTS shopping_cart;

CREATE TABLE shopping_cart (
	organizer_id BIGINT NOT NULL,
	cart_item TEXT NULL,
	shoppable_id BIGINT NOT NULL
);

ALTER TABLE shopping_cart ADD CONSTRAINT fk_shopping_cart_organizer FOREIGN KEY (organizer_id) REFERENCES organizer(id);