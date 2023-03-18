ALTER TABLE account ALTER COLUMN hash_tag SET DEFAULT array[]::TEXT[];

UPDATE account SET hash_tag = array[]::TEXT[];
