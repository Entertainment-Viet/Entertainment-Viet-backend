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

