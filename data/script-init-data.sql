-- Add category
insert into category values
(1, 'Dịch vụ 1', null, 'a4e0a204-193a-4d49-bfc5-429fffcb9dde'),
(2, 'Dịch vụ 2', null, '6885ba7a-d3f8-4e71-bee1-39fef806f8c2'),
(3, 'Dịch vụ 3', null, '6e4b05d0-9829-474e-bcdb-461962f516ac'),
(4, 'Dịch vụ 4', null, 'bd29ecda-0352-41fd-a6e6-5087782cf377'),
(5, 'Dịch vụ 5', null, '01b4a1de-c90a-4691-a51a-f9e25d51b53a'),
(6, 'Dịch vụ 6', null, '37a46bac-5284-40ca-ae60-b86bcd434410'),
(7, 'Dịch vụ 7', null, 'b7cc171a-c8dd-40c2-8b23-9c5b8f130ccd'),
(8, 'Dịch vụ 8', null, '6041327b-5f6b-400c-9a28-237891df21d5'),
(9, 'Dịch vụ 9', null, '1e635905-ad62-47ce-9172-eca905e32b71'),
(10, 'Dịch vụ 10', null, 'a0fafa8a-454c-4875-b002-d0fe718b1dc5');

-- Add organizer
insert into organizer values
(1, '+84 93 720 92 61', 'jhf18032@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi',
'cc5291f7-523c-4129-8f34-6f599c13ebb9', null, 'VIETNAMESE', 'Test 1', null),
(2, '+84 93 721 92 61', 'jhf180sdf32@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 2',
'5f5ae5f9-919d-4088-b1fe-e6eb4a7602ff', null, 'VIETNAMESE', 'Test 2', null),
(3, '+84 93 722 92 61', 'jhf180sdfsdf32@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 3',
'20ecb18f-cfcb-4b92-8d89-2e5ab09daef4', null, 'VIETNAMESE', 'Test 3', null),
(4, '+84 93 723 92 61', 'jhf18sdf032@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 4',
'3ff617ac-16cf-493c-b32f-fdaab0f9147a', null, 'VIETNAMESE', 'Test 4', null),
(5, '+84 93 724 92 61', 'jhf18sdf032@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 5',
'b299a265-1b9b-41ca-89ba-fc7a1abbaf77', null, 'VIETNAMESE', 'Test 5', null),
(6, '+84 93 725 92 61', 'jhf1fff8032@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 6',
'cc5291f7-523c-4129-8f34-6f599c13ebb9', null, 'VIETNAMESE', 'Test 6', null),
(7, '+84 93 726 92 61', 'jhf18324032@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 7',
'5fcc4613-908a-4d0c-9472-9dc51f53c6ed', null, 'VIETNAMESE', 'Test 7', null),
(8, '+84 93 727 92 61', 'jhf18032342@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 8',
'cc5291f7-523c-4129-8f34-6f599c13eab9', null, 'VIETNAMESE', 'Test 8', null),
(9, '+84 93 728 92 61', 'jhf18023432@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 9',
'8f53b4b5-07fd-4061-b9bc-f7be9dc55365', null, 'VIETNAMESE', 'Test 9', null),
(10, '+84 93 729 92 61', 'jhf18234032@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 10',
'8218a647-80c7-4a85-accb-87e3528556f1', null, 'VIETNAMESE', 'Test 10', null);

-- Add job_detail
insert into job_detail values
(1, 1, 'SINGLE_TIME', 1231231, '2022-08-18 19:42:44.843000 +00:00', 'VIETNAMESE', 'Test 1', null, 678678, 123123123, 'VND', null),
(2, 2, 'SINGLE_TIME', 1231435, '2022-08-18 19:42:44.843000 +00:00', 'VIETNAMESE', 'Test 2', null, 345345, 123123123, 'VND', null),
(3, 3, 'SINGLE_TIME', 1231231345, '2022-08-18 19:42:44.843000 +00:00', 'VIETNAMESE', 'Test 3', null, 345345, 123123123, 'VND', null),
(4, 4, 'SINGLE_TIME', 1231233451, '2022-08-18 19:42:44.843000 +00:00', 'VIETNAMESE', 'Test 4', null, 34534534, 123123123, 'VND', null),
(5, 5, 'SINGLE_TIME', 334345, '2022-08-18 19:42:44.843000 +00:00', 'VIETNAMESE', 'Test 5', null, 345345345, 123123123, 'VND', null),
(6, 6, 'SINGLE_TIME', 1231234531, '2022-08-18 19:42:44.843000 +00:00', 'VIETNAMESE', 'Test 6', null, 345345345, 123123123, 'VND', null),
(7, 7, 'SINGLE_TIME', 1231345231, '2022-08-18 19:42:44.843000 +00:00', 'VIETNAMESE', 'Test 7', null, 345345345, 123123123, 'VND', null),
(8, 8, 'SINGLE_TIME', 1231345231, '2022-08-18 19:42:44.843000 +00:00', 'VIETNAMESE', 'Test 8', null, 345345345, 345345345345, 'VND', null),
(9, 9, 'SINGLE_TIME', 1231234531, '2022-08-18 19:42:44.843000 +00:00', 'VIETNAMESE', 'Test 9', null, 345345345, 345345345345, 'VND', null),
(10, 10, 'SINGLE_TIME', 1233451231, '2022-08-18 19:42:44.843000 +00:00', 'VIETNAMESE', 'Test 10', null, 345345345, 12312312312, 'VND', null);

-- Add job_offer
insert into job_offer values
(1, 'b0405757-efe6-4b48-aac9-e8db6cfcda09', 'Offer 1', true, 123123, 1, 1),
(2, '0b66300f-4737-42bc-b226-a1a9062377c8', 'Offer 2', true, 1231323, 2, 2),
(3, 'b4162b69-8749-42ce-a967-7d8ffbc0ca8d', 'Offer 3', true, 1231223, 3, 3),
(4, '7f8a97c2-fd6e-40b8-a338-d153f68f0d7a', 'Offer 4', true, 1233423, 4, 4),
(5, 'b4162b69-8749-42ce-a967-7d8ffbc0ca8d', 'Offer 5', true, 12332123, 5, 5),
(6, '76926e0e-5f3c-40d8-928f-2eb600550af3', 'Offer 6', true, 12315723, 6, 6),
(7, '63009811-c296-40d3-873b-57550e19c5fa', 'Offer 7', true, 1231673, 7, 7),
(8, '4f529994-bd62-44ee-a73c-403c338eb998', 'Offer 8', true, 57777767, 8, 8),
(9, 'dc8bc01c-06ae-473c-9687-6e8d32e1ad8e', 'Offer 9', true, 5675567, 9, 9),
(10, 'f8689ec3-10c1-4458-a8a9-b02b32de5fd0', 'Offer 10', true, 1236123, 10, 10);

-- Add talent
insert into talent values
(1, '+84 93 720 92 61', 'jhf18032@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi',
'cc5291f7-523c-4129-8f34-6f599c13ebb9', null, 'VIETNAMESE', 'Test 1', null),
(2, '+84 93 721 92 61', 'jhf180sdf32@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 2',
'5f5ae5f9-919d-4088-b1fe-e6eb4a7602ff', null, 'VIETNAMESE', 'Test 2', null),
(3, '+84 93 722 92 61', 'jhf180sdfsdf32@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 3',
'20ecb18f-cfcb-4b92-8d89-2e5ab09daef4', null, 'VIETNAMESE', 'Test 3', null),
(4, '+84 93 723 92 61', 'jhf18sdf032@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 4',
'3ff617ac-16cf-493c-b32f-fdaab0f9147a', null, 'VIETNAMESE', 'Test 4', null),
(5, '+84 93 724 92 61', 'jhf18sdf032@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 5',
'b299a265-1b9b-41ca-89ba-fc7a1abbaf77', null, 'VIETNAMESE', 'Test 5', null),
(6, '+84 93 725 92 61', 'jhf1fff8032@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 6',
'cc5291f7-523c-4129-8f34-6f599c13ebb9', null, 'VIETNAMESE', 'Test 6', null),
(7, '+84 93 726 92 61', 'jhf18324032@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 7',
'5fcc4613-908a-4d0c-9472-9dc51f53c6ed', null, 'VIETNAMESE', 'Test 7', null),
(8, '+84 93 727 92 61', 'jhf18032342@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 8',
'cc5291f7-523c-4129-8f34-6f599c13eab9', null, 'VIETNAMESE', 'Test 8', null),
(9, '+84 93 728 92 61', 'jhf18023432@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 9',
'8f53b4b5-07fd-4061-b9bc-f7be9dc55365', null, 'VIETNAMESE', 'Test 9', null),
(10, '+84 93 729 92 61', 'jhf18234032@xcoxc.com', 'Group 3Phu Thinh Block, Nguyen Hue Street', '2022-08-18 19:42:44.843000 +00:00', 'GUEST', 'Khôi Khôi 10',
'8218a647-80c7-4a85-accb-87e3528556f1', null, 'VIETNAMESE', 'Test 10', null);

-- Add booking
insert into booking values
(1, 'ec30d150-ee90-420a-8fdd-038844795788', true, '2022-08-18 19:42:44.843000 +00:00', 'TALENT_PENDING', 1, 1, 1),
(2, 'e4bc6184-609f-4be6-9240-91ddeb15dd87', true, '2022-08-18 19:42:44.843000 +00:00', 'TALENT_PENDING', 2, 2, 2),
(3, 'd8b9fc05-ec72-4c12-9f8b-aa9b68a598a3', true, '2022-08-18 19:42:44.843000 +00:00', 'TALENT_PENDING', 3, 3, 3),
(4, '2873ad11-13e0-4c2f-b8d3-1c7af9081979', true, '2022-08-18 19:42:44.843000 +00:00', 'TALENT_PENDING', 4, 4, 4),
(5, '69601014-c397-4753-8c6b-33c271b52c42', true, '2022-08-18 19:42:44.843000 +00:00', 'TALENT_PENDING', 5, 5, 5),
(6, '57b7e64c-961e-483a-87d4-ea7d810167bb', true, '2022-08-18 19:42:44.843000 +00:00', 'TALENT_PENDING', 6, 6, 6),
(7, '2446e876-f430-4cf4-85c8-ab78393c9728', true, '2022-08-18 19:42:44.843000 +00:00', 'TALENT_PENDING', 7, 7, 7),
(8, '944179a3-8e0c-4807-90b7-3b9e6f19880d', true, '2022-08-18 19:42:44.843000 +00:00', 'TALENT_PENDING', 8, 8, 8),
(9, '9ded56ea-d4cc-4766-a787-185d108192f7', true, '2022-08-18 19:42:44.843000 +00:00', 'TALENT_PENDING', 9, 9, 9),
(10, '3513e3d4-6123-42cd-81b4-d95ffca78b14', true, '2022-08-18 19:42:44.843000 +00:00', 'TALENT_PENDING', 10, 10, 10);

-- Add package
insert into package values
(1, 'a7d73bd4-cfd7-4869-86dc-7ec587047e41', 'Package 1', 1, true, 1),
(2, 'bc99559d-636a-414a-bbe6-b25435bce018', 'Package 2', 2, true, 2),
(3, '611f6897-a138-4208-a765-e5b84d77910c', 'Package 3', 3, true, 3),
(4, 'c655035c-d0b8-4dc9-8375-8ba5598a25d5', 'Package 4', 4, true, 4),
(5, '60e2d4a7-a735-4b2a-8f23-dd4059511259', 'Package 5', 5, true, 5),
(6, 'f24ed3c3-7ab0-44df-a957-863a8c5c81d1', 'Package 6', 6, true, 6),
(7, 'a0a9c7e9-d892-447c-88e3-04d996ac9ec3', 'Package 7', 7, true, 7),
(8, 'f7858af5-9e2c-4932-ad72-ef3ea84054ab', 'Package 8', 8, true, 8),
(9, '73b5ea67-feff-4486-bef1-3a32f874d484', 'Package 9', 9, true, 9),
(10, '41f8588a-e751-4d42-b441-4d0b2f2fa1fe', 'Package 10', 10, true, 10);

-- Add admin
insert into admin values
(1, 'Admin', '74bcd4eb-0384-4ab9-ab2f-679f8127dc04');

-- Add feedback
insert into talent_feedback values
(1, '2022-08-18 19:42:44.843000 +00:00', 'SOLVED', 1, 'b9650052-04a7-4ecf-b656-b9398f2f6823', 'VIETNAMESE', 'Test 1', null, 1),
(2, '2022-08-18 19:42:44.843000 +00:00', 'SOLVED', 1, 'ce0fd09e-4157-4d95-b004-9f52ba0da311', 'VIETNAMESE', 'Test 2', null, 2),
(3, '2022-08-18 19:42:44.843000 +00:00', 'SOLVED', 1, '3717b1ae-4e16-46cc-8bb2-39944d5d13d7', 'VIETNAMESE', 'Test 3', null, 3),
(4, '2022-08-18 19:42:44.843000 +00:00', 'SOLVED', 1, '59b3cf80-39a3-4e17-9308-2c6fc03dd15e', 'VIETNAMESE', 'Test 4', null, 4),
(5, '2022-08-18 19:42:44.843000 +00:00', 'SOLVED', 1, '90bf08ae-769b-46ba-835e-fea64ba55cfa', 'VIETNAMESE', 'Test 5', null, 5),
(6, '2022-08-18 19:42:44.843000 +00:00', 'SOLVED', 1, 'dc9b8015-1d2e-4e0d-abc6-7d0b68e192b0', 'VIETNAMESE', 'Test 6', null, 6),
(7, '2022-08-18 19:42:44.843000 +00:00', 'SOLVED', 1, '5074fdeb-0213-4db9-9d63-a298995d585a', 'VIETNAMESE', 'Test 7', null, 7),
(8, '2022-08-18 19:42:44.843000 +00:00', 'SOLVED', 1, '33319b49-7d6c-457d-9a28-ac0a5749c27c', 'VIETNAMESE', 'Test 8', null, 8),
(9, '2022-08-18 19:42:44.843000 +00:00', 'SOLVED', 1, '8ddd3d9a-7435-4b36-a9b5-81ab73f9784b', 'VIETNAMESE', 'Test 9', null, 9),
(10, '2022-08-18 19:42:44.843000 +00:00', 'SOLVED', 1, '8be4a1bb-46c9-465c-bc96-ffeff1d21815', 'VIETNAMESE', 'Test 10', null, 10);

