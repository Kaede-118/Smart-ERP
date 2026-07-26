# 系统架构设计

## 整体架构

```
Browser / HTTP Client
        │
        ▼
    Nginx (部署用)
        │
        ▼
┌─────────────────────────────┐
│     Spring Boot 3.5.12      │
│                             │
│  ┌───────┐  ┌───────────┐  │
│  │Security│  │ Controller│  │
│  │ JWT +  │  │ REST API  │  │
│  │  RBAC  │  └─────┬─────┘  │
│  └───┬───┘        │        │
│      │            ▼        │
│      │      ┌───────────┐  │
│      │      │  Service  │  │
│      │      └─────┬─────┘  │
│      │            │        │
│      │            ▼        │
│      │      ┌───────────┐  │
│      │      │  Mapper   │  │
│      │      └─────┬─────┘  │
│      │            │        │
└──────┼────────────┼────────┘
       │            ▼
       │      ┌──────────┐
       │      │  MySQL 8 │
       │      └──────────┘
       ▼
  Flyway (DB迁移)
```

## 模块依赖关系

```
auth ───→ JWT / Spring Security
  │
  ├──→ user ───→ CRUD + 重置密码
  │
  ├──→ rbac ───→ role + permission + user-role assignment
  │                │
  │                ▼
  │         @EnableMethodSecurity
  │         @PreAuthorize("hasAuthority('perm:code')")
  │
  ├──→ product ───→ category + product CRUD
  │
  ├──→ inventory ───→ inventory (当前库存)
  │      │               │
  │      │               └──→ inventory_record (流水追溯)
  │      │
  │      ├──→ purchase ───→ supplier + order + item + inventory.increase()
  │      │                     │
  │      │                     └─── @Transactional + inventory_record
  │      │
  │      └──→ sales ───→ customer + order + item + inventory.decrease()
  │                        │
  │                        └─── @Transactional + inventory_record
  │
  ├──→ dashboard ───→ COUNT / SUM / GROUP BY 聚合查询
  │
  ├──→ enterprise ───→ file upload + Excel import/export + operation log AOP
  │
  └──→ ai ───→ ContextService → 现有业务 Service DTO
                  │
                  ▼
              PromptService → prompt/*.md 模板
                  │
                  ▼
              DeepSeekClient → DeepSeek API
                  │
                  ▼
              ai_report (只读，不操作业务表)
```

## AI 模块详细流程

```
POST /api/ai/reports { type, range }
        │
        ▼
  AiService.generate()
        │
        ├──→ ContextService
        │       ├── SALES     → DashboardService + JdbcTemplate 聚合
        │       ├── INVENTORY → DashboardService + InventoryService
        │       └── OVERVIEW  → DashboardService（综合数据）
        │
        ├──→ PromptService
        │       └── 读取 resources/prompt/{type}.md → 替换 {{placeholder}}
        │
        ├──→ DeepSeekClient.chat(prompt)
        │       └── HTTP POST → api.deepseek.com/chat/completions
        │
        ├──→ 解析 LLM 返回的 JSON → 存入 ai_report
        │
        └──→ 返回 AiReportResponse { id, title, summary, content }
```

### 设计原则

```
AI 层只负责"理解和总结"，不负责"计算和查询"。
所有业务数据都应由现有 Service 提供，AI 只消费 DTO 与 Prompt，不直接访问 Mapper 或数据库。
所有大模型调用统一封装在 AIClient 中，便于未来切换模型供应商。
```

## 数据层设计

### 库存流水核心链路

```
采购入库 → Inventory.increase() → inventory_record (PURCHASE)
                                     │
                                     ▼
                                  inventory.quantity += change
                                     ▲
销售出库 → Inventory.decrease() → inventory_record (SALES)
                                     │
                                     ▼
                                  inventory.quantity -= change
                                     ▲
手动调整 → Inventory.adjust()   → inventory_record (ADJUST)
```

### 表关系

```
sys_user ──→ sys_user_role ──→ sys_role ──→ sys_role_permission ──→ sys_permission
product_category ──→ product
inventory ──→ product
inventory_record ──→ product
purchase_order ──→ supplier
purchase_item ──→ purchase_order ──→ product
sales_order ──→ customer
sales_item ──→ sales_order ──→ product
ai_report（独立，不与业务表关联）
```

## 部署架构

```
Docker Compose:
  mysql:8.0 ──── 数据持久化 volume
      │
      ▼
  app:jar ──── Spring Boot 应用
                │
                ├── Flyway 自动迁移
                ├── JWT 无状态认证
                └── 端口 8080
```

## 技术栈汇总

| 层级 | 技术 | 用途 |
|------|------|------|
| 语言 | Java 21 | 运行时 |
| 框架 | Spring Boot 3.5.12 | Web 容器 |
| 安全 | Spring Security 6 + JWT + RBAC | 认证授权 |
| ORM | MyBatis-Plus 3.5.12 | 数据库操作 |
| 数据库 | MySQL 8.0 | 数据存储 |
| 迁移 | Flyway | 数据库版本管理 |
| AI | DeepSeek API | 经营分析生成 |
| 部署 | Docker + Docker Compose | 容器化 |
| 构建 | Maven + Maven Wrapper | 项目构建 |
