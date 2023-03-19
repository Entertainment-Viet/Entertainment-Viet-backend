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
	WHERE a.search_token @@ to_tsquery('english', CONCAT(search_input, ':*'));
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
   WHERE e.search_token @@ to_tsquery('english', CONCAT(search_input, ':*'));
END
$function$
;
