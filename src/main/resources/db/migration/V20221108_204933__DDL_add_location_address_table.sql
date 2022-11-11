CREATE EXTENSION IF NOT EXISTS plpgsql;
CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE IF NOT EXISTS location_address
(
    id BIGINT NOT NULL,
	city TEXT NOT NULL,
	district TEXT,
	street TEXT,
	coordinates geography(POINT),
	uid UUID,
	CONSTRAINT pk_location_address primary key (id)
);

CREATE INDEX IF NOT EXISTS location_address_geo_index ON location_address USING GIST (coordinates);


ALTER TABLE event_detail
DROP COLUMN occurrence_address;

ALTER TABLE event_detail
ADD COLUMN location_address_id BIGINT;

ALTER TABLE event_detail ADD CONSTRAINT FK_EVENT_DETAIL_ON_LOCATION_ADDRESS FOREIGN KEY (location_address_id) REFERENCES
    location_address(id);


ALTER TABLE job_detail
	DROP COLUMN location;

ALTER TABLE job_detail
	ADD COLUMN location_address_id BIGINT;

ALTER TABLE job_detail ADD CONSTRAINT FK_JOB_DETAIL_ON_LOCATION_ADDRESS FOREIGN KEY (location_address_id) REFERENCES
	location_address(id);


ALTER TABLE organizer_detail
	DROP COLUMN address;

ALTER TABLE organizer_detail
	ADD COLUMN location_address_id BIGINT;

ALTER TABLE organizer_detail ADD CONSTRAINT FK_ORGANIZER_DETAIL_ON_LOCATION_ADDRESS FOREIGN KEY (location_address_id) REFERENCES
	location_address(id);


ALTER TABLE talent_detail
	DROP COLUMN address;

ALTER TABLE talent_detail
	ADD COLUMN location_address_id BIGINT;

ALTER TABLE talent_detail ADD CONSTRAINT FK_TALENT_DETAIL_ON_LOCATION_ADDRESS FOREIGN KEY (location_address_id) REFERENCES
	location_address(id);


-- create mock data for location_address
insert into location_address(id, city, district, street, coordinates, uid) VALUES
	(1,'Ho Chi Minh', null, null, st_geomfromtext('POINT(106.660172 10.762622)'), gen_random_uuid());
insert into location_address(id, city, district, street, coordinates, uid) VALUES
	(2,'Ha Noi', null, null, st_geomfromtext('POINT(105.804817 21.028511)'), gen_random_uuid());
insert into location_address(id, city, district, street, coordinates, uid) VALUES
	(3,'Ho Chi Minh', 'Go Vap', null, st_geomfromtext('POINT(105.804817 21.028511)'), gen_random_uuid());
insert into location_address(id, city, district, street, coordinates, uid) VALUES
	(4,'Hồ Chí Minh', 'Phú Nhuận', null, st_geomfromtext('POINT(105.804817 21.028511)'), gen_random_uuid());
insert into location_address(id, city, district, street, coordinates, uid) VALUES
	(5,'Hồ Chí Minh', 'Tân Bình', 'Cộng Hoà', st_geomfromtext('POINT(105.804817 21.028511)'), gen_random_uuid());