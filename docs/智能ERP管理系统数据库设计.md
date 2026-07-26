# 智能ERP管理系统数据库设计 v0.1

```
# 智能ERP管理系统数据库设计## 1. 数据库信息数据库：erp_system数据库版本：MySQL 8.0字符集：utf8mb4设计原则：- 业务模块独立- 核心数据可追溯- 支持后续AI分析
```

---

# 2. 公共字段规范

所有业务表：

```
id bigint primary key

create_time datetimeupdate_time datetimecreate_by bigintupdate_by bigintdeleted tinyint
```

说明：

| 字段          | 作用   |
| ----------- | ---- |
| id          | 主键   |
| create_time | 创建时间 |
| update_time | 更新时间 |
| create_by   | 创建人  |
| deleted     | 逻辑删除 |

---

# 3. 系统权限模块

## 3.1 用户表

表：

```
sys_user
```

设计：

```
CREATE TABLE sys_user(    id BIGINT PRIMARY KEY,    username VARCHAR(50),    password VARCHAR(255),    nickname VARCHAR(50),    phone VARCHAR(20),    email VARCHAR(100),    status INT,    create_time DATETIME,    update_time DATETIME);
```

示例：

| id  | username  | nickname |
| --- | --------- | -------- |
| 1   | admin     | 管理员      |
| 2   | warehouse | 仓库员      |

---

## 3.2 角色表

```
sys_role
```

字段：

```
idrole_namerole_codedescription
```

数据：

```
ADMINWAREHOUSESALES
```

---

## 3.3 权限表

```
sys_permission
```

字段：

```
idnamecodetypeparent_id
```

支持树形菜单：

```
商品管理├── 查看商品├── 添加商品└── 删除商品
```

---

## 3.4 用户角色关系

```
sys_user_role
```

关系：

```
User N : M Role
```

字段：

```
user_idrole_id
```

---

## 3.5 角色权限关系

```
sys_role_permission
```

字段：

```
role_idpermission_id
```

---

# 4. 商品模块

## 4.1 商品分类

表：

```
product_category
```

字段：

```
idnameparent_idstatus
```

支持：

```
电子产品├── 手机└── 电脑
```

---

## 4.2 商品表

表：

```
product
```

字段：

```
idcategory_idnamecodecover_urlcost_pricesale_priceunitstatusdescription
```

说明：

| 字段         | 说明   |
| ---------- | ---- |
| cost_price | 成本价  |
| sale_price | 售价   |
| cover_url  | 图片地址 |

---

# 5. 库存模块

这里重点设计。

## 5.1 库存表

表：

```
inventory
```

字段：

```
idproduct_idquantitywarning_valueupdate_time
```

例：

```
商品A库存:100
```

---

## 5.2 库存流水表

表：

```
inventory_record
```

字段：

```
idproduct_idtypequantitybefore_stockafter_stockbusiness_typebusiness_idoperator_idcreate_time
```

type：

```
IN入库OUT出库ADJUST调整
```

business_type：

```
PURCHASE采购入库SALES销售出库
```

示例：

采购50个：

```
before_stock:100quantity:50after_stock:150
```

---

# 6. 采购模块

## 6.1 供应商

表：

```
supplier
```

字段：

```
idnamecontactphoneaddressstatus
```

---

## 6.2 采购订单

表：

```
purchase_order
```

字段：

```
idorder_nosupplier_idtotal_amountstatuscreator_idcreate_time
```

状态：

```
DRAFT草稿AUDIT审核中COMPLETED完成CANCEL取消
```

---

## 6.3 采购明细

表：

```
purchase_item
```

字段：

```
idorder_idproduct_idquantitypriceamount
```

关系：

```
purchase_order1|Npurchase_item
```

---

# 7. 销售模块

## 7.1 客户表

```
customer
```

字段：

```
idnamephoneaddress

level
```

---

## 7.2 销售订单

```
sales_order
```

字段：

```
idorder_nocustomer_idtotal_amountstatuscreate_time
```

---

## 7.3 销售明细

```
sales_item
```

字段：

```
idorder_idproduct_idquantitypriceamount
```

---

# 8. 数据统计模块

## 销售统计

不直接建表。

通过：

```
sales_order+sales_item
```

聚合。

例如：

```
SELECT

SUM(amount)

FROM sales_item
```

得到销售额。

---

## 库存统计

来源：

```
inventory
```

---

# 9. 操作日志模块（增强）

表：

```
operation_log
```

字段：

```
iduser_id

module

operationrequest_urlipcreate_time
```

记录：

```
管理员删除商品时间:2026-08-01
```

---

# 10. AI分析模块（预留）

## AI任务表

```
ai_analysis_task
```

字段：

```
idtypestatusinput_datacreate_time
```

type:

```
SALES_REPORTINVENTORY_ANALYSISPURCHASE_ADVICE
```

---

## AI报告表

```
ai_report
```

字段：

```
idtask_idtitlecontentcreate_time
```

保存：

AI生成的分析报告。

---

# 11. 核心业务关系

```
用户 |权限商品 |库存供应商 |采购订单 |库存增加客户 |销售订单 |库存减少
```

---

# 12. 后续优化方向

## 索引

重点：

product.code

order_no

create_time

## 数据归档

历史订单量大时：

按年份归档。

## AI分析

提供：

- 销售趋势数据
- 库存变化数据
- 商品热度数据

作为模型输入。

```
---这一版数据库设计完成后，下一步就不是继续画大饼了喵～应该进入**接口设计**。因为ERP项目面试时最容易被问：> “你采购完成后库存怎么变化？”>> “订单和库存怎么保证一致？”>> “为什么设计库存流水？”接口设计阶段可以把这些业务流程固定下来。下一步建议做：**《智能ERP系统 REST API接口文档 v0.1》**然后基本就能开始敲代码了 ✨
```



---
