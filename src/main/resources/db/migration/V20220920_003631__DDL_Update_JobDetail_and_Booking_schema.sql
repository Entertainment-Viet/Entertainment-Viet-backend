ALTER TABLE job_detail
ADD COLUMN performance_start_time TIMESTAMP WITH TIME zone,
ADD COLUMN performance_end_time TIMESTAMP WITH TIME zone,
ADD COLUMN performance_count INTEGER;

ALTER TABLE job_detail
DROP COLUMN performance_time,
DROP COLUMN performance_duration;

ALTER TABLE booking
ADD COLUMN confirmed_at TIMESTAMP WITH TIME zone;