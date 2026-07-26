CREATE TABLE company_account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_name VARCHAR(50) NOT NULL DEFAULT '企业账户',
    balance DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE expense (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    expense_no VARCHAR(50) NOT NULL UNIQUE,
    employee_id BIGINT DEFAULT NULL,
    employee_name VARCHAR(50) DEFAULT NULL,
    department VARCHAR(50) DEFAULT NULL,
    type VARCHAR(30) NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    description VARCHAR(500) DEFAULT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    attachment_url VARCHAR(255) DEFAULT NULL,
    remark VARCHAR(255) DEFAULT NULL,
    create_by BIGINT DEFAULT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    approve_time DATETIME DEFAULT NULL,
    pay_time DATETIME DEFAULT NULL,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Initialize company account
INSERT INTO company_account (account_name, balance) VALUES ('企业账户', 100000.00);

-- Seed expenses
INSERT INTO expense (expense_no, employee_id, employee_name, department, type, amount, description, status, create_time) VALUES
('BX20260701001', 1, '管理员', '管理部', 'TRAVEL', 3500.00, '深圳出差住宿交通费', 'PAID', DATE_SUB(NOW(), INTERVAL 25 DAY)),
('BX20260702001', 1, '管理员', '管理部', 'OFFICE', 860.00, '采购办公用品', 'PAID', DATE_SUB(NOW(), INTERVAL 22 DAY)),
('BX20260703001', 1, '管理员', '管理部', 'ENTERTAINMENT', 1200.00, '客户招待餐费', 'PAID', DATE_SUB(NOW(), INTERVAL 20 DAY)),
('BX20260704001', 1, '管理员', '管理部', 'TRAINING', 5000.00, '员工培训课程费用', 'PAID', DATE_SUB(NOW(), INTERVAL 18 DAY)),
('BX20260705001', 1, '管理员', '管理部', 'TRANSPORT', 320.00, '市内交通费', 'APPROVED', DATE_SUB(NOW(), INTERVAL 15 DAY)),
('BX20260706001', 1, '管理员', '管理部', 'MAINTENANCE', 1500.00, '空调维修费', 'APPROVED', DATE_SUB(NOW(), INTERVAL 12 DAY)),
('BX20260707001', 1, '管理员', '管理部', 'OTHER', 680.00, '快递费及杂项', 'PENDING', DATE_SUB(NOW(), INTERVAL 8 DAY)),
('BX20260708001', 1, '管理员', '管理部', 'TRAVEL', 2800.00, '广州出差费用', 'PENDING', DATE_SUB(NOW(), INTERVAL 5 DAY)),
('BX20260709001', 1, '管理员', '管理部', 'OFFICE', 450.00, '打印纸墨盒', 'PENDING', DATE_SUB(NOW(), INTERVAL 3 DAY)),
('BX20260710001', 1, '管理员', '管理部', 'ENTERTAINMENT', 900.00, '商务宴请', 'REJECTED', DATE_SUB(NOW(), INTERVAL 1 DAY));
