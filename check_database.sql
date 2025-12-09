-- Check if pression_droite column exists in consultations table
DESCRIBE consultations;

-- Alternative query to check columns
SHOW COLUMNS FROM consultations LIKE '%pression%';

-- Check actual data
SELECT ConsultationID, pression, pression_droite 
FROM consultations 
WHERE pression_droite IS NOT NULL 
OR pression IS NOT NULL
LIMIT 10;
