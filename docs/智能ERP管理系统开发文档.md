我认真结合了我们最初的三份设计文档，以及这段时间项目已经完成的认证、DTO/VO、Spring Security、JWT 等内容，重新整理了一版。

我的目标不是"画大饼"，而是做到：

> **现在能开发 → 实习能写简历 → 毕设还能继续扩展。**

---

# 智能企业资源管理平台（Smart ERP Platform）架构设计 v2.0

## 一、项目定位

### 项目名称

**Smart ERP Platform（智能企业资源管理平台）**

### 项目简介

基于 **Spring Boot 3 + Spring Security + Vue3** 开发的企业资源管理平台，实现企业日常业务流程的信息化管理，并预留 AI 智能分析能力。

项目定位不是课程 CRUD，而是围绕企业真实业务流程设计。

核心业务：

```
权限管理

↓

商品管理

↓

库存管理

↓

采购管理

↓

销售管理

↓

数据统计

↓

AI经营分析（扩展）
```

整体定位继承了最初文档中的业务模块划分和 AI 扩展思路。

---

# 二、总体架构

```
                Browser

                    │

              Vue3 + Element Plus

                    │

              Axios + JWT

                    │

──────────────────────────────────

           Spring Boot 3

        RESTful API Server

                    │

        Spring Security

                    │

        Service Business

                    │

         MyBatis-Plus

                    │

              MySQL 8

                    │

               Redis（V2）

                    │

           AI Analysis（V3）
```

---

# 三、技术栈

## 后端

```
Java 21

Spring Boot 3

Spring Security 6

JWT

MyBatis-Plus

MySQL 8

Redis（第二阶段）

Validation

Lombok

Knife4j(OpenAPI)

Maven
```

---

## 前端

```
Vue3

TypeScript

Pinia

Vue Router

Axios

Element Plus

ECharts
```

---

## 部署

```
Linux

Docker

Docker Compose

Nginx
```

---

# 四、系统架构

采用经典三层架构。

```
Controller

↓

DTO

↓

Service

↓

Entity

↓

Mapper

↓

MySQL

↓

Entity

↓

Converter

↓

VO

↓

Result<T>

↓

JSON
```

所有接口统一遵循这一流程。

---

# 五、项目目录

```
backend
│
├── common
│   ├── annotation
│   ├── aspect
│   ├── constant
│   ├── context
│   ├── converter
│   ├── enums
│   ├── exception
│   ├── filter
│   ├── result
│   ├── security
│   └── utils
│
├── config
│
├── controller
│
├── dto
│
├── entity
│
├── mapper
│
├── service
│   └── impl
│
├── vo
│
└── SmartERPApplication
```

说明：

暂时保持**单模块 Maven**。

毕业设计再考虑拆分微服务。

---

# 六、数据库设计

采用 MySQL 8。

数据库：

```
erp_system
```

---

## 权限模块

```
sys_user

sys_role

sys_permission

sys_user_role

sys_role_permission
```

采用标准 RBAC。

---

## 商品模块

```
product_category

product
```

---

## 库存模块（核心）

```
inventory

inventory_record
```

整个 ERP 的核心思想：

> **库存永远来源于业务，不允许直接修改库存数量。**

所有库存变化：

```
采购入库

销售出库

库存调整

盘点
```

全部生成：

```
inventory_record
```

方便追溯。

这一点保持最初数据库设计的核心思想。

---

## 采购模块

```
supplier

purchase_order

purchase_item
```

---

## 销售模块

```
customer

sales_order

sales_item
```

---

## 日志模块（第二阶段）

```
operation_log
```

---

## AI模块（第三阶段）

```
ai_analysis_task

ai_report
```

---

# 七、REST API 设计

采用 RESTful 风格。

登录：

```
POST

/api/auth/login
```

当前用户：

```
GET

/api/auth/me
```

用户：

```
GET

/api/users

GET

/api/users/{id}

POST

/api/users

PUT

/api/users/{id}

DELETE

/api/users/{id}
```

商品：

```
GET /api/products

POST /api/products

PUT /api/products/{id}

DELETE /api/products/{id}
```

库存：

```
GET /api/inventory

POST /api/inventory/adjust

GET /api/inventory/records
```

采购：

```
POST /api/purchase/orders

POST /api/purchase/orders/{id}/complete
```

销售：

```
POST /api/sales/orders

POST /api/sales/orders/{id}/complete
```

Dashboard：

```
GET /api/dashboard/stat

GET /api/dashboard/trend
```

接口设计保持了最初业务接口的思想，但统一调整为更规范的 REST 风格。

---

# 八、开发路线

## v0.1

项目初始化

```
Spring Boot

MyBatis-Plus

MySQL

统一返回

全局异常
```

---

## v0.2（已完成）

登录认证

```
Spring Security

JWT

登录

当前用户

认证过滤器

Result<T>

DTO

VO
```

---

## v0.3（当前）

用户管理

```
分页

条件查询

新增

修改

删除

重置密码

UserConverter
```

---

## v0.4

RBAC

```
角色

权限

菜单

动态权限

按钮权限
```

---

## v0.5

商品管理

```
商品

分类

图片

状态
```

---

## v0.6（ERP核心）

库存管理

```
库存

库存流水

库存调整

库存预警
```

---

## v0.7

采购管理

```
供应商

采购订单

采购入库

@Transactional
```

---

## v0.8

销售管理

```
客户

销售订单

销售出库

库存扣减
```

---

## v0.9

Dashboard

```
ECharts

销售统计

库存统计
```

---

## v1.0

企业增强

```
Redis

Docker

操作日志

文件上传

Excel导入导出
```

---

## v1.1

AI扩展

```
经营分析

库存建议

销售预测

LLM接口

自然语言查询
```

AI 始终定位为增强能力，不参与 ERP 核心业务流程，延续最初设计中"AI 独立模块"的思路。

---

# 九、项目亮点（简历）

完成 v1.0 后，可以作为个人核心项目。

可提炼为：

> **Smart ERP Platform（智能企业资源管理平台）**

技术栈：

Spring Boot 3、Spring Security、JWT、MyBatis-Plus、MySQL、Redis、Vue3、Element Plus、Docker

项目亮点：

- 基于 Spring Security + JWT 实现无状态身份认证与 RBAC 权限控制。

- 采用 DTO / VO / Entity 分层设计，实现 Controller、Service、Mapper 解耦。

- 基于 MyBatis-Plus 完成用户、商品、库存等模块 CRUD 与分页查询。

- 设计库存流水机制，实现库存变更全过程可追溯，保证业务数据一致性。

- 使用事务管理采购入库、销售出库等核心业务流程，确保订单与库存同步更新。

- 使用 Redis 对热点数据进行缓存，并通过 Docker Compose 完成项目部署。

---

# 十、毕业设计扩展方向

在保持 ERP 主体不变的前提下，可以继续扩展：

```
ERP

↓

AI经营分析

↓

库存预测

↓

采购建议

↓

销售趋势分析

↓

Agent 自动生成经营报告
```

这样可以做到：

- **现在**：作为实习/校招简历项目，有完整的企业级后端架构和业务流程。

- **以后**：作为毕业设计基础，不需要推倒重来，只需增加 AI 能力和数据分析模块即可。

---

## 我还有一个建议（也是我最希望我们一起做的）

这份文档可以作为 **v2.0 基线**，以后开发时不再边聊边决定方向，而是像真正的软件项目一样维护：

- `ARCHITECTURE.md`（系统架构）

- `DATABASE.md`（数据库设计）

- `API.md`（接口文档）

- `ROADMAP.md`（开发路线）

- `CHANGELOG.md`（版本变更）

- `README.md`（项目介绍）

以后每完成一个版本（例如 v0.3 用户管理、v0.4 RBAC），同步更新文档和 Git Tag。这样到找实习或做毕业设计时，这套文档本身就是项目成熟度的一部分。
