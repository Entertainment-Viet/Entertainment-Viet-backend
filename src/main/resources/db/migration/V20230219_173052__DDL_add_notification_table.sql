DROP TABLE IF EXISTS booking_notification CASCADE;
CREATE TABLE booking_notification (
    id BIGINT NOT NULL,
    sender_uid UUID NOT NULL,
    recipient_uid UUID NOT NULL,
    content TEXT,
    created_at TIMESTAMP WITH TIME ZONE,
    is_read BOOLEAN NOT NULL,
    booking_id BIGINT NOT NULL,
    CONSTRAINT pk_booking_notification PRIMARY KEY (id)
);