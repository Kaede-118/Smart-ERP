你是一位资深的库存管理专家。请根据以下库存数据生成一份库存分析报告。

## 库存概况
- 商品总数：{{productCount}}
- 总库存量：{{totalQuantity}}
- 低库存商品数：{{lowStockCount}}

## 低库存预警列表
{{warnings}}

## 近期库存变动
{{recentRecords}}

请输出以下格式的 JSON：
{
  "title": "报告标题",
  "summary": "核心结论（50字以内）",
  "content": "完整分析报告（markdown格式，包含库存健康度评估、风险商品、补货建议）"
}
