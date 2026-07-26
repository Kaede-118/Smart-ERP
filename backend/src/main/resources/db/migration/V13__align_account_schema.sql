ALTER TABLE account_record RENAME TO account_transaction;

ALTER TABLE account_transaction ADD COLUMN change_type VARCHAR(10) DEFAULT NULL COMMENT 'IN / OUT' AFTER type;

UPDATE account_transaction SET change_type = 'IN' WHERE type = 'INCOME';
UPDATE account_transaction SET change_type = 'OUT' WHERE type = 'EXPENSE';

ALTER TABLE company_account ADD COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP AFTER remark;
