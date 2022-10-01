ALTER TABLE organizer_shopping_cart
ADD COLUMN price DOUBLE PRECISION,
ADD COLUMN uid UUID NOT NULL DEFAULT gen_random_uuid();