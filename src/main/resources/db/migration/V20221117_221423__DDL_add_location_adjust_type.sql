alter table location
add constraint fk_location_on_parent foreign key(parent_id) references location(id);

alter table location
alter column name_code type text;

alter table location
add column phone_code bigint;

