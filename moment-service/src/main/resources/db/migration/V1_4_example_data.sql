-- Initialize row number
SET @row_num = 0;

-- Create a temporary table with row numbers
CREATE TEMPORARY TABLE temp_moments AS
SELECT id, (@row_num := @row_num + 1) AS rn
FROM moments
ORDER BY id;

-- Update host_id = 1 for rows 1 to 11
UPDATE moments
    JOIN temp_moments ON moments.id = temp_moments.id
SET moments.host_id = 1
    WHERE temp_moments.rn BETWEEN 1 AND 11;

-- Update host_id = 2 for rows 12 to 22
UPDATE moments
    JOIN temp_moments ON moments.id = temp_moments.id
SET moments.host_id = 2
    WHERE temp_moments.rn BETWEEN 12 AND 22;

-- Update host_id = 3 for rows 23 to 34
UPDATE moments
    JOIN temp_moments ON moments.id = temp_moments.id
SET moments.host_id = 3
    WHERE temp_moments.rn BETWEEN 23 AND 34;

-- Clean up
DROP TEMPORARY TABLE temp_moments;