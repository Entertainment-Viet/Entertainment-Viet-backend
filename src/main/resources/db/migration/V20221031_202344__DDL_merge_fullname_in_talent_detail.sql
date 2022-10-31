ALTER TABLE talent_detail
DROP COLUMN first_name;

ALTER TABLE talent_detail
DROP COLUMN last_name;

ALTER TABLE talent_detail
ADD COLUMN full_name TEXT;
