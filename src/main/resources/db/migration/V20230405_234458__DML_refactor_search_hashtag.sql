DROP INDEX IF EXISTS account_fulltext_idx;
DROP INDEX IF EXISTS event_fulltext_idx;

ALTER TABLE account
DROP column search_token;
ALTER TABLE event
DROP column search_token;

ALTER TABLE account
ADD COLUMN search_token TSVECTOR GENERATED ALWAYS AS (to_tsvector('simple', display_name || ' ' || immutable_array_to_string(hash_tag, ' '::TEXT))) STORED;
ALTER TABLE event
ADD COLUMN search_token TSVECTOR GENERATED ALWAYS AS (to_tsvector('simple', name || ' ' || immutable_array_to_string(hash_tag, ' '::TEXT))) STORED;

CREATE INDEX account_fulltext_idx
    ON account USING GIN (search_token);
CREATE INDEX event_fulltext_idx
    ON event USING GIN (search_token);

ALTER TABLE account ALTER COLUMN hash_tag SET DEFAULT array[]::TEXT[];
UPDATE account SET hash_tag = array[]::TEXT[];
ALTER TABLE event ALTER COLUMN hash_tag SET DEFAULT array[]::TEXT[];
UPDATE event SET hash_tag = array[]::TEXT[];

CREATE OR REPLACE FUNCTION search_talent(search_input TEXT)
 RETURNS TABLE(idx BIGINT)
 LANGUAGE plpgsql
AS $function$
BEGIN
RETURN QUERY
   SELECT a.id
   FROM account a
		inner join users u on a.id = u.id
		inner join talent t on u.id = t.id
	WHERE a.search_token @@ to_tsquery('simple', concat(regexp_replace(trim(search_input), '\W+', ':* & ', 'gm'), ':*'));
END
$function$
;


CREATE OR REPLACE FUNCTION search_event(search_input TEXT)
 RETURNS TABLE(idx BIGINT)
 LANGUAGE plpgsql
AS $function$
BEGIN
RETURN QUERY
   SELECT e.id
   FROM event e
   WHERE a.search_token @@ to_tsquery('simple', concat(regexp_replace(trim(search_input), '\W+', ':* & ', 'gm'), ':*'));
END
$function$
;