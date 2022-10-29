ALTER TABLE talent_detail
DROP COLUMN address;

ALTER TABLE talent_detail
ADD COLUMN address JSONB;

ALTER TABLE organizer_detail
DROP COLUMN address;

ALTER TABLE organizer_detail
ADD COLUMN address JSONB;

ALTER TABLE job_detail
DROP COLUMN location;

ALTER TABLE job_detail
ADD COLUMN location JSONB;