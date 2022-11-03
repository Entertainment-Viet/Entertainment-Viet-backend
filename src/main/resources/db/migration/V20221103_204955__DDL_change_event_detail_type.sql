ALTER TABLE event_detail
	DROP COLUMN occurrence_address;

ALTER TABLE event_detail
	ADD COLUMN occurrence_address JSONB;

