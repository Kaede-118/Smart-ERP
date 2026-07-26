# Changelog

## v1.2 (2026-07-27)

**Expense Management & Company Account**

- 费用管理模块：新增/编辑/审批/驳回/付款 + @OperationLog
- 企业账户升级：`CompanyAccountService` 统一资金入口（increase/decrease）
- 资金流水 `account_transaction`：采购/销售/费用自动记录资金变动
- Dashboard 新增资金摘要（今日收入/支出/现金流 + 余额趋势）
- AI 经营分析扩展：费用数据 + 现金流上下文
- 商品管理 UI 完善：编辑/详情/删除确认/库存展示/商品图片
- 前端资金管理页面：KPI 卡 + 收支趋势图 + 余额折线
- Header 动态问候语 + 本地时间显示
- 采购/销售列表新增「内容」列（GROUP_CONCAT 商品名）

**AI Business Analysis**

- 经营分析报告生成（销售分析 / 库存分析 / 经营概览）
- 四层 AI 架构：AiService → ContextService + PromptService → AIClient
- Prompt 模板外置到 `prompt/*.md`，无需重新编译即可修改
- `AIClient` 接口封装 DeepSeek API，支持切换模型供应商
- 报告持久化到 `ai_report` 表，支持历史回溯
- AI 只读业务数据，不操作业务表

## v1.0 (2026-07-27)

**Enterprise Enhancement**

- Docker 容器化部署（Dockerfile + docker-compose）
- 操作日志 AOP（@OperationLog 注解 + @Aspect 切面）
- 文件上传（POST /api/files/upload）
- Excel 导入导出（EasyExcel 实现商品导出/导入）
- `InventoryService` 统一库存变更入口，采购/销售不再直接操作 Mapper
- 新增 `ResultCode.INSUFFICIENT_STOCK` / `INVALID_STATUS` / `DUPLICATE_CODE`
- 全局异常新增 `MethodArgumentNotValidException` 处理

## v0.9 (2026-07-27)

**Dashboard Statistics**

- 经营总览摘要（COUNT / SUM 聚合 SQL）
- 近 7 天采购/销售趋势合并
- 低库存预警列表
- 热销商品 TOP10
- `DashboardMapper` 纯 SQL 聚合，无 Java 计算
- 用户登录时联表查询角色+权限，写入 SecurityContext
- 启用 `@EnableMethodSecurity`，支持 `@PreAuthorize`
- 全局异常处理器新增 `AccessDeniedException` → HTTP 403
- `RoleController.list()` 第一个接入 `@PreAuthorize("hasAuthority('role:list')")`
- 登录响应 `LoginVO` 新增 `roles` 和 `permissions` 字段
- `/api/auth/me` 返回用户角色和权限列表

## v0.4.3 (2026-07-26)

**Role and Permission Assignment**

- 新增 `PUT /api/users/{id}/roles` — 分配角色（去重 + ID校验 + @Transactional）
- 新增 `GET /api/users/{id}/roles` — 查看用户角色
- 新增 `PUT /api/roles/{id}/permissions` — 分配权限
- 新增 `GET /api/roles/{id}/permissions` — 查看角色权限
- Mapper 批量插入，避免 N+1
- Service 层 `@Transactional` 保证先删后插原子性

## v0.4.2 (2026-07-26)

**Role and Permission CRUD**

- 角色 CRUD: `POST/GET/PUT/DELETE /api/roles`
- 权限 CRUD: `POST/GET/PUT/DELETE /api/permissions`
- 引入 MyBatis-Plus MetaObjectHandler 自动填充 `create_time`/`update_time`/`deleted`
- `RBACMapper` 独立承载跨表 Join 查询（`selectRoleCodesByUserId`, `selectPermissionCodesByUserId`）

## v0.4.1 (2026-07-26)

**RBAC Database Schema**

- 引入 Flyway 数据库版本管理
- 创建 `sys_role`、`sys_permission`、`sys_user_role`、`sys_role_permission` 四张表
- 种子数据：3 个角色（ADMIN/WAREHOUSE/SALES）、15 条基础权限码
- admin 用户默认分配 ADMIN 角色和全部权限
- 联合唯一索引防止重复分配

## v0.3 (2026-07-26)

**User Management**

- 用户 CRUD：`POST/GET/PUT/DELETE /api/users`
- 重置密码：`PUT /api/users/{id}/reset-password`，默认 `123456`
- 用户名重复校验、BCrypt 加密、逻辑删除
- `UserConverter` 实体→VO 转换

## v0.2 (2026-07-26)

**Authentication & JWT**

- Spring Security + JWT 无状态认证
- `POST /api/auth/login` 登录接口
- `GET /api/auth/me` 当前用户信息
- `JwtAuthenticationFilter` 每次请求校验 Token
- `UserContext`（ThreadLocal）存储当前用户 ID
- `Result<T>` 统一响应格式
- `BusinessException` + `GlobalExceptionHandler` 统一异常处理
- 自定义 5 位错误码体系

## v0.1 (2026-07-26)

**Project Init**

- Spring Boot 3.5.12 + Java 21 + Maven 项目初始化
- MyBatis-Plus + MySQL 8.0 集成
- 分页插件配置
- 基础项目结构和包命名
