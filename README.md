# Smart ERP Platform

基于 Spring Boot 3 + Spring Security + MyBatis-Plus + Vue 3 的企业资源管理系统。

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端 | Java 21, Spring Boot 3.5.12, Spring Security 6 |
| ORM | MyBatis-Plus 3.5.12, Flyway, MySQL 8.0 |
| 前端 | Vue 3, TypeScript, Vite, Element Plus, Pinia, ECharts |
| AI | DeepSeek API（经营分析） |
| 部署 | Docker + Docker Compose |
| 构建 | Maven + Maven Wrapper 3.9.16 |

## 快速启动

### 前置条件

- JDK 21+
- MySQL 8.0 已运行（数据库 `erp_system`，用户 `erp` / 密码 `123456`）

### 启动后端

```powershell
cd backend
.\mvnw spring-boot:run
```

服务器启动在 `http://localhost:8080`，Flyway 自动建表+种子数据。

### 启动前端

```powershell
cd frontend
npm install
npm run dev
```

访问 `http://localhost:3000`，Vite 自动代理 `/api` 到后端。

### 默认账户

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | 123456 | ADMIN（全部权限） |

## 项目结构

```
backend/
├── ai/              AI 经营分析模块
├── common/          公共组件（异常、响应、安全、转换器）
├── config/          Spring 配置类
├── controller/      REST 控制器
├── dto/             请求数据传输对象
├── entity/          数据库实体
├── mapper/          MyBatis-Plus Mapper + RBACMapper
├── service/         业务逻辑接口 + 实现
├── vo/              响应视图对象
└── resources/
    ├── db/migration/  Flyway 迁移脚本
    └── prompt/        AI Prompt 模板

frontend/
├── src/api/          API 客户端
├── src/views/        页面组件
├── src/router/       路由配置
├── src/stores/       Pinia 状态管理
└── src/layout/       布局组件
```

## 已实现模块

| 版本 | 模块 | 状态 |
|------|------|------|
| v0.1 | 项目初始化、统一响应、全局异常 | ✅ |
| v0.2 | Spring Security + JWT 登录认证 | ✅ |
| v0.3 | 用户管理 CRUD | ✅ |
| v0.4 | RBAC 权限控制（角色/权限/分配/动态鉴权） | ✅ |
| v0.5 | 商品管理（分类 + 商品 CRUD） | ✅ |
| v0.6 | 库存管理（流水追溯） | ✅ |
| v0.7 | 采购管理（采购单 + 入库 + 库存联动） | ✅ |
| v0.8 | 销售管理（销售单 + 出库 + 库存扣减） | ✅ |
| v0.9 | Dashboard（ECharts + 聚合统计） | ✅ |
| v1.0 | 企业增强（Docker、AOP日志、文件上传、Excel） | ✅ |
| v1.1 | AI 经营分析（DeepSeek API） | ✅ |
| v1.2 | 费用管理 + 企业资金 + 商品配图 | ✅ |

## 核心设计原则

- **库存永远来源于业务** —— 所有变更通过 `inventory_record` 流水追溯
- **资金统一通过 `CompanyAccountService`** —— 采购扣款、销售收款、费用支付统一入口
- 前后端分离 + 单模块 Maven 单体架构
- DTO / Entity / VO 分层，禁止跨层调用
- 动态 RBAC：JWT 携带权限列表，方法级 `@PreAuthorize` 控制
