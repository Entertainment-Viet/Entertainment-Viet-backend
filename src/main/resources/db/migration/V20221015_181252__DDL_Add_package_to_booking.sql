ALTER TABLE booking
ADD COLUMN talent_package_id BIGINT;

ALTER TABLE booking ADD CONSTRAINT FK_BOOKING_ON_PACKAGE FOREIGN KEY (talent_package_id) REFERENCES package (id);
