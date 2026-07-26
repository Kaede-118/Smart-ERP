---
```
# 智能ERP管理系统 REST API接口设计版本：v0.1基础地址：/api数据格式：JSON认证方式：JWT Token请求头：Authorization: Bearer {token}
```

---

# 1. 统一响应结构

所有接口：

```
{    "code": 200,    "message": "success",    "data": {}}
```

---

## 状态码设计

| code | 含义    |
| ---- | ----- |
| 200  | 成功    |
| 400  | 参数错误  |
| 401  | 未登录   |
| 403  | 无权限   |
| 500  | 服务器异常 |

---

# 2. 登录认证模块

## 2.1 用户登录

### POST

```
/api/auth/login
```

请求：

```
{    "username":"admin",    "password":"123456"
}
```

返回：

```
{    "token":"xxxxx",    "userInfo":{        "id":1,        "username":"admin",        "roles":[            "ADMIN"
        ]    }}
```

流程：

```
用户名密码↓Spring Security验证↓生成JWT↓返回Token
```

---

# 3. 用户权限模块

## 3.1 查询用户列表

GET

```
/api/system/user/list
```

参数：

```
pagesizekeyword
```

返回：

```
{"records":[{"id":1,"username":"admin","nickname":"管理员"
}]}
```

---

## 3.2 新增用户

POST

```
/api/system/user
```

请求：

```
{"username":"zhangsan","password":"123456","roleIds":[2]}
```

---

## 3.3 修改角色权限

PUT

```
/api/system/user/{id}/roles
```

请求：

```
{"roleIds":[
2,
3
]}
```

---

# 4. 商品管理模块

## 4.1 商品列表

GET

```
/api/product/list
```

参数：

```
categoryIdkeywordpagesize
```

返回：

```
{"id":1,"name":"机械键盘","price":299,"stock":100
}
```

---

## 4.2 新增商品

POST

```
/api/product
```

请求：

```
{"name":"机械键盘","categoryId":1,"costPrice":200,"salePrice":299
}
```

---

## 4.3 修改商品

PUT

```
/api/product/{id}
```

---

## 4.4 删除商品

DELETE

```
/api/product/{id}
```

---

# 5. 库存管理模块 ⭐

ERP核心。

## 5.1 查询库存

GET

```
/api/inventory/list
```

返回：

```
[{"productName":"键盘","quantity":100,"warning":20
}]
```

---

## 5.2 库存调整

POST

```
/api/inventory/adjust
```

请求：

```
{"productId":1,"quantity":-10,"reason":"盘点调整"
}
```

业务：

```
修改库存↓生成inventory_record
```

---

## 5.3 查询库存流水

GET

```
/api/inventory/record/list
```

返回：

```
[{"type":"IN","quantity":50,"before":100,"after":150
}]
```

---

# 6. 采购模块

## 6.1 创建采购订单

POST

```
/api/purchase/order
```

请求：

```
{"supplierId":1,"items":[{"productId":1,"quantity":50,"price":200
}]}
```

生成：

```
purchase_order+purchase_item
```

---

## 6.2 查询采购订单

GET

```
/api/purchase/order/list
```

---

## 6.3 完成采购入库 ⭐

POST

```
/api/purchase/order/{id}/complete
```

这是重要业务接口。

流程：

```
采购订单完成↓查询采购明细↓增加库存↓生成库存流水↓更新订单状态
```

事务：

```
@Transactional
```

保证：

库存增加成功，订单才完成。

---

# 7. 销售模块

## 7.1 创建销售订单

POST

```
/api/sales/order
```

请求：

```
{"customerId":1,"items":[{"productId":2,"quantity":5
}]}
```

---

## 7.2 完成销售出库 ⭐

POST

```
/api/sales/order/{id}/complete
```

流程：

```
检查库存↓扣减库存↓生成库存流水↓订单完成
```

如果库存不足：

返回：

```
{"code":400,"message":"库存不足"
}
```

---

# 8. Dashboard统计模块

## 8.1 首页数据

GET

```
/api/dashboard/stat
```

返回：

```
{"todaySales":10000,"orderCount":50,"inventoryWarning":3
}
```

---

## 8.2 销售趋势

GET

```
/api/dashboard/sales/trend
```

返回：

```
[{"date":"2026-08","value":30000
}]
```

用于ECharts。

---

# 9. 文件管理模块（增强）

## 上传文件

POST

```
/api/file/upload
```

返回：

```
{"url":"xxx.jpg"
}
```

用途：

- 商品图片
- 附件

---

# 10. 操作日志模块

## 查询日志

GET

```
/api/log/list
```

返回：

```
{"user":"admin","operation":"删除商品","time":"2026-08-01"
}
```

---

# 11. AI扩展接口（预留）

## 11.1 生成经营分析

POST

```
/api/ai/report
```

请求：

```
{"type":"SALES_REPORT","range":"MONTH"
}
```

后端流程：

```
查询销售数据↓数据统计↓构造Prompt↓调用LLM↓保存报告↓返回结果
```

---

返回：

```
{"title":"8月经营分析","content":"本月销售下降..."
}
```

---

# 12. 核心事务流程

## 采购入库

```
用户点击完成采购        |PurchaseController        |PurchaseService        |开启事务        |修改订单状态        |增加库存        |新增库存流水        |提交事务
```

---

## 销售出库

```
用户确认订单        |检查库存        |扣减库存        |生成流水        |订单完成
```

---

# 13. 接口开发优先级

## 第一阶段

必须：

- 登录
- 权限
- 商品CRUD
- 库存查询

## 第二阶段

核心业务：

- 采购订单
- 入库流程
- 销售订单
- 出库流程

## 第三阶段

增强：

- Dashboard
- Excel
- 文件上传
- 日志

## 第四阶段

AI：

- 智能分析
- AI报告
- 自然语言查询
