# Smart ERP Platform

基于 Spring Boot 3 + Spring Security + MyBatis-Plus 的企业资源管理系统。

## 技术栈

| 层级 | 技术 |
|------|------|
| 语言 | Java 21 |
| 框架 | Spring Boot 3.5.12 |
| 安全 | Spring Security 6 + JWT + RBAC |
| ORM | MyBatis-Plus 3.5.12 |
| 数据库 | MySQL 8.0 (utf8mb4) |
| 迁移 | Flyway |
| 构建 | Maven + Maven Wrapper 3.9.16 |

## 快速启动

### 前置条件

- JDK 21+
- MySQL 8.0 已运行
- 数据库 `erp_system`，用户 `erp` / 密码 `123456`

### 启动

```powershell
cd backend
.\mvnw spring-boot:run
```

服务器启动在 `http://localhost:8080`。

### 默认账户

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | 123456 | ADMIN（全部权限） |

## 项目结构

```
backend/
├── common/         公共组件（异常、响应、安全、转换器）
├── config/         Spring 配置类
├── controller/     REST 控制器
├── dto/            请求数据传输对象
├── entity/         数据库实体
├── mapper/         MyBatis-Plus Mapper + RBACMapper
├── service/        业务逻辑接口 + 实现
├── vo/             响应视图对象
└── resources/
    ├── db/migration/  Flyway 迁移脚本
    └── application.yaml
```

## 已实现模块

| 版本 | 模块 | 状态 |
|------|------|------|
| v0.1 | 项目初始化、统一响应、全局异常 | ✅ |
| v0.2 | Spring Security + JWT 登录认证 | ✅ |
| v0.3 | 用户管理 CRUD | ✅ |
| v0.4 | RBAC 权限控制（角色/权限/分配/动态鉴权） | ✅ |

## API 概览

所有接口路径以 `/api` 开头，认证方式为 `Authorization: Bearer <JWT Token>`。

| 分组 | 路径 | 说明 |
|------|------|------|
| 认证 | `/api/auth/*` | 登录、当前用户 |
| 用户 | `/api/users/*` | 用户 CRUD、角色分配 |
| 角色 | `/api/roles/*` | 角色 CRUD、权限分配 |
| 权限 | `/api/permissions/*` | 权限 CRUD |

完整接口文档见 [`docs/API.md`](docs/API.md)。

## 开发路线

```
v0.5 → 商品管理（分类 + 商品 CRUD）
v0.6 → 库存管理（ERP 核心，流水追溯）
v0.7 → 采购管理（采购单 + 入库 + @Transactional）
v0.8 → 销售管理（销售单 + 出库 + 库存扣减）
v0.9 → Dashboard（ECharts + 聚合统计）
v1.0 → 企业增强（Redis、Docker、操作日志、文件上传、Excel）
v1.1 → AI 扩展（经营分析，只读数据库）
```

## 核心设计原则

- **库存永远来源于业务**，不允许直接修改库存数量，所有变更通过 `inventory_record` 流水追溯
- 前后端分离 + 单模块 Maven 单体架构
- DTO / Entity / VO 分层，禁止跨层调用
- 动态 RBAC：登录时加载权限，JWT 携带权限列表，方法级 `@PreAuthorize` 控制
