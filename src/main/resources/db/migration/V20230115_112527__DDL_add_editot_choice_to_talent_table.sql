ALTER TABLE talent
ADD COLUMN editor_choice BOOLEAN;

UPDATE talent
SET editor_choice = FALSE;