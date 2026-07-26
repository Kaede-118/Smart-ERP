-- ============================================
-- 新增商品分类
-- ============================================
INSERT INTO product_category (id, name, parent_id, status) VALUES
(4, '食品饮料', NULL, 1),
(5, '家居家装', NULL, 1),
(6, '运动户外', NULL, 1);

-- ============================================
-- 新增商品 (id 4~18)
-- ============================================
INSERT INTO product (id, category_id, name, code, cost_price, sale_price, unit, status) VALUES
(4,  1, '蓝牙耳机',    'P004', 80.00,  159.00, '副', 1),
(5,  1, '移动电源',    'P005', 60.00,  129.00, '个', 1),
(6,  1, 'USB集线器',   'P006', 25.00,  49.00,  '个', 1),
(7,  2, '文件夹',      'P007', 3.50,   8.00,   '个', 1),
(8,  2, '签字笔',      'P008', 2.00,   5.00,   '支', 1),
(9,  2, '订书机',      'P009', 8.00,   18.00,  '个', 1),
(10, 4, '矿泉水',      'P010', 0.80,   2.00,   '瓶', 1),
(11, 4, '苏打饼干',    'P011', 4.00,   9.00,   '包', 1),
(12, 4, '现磨咖啡',    'P012', 12.00,  28.00,  '杯', 1),
(13, 5, 'LED台灯',     'P013', 40.00,  89.00,  '个', 1),
(14, 5, '收纳盒',      'P014', 10.00,  22.00,  '个', 1),
(15, 5, '居家拖鞋',    'P015', 15.00,  35.00,  '双', 1),
(16, 6, '跳绳',        'P016', 12.00,  29.00,  '根', 1),
(17, 6, '瑜伽垫',      'P017', 25.00,  59.00,  '张', 1),
(18, 6, '运动水杯',    'P018', 18.00,  39.00,  '个', 1);

-- ============================================
-- 新增商品初始库存
-- ============================================
INSERT INTO inventory (product_id, quantity, warning_value) VALUES
(4, 200, 20),  (5, 150, 15),  (6, 300, 30),
(7, 500, 50),  (8, 1000, 100), (9, 200, 20),
(10, 2000, 200), (11, 800, 50), (12, 300, 30),
(13, 100, 10), (14, 400, 40),  (15, 300, 30),
(16, 500, 50), (17, 200, 20),  (18, 350, 30);


-- ============================================
-- 30 天采购数据（以天为单位生成）
-- ============================================
-- 辅助：用变量追踪库存，简化 inventory_record 生成
-- 由于 MySQL 不支持数组，直接硬编码各天数据

-- Day -30
INSERT INTO purchase_order (order_no, supplier_id, total_amount, status, creator_id, create_time, update_time)
VALUES ('PO20260627001', 1, 4250.00, 'RECEIVED', 1, DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY));
INSERT INTO purchase_item (order_id, product_id, quantity, price, amount) VALUES
(LAST_INSERT_ID(), 1, 10, 200, 2000), (LAST_INSERT_ID(), 2, 50, 45, 2250);
INSERT INTO inventory_record (product_id, change_qty, before_qty, after_qty, type, business_type, business_id, remark, operator_id, create_time)
VALUES (1, 10, 100, 110, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 30 DAY)),
       (2, 50, 200, 250, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 30 DAY));

-- Day -28
INSERT INTO purchase_order (order_no, supplier_id, total_amount, status, creator_id, create_time, update_time)
VALUES ('PO20260629001', 2, 1800.00, 'RECEIVED', 1, DATE_SUB(NOW(), INTERVAL 28 DAY), DATE_SUB(NOW(), INTERVAL 28 DAY));
INSERT INTO purchase_item (order_id, product_id, quantity, price, amount) VALUES
(LAST_INSERT_ID(), 3, 50, 18, 900), (LAST_INSERT_ID(), 7, 100, 3.5, 350), (LAST_INSERT_ID(), 8, 200, 2, 400);
INSERT INTO inventory_record (product_id, change_qty, before_qty, after_qty, type, business_type, business_id, remark, operator_id, create_time)
VALUES (3, 50, 500, 550, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 28 DAY)),
       (7, 100, 500, 600, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 28 DAY)),
       (8, 200, 1000, 1200, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 28 DAY));

-- Day -25
INSERT INTO purchase_order (order_no, supplier_id, total_amount, status, creator_id, create_time, update_time)
VALUES ('PO20260702001', 1, 6800.00, 'RECEIVED', 1, DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 25 DAY));
INSERT INTO purchase_item (order_id, product_id, quantity, price, amount) VALUES
(LAST_INSERT_ID(), 4, 80, 80, 6400), (LAST_INSERT_ID(), 6, 40, 25, 1000);
INSERT INTO inventory_record (product_id, change_qty, before_qty, after_qty, type, business_type, business_id, remark, operator_id, create_time)
VALUES (4, 80, 200, 280, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 25 DAY)),
       (6, 40, 300, 340, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 25 DAY));

-- Day -22
INSERT INTO purchase_order (order_no, supplier_id, total_amount, status, creator_id, create_time, update_time)
VALUES ('PO20260705001', 2, 3800.00, 'RECEIVED', 1, DATE_SUB(NOW(), INTERVAL 22 DAY), DATE_SUB(NOW(), INTERVAL 22 DAY));
INSERT INTO purchase_item (order_id, product_id, quantity, price, amount) VALUES
(LAST_INSERT_ID(), 10, 1000, 0.80, 800), (LAST_INSERT_ID(), 12, 100, 12, 1200), (LAST_INSERT_ID(), 13, 50, 40, 2000);
INSERT INTO inventory_record (product_id, change_qty, before_qty, after_qty, type, business_type, business_id, remark, operator_id, create_time)
VALUES (10, 1000, 2000, 3000, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 22 DAY)),
       (12, 100, 300, 400, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 22 DAY)),
       (13, 50, 100, 150, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 22 DAY));

-- Day -19
INSERT INTO purchase_order (order_no, supplier_id, total_amount, status, creator_id, create_time, update_time)
VALUES ('PO20260708001', 1, 5500.00, 'RECEIVED', 1, DATE_SUB(NOW(), INTERVAL 19 DAY), DATE_SUB(NOW(), INTERVAL 19 DAY));
INSERT INTO purchase_item (order_id, product_id, quantity, price, amount) VALUES
(LAST_INSERT_ID(), 5, 50, 60, 3000), (LAST_INSERT_ID(), 1, 10, 200, 2000), (LAST_INSERT_ID(), 18, 50, 18, 900);
INSERT INTO inventory_record (product_id, change_qty, before_qty, after_qty, type, business_type, business_id, remark, operator_id, create_time)
VALUES (5, 50, 150, 200, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 19 DAY)),
       (1, 10, 110, 120, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 19 DAY)),
       (18, 50, 350, 400, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 19 DAY));

-- Day -16
INSERT INTO purchase_order (order_no, supplier_id, total_amount, status, creator_id, create_time, update_time)
VALUES ('PO20260711001', 2, 4200.00, 'RECEIVED', 1, DATE_SUB(NOW(), INTERVAL 16 DAY), DATE_SUB(NOW(), INTERVAL 16 DAY));
INSERT INTO purchase_item (order_id, product_id, quantity, price, amount) VALUES
(LAST_INSERT_ID(), 14, 200, 10, 2000), (LAST_INSERT_ID(), 15, 100, 15, 1500), (LAST_INSERT_ID(), 11, 100, 4, 400);
INSERT INTO inventory_record (product_id, change_qty, before_qty, after_qty, type, business_type, business_id, remark, operator_id, create_time)
VALUES (14, 200, 400, 600, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 16 DAY)),
       (15, 100, 300, 400, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 16 DAY)),
       (11, 100, 800, 900, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 16 DAY));

-- Day -13
INSERT INTO purchase_order (order_no, supplier_id, total_amount, status, creator_id, create_time, update_time)
VALUES ('PO20260714001', 1, 7200.00, 'RECEIVED', 1, DATE_SUB(NOW(), INTERVAL 13 DAY), DATE_SUB(NOW(), INTERVAL 13 DAY));
INSERT INTO purchase_item (order_id, product_id, quantity, price, amount) VALUES
(LAST_INSERT_ID(), 17, 80, 25, 2000), (LAST_INSERT_ID(), 16, 100, 12, 1200), (LAST_INSERT_ID(), 9, 100, 8, 800),
(LAST_INSERT_ID(), 4, 40, 80, 3200);
INSERT INTO inventory_record (product_id, change_qty, before_qty, after_qty, type, business_type, business_id, remark, operator_id, create_time)
VALUES (17, 80, 200, 280, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 13 DAY)),
       (16, 100, 500, 600, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 13 DAY)),
       (9, 100, 200, 300, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 13 DAY)),
       (4, 40, 280, 320, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 13 DAY));

-- Day -10
INSERT INTO purchase_order (order_no, supplier_id, total_amount, status, creator_id, create_time, update_time)
VALUES ('PO20260717001', 2, 3400.00, 'RECEIVED', 1, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY));
INSERT INTO purchase_item (order_id, product_id, quantity, price, amount) VALUES
(LAST_INSERT_ID(), 10, 1500, 0.80, 1200), (LAST_INSERT_ID(), 11, 200, 4, 800), (LAST_INSERT_ID(), 8, 500, 2, 1000);
INSERT INTO inventory_record (product_id, change_qty, before_qty, after_qty, type, business_type, business_id, remark, operator_id, create_time)
VALUES (10, 1500, 3000, 4500, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 10 DAY)),
       (11, 200, 900, 1100, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 10 DAY)),
       (8, 500, 1200, 1700, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 10 DAY));

-- Day -7
INSERT INTO purchase_order (order_no, supplier_id, total_amount, status, creator_id, create_time, update_time)
VALUES ('PO20260720001', 1, 6300.00, 'RECEIVED', 1, DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY));
INSERT INTO purchase_item (order_id, product_id, quantity, price, amount) VALUES
(LAST_INSERT_ID(), 2, 60, 45, 2700), (LAST_INSERT_ID(), 5, 30, 60, 1800), (LAST_INSERT_ID(), 18, 100, 18, 1800);
INSERT INTO inventory_record (product_id, change_qty, before_qty, after_qty, type, business_type, business_id, remark, operator_id, create_time)
VALUES (2, 60, 250, 310, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 7 DAY)),
       (5, 30, 200, 230, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 7 DAY)),
       (18, 100, 400, 500, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 7 DAY));

-- Day -4
INSERT INTO purchase_order (order_no, supplier_id, total_amount, status, creator_id, create_time, update_time)
VALUES ('PO20260723001', 2, 5100.00, 'RECEIVED', 1, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY));
INSERT INTO purchase_item (order_id, product_id, quantity, price, amount) VALUES
(LAST_INSERT_ID(), 13, 60, 40, 2400), (LAST_INSERT_ID(), 17, 50, 25, 1250), (LAST_INSERT_ID(), 12, 50, 12, 600),
(LAST_INSERT_ID(), 6, 40, 25, 1000);
INSERT INTO inventory_record (product_id, change_qty, before_qty, after_qty, type, business_type, business_id, remark, operator_id, create_time)
VALUES (13, 60, 150, 210, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 4 DAY)),
       (17, 50, 280, 330, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 4 DAY)),
       (12, 50, 400, 450, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 4 DAY)),
       (6, 40, 340, 380, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 4 DAY));

-- Day -1 (昨天)
INSERT INTO purchase_order (order_no, supplier_id, total_amount, status, creator_id, create_time, update_time)
VALUES ('PO20260726001', 1, 4800.00, 'RECEIVED', 1, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY));
INSERT INTO purchase_item (order_id, product_id, quantity, price, amount) VALUES
(LAST_INSERT_ID(), 1, 15, 200, 3000), (LAST_INSERT_ID(), 3, 100, 18, 1800);
INSERT INTO inventory_record (product_id, change_qty, before_qty, after_qty, type, business_type, business_id, remark, operator_id, create_time)
VALUES (1, 15, 120, 135, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 1 DAY)),
       (3, 100, 550, 650, 'INBOUND', 'PURCHASE', (SELECT MAX(id) FROM purchase_order), '采购入库', 1, DATE_SUB(NOW(), INTERVAL 1 DAY));


-- ============================================
-- 30 天销售数据
-- ============================================

-- Day -30
INSERT INTO sales_order (order_no, customer_id, total_amount, status, creator_id, create_time, update_time)
VALUES ('SO20260627001', 1, 2895.00, 'COMPLETED', 1, DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY));
INSERT INTO sales_item (order_id, product_id, quantity, price, amount) VALUES
(LAST_INSERT_ID(), 1, 5, 299, 1495), (LAST_INSERT_ID(), 2, 10, 89, 890), (LAST_INSERT_ID(), 10, 100, 2, 200);
INSERT INTO inventory_record (product_id, change_qty, before_qty, after_qty, type, business_type, business_id, remark, operator_id, create_time)
VALUES (1, -5, 110, 105, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 30 DAY)),
       (2, -10, 250, 240, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 30 DAY)),
       (10, -100, 3000, 2900, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 30 DAY));

-- Day -28
INSERT INTO sales_order (order_no, customer_id, total_amount, status, creator_id, create_time, update_time)
VALUES ('SO20260629001', 2, 1590.00, 'COMPLETED', 1, DATE_SUB(NOW(), INTERVAL 28 DAY), DATE_SUB(NOW(), INTERVAL 28 DAY));
INSERT INTO sales_item (order_id, product_id, quantity, price, amount) VALUES
(LAST_INSERT_ID(), 3, 20, 25, 500), (LAST_INSERT_ID(), 7, 50, 8, 400), (LAST_INSERT_ID(), 8, 100, 5, 500);
INSERT INTO inventory_record (product_id, change_qty, before_qty, after_qty, type, business_type, business_id, remark, operator_id, create_time)
VALUES (3, -20, 550, 530, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 28 DAY)),
       (7, -50, 600, 550, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 28 DAY)),
       (8, -100, 1200, 1100, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 28 DAY));

-- Day -26
INSERT INTO sales_order (order_no, customer_id, total_amount, status, creator_id, create_time, update_time)
VALUES ('SO20260701001', 1, 3780.00, 'COMPLETED', 1, DATE_SUB(NOW(), INTERVAL 26 DAY), DATE_SUB(NOW(), INTERVAL 26 DAY));
INSERT INTO sales_item (order_id, product_id, quantity, price, amount) VALUES
(LAST_INSERT_ID(), 4, 10, 159, 1590), (LAST_INSERT_ID(), 5, 10, 129, 1290), (LAST_INSERT_ID(), 18, 20, 39, 780);
INSERT INTO inventory_record (product_id, change_qty, before_qty, after_qty, type, business_type, business_id, remark, operator_id, create_time)
VALUES (4, -10, 280, 270, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 26 DAY)),
       (5, -10, 200, 190, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 26 DAY)),
       (18, -20, 400, 380, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 26 DAY));

-- Day -24
INSERT INTO sales_order (order_no, customer_id, total_amount, status, creator_id, create_time, update_time)
VALUES ('SO20260703001', 2, 2220.00, 'COMPLETED', 1, DATE_SUB(NOW(), INTERVAL 24 DAY), DATE_SUB(NOW(), INTERVAL 24 DAY));
INSERT INTO sales_item (order_id, product_id, quantity, price, amount) VALUES
(LAST_INSERT_ID(), 1, 3, 299, 897), (LAST_INSERT_ID(), 13, 5, 89, 445), (LAST_INSERT_ID(), 10, 200, 2, 400);
INSERT INTO inventory_record (product_id, change_qty, before_qty, after_qty, type, business_type, business_id, remark, operator_id, create_time)
VALUES (1, -3, 105, 102, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 24 DAY)),
       (13, -5, 150, 145, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 24 DAY)),
       (10, -200, 2900, 2700, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 24 DAY));

-- Day -22
INSERT INTO sales_order (order_no, customer_id, total_amount, status, creator_id, create_time, update_time)
VALUES ('SO20260705001', 1, 4510.00, 'COMPLETED', 1, DATE_SUB(NOW(), INTERVAL 22 DAY), DATE_SUB(NOW(), INTERVAL 22 DAY));
INSERT INTO sales_item (order_id, product_id, quantity, price, amount) VALUES
(LAST_INSERT_ID(), 4, 8, 159, 1272), (LAST_INSERT_ID(), 5, 8, 129, 1032), (LAST_INSERT_ID(), 12, 30, 28, 840),
(LAST_INSERT_ID(), 17, 10, 59, 590);
INSERT INTO inventory_record (product_id, change_qty, before_qty, after_qty, type, business_type, business_id, remark, operator_id, create_time)
VALUES (4, -8, 270, 262, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 22 DAY)),
       (5, -8, 190, 182, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 22 DAY)),
       (12, -30, 400, 370, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 22 DAY)),
       (17, -10, 280, 270, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 22 DAY));

-- Day -20
INSERT INTO sales_order (order_no, customer_id, total_amount, status, creator_id, create_time, update_time)
VALUES ('SO20260707001', 2, 1680.00, 'COMPLETED', 1, DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY));
INSERT INTO sales_item (order_id, product_id, quantity, price, amount) VALUES
(LAST_INSERT_ID(), 10, 500, 2, 1000), (LAST_INSERT_ID(), 11, 50, 9, 450), (LAST_INSERT_ID(), 15, 10, 35, 350);
INSERT INTO inventory_record (product_id, change_qty, before_qty, after_qty, type, business_type, business_id, remark, operator_id, create_time)
VALUES (10, -500, 2700, 2200, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 20 DAY)),
       (11, -50, 900, 850, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 20 DAY)),
       (15, -10, 400, 390, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 20 DAY));

-- Day -18
INSERT INTO sales_order (order_no, customer_id, total_amount, status, creator_id, create_time, update_time)
VALUES ('SO20260709001', 1, 5900.00, 'COMPLETED', 1, DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 18 DAY));
INSERT INTO sales_item (order_id, product_id, quantity, price, amount) VALUES
(LAST_INSERT_ID(), 1, 10, 299, 2990), (LAST_INSERT_ID(), 2, 20, 89, 1780), (LAST_INSERT_ID(), 18, 30, 39, 1170);
INSERT INTO inventory_record (product_id, change_qty, before_qty, after_qty, type, business_type, business_id, remark, operator_id, create_time)
VALUES (1, -10, 102, 92, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 18 DAY)),
       (2, -20, 240, 220, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 18 DAY)),
       (18, -30, 380, 350, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 18 DAY));

-- Day -16
INSERT INTO sales_order (order_no, customer_id, total_amount, status, creator_id, create_time, update_time)
VALUES ('SO20260711001', 2, 3650.00, 'COMPLETED', 1, DATE_SUB(NOW(), INTERVAL 16 DAY), DATE_SUB(NOW(), INTERVAL 16 DAY));
INSERT INTO sales_item (order_id, product_id, quantity, price, amount) VALUES
(LAST_INSERT_ID(), 4, 5, 159, 795), (LAST_INSERT_ID(), 6, 30, 49, 1470), (LAST_INSERT_ID(), 14, 50, 22, 1100);
INSERT INTO inventory_record (product_id, change_qty, before_qty, after_qty, type, business_type, business_id, remark, operator_id, create_time)
VALUES (4, -5, 262, 257, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 16 DAY)),
       (6, -30, 340, 310, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 16 DAY)),
       (14, -50, 600, 550, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 16 DAY));

-- Day -14
INSERT INTO sales_order (order_no, customer_id, total_amount, status, creator_id, create_time, update_time)
VALUES ('SO20260713001', 1, 2880.00, 'COMPLETED', 1, DATE_SUB(NOW(), INTERVAL 14 DAY), DATE_SUB(NOW(), INTERVAL 14 DAY));
INSERT INTO sales_item (order_id, product_id, quantity, price, amount) VALUES
(LAST_INSERT_ID(), 5, 10, 129, 1290), (LAST_INSERT_ID(), 16, 30, 29, 870), (LAST_INSERT_ID(), 9, 30, 18, 540);
INSERT INTO inventory_record (product_id, change_qty, before_qty, after_qty, type, business_type, business_id, remark, operator_id, create_time)
VALUES (5, -10, 182, 172, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 14 DAY)),
       (16, -30, 600, 570, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 14 DAY)),
       (9, -30, 300, 270, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 14 DAY));

-- Day -12
INSERT INTO sales_order (order_no, customer_id, total_amount, status, creator_id, create_time, update_time)
VALUES ('SO20260715001', 2, 4320.00, 'COMPLETED', 1, DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 12 DAY));
INSERT INTO sales_item (order_id, product_id, quantity, price, amount) VALUES
(LAST_INSERT_ID(), 1, 8, 299, 2392), (LAST_INSERT_ID(), 3, 50, 25, 1250), (LAST_INSERT_ID(), 12, 20, 28, 560);
INSERT INTO inventory_record (product_id, change_qty, before_qty, after_qty, type, business_type, business_id, remark, operator_id, create_time)
VALUES (1, -8, 92, 84, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 12 DAY)),
       (3, -50, 530, 480, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 12 DAY)),
       (12, -20, 370, 350, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 12 DAY));

-- Day -10
INSERT INTO sales_order (order_no, customer_id, total_amount, status, creator_id, create_time, update_time)
VALUES ('SO20260717001', 1, 5100.00, 'COMPLETED', 1, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY));
INSERT INTO sales_item (order_id, product_id, quantity, price, amount) VALUES
(LAST_INSERT_ID(), 4, 15, 159, 2385), (LAST_INSERT_ID(), 5, 12, 129, 1548), (LAST_INSERT_ID(), 17, 20, 59, 1180);
INSERT INTO inventory_record (product_id, change_qty, before_qty, after_qty, type, business_type, business_id, remark, operator_id, create_time)
VALUES (4, -15, 257, 242, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 10 DAY)),
       (5, -12, 172, 160, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 10 DAY)),
       (17, -20, 270, 250, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 10 DAY));

-- Day -8
INSERT INTO sales_order (order_no, customer_id, total_amount, status, creator_id, create_time, update_time)
VALUES ('SO20260719001', 2, 1760.00, 'COMPLETED', 1, DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY));
INSERT INTO sales_item (order_id, product_id, quantity, price, amount) VALUES
(LAST_INSERT_ID(), 10, 300, 2, 600), (LAST_INSERT_ID(), 11, 80, 9, 720), (LAST_INSERT_ID(), 14, 20, 22, 440);
INSERT INTO inventory_record (product_id, change_qty, before_qty, after_qty, type, business_type, business_id, remark, operator_id, create_time)
VALUES (10, -300, 2200, 1900, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 8 DAY)),
       (11, -80, 850, 770, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 8 DAY)),
       (14, -20, 550, 530, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 8 DAY));

-- Day -6
INSERT INTO sales_order (order_no, customer_id, total_amount, status, creator_id, create_time, update_time)
VALUES ('SO20260721001', 1, 6480.00, 'COMPLETED', 1, DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY));
INSERT INTO sales_item (order_id, product_id, quantity, price, amount) VALUES
(LAST_INSERT_ID(), 1, 12, 299, 3588), (LAST_INSERT_ID(), 2, 15, 89, 1335), (LAST_INSERT_ID(), 6, 20, 49, 980),
(LAST_INSERT_ID(), 8, 50, 5, 250);
INSERT INTO inventory_record (product_id, change_qty, before_qty, after_qty, type, business_type, business_id, remark, operator_id, create_time)
VALUES (1, -12, 84, 72, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 6 DAY)),
       (2, -15, 220, 205, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 6 DAY)),
       (6, -20, 310, 290, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 6 DAY)),
       (8, -50, 1100, 1050, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 6 DAY));

-- Day -4
INSERT INTO sales_order (order_no, customer_id, total_amount, status, creator_id, create_time, update_time)
VALUES ('SO20260723001', 2, 3950.00, 'COMPLETED', 1, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY));
INSERT INTO sales_item (order_id, product_id, quantity, price, amount) VALUES
(LAST_INSERT_ID(), 13, 20, 89, 1780), (LAST_INSERT_ID(), 15, 30, 35, 1050), (LAST_INSERT_ID(), 16, 40, 29, 1160);
INSERT INTO inventory_record (product_id, change_qty, before_qty, after_qty, type, business_type, business_id, remark, operator_id, create_time)
VALUES (13, -20, 210, 190, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 4 DAY)),
       (15, -30, 390, 360, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 4 DAY)),
       (16, -40, 570, 530, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 4 DAY));

-- Day -2
INSERT INTO sales_order (order_no, customer_id, total_amount, status, creator_id, create_time, update_time)
VALUES ('SO20260725001', 1, 3240.00, 'COMPLETED', 1, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY));
INSERT INTO sales_item (order_id, product_id, quantity, price, amount) VALUES
(LAST_INSERT_ID(), 4, 6, 159, 954), (LAST_INSERT_ID(), 18, 25, 39, 975), (LAST_INSERT_ID(), 10, 400, 2, 800);
INSERT INTO inventory_record (product_id, change_qty, before_qty, after_qty, type, business_type, business_id, remark, operator_id, create_time)
VALUES (4, -6, 242, 236, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 2 DAY)),
       (18, -25, 350, 325, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 2 DAY)),
       (10, -400, 1900, 1500, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 2 DAY));

-- Day -1 (昨天)
INSERT INTO sales_order (order_no, customer_id, total_amount, status, creator_id, create_time, update_time)
VALUES ('SO20260726001', 1, 2830.00, 'COMPLETED', 1, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY));
INSERT INTO sales_item (order_id, product_id, quantity, price, amount) VALUES
(LAST_INSERT_ID(), 1, 5, 299, 1495), (LAST_INSERT_ID(), 5, 5, 129, 645), (LAST_INSERT_ID(), 10, 200, 2, 400);
INSERT INTO inventory_record (product_id, change_qty, before_qty, after_qty, type, business_type, business_id, remark, operator_id, create_time)
VALUES (1, -5, 72, 67, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 1 DAY)),
       (5, -5, 160, 155, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 1 DAY)),
       (10, -200, 1500, 1300, 'OUTBOUND', 'SALES', (SELECT MAX(id) FROM sales_order), '销售出库', 1, DATE_SUB(NOW(), INTERVAL 1 DAY));
