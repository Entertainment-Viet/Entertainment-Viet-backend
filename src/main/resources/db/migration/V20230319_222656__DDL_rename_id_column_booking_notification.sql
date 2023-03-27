ALTER TABLE booking_notification
DROP COLUMN booking_id;

ALTER TABLE booking_notification
ADD COLUMN booking_uid UUID;
