ALTER TYPE user_state ADD VALUE 'PENDING';

-- Create Table: organizer_detail --
DROP TABLE IF EXISTS organizer_detail CASCADE;
CREATE TABLE organizer_detail (
	organizer_id BIGINT NOT NULL,
	address TEXT,
	bank_account_number TEXT,
	bank_account_owner TEXT,
	bank_branch_name TEXT,
	bank_name TEXT,
	bio_input_lang TEXT,
	bio_input_translation JSONB,
	bio_raw_input TEXT,
	email TEXT,
	extensions JSONB ,
	phone_number TEXT,
	tax_id TEXT,
	business_paper TEXT[],
	company_name TEXT,
	position TEXT,
	representative TEXT,
	CONSTRAINT organizer_detail_pkey PRIMARY KEY (organizer_id)
);
ALTER TABLE organizer_detail ADD CONSTRAINT FK_ORGANIZERDETAIL_ON_ORGANIZER FOREIGN KEY (organizer_id) REFERENCES organizer (id);

-- Create Table: talent_detail --
DROP TABLE IF EXISTS talent_detail CASCADE;
CREATE TABLE talent_detail (
	talent_id BIGINT NOT NULL,
	address TEXT,
	bank_account_number TEXT,
	bank_account_owner TEXT,
	bank_branch_name TEXT,
	bank_name TEXT,
	bio_input_lang TEXT,
	bio_input_translation JSONB,
	bio_raw_input TEXT,
	email TEXT,
	extensions JSONB,
	phone_number TEXT,
	tax_id TEXT,
	citizen_id TEXT,
	citizen_paper TEXT[],
	first_name TEXT,
	last_name TEXT,
	CONSTRAINT pk_talentdetail PRIMARY KEY (talent_id)
);
ALTER TABLE talent_detail ADD CONSTRAINT FK_TALENTDETAIL_ON_TALENT FOREIGN KEY (talent_id) REFERENCES talent (id);

-- Create Table: event_detail --
DROP TABLE IF EXISTS event_detail CASCADE;
CREATE TABLE event_detail (
	event_id BIGINT NOT NULL,
	occurrence_address TEXT NOT NULL,
	occurrence_start_time TIMESTAMP WITH TIME ZONE NOT NULL,
	occurrence_end_time TIMESTAMP WITH TIME ZONE NOT NULL,
	address TEXT,
    legal_paper TEXT[],
	description_input_lang TEXT,
	description_input_translation JSONB,
	description_raw_input TEXT,
	CONSTRAINT pk_eventdetail PRIMARY KEY (event_id)
);
ALTER TABLE event_detail ADD CONSTRAINT FK_EVENTDETAIL_ON_EVENT FOREIGN KEY (event_id) REFERENCES event (id);


-- Move current data to new table
INSERT INTO talent_detail(talent_id, phone_number, email, address, extensions, bio_input_lang, bio_input_translation, bio_raw_input)
SELECT id, phone_number, email, address, extensions, bio_input_lang, bio_input_translation, bio_raw_input
FROM talent;

INSERT INTO organizer_detail(organizer_id, phone_number, email, address, extensions, bio_input_lang, bio_input_translation, bio_raw_input)
SELECT id, phone_number, email, address, extensions, bio_input_lang, bio_input_translation, bio_raw_input
FROM organizer;

INSERT INTO event_detail(event_id, occurrence_address, occurrence_start_time, occurrence_end_time)
SELECT id, occurrence_address, occurrence_time, occurrence_time
FROM event;

-- Remove column from origin table
ALTER TABLE talent
DROP COLUMN phone_number,
DROP COLUMN email,
DROP COLUMN address,
DROP COLUMN extensions,
DROP COLUMN bio_input_lang,
DROP COLUMN bio_input_translation,
DROP COLUMN bio_raw_input;

ALTER TABLE organizer
DROP COLUMN phone_number,
DROP COLUMN email,
DROP COLUMN address,
DROP COLUMN extensions,
DROP COLUMN bio_input_lang,
DROP COLUMN bio_input_translation,
DROP COLUMN bio_raw_input;

ALTER TABLE event
DROP COLUMN occurrence_address,
DROP COLUMN occurrence_time;
