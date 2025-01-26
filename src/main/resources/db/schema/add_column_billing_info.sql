-- Add createdAt collumn on billing_info table
ALTER TABLE billing_info ADD COLUMN IF NOT EXISTS created_at timestamp(6) NOT NULL;

-- Add updatedAt collumn on billing_info table
ALTER TABLE billing_info ADD COLUMN IF NOT EXISTS updated_at timestamp(6) NOT NULL;