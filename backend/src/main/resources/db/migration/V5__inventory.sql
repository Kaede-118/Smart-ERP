CREATE TABLE inventory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    quantity INT DEFAULT 0,
    warning_value INT DEFAULT 10,
    warehouse_id BIGINT DEFAULT 1,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE inventory_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    change_qty INT NOT NULL,
    before_qty INT NOT NULL,
    after_qty INT NOT NULL,
    type VARCHAR(20) NOT NULL,
    business_type VARCHAR(20) DEFAULT NULL,
    business_id BIGINT DEFAULT NULL,
    remark VARCHAR(255) DEFAULT NULL,
    operator_id BIGINT DEFAULT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Initialize inventory for seeded products
INSERT INTO inventory (product_id, quantity) VALUES
(1, 100),  -- 机械键盘
(2, 200),  -- 无线鼠标
(3, 500);  -- A4打印纸
