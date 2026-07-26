# REST API 接口文档

基础路径：`/api`

认证方式：`Authorization: Bearer <JWT Token>`

响应格式：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

| HTTP 状态码 | 业务码 | 含义 |
|-------------|--------|------|
| 200 | 200 | 成功 |
| 200 | 40000 | 参数/业务错误 |
| 401 | 40100 | 未登录 |
| 403 | 40300 | 无权限 |
| 404 | 40400 | 资源不存在 |
| 500 | 50000 | 系统异常 |

---

## 1. 认证模块

### POST /api/auth/login

公开接口，无需 Token。

请求：

```json
{
  "username": "admin",
  "password": "123456"
}
```

响应：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "user": {
      "id": 1,
      "username": "admin",
      "nickname": "管理员",
      "status": 1
    },
    "roles": ["ADMIN"],
    "permissions": ["user:list", "user:create", "role:list", "..."]
  }
}
```

### GET /api/auth/me

获取当前登录用户信息（含角色和权限）。

响应：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "admin",
    "nickname": "管理员",
    "status": 1
  }
}
```

---

## 2. 用户管理

### POST /api/users/list

分页查询用户列表。

请求：

```json
{
  "page": 1,
  "size": 10,
  "username": "",
  "status": null
}
```

响应：

```json
{
  "code": 200,
  "data": {
    "records": [{ "id": 1, "username": "admin", "nickname": "管理员", "status": 1 }],
    "total": 1,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

### GET /api/users/detail/{id}

获取用户详情。

### POST /api/users

新增用户。

```json
{
  "username": "zhangsan",
  "password": "123456",
  "nickname": "张三",
  "status": 1
}
```

约束：用户名唯一，密码自动 BCrypt 加密。

### PUT /api/users/{id}

修改用户（用户名不可改）。

```json
{
  "username": "zhangsan",
  "nickname": "张三三",
  "status": 1
}
```

### DELETE /api/users/{id}

逻辑删除用户。

### PUT /api/users/{id}/reset-password

重置密码。`newPassword` 为空则默认 `123456`。

```json
{
  "newPassword": "newpass123"
}
```

### PUT /api/users/{id}/roles

分配用户角色（覆盖式，先清后插）。

```json
{
  "roleIds": [1, 2]
}
```

`roleIds` 为空数组则清空所有角色。重复 ID 自动去重。

### GET /api/users/{id}/roles

获取用户拥有的角色列表。

---

## 3. 角色管理

### POST /api/roles

新增角色。`roleCode` 唯一。

```json
{
  "roleName": "仓库管理员",
  "roleCode": "WAREHOUSE",
  "description": "管理库存相关业务"
}
```

### GET /api/roles

分页查询角色。支持 `keyword` 模糊搜索角色名/编码。

需要权限：`role:list`

```
GET /api/roles?keyword=仓库&page=1&size=10
```

### GET /api/roles/{id}

获取角色详情。

### PUT /api/roles/{id}

修改角色名称和描述。`roleCode` 不可修改。

```json
{
  "roleName": "仓库管理员",
  "description": "管理库存及采购"
}
```

### DELETE /api/roles/{id}

逻辑删除角色。

### PUT /api/roles/{id}/permissions

分配角色权限（覆盖式，先清后插）。

```json
{
  "permissionIds": [1, 2, 3, 4, 5]
}
```

### GET /api/roles/{id}/permissions

获取角色拥有的权限列表。

---

## 4. 权限管理

### POST /api/permissions

新增权限。`code` 唯一。

```json
{
  "name": "用户列表",
  "code": "user:list",
  "type": "button",
  "parentId": null
}
```

### GET /api/permissions

获取全部权限列表（非树形）。

### PUT /api/permissions/{id}

修改权限（`code` 不可修改）。

```json
{
  "name": "用户列表已改",
  "type": "button",
  "parentId": null
}
```

### DELETE /api/permissions/{id}

逻辑删除权限。

---

## 5. 权限码一览

当前预设 15 条基础权限码：

| 模块 | 权限码 |
|------|--------|
| 用户 | `user:list`、`user:view`、`user:create`、`user:update`、`user:delete` |
| 角色 | `role:list`、`role:view`、`role:create`、`role:update`、`role:delete` |
| 权限 | `perm:list`、`perm:view`、`perm:create`、`perm:update`、`perm:delete` |

命名规范：`module:action`

后续模块按此扩展。
