ALTER TABLE company_account
    ADD COLUMN currency VARCHAR(10) DEFAULT 'CNY',
    ADD COLUMN status INT DEFAULT 1,
    ADD COLUMN remark VARCHAR(255) DEFAULT NULL;

-- Reset balance to 500,000 and insert if not exists
UPDATE company_account SET balance = 500000.00, account_name = '企业基本账户', remark = 'ERP 系统主账户' WHERE id = 1;

INSERT IGNORE INTO company_account (id, account_name, balance, currency, status, remark)
VALUES (1, '企业基本账户', 500000.00, 'CNY', 1, 'ERP 系统主账户');

CREATE TABLE IF NOT EXISTS account_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    change_amount DECIMAL(14,2) NOT NULL,
    before_balance DECIMAL(14,2) NOT NULL,
    after_balance DECIMAL(14,2) NOT NULL,
    type VARCHAR(20) NOT NULL COMMENT 'INCOME / EXPENSE',
    business_type VARCHAR(30) DEFAULT NULL COMMENT 'PURCHASE / SALES / EXPENSE / ADJUST',
    business_id BIGINT DEFAULT NULL,
    remark VARCHAR(255) DEFAULT NULL,
    operator_id BIGINT DEFAULT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);
