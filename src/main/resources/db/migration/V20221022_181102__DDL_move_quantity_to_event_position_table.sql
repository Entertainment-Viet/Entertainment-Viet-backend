ALTER TABLE event_open_position
ADD COLUMN quantity INTEGER NOT NULL DEFAULT 0;

UPDATE event_open_position
SET quantity = (SELECT quantity FROM job_offer WHERE event_open_position.job_offer_id = job_offer.id);

-- Remove column from origin table
ALTER TABLE job_offer
DROP COLUMN quantity;
