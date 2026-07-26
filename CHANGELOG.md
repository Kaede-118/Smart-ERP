# Changelog

## v0.4.4 (2026-07-27)

**Dynamic Authorization with Spring Security**

- JWT 令牌嵌入权限列表，每个请求从 Token 直接提取权限
- `SecurityUser` 从 `permissionCodes` 动态构造 `GrantedAuthority`
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
