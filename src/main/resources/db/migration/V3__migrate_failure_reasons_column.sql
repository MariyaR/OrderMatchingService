ALTER TABLE "trade"
ADD COLUMN failure_reasons TEXT;


ALTER TABLE "trade"
DROP COLUMN IF EXISTS failure_reason;
