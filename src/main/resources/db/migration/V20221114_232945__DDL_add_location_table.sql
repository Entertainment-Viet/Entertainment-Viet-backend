ALTER TABLE job_detail DROP COLUMN IF EXISTS location_address_id;
ALTER TABLE job_detail DROP CONSTRAINT IF EXISTS fk_job_detail_on_location_address;

ALTER TABLE organizer_detail DROP COLUMN IF EXISTS location_address_id;
ALTER TABLE organizer_detail DROP CONSTRAINT IF EXISTS fk_organizer_detail_on_location_address;

ALTER TABLE talent_detail DROP COLUMN IF EXISTS location_address_id;
ALTER TABLE talent_detail DROP CONSTRAINT IF EXISTS fk_talent_detail_on_location_address;

ALTER TABLE event_detail DROP COLUMN IF EXISTS location_address_id;
ALTER TABLE event_detail DROP CONSTRAINT IF EXISTS fk_event_detail_on_location_address;

DROP TABLE IF EXISTS location_address;

CREATE TABLE location_type (
  id BIGINT NOT NULL,
  type TEXT NOT NULL,
  level TEXT NOT NULL,
  CONSTRAINT pk_location_type PRIMARY KEY (id)
);

CREATE TABLE location (
  id BIGINT NOT NULL,
  uid UUID NOT NULL,
  type_id BIGINT NOT NULL,
  name TEXT NOT NULL,
  name_code BIGINT,
  zipcode BIGINT,
  coordinate geography(POINT),
  boundary geography(POLYGON),
  parent_id BIGINT,
  CONSTRAINT pk_location PRIMARY KEY (id),
  CONSTRAINT FK_TYPE_ON_LOCATION_TYPE
      FOREIGN KEY (type_id)
      REFERENCES location_type(id)
);

ALTER TABLE event_detail
	ADD COLUMN location_id BIGINT;

ALTER TABLE event_detail ADD CONSTRAINT FK_EVENT_DETAIL_ON_LOCATION FOREIGN KEY (location_id) REFERENCES
	location(id);

ALTER TABLE job_detail
	ADD COLUMN location_id BIGINT;

ALTER TABLE job_detail ADD CONSTRAINT FK_JOB_DETAIL_ON_LOCATION FOREIGN KEY (location_id) REFERENCES
	location(id);

ALTER TABLE organizer_detail
	ADD COLUMN location_id BIGINT;

ALTER TABLE organizer_detail ADD CONSTRAINT FK_ORGANIZER_DETAIL_ON_LOCATION FOREIGN KEY (location_id) REFERENCES
	location(id);

ALTER TABLE talent_detail
	ADD COLUMN location_id BIGINT;

ALTER TABLE talent_detail ADD CONSTRAINT FK_TALENT_DETAIL_ON_LOCATION FOREIGN KEY (location_id) REFERENCES
	location(id);


