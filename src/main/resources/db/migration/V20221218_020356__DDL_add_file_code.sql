ALTER TABLE booking
ADD COLUMN finish_proof TEXT[];

ALTER TABLE event_detail
ADD COLUMN description_img TEXT[];

ALTER TABLE review
ADD COLUMN review_img TEXT[];

ALTER TABLE organizer_detail
ADD COLUMN description_img TEXT[];

ALTER TABLE talent_detail
ADD COLUMN description_img TEXT[];