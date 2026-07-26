# REST API 接口文档

版本：v1.1 | 更新：2026-07-27

基础路径：`/api`

认证方式：`Authorization: Bearer <JWT Token>`

---

## 响应格式

```json
{ "code": 200, "message": "success", "data": {} }
```

| HTTP 状态码 | 业务码 | 含义 |
|-------------|--------|------|
| 200 | 200 | 成功 |
| 200 | 40000~40003 | 参数/业务/库存不足/状态异常 |
| 401 | 40100 | 未登录 |
| 403 | 40300 | 无权限 |
| 404 | 40400~40401 | 资源/用户不存在 |
| 500 | 50000 | 系统异常 |

---

## 1. 认证

### POST /api/auth/login

公开接口，无需 Token。

```json
// Request
{ "username": "admin", "password": "123456" }
// Response
{ "token": "eyJ...", "user": { "id":1, "username":"admin", "nickname":"管理员", "status":1 },
  "roles":["ADMIN"], "permissions":["user:list","user:create",...] }
```

### GET /api/auth/me

当前用户信息。

---

## 2. 用户管理

| 方法 | 路径 | 说明 | 约束 |
|------|------|------|------|
| POST | `/api/users/list` | 分页查询 | page/size/username/status |
| GET | `/api/users/detail/{id}` | 用户详情 | |
| POST | `/api/users` | 新增 | 用户名唯一，密码 BCrypt |
| PUT | `/api/users/{id}` | 修改 | |
| DELETE | `/api/users/{id}` | 逻辑删除 | |
| PUT | `/api/users/{id}/reset-password` | 重置密码 | `newPassword` 为空默认 123456 |
| PUT | `/api/users/{id}/roles` | 分配角色 | 覆盖式，先清后插；空数组=清空 |
| GET | `/api/users/{id}/roles` | 用户角色列表 | |

---

## 3. 角色管理

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/api/roles` | `role:list` | 分页查询，?keyword=&page=&size= |
| GET | `/api/roles/{id}` | | 详情 |
| POST | `/api/roles` | | 新增，`roleCode` 唯一 |
| PUT | `/api/roles/{id}` | | 修改名称/描述，code 不可改 |
| DELETE | `/api/roles/{id}` | | 逻辑删除 |
| PUT | `/api/roles/{id}/permissions` | | 分配权限（覆盖式） |
| GET | `/api/roles/{id}/permissions` | | 角色权限列表 |

---

## 4. 权限管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/permissions` | 全部权限列表（非树形） |
| POST | `/api/permissions` | 新增，`code` 唯一 |
| PUT | `/api/permissions/{id}` | 修改，code 不可改 |
| DELETE | `/api/permissions/{id}` | 逻辑删除 |

### 权限码一览

| 模块 | 权限码 |
|------|--------|
| 用户 | `user:list` `user:view` `user:create` `user:update` `user:delete` |
| 角色 | `role:list` `role:view` `role:create` `role:update` `role:delete` |
| 权限 | `perm:list` `perm:view` `perm:create` `perm:update` `perm:delete` |

命名规范：`module:action`，后续模块按此扩展。

---

## 5. 商品管理

### 商品分类

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/product-categories` | 分类列表 |
| POST | `/api/product-categories` | 新增 `{ name, parentId?, status? }` |
| PUT | `/api/product-categories/{id}` | 修改 |
| DELETE | `/api/product-categories/{id}` | 逻辑删除 |

### 商品

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/products/list` | 分页查询 `?categoryId=&keyword=&page=&size=` |
| GET | `/api/products/{id}` | 详情 |
| POST | `/api/products` | 新增，`code` 唯一 |
| PUT | `/api/products/{id}` | 修改 |
| DELETE | `/api/products/{id}` | 逻辑删除 |

```json
// POST /api/products
{ "categoryId":1, "name":"机械键盘", "code":"P001",
  "costPrice":200, "salePrice":299, "unit":"个", "status":1 }
```

---

## 6. 库存管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/inventory/list` | 库存列表 `?keyword=&categoryId=` |
| GET | `/api/inventory/{productId}` | 单品库存 |
| POST | `/api/inventory/adjust` | 库存调整（增量） |
| GET | `/api/inventory/records` | 流水追溯 `?productId=&type=` |

```json
// POST /api/inventory/adjust
{ "productId":1, "changeQty":20, "remark":"盘点增加" }
// changeQty > 0 = 入库，< 0 = 出库（库存不足返回 40001）
```

---

## 7. 采购管理

### 供应商

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/suppliers` | 供应商列表 |
| POST | `/api/suppliers` | 新增 `{ name, contact?, phone?, address? }` |
| PUT | `/api/suppliers/{id}` | 修改 |
| DELETE | `/api/suppliers/{id}` | 逻辑删除 |

### 采购单

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/purchase/orders` | 采购单列表 |
| GET | `/api/purchase/orders/{id}` | 详情（含明细） |
| POST | `/api/purchase/orders` | 创建（DRAFT，库存不变） |
| POST | `/api/purchase/orders/{id}/receive` | 入库（@Transactional + 库存增加 + 流水） |

```json
// POST /api/purchase/orders
{ "supplierId":1, "items":[
  { "productId":1, "quantity":50, "price":200 }
]}

// POST /api/purchase/orders/{id}/receive
// 状态：DRAFT → RECEIVED（重复入库返回 400）
```

---

## 8. 销售管理

### 客户

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/customers` | 客户列表 |
| POST | `/api/customers` | 新增 `{ name, phone?, address?, level? }` |
| PUT | `/api/customers/{id}` | 修改 |
| DELETE | `/api/customers/{id}` | 逻辑删除 |

### 销售单

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/sales/orders` | 销售单列表 |
| GET | `/api/sales/orders/{id}` | 详情（含明细） |
| POST | `/api/sales/orders` | 创建（PENDING，库存不变） |
| POST | `/api/sales/orders/{id}/complete` | 出库（@Transactional + 库存扣减 + 流水） |

```json
// POST /api/sales/orders
{ "customerId":1, "items":[
  { "productId":1, "quantity":5, "price":299 }
]}

// POST /api/sales/orders/{id}/complete
// 库存不足返回 40001；重复出库返回 400
```

---

## 9. Dashboard

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/dashboard/summary` | 经营摘要（商品数/客户数/库存量/今日购销额/低库存数） |
| GET | `/api/dashboard/trend` | 近 7 天购销趋势 |
| GET | `/api/dashboard/warnings` | 低库存预警列表 |
| GET | `/api/dashboard/top-products` | 热销商品 TOP10 |

---

## 10. 企业增强

### 文件上传

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/files/upload` | 上传文件（multipart/form-data），返回 URL |

### Excel 导入导出

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/excel/products/export` | 导出商品数据（.xlsx） |
| POST | `/api/excel/products/import` | 导入商品数据（multipart） |

---

## 11. AI 经营分析

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/ai/reports` | 生成分析报告（约 5-10 秒） |
| GET | `/api/ai/reports` | 历史报告列表 |
| GET | `/api/ai/reports/{id}` | 报告详情 |

### 报告类型

| type | 数据源 | 说明 |
|------|--------|------|
| `OVERVIEW` | Dashboard 聚合数据 | 综合经营概览 |
| `SALES` | 销售订单 + 明细 | 月度销售趋势、热销商品 |
| `INVENTORY` | 库存 + 流水 | 库存健康度、补货建议 |

```json
// POST /api/ai/reports
{ "type":"OVERVIEW", "range":"MONTH" }
// range: MONTH / WEEK / QUARTER
```

---

## 12. 权限码一览

当前预设 15 条权限码，通过 role:list 等接口可动态管理：

| 模块 | 权限码 |
|------|--------|
| user | `user:list` `user:view` `user:create` `user:update` `user:delete` |
| role | `role:list` `role:view` `role:create` `role:update` `role:delete` |
| perm | `perm:list` `perm:view` `perm:create` `perm:update` `perm:delete` |

后续模块的权限码在产品/库存/采购/销售等 Controller 添加 `@PreAuthorize` 时逐步启用。

---

## 13. 完整端点概览

| # | 方法 | 路径 | 模块 |
|---|------|------|------|
| 1 | POST | `/api/auth/login` | 认证 |
| 2 | GET | `/api/auth/me` | 认证 |
| 3 | POST | `/api/users/list` | 用户 |
| 4 | GET | `/api/users/detail/{id}` | 用户 |
| 5 | POST | `/api/users` | 用户 |
| 6 | PUT | `/api/users/{id}` | 用户 |
| 7 | DELETE | `/api/users/{id}` | 用户 |
| 8 | PUT | `/api/users/{id}/reset-password` | 用户 |
| 9 | PUT | `/api/users/{id}/roles` | 用户-RBAC |
| 10 | GET | `/api/users/{id}/roles` | 用户-RBAC |
| 11 | GET | `/api/roles` | RBAC |
| 12 | POST | `/api/roles` | RBAC |
| 13 | GET | `/api/roles/{id}` | RBAC |
| 14 | PUT | `/api/roles/{id}` | RBAC |
| 15 | DELETE | `/api/roles/{id}` | RBAC |
| 16 | PUT | `/api/roles/{id}/permissions` | RBAC |
| 17 | GET | `/api/roles/{id}/permissions` | RBAC |
| 18 | GET | `/api/permissions` | RBAC |
| 19 | POST | `/api/permissions` | RBAC |
| 20 | PUT | `/api/permissions/{id}` | RBAC |
| 21 | DELETE | `/api/permissions/{id}` | RBAC |
| 22 | GET | `/api/product-categories` | 商品 |
| 23 | POST | `/api/product-categories` | 商品 |
| 24 | PUT | `/api/product-categories/{id}` | 商品 |
| 25 | DELETE | `/api/product-categories/{id}` | 商品 |
| 26 | GET | `/api/products/list` | 商品 |
| 27 | GET | `/api/products/{id}` | 商品 |
| 28 | POST | `/api/products` | 商品 |
| 29 | PUT | `/api/products/{id}` | 商品 |
| 30 | DELETE | `/api/products/{id}` | 商品 |
| 31 | GET | `/api/inventory/list` | 库存 |
| 32 | GET | `/api/inventory/{productId}` | 库存 |
| 33 | POST | `/api/inventory/adjust` | 库存 |
| 34 | GET | `/api/inventory/records` | 库存 |
| 35 | GET | `/api/suppliers` | 采购 |
| 36 | POST | `/api/suppliers` | 采购 |
| 37 | PUT | `/api/suppliers/{id}` | 采购 |
| 38 | DELETE | `/api/suppliers/{id}` | 采购 |
| 39 | GET | `/api/purchase/orders` | 采购 |
| 40 | POST | `/api/purchase/orders` | 采购 |
| 41 | GET | `/api/purchase/orders/{id}` | 采购 |
| 42 | POST | `/api/purchase/orders/{id}/receive` | 采购 |
| 43 | GET | `/api/customers` | 销售 |
| 44 | POST | `/api/customers` | 销售 |
| 45 | PUT | `/api/customers/{id}` | 销售 |
| 46 | DELETE | `/api/customers/{id}` | 销售 |
| 47 | GET | `/api/sales/orders` | 销售 |
| 48 | POST | `/api/sales/orders` | 销售 |
| 49 | GET | `/api/sales/orders/{id}` | 销售 |
| 50 | POST | `/api/sales/orders/{id}/complete` | 销售 |
| 51 | GET | `/api/dashboard/summary` | Dashboard |
| 52 | GET | `/api/dashboard/trend` | Dashboard |
| 53 | GET | `/api/dashboard/warnings` | Dashboard |
| 54 | GET | `/api/dashboard/top-products` | Dashboard |
| 55 | POST | `/api/files/upload` | 企业 |
| 56 | GET | `/api/excel/products/export` | 企业 |
| 57 | POST | `/api/excel/products/import` | 企业 |
| 58 | POST | `/api/ai/reports` | AI |
| 59 | GET | `/api/ai/reports` | AI |
| 60 | GET | `/api/ai/reports/{id}` | AI |
| 61 | GET | `/api/expenses` | 费用 |
| 62 | GET | `/api/expenses/{id}` | 费用 |
| 63 | POST | `/api/expenses` | 费用 |
| 64 | PUT | `/api/expenses/{id}` | 费用 |
| 65 | DELETE | `/api/expenses/{id}` | 费用 |
| 66 | POST | `/api/expenses/{id}/approve` | 费用 |
| 67 | POST | `/api/expenses/{id}/reject` | 费用 |
| 68 | POST | `/api/expenses/{id}/pay` | 费用 |
| 69 | GET | `/api/company-account` | 资金 |
| 70 | PUT | `/api/company-account` | 资金 |
| 71 | GET | `/api/company-account/today-income` | 资金 |
| 72 | GET | `/api/company-account/today-expense` | 资金 |
| 73 | GET | `/api/company-account/trend` | 资金 |
| 74 | GET | `/api/company-account/transactions` | 资金 |
| 75 | GET | `/api/dashboard/expense-summary` | Dashboard |

共 **75 个端点**，覆盖 13 个模块。
