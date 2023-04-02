DROP TYPE IF EXISTS package_type CASCADE;
CREATE TYPE package_type AS ENUM (
    'ONCE',
    'RECURRING'
);

ALTER TABLE package
ADD COLUMN start_period TIMESTAMP WITH TIME zone,
ADD COLUMN end_period TIMESTAMP WITH TIME zone,
ADD COLUMN cron_expression TEXT,
ADD COLUMN package_type PACKAGE_TYPE;