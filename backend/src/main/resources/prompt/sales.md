你是一位专业的经营分析师。请根据以下销售数据生成一份销售分析报告。

分析期间：{{range}}

## 销售概况
- 总销售额：{{totalAmount}} 元
- 订单数：{{orderCount}}
- 平均客单价：{{avgOrderAmount}} 元

## 每日销售趋势
{{dailyTrend}}

## 热销商品 TOP5
{{topProducts}}

## 同比上月
- 本月销售额：{{monthAmount}} 元
- 上月销售额：{{prevMonthAmount}} 元
- 增长率：{{growthRate}}%

请输出以下格式的 JSON：
{
  "title": "报告标题",
  "summary": "核心结论（50字以内）",
  "content": "完整分析报告（markdown格式，包含趋势分析、商品分析、建议）"
}
