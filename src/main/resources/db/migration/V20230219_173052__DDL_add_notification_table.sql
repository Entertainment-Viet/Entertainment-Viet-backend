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

DROP TABLE IF EXISTS read_tracking_notification CASCADE;
CREATE TABLE read_tracking_notification (
    id BIGINT NOT NULL,
    user_id UUID NOT NULL,
    read_until TIMESTAMP WITH TIME ZONE,
    CONSTRAINT pk_read_tracking_notification PRIMARY KEY (id)
);