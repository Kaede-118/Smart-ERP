CREATE TABLE product_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    parent_id BIGINT DEFAULT NULL,
    status INT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT(1) DEFAULT 0
);

CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id BIGINT DEFAULT NULL,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    cover_url VARCHAR(255) DEFAULT NULL,
    cost_price DECIMAL(10,2) DEFAULT 0.00,
    sale_price DECIMAL(10,2) DEFAULT 0.00,
    unit VARCHAR(20) DEFAULT NULL,
    status INT DEFAULT 1,
    description TEXT DEFAULT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT(1) DEFAULT 0
);

-- Seed categories
INSERT INTO product_category (name, status) VALUES
('电子产品', 1),
('办公用品', 1),
('日用品', 1);

-- Seed products
INSERT INTO product (category_id, name, code, cost_price, sale_price, unit, status) VALUES
(1, '机械键盘', 'P001', 200.00, 299.00, '个', 1),
(1, '无线鼠标', 'P002', 50.00, 89.00, '个', 1),
(2, 'A4打印纸', 'P003', 18.00, 25.00, '包', 1);
