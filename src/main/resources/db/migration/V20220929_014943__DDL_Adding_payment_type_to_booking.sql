DROP TYPE IF EXISTS payment_type CASCADE;
CREATE TYPE payment_type AS ENUM (
    'ONLINE',
    'OFFLINE'
);

ALTER TABLE booking
ADD COLUMN payment_type PAYMENT_TYPE;