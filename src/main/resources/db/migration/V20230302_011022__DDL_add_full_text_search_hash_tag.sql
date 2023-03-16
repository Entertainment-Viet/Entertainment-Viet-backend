CREATE OR REPLACE FUNCTION immutable_array_to_string(arr ANYARRAY, sep TEXT)
RETURNS TEXT LANGUAGE SQL IMMUTABLE
AS $$
    SELECT array_to_string(arr, sep);
$$;


ALTER TABLE account
ADD COLUMN hash_tag TEXT[],
ADD COLUMN search_token TSVECTOR GENERATED ALWAYS AS (to_tsvector('english', display_name || ' ' || immutable_array_to_string(hash_tag, ' '::TEXT))) STORED;

DROP INDEX IF EXISTS account_fulltext_idx;
CREATE INDEX account_fulltext_idx
    ON account using GIN (search_token);


ALTER TABLE event
ADD COLUMN hash_tag TEXT[],
ADD COLUMN search_token TSVECTOR GENERATED ALWAYS AS (to_tsvector('english', name || ' ' || immutable_array_to_string(hash_tag, ' '::TEXT))) STORED;

DROP INDEX IF EXISTS event_fulltext_idx;
CREATE INDEX event_fulltext_idx
    ON event using GIN (search_token);