# AGENTS.md

## 项目结构

```
backend/          Spring Boot 3.5.12 + Java 21 + Maven（当前唯一运行中的子系统）
frontend/         空目录（.gitkeep），规划 Vue 3 + TypeScript + Vite + Element Plus
docs/             设计文档，开发文档为权威基线
```

## 当前版本

v0.4（RBAC）已完成。已完成：
- v0.1：项目初始化、统一返回、全局异常
- v0.2：Spring Security + JWT 登录认证
- v0.3：用户管理（CRUD + 重置密码）
- v0.4：RBAC（角色/权限/分配/Spring Security 动态鉴权）

### 版本路线

```
v0.4 → RBAC ✅
v0.5 → 商品管理（分类 + 商品 CRUD）✅
v0.6 → 库存管理（ERP 核心，流水追溯）
v0.7 → 采购管理（采购单 + 入库 + @Transactional）
v0.8 → 销售管理（销售单 + 出库 + 库存扣减 + 事务）
v0.9 → Dashboard（ECharts + 聚合统计）
v1.0 → 企业增强（Redis、Docker、操作日志 AOP、文件上传、Excel 导入导出）
v1.1 → AI 扩展（经营分析，只读数据库，不操作业务）
```

## 开发命令

所有命令在 `backend/` 目录下执行：

```powershell
# 编译 + 运行测试
.\mvnw verify

# 仅编译
.\mvnw compile

# 运行单个测试类
.\mvnw test -pl . "-Dtest=com.kaede.erp.ErpSystemApplicationTests"

# 启动开发服务器（端口 8080）
.\mvnw spring-boot:run

# 打包
.\mvnw package -DskipTests
```

## 运行前提

- **MySQL 必须启动**，数据库 `erp_system`，用户 `erp` / 密码 `123456`
- `src/main/resources/application.yaml` 中配置了数据源
- 测试也是集成测试，依赖 MySQL（无 H2 / Testcontainers）

## 认证与安全

- **只有 `/api/auth/login` 是公开接口**，其余全部需要 JWT
- JWT 格式：`Authorization: Bearer <token>`，24 小时过期
- JWT 密钥硬编码在 `common/security/JwtTokenProvider.java:17-20`（`kaede-erp-system-secret-key-2026-kaede`），不在配置文件中
- JWT 令牌中嵌入了用户权限列表，Filter 解析 Token 直接设置 `SecurityContext`，**不查数据库**
- 权限通过 `@EnableMethodSecurity` + `@PreAuthorize("hasAuthority('permission:code')")` 控制
- `UserContext`（ThreadLocal）在 JWT Filter 的 `finally` 块中清理 —— **不要删除这个清除**
- 登录响应包含 `roles` 和 `permissions` 字段，供前端进行按钮级权限控制

## 响应格式

所有接口返回 `Result<T>` (`common/result/Result.java`)：

```json
{ "code": 200, "message": "success", "data": {} }
```

自定义状态码定义在 `common/constant/ResultCode.java`：
- `200` 成功
- `40000` 参数错误
- `40100` 未登录
- `40300` 无权限
- `40400`/`40401` 未找到
- `50000` 系统异常

业务异常通过 `BusinessException(ResultCode)` 抛出，由 `GlobalExceptionHandler` 统一处理。

## 代码约定

- **依赖注入**：使用 `@RequiredArgsConstructor` + `final` 字段（不用 `@Autowired` 字段注入）
- **Controller** 中部分使用显式构造函数注入而非 `@RequiredArgsConstructor`，保持一致即可
- **代码风格**：大量空行分隔语句，新增代码沿用此风格，不要压缩空行
- **调试日志**：代码中散布 `System.out.println` 用于开发期调试，不要主动清理
- **实体名**：`SysUser`（对应 `sys_user` 表），MyBatis-Plus 逻辑删除字段 `deleted`（0=正常 / 1=删除）
- **DTO 验证**：使用 `jakarta.validation`，在 Controller 参数上加 `@Valid`

### 数据流（强制）

所有接口必须遵循此链路，不允许跳过任何层：

```
Controller 接收 DTO
    ↓
Service 处理 DTO → 操作 Entity
    ↓
Converter 将 Entity → VO
    ↓
Controller 返回 Result<VO>
```

**禁止**：
- Controller 直接操作 Entity 或 Mapper
- Entity 直接返回给前端
- VO 直接写入数据库



## 数据库

- MySQL 8.0，字符集 utf8mb4
- MyBatis-Plus 分页插件已配置（`config/MybatisPlusConfig.java`）
- XML Mapper 路径：`classpath:mapper/*.xml`
- 公共表字段：`id` / `create_time` / `update_time` / `create_by` / `update_by` / `deleted`

## 测试

- 仅有一个 `contextLoads()` 测试（`ErpSystemApplicationTests`）
- 是 `@SpringBootTest` 集成测试，依赖 MySQL 运行
- `target/` 已 gitignored

## 前端

- 尚未初始化，目录仅有 `.gitkeep`
- 设计文档中规划技术栈：Vue 3 + TS + Vite + Element Plus + Pinia + ECharts
- API 基础路径：`/api/`

## 核心设计原则

- **库存永远来源于业务，不允许直接修改库存数量** —— 所有变更通过 `inventory_record` 流水追溯
- 前后端分离 + 单模块 Maven 单体架构（不拆微服务）

## Git 规范

Commit 采用 Conventional Commits：

```
feat(user): 新增用户管理
refactor(user): 提取 UserConverter
fix(auth): 修复JWT解析异常
docs(api): 更新接口文档
```

每个版本完成后打 Tag：

```
v0.1-init   v0.2-auth   v0.3-user    v0.4-rbac
v0.5-product v0.6-inventory v0.7-purchase v0.8-sales
v0.9-dashboard v1.0-enterprise v1.1-ai
```

## IntelliJ HTTP 测试

- `backend/auth.http` — 登录 + `/api/auth/me` 测试用例
- `backend/test.http` — 用户相关端点测试用例
- 可使用 IntelliJ HTTP Client 直接调试，不需要 Postman

## 设计文档

实现新功能前先查阅 `docs/` 中的对应文档（接口设计、数据库设计、架构设计），这些是权威参考。
`智能ERP管理系统开发文档.md` 是项目基线，包含版本路线和技术选型，与当前代码冲突时以开发文档为准。

## 调试原则

遇到接口异常时，优先用 SQL 验证数据库实际状态，再继续推理。能用一条 SQL 验证的假设，不要连续进行多轮理论分析。
