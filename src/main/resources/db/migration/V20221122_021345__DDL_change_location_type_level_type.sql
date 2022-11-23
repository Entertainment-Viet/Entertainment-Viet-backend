ALTER TABLE location_type
DROP COLUMN level;

ALTER TABLE location_type
ADD COLUMN level INTEGER;

-- Add default type for specific address
insert into location_type (id, type, level)
VALUES (1, 'address', 0);