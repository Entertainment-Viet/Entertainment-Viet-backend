ALTER TABLE account
ADD COLUMN email TEXT;

UPDATE account
SET email = (SELECT email FROM organizer_detail WHERE account.id = organizer_detail.organizer_id)
WHERE id IN (SELECT id FROM organizer);

UPDATE account
SET email = (SELECT email FROM talent_detail WHERE account.id = talent_detail.talent_id)
WHERE id IN (SELECT id FROM talent);

ALTER TABLE organizer_detail
DROP COLUMN email;

ALTER TABLE talent_detail
DROP COLUMN email;
