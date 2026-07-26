-- ============================================
-- 清空资金流水，重置余额 50 万，重新生成合理流水
-- ============================================

DELETE FROM account_transaction;
UPDATE company_account SET balance = 500000.00, account_name = '企业基本账户', remark = 'ERP 系统主账户' WHERE id = 1;

-- 用变量追踪余额
SET @bal = 500000.00;

-- 采购入库 → 资金支出
INSERT INTO account_transaction (change_amount, before_balance, after_balance, type, change_type, business_type, business_id, remark, operator_id, create_time)
SELECT CAST(p.total_amount AS CHAR), @bal := @bal, @bal := @bal - p.total_amount, 'EXPENSE', 'OUT', 'PURCHASE', p.id, '采购付款', 1, p.create_time
FROM purchase_order p WHERE p.status = 'RECEIVED' ORDER BY p.create_time;

-- 销售出库 → 资金收入
INSERT INTO account_transaction (change_amount, before_balance, after_balance, type, change_type, business_type, business_id, remark, operator_id, create_time)
SELECT CAST(s.total_amount AS CHAR), @bal := @bal, @bal := @bal + s.total_amount, 'INCOME', 'IN', 'SALES', s.id, '销售收款', 1, s.create_time
FROM sales_order s WHERE s.status = 'COMPLETED' ORDER BY s.create_time;

-- 费用付款 → 资金支出
INSERT INTO account_transaction (change_amount, before_balance, after_balance, type, change_type, business_type, business_id, remark, operator_id, create_time)
SELECT CAST(e.amount AS CHAR), @bal := @bal, @bal := @bal - e.amount, 'EXPENSE', 'OUT', 'EXPENSE', e.id, '费用付款', 1, e.pay_time
FROM expense e WHERE e.status = 'PAID' ORDER BY e.pay_time;
