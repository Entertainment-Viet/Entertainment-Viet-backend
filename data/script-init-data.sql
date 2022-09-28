-- Add category901
insert into category (id, name, parent_id, uid) values
(100000, 'Dịch vụ 1', null, 'a4e0a204-193a-4d49-bfc5-429fffcb9dde'),
(200000, 'Dịch vụ 2', null, '6885ba7a-d3f8-4e71-bee1-39fef806f8c2'),
(300000, 'Dịch vụ 3', null, '6e4b05d0-9829-474e-bcdb-461962f516ac'),
(400000, 'Dịch vụ 4', null, 'bd29ecda-0352-41fd-a6e6-5087782cf377'),
(500000, 'Dịch vụ 5', null, '01b4a1de-c90a-4691-a51a-f9e25d51b53a'),
(600000, 'Dịch vụ 6', null, '37a46bac-5284-40ca-ae60-b86bcd434410'),
(700000, 'Dịch vụ 7', null, 'b7cc171a-c8dd-40c2-8b23-9c5b8f130ccd'),
(800000, 'Dịch vụ 8', null, '6041327b-5f6b-400c-9a28-237891df21d5'),
(900000, 'Dịch vụ 9', null, '1e635905-ad62-47ce-9172-eca905e32b71'),
(1000000, 'Dịch vụ 10', null, 'a0fafa8a-454c-4875-b002-d0fe718b1dc5');

-- Add organizer
insert into organizer (id, phone_number, email, address, created_at, user_state, display_name, uid, extensions, bio_input_lang, bio_raw_input, bio_input_translation) values
(100000, '+84 93 720 92 61', 'jhf18032@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi',
'cc5291f7-523c-4129-8f34-6f599c13ebb1', null, 'VIETNAMESE', 'Test 1', null),
(200000, '+84 93 721 92 61', 'jhf180sdf32@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 2',
'5f5ae5f9-919d-4088-b1fe-e6eb4a7602ff', null, 'VIETNAMESE', 'Test 2', null),
(300000, '+84 93 722 92 61', 'jhf180sdfsdf32@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 3',
'20ecb18f-cfcb-4b92-8d89-2e5ab09daef4', null, 'VIETNAMESE', 'Test 3', null),
(400000, '+84 93 723 92 61', 'jhf18sdf032@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 4',
'3ff617ac-16cf-493c-b32f-fdaab0f9147a', null, 'VIETNAMESE', 'Test 4', null),
(500000, '+84 93 724 92 61', 'jhf18sdf032@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 5',
'b299a265-1b9b-41ca-89ba-fc7a1abbaf77', null, 'VIETNAMESE', 'Test 5', null),
(600000, '+84 93 725 92 61', 'jhf1fff8032@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 6',
'cc5291f7-523c-4129-8f34-6f599c13ebb9', null, 'VIETNAMESE', 'Test 6', null),
(700000, '+84 93 726 92 61', 'jhf18324032@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 7',
'5fcc4613-908a-4d0c-9472-9dc51f53c6ed', null, 'VIETNAMESE', 'Test 7', null),
(800000, '+84 93 727 92 61', 'jhf18032342@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 8',
'cc5291f7-523c-4129-8f34-6f599c13eab9', null, 'VIETNAMESE', 'Test 8', null),
(900000, '+84 93 728 92 61', 'jhf18023432@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 9',
'8f53b4b5-07fd-4061-b9bc-f7be9dc55365', null, 'VIETNAMESE', 'Test 9', null),
(1000000, '+84 93 729 92 61', 'jhf18234032@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 10',
'8218a647-80c7-4a85-accb-87e3528556f1', null, 'VIETNAMESE', 'Test 10', null);

-- Add job_detail
insert into job_detail (id, category_id, work_type, performance_count, performance_end_time, performance_start_time, note_input_lang, note_raw_input, note_input_translation, min, max, currency, extensions) values
(100000, 100000, 'SINGLE_TIME', 1, '2022-08-18 22:42:44.843000 +00:00', '2022-08-18 19:42:44.843000 +00:00', 'VIETNAMESE', 'Test 1', null, 67867800000, 12312312300000, 'VND', null),
(200000, 200000, 'SINGLE_TIME', 1, '2022-08-18 22:42:44.843000 +00:00', '2022-08-18 19:42:44.843000 +00:00', 'VIETNAMESE', 'Test 2', null, 34534500000, 12312312300000, 'VND', null),
(300000, 300000, 'SINGLE_TIME', 1, '2022-08-18 22:42:44.843000 +00:00', '2022-08-18 19:42:44.843000 +00:00', 'VIETNAMESE', 'Test 3', null, 34534500000, 12312312300000, 'VND', null),
(400000, 400000, 'SINGLE_TIME', 1, '2022-08-18 22:42:44.843000 +00:00', '2022-08-18 19:42:44.843000 +00:00', 'VIETNAMESE', 'Test 4', null, 3453453400000, 12312312300000, 'VND', null),
(500000, 500000, 'SINGLE_TIME', 1, '2022-08-18 22:42:44.843000 +00:00', '2022-08-18 19:42:44.843000 +00:00', 'VIETNAMESE', 'Test 5', null, 34534534500000, 12312312300000, 'VND', null),
(600000, 600000, 'SINGLE_TIME', 1, '2022-08-18 22:42:44.843000 +00:00', '2022-08-18 19:42:44.843000 +00:00', 'VIETNAMESE', 'Test 6', null, 34534534500000, 12312312300000, 'VND', null),
(700000, 700000, 'SINGLE_TIME', 1, '2022-08-18 22:42:44.843000 +00:00', '2022-08-18 19:42:44.843000 +00:00', 'VIETNAMESE', 'Test 7', null, 34534534500000, 12312312300000, 'VND', null),
(800000, 800000, 'SINGLE_TIME', 1, '2022-08-18 22:42:44.843000 +00:00', '2022-08-18 19:42:44.843000 +00:00', 'VIETNAMESE', 'Test 8', null, 34534534500000, 34534534534500000, 'VND', null),
(900000, 900000, 'SINGLE_TIME', 1, '2022-08-18 22:42:44.843000 +00:00', '2022-08-18 19:42:44.843000 +00:00', 'VIETNAMESE', 'Test 9', null, 34534534500000, 34534534534500000, 'VND', null),
(1000000, 1000000, 'SINGLE_TIME', 1, '2022-08-18 22:42:44.843000 +00:00', '2022-08-18 19:42:44.843000 +00:00', 'VIETNAMESE', 'Test 10', null, 34534534500000, 1231231231200000, 'VND', null);


-- Add job_offer
insert into job_offer (id, uid, name, is_active, quantity, job_detail_id, organizer_id) values
(100000, 'b0405757-efe6-4b48-aac9-e8db6cfcda09', 'Offer 1', true, 100000, 100000, 100000),
(200000, '0b66300f-4737-42bc-b226-a1a9062377c8', 'Offer 2', true, 200000, 200000, 200000),
(300000, 'b4162b69-8749-42ce-a967-7d8ffbc0ca8d', 'Offer 3', true, 300000, 300000, 300000),
(400000, '7f8a97c2-fd6e-40b8-a338-d153f68f0d7a', 'Offer 4', true, 400000, 400000, 400000),
(500000, 'b4162b69-8749-42ce-a967-7d8ffbc0ca8d', 'Offer 5', true, 500000, 500000, 500000),
(600000, '76926e0e-5f3c-40d8-928f-2eb600550af3', 'Offer 6', true, 600000, 600000, 600000),
(700000, '63009811-c296-40d3-873b-57550e19c5fa', 'Offer 7', true, 700000, 700000, 700000),
(800000, '4f529994-bd62-44ee-a73c-403c338eb998', 'Offer 8', true, 800000, 800000, 800000),
(900000, 'dc8bc01c-06ae-473c-9687-6e8d32e1ad8e', 'Offer 9', true, 900000, 900000, 900000),
(1000000, 'f8689ec3-10c1-4458-a8a9-b02b32de5fd0', 'Offer 10', true, 1000000, 1000000, 1000000);

-- Add talent

insert into talent (id, phone_number, email, address, created_at, user_state, display_name, uid, extensions, bio_input_lang, bio_raw_input, bio_input_translation) values
(100000, '+84 93 720 92 61', 'jhf18032@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi',
'cc5291f7-523c-4129-8f34-6f599c13ebb9', null, 'VIETNAMESE', 'Test 1', null),
(200000, '+84 93 721 92 61', 'jhf180sdf32@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 2',
'5f5ae5f9-919d-4088-b1fe-e6eb4a7602ff', null, 'VIETNAMESE', 'Test 2', null),
(300000, '+84 93 722 92 61', 'jhf180sdfsdf32@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 3',
'20ecb18f-cfcb-4b92-8d89-2e5ab09daef4', null, 'VIETNAMESE', 'Test 3', null),
(400000, '+84 93 723 92 61', 'jhf18sdf032@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 4',
'3ff617ac-16cf-493c-b32f-fdaab0f9147a', null, 'VIETNAMESE', 'Test 4', null),
(500000, '+84 93 724 92 61', 'jhf18sdf032@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 5',
'b299a265-1b9b-41ca-89ba-fc7a1abbaf77', null, 'VIETNAMESE', 'Test 5', null),
(600000, '+84 93 725 92 61', 'jhf1fff8032@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 6',
'cc5291f7-523c-4129-8f34-6f599c13ebb0', null, 'VIETNAMESE', 'Test 6', null),
(700000, '+84 93 726 92 61', 'jhf18324032@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 7',
'5fcc4613-908a-4d0c-9472-9dc51f53c6ed', null, 'VIETNAMESE', 'Test 7', null),
(800000, '+84 93 727 92 61', 'jhf18032342@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 8',
'cc5291f7-523c-4129-8f34-6f599c13eab9', null, 'VIETNAMESE', 'Test 8', null),
(900000, '+84 93 728 92 61', 'jhf18023432@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 9',
'8f53b4b5-07fd-4061-b9bc-f7be9dc55365', null, 'VIETNAMESE', 'Test 9', null),
(1000000, '+84 93 729 92 61', 'jhf18234032@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 10',
'8218a647-80c7-4a85-accb-87e3528556f1', null, 'VIETNAMESE', 'Test 10', null);

-- Add booking
insert into booking (id, uid, is_paid, created_at, status, organizer_id, talent_id, job_detail_id, payment_type) values
(100000, 'ec30d150-ee90-420a-8fdd-038844795788', true, '2022-08-18 19:42:44.843000 +00:00', 'ORGANIZER_PENDING', 100000, 100000, 100000, 'ONLINE'),
(200000, 'e4bc6184-609f-4be6-9240-91ddeb15dd87', false, '2022-08-18 19:42:44.843000 +00:00', 'CONFIRMED', 100000, 100000, 200000, 'ONLINE'),
(300000, 'd8b9fc05-ec72-4c12-9f8b-aa9b68a598a3', false, '2022-08-18 19:42:44.843000 +00:00', 'TALENT_PENDING', 100000, 200000, 300000, 'ONLINE'),
(400000, '2873ad11-13e0-4c2f-b8d3-1c7af9081979', false, '2022-08-18 19:42:44.843000 +00:00', 'TALENT_PENDING', 400000, 400000, 400000, 'ONLINE'),
(500000, '69601014-c397-4753-8c6b-33c271b52c42', true, '2022-08-18 19:42:44.843000 +00:00', 'CANCELLED', 500000, 400000, 500000, 'ONLINE'),
(600000, '57b7e64c-961e-483a-87d4-ea7d810167bb', true, '2022-08-18 19:42:44.843000 +00:00', 'TALENT_PENDING', 100000, 400000, 100000, 'ONLINE'),
(700000, '2446e876-f430-4cf4-85c8-ab78393c9728', false, '2022-08-18 19:42:44.843000 +00:00', 'TALENT_PENDING', 100000, 700000, 200000, 'ONLINE'),
(800000, '944179a3-8e0c-4807-90b7-3b9e6f19880d', false, '2022-08-18 19:42:44.843000 +00:00', 'TALENT_PENDING', 100000, 800000, 100000, 'ONLINE'),
(900000, '9ded56ea-d4cc-4766-a787-185d108192f7', true, '2022-08-18 19:42:44.843000 +00:00', 'ARCHIVED', 200000, 900000, 300000, 'ONLINE'),
(1000000, '3513e3d4-6123-42cd-81b4-d95ffca78b14', true, '2022-08-18 19:42:44.843000 +00:00', 'FINISHED', 1000000, 1000000, 1000000, 'ONLINE'),
(1100000, '728944c9-8b8b-4946-8a25-a1b67f227974', true, '2022-08-18 19:42:44.843000 +00:00', 'FINISHED', 400000, 400000, 100000, 'ONLINE'),
(1200000, '94871295-c57f-4e53-89f0-416d1b81c97a', true, '2022-08-18 19:42:44.843000 +00:00', 'FINISHED', 500000, 500000, 200000, 'ONLINE'),
(1300000, 'a8cf3e71-839c-45b7-823d-fdb29bb2e625', true, '2022-08-18 19:42:44.843000 +00:00', 'FINISHED', 400000, 600000, 600000, 'ONLINE'),
(1400000, 'cdac42ba-b77f-4965-a4e2-86028a5cfe2c', true, '2022-08-18 19:42:44.843000 +00:00', 'FINISHED', 1000000, 1000000, 500000, 'ONLINE'),
(1500000, '2f371708-22f5-11ed-861d-0242ac120002', true, '2022-08-18 19:42:44.843000 +00:00', 'FINISHED', 300000, 200000, 500000, 'ONLINE');

-- Add package
insert into package (id, uid, name, talent_id, is_active, job_detail_id) values
(100000, 'a7d73bd4-cfd7-4869-86dc-7ec587047e41', 'Package 1', 100000, true, 100000),
(200000, 'bc99559d-636a-414a-bbe6-b25435bce018', 'Package 2', 100000, true, 200000),
(300000, '611f6897-a138-4208-a765-e5b84d77910c', 'Package 3', 500000, false, 300000),
(400000, 'c655035c-d0b8-4dc9-8375-8ba5598a25d5', 'Package 4', 200000, true, 400000),
(500000, '60e2d4a7-a735-4b2a-8f23-dd4059511259', 'Package 5', 200000, true, 500000),
(600000, 'f24ed3c3-7ab0-44df-a957-863a8c5c81d1', 'Package 6', 600000, true, 600000),
(700000, 'a0a9c7e9-d892-447c-88e3-04d996ac9ec3', 'Package 7', 700000, true, 700000),
(800000, 'f7858af5-9e2c-4932-ad72-ef3ea84054ab', 'Package 8', 800000, true, 800000),
(900000, '73b5ea67-feff-4486-bef1-3a32f874d484', 'Package 9', 900000, true, 900000),
(1000000, '41f8588a-e751-4d42-b441-4d0b2f2fa1fe', 'Package 10', 1000000, true, 1000000);

-- Add admin
insert into admin (id, display_name, uid) values
(100000, 'Admin', '74bcd4eb-0384-4ab9-ab2f-679f8127dc04');

-- Add feedback
insert into talent_feedback (id, created_at, status, admin_id, uid, content_input_lang, content_raw_input, content_input_translation, talent_id) values
(100000, '2022-08-18 19:42:44.843000 +00:00', 'SOLVED', 100000, 'b9650052-04a7-4ecf-b656-b9398f2f6823', 'VIETNAMESE', 'Test 1', null, 100000),
(200000, '2022-08-18 19:42:44.843000 +00:00', 'SOLVED', 100000, 'ce0fd09e-4157-4d95-b004-9f52ba0da311', 'VIETNAMESE', 'Test 2', null, 100000),
(300000, '2022-08-18 19:42:44.843000 +00:00', 'SOLVED', 100000, '3717b1ae-4e16-46cc-8bb2-39944d5d13d7', 'VIETNAMESE', 'Test 3', null, 200000),
(400000, '2022-08-18 19:42:44.843000 +00:00', 'SOLVED', 100000, '59b3cf80-39a3-4e17-9308-2c6fc03dd15e', 'VIETNAMESE', 'Test 4', null, 400000),
(500000, '2022-08-18 19:42:44.843000 +00:00', 'SOLVED', 100000, '90bf08ae-769b-46ba-835e-fea64ba55cfa', 'VIETNAMESE', 'Test 5', null, 500000),
(600000, '2022-08-18 19:42:44.843000 +00:00', 'SOLVED', 100000, 'dc9b8015-1d2e-4e0d-abc6-7d0b68e192b0', 'VIETNAMESE', 'Test 6', null, 600000),
(700000, '2022-08-18 19:42:44.843000 +00:00', 'SOLVED', 100000, '5074fdeb-0213-4db9-9d63-a298995d585a', 'VIETNAMESE', 'Test 7', null, 700000),
(800000, '2022-08-18 19:42:44.843000 +00:00', 'SOLVED', 100000, '33319b49-7d6c-457d-9a28-ac0a5749c27c', 'VIETNAMESE', 'Test 8', null, 800000),
(900000, '2022-08-18 19:42:44.843000 +00:00', 'SOLVED', 100000, '8ddd3d9a-7435-4b36-a9b5-81ab73f9784b', 'VIETNAMESE', 'Test 9', null, 900000),
(1000000, '2022-08-18 19:42:44.843000 +00:00', 'SOLVED', 100000, '8be4a1bb-46c9-465c-bc96-ffeff1d21815', 'VIETNAMESE', 'Test 10', null, 1000000);

INSERT INTO organizer_feedback (id, created_at, status, admin_id, uid, content_input_lang, content_raw_input, organizer_id) VALUES
(100000, '2022-08-18 19:42:44.843000 +00:00', 'IN_PROGRESS', 100000, 'c4c18fdc-b2cb-49cd-9cbf-86030c94cd63', 'VIETNAMESE', '123', 100000),
(200000, '2022-08-18 19:42:44.843000 +00:00', 'IN_PROGRESS', 100000, '9a7ec4ff-efde-4799-8144-c0f7253b0826', 'VIETNAMESE', '123', 100000),
(400000, '2022-08-18 19:42:44.843000 +00:00', 'IN_PROGRESS', 100000, 'dca21e61-1c89-4982-a443-4a399805b0b4', 'VIETNAMESE', '123', 200000),
(500000, '2022-08-18 19:42:44.843000 +00:00', 'IN_PROGRESS', 100000, '59a658db-6e0b-4846-ba67-7781a008ec35', 'VIETNAMESE', '123', 300000);

-- Add event
INSERT INTO event (id, uid, "name", is_active, occurrence_address, occurrence_time, organizer_id) VALUES
(100000, '33319b49-7d6c-457d-9a28-ac0a5749c27c', 'Event 1', true, 'New York', '2022-08-18 19:42:44.843000 +00:00', 100000),
(200000, 'dac4565e-22f4-11ed-861d-0242ac120002', 'Event 2', true, 'New York Nowhere', '2022-08-18 19:42:44.843000 +00:00', 100000),
(300000, '2548523e-22f5-11ed-861d-0242ac120002', 'Event 3', false, 'New York District', '2022-08-18 19:42:44.843000 +00:00', 100000),
(400000, '2826168a-22f5-11ed-861d-0242ac120002', 'Event 4', true, 'New York Town', '2022-08-18 19:42:44.843000 +00:00', 300000),
(500000, '076b2e41-3fd7-449b-a884-2738c7096e62', 'Event 5', true, 'New York City', '2022-08-18 19:42:44.843000 +00:00', 300000);

INSERT INTO event_advertisement (id, expired_time, priority, uid, event_id) VALUES
(100000, '2022-08-18 19:42:44.843000 +00:00', 100000, 'dca21e61-1c89-4982-a443-4a399805b0b4', 100000),
(200000, '2022-08-18 19:42:44.843000 +00:00', 1000000, '00dd9128-9dac-4444-a548-e99bf295d8a8', 100000),
(300000, '2022-08-18 19:42:44.843000 +00:00', 100000, '59a658db-6e0b-4846-ba67-7781a008ec35', 300000),
(400000, '2022-08-18 19:42:44.843000 +00:00', 500000, '26f541f7-03b7-47fb-8dc7-fd6655693990', 200000),
(500000, '2022-08-18 19:42:44.843000 +00:00', 500000, '9359035c-c550-4307-b855-5342f07203eb', 400000);

INSERT INTO event_open_position (id, uid, event_id, job_offer_id) VALUES
(100000, 'dca21e61-1c89-4982-a443-4a399805b0b4', 100000, 100000),
(200000, '00dd9128-9dac-4444-a548-e99bf295d8a8', 100000, 200000),
(300000, '9a7ec4ff-efde-4799-8144-c0f7253b0826', 200000, 300000),
(400000, '27b8867a-55c7-4651-85e3-783598bf6cf4', 100000, 400000),
(500000, 'c4c18fdc-b2cb-49cd-9cbf-86030c94cd63', 300000, 500000);

INSERT INTO open_position_applicant (applicant_id, open_position_id) VALUES
(600000, 100000),
(700000, 200000),
(800000, 100000),
(900000, 300000);

INSERT INTO package_order (order_id, package_id) VALUES
(1000000, 100000),
(1100000, 100000),
(1200000, 300000);

-- Add review
INSERT INTO review (id, created_at, talent_id, score, comment_input_lang, comment_raw_input, comment_input_translation) VALUES
(100000, '2022-08-18 19:42:44.843000 +00:00', 100000, 300000, 'VIETNAMESE', 'good', NULL),
(200000, '2022-08-18 19:42:44.843000 +00:00', 100000, 400000, 'VIETNAMESE', 'well', NULL),
(300000, '2022-08-18 19:42:44.843000 +00:00', 100000, 100000, 'VIETNAMESE', 'bad', NULL);

-- Add organizer_shopping_cart
INSERT INTO organizer_shopping_cart (organizer_id, package_id) VALUES
(100000, 100000),
(100000, 200000),
(200000, 300000),
(300000, 100000);


-- Add advertisement
INSERT INTO talent_advertisement (id, expired_time, priority, uid, talent_id) VALUES
(100000, '2022-08-18 19:42:44.843000 +00:00', 400000, '59a658db-6e0b-4846-ba67-7781a008ec35', 100000),
(200000, '2022-08-18 19:42:44.843000 +00:00', 1000000, '2f370ed4-22f5-11ed-861d-0242ac120002', 100000),
(300000, '2023-08-18 19:42:44.843000 +00:00', 300000, '2f371226-22f5-11ed-861d-0242ac120002', 200000);

-- Add talent_category

INSERT INTO talent_category (talent_id, category_id) VALUES
(300000, 200000),
(300000, 100000),
(900000, 200000),
(3, 1000000),
(3, 400000);
