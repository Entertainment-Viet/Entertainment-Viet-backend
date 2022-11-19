alter table location
add constraint fk_location_on_parent foreign key(parent_id) references location(id);
