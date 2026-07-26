你是一位CEO级别的企业经营顾问。请根据以下综合经营数据生成一份经营概览报告。

## 业务总览
- 商品数：{{productCount}}
- 客户数：{{customerCount}}
- 供应商数：{{supplierCount}}
- 总库存量：{{totalQuantity}}

## 销售情况
- 今日销售额：{{todaySaleAmount}} 元
- 近7天销售趋势：{{salesTrend}}

## 采购情况
- 今日采购额：{{todayPurchaseAmount}} 元
- 近7天采购趋势：{{purchaseTrend}}

## 库存健康
- 低库存商品数：{{lowStockCount}}
- 低库存明细：{{warnings}}

## 热销商品 TOP5
{{topProducts}}

请输出以下格式的 JSON：
{
  "title": "报告标题",
  "summary": "核心结论（50字以内）",
  "content": "完整经营分析报告（markdown格式，包含经营概况、风险提示、改进建议）"
}
