-- Adding method to find if the search_loc location is a start_loc location or its ancestors
DROP FUNCTION IF EXISTS check_location(UUID,UUID);
CREATE FUNCTION check_location(search_loc UUID, start_loc UUID)
	RETURNS INT
	LANGUAGE PLPGSQL
  AS
$$
DECLARE
	location_count integer;
BEGIN
	WITH RECURSIVE name_tree AS (
	   SELECT id, parent_id, name, uid
	   FROM "location" l
	   WHERE uid = start_loc-- this is the starting point you want in your recursion

	   UNION all

	   SELECT l2.id, l2.parent_id, l2.name, l2.uid
	   FROM "location" l2
	     JOIN name_tree p ON p.parent_id = l2.id  -- this is the recursion
	)
	SELECT COUNT(*) -- Find number of matched uid of search location
	INTO location_count
	FROM name_tree
	WHERE uid = search_loc;

	RETURN location_count;
END;
$$;