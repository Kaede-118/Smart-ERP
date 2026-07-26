<template>
  <div class="dashboard">
    <div class="greeting-card">
      <h2>👋 上午好，{{ auth.user?.nickname }}</h2>
      <p v-if="warnings.length > 0" class="warning-text">⚠️ 共有 {{ warnings.length }} 条库存预警</p>
      <p v-else class="ok-text">✅ 库存状况良好</p>
    </div>

    <div class="stat-grid">
      <el-card shadow="never" class="stat-card">
        <div class="stat-value">{{ summary.productCount }}</div>
        <div class="stat-label">商品总数</div>
      </el-card>
      <el-card shadow="never" class="stat-card">
        <div class="stat-value">{{ summary.inventoryQuantity }}</div>
        <div class="stat-label">库存总量</div>
      </el-card>
      <el-card shadow="never" class="stat-card">
        <div class="stat-value">¥{{ summary.todayPurchaseAmount }}</div>
        <div class="stat-label">今日采购</div>
      </el-card>
      <el-card shadow="never" class="stat-card">
        <div class="stat-value">¥{{ summary.todaySaleAmount }}</div>
        <div class="stat-label">今日销售</div>
      </el-card>
    </div>

    <div class="chart-grid">
      <el-card shadow="never" class="chart-card">
        <template #header><span>销售趋势（近7天）</span></template>
        <v-chart :option="trendOption" style="height:280px" autoresize />
      </el-card>
      <el-card shadow="never" class="chart-card">
        <template #header>
          <span>🤖 AI 企业助手</span>
          <el-button size="small" type="primary" style="float:right" :loading="aiLoading" @click="generateAI">
            生成经营分析
          </el-button>
        </template>
        <div v-if="aiReport" class="ai-preview">
          <h4>{{ aiReport.title }}</h4>
          <p class="ai-summary">{{ aiReport.summary }}</p>
          <el-button size="small" text type="primary" @click="$router.push('/ai')">查看完整报告</el-button>
        </div>
        <div v-else class="ai-empty">
          <p>点击生成获取今日经营建议</p>
        </div>
      </el-card>
    </div>

    <div class="chart-grid">
      <el-card shadow="never" class="chart-card">
        <template #header><span>库存预警</span></template>
        <el-table :data="warnings" size="small" v-if="warnings.length > 0">
          <el-table-column prop="productName" label="商品" />
          <el-table-column prop="quantity" label="当前库存" width="100" />
          <el-table-column prop="warningValue" label="预警值" width="100" />
        </el-table>
        <el-empty v-else description="暂无预警" :image-size="80" />
      </el-card>
      <el-card shadow="never" class="chart-card">
        <template #header><span>热销商品 TOP10</span></template>
        <v-chart :option="topOption" style="height:280px" autoresize />
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useAuthStore } from '../../stores/auth'
import { dashboardApi, aiApi } from '../../api'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, BarChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent } from 'echarts/components'

use([CanvasRenderer, LineChart, BarChart, GridComponent, TooltipComponent, LegendComponent])

const auth = useAuthStore()
const summary = ref<any>({})
const warnings = ref<any[]>([])
const topProducts = ref<any[]>([])
const trend = ref<any[]>([])
const aiReport = ref<any>(null)
const aiLoading = ref(false)

const trendOption = computed(() => ({
  tooltip: { trigger: 'axis' },
  grid: { left: 40, right: 20, bottom: 30, top: 10 },
  xAxis: { type: 'category', data: trend.value.map(t => t.date?.slice(5) || '') },
  yAxis: { type: 'value' },
  series: [
    { name: '销售', type: 'line', data: trend.value.map(t => t.saleAmount), smooth: true, color: '#1677ff' },
    { name: '采购', type: 'line', data: trend.value.map(t => t.purchaseAmount), smooth: true, color: '#52c41a' }
  ],
  legend: { bottom: 0 }
}))

const topOption = computed(() => ({
  tooltip: { trigger: 'axis' },
  grid: { left: 40, right: 20, bottom: 50, top: 10 },
  xAxis: { type: 'category', data: topProducts.value.map(p => p.productName), axisLabel: { rotate: 20, fontSize: 11 } },
  yAxis: { type: 'value' },
  series: [{ type: 'bar', data: topProducts.value.map(p => p.saleQuantity), color: '#1677ff', barWidth: 24 }]
}))

async function generateAI() {
  aiLoading.value = true
  try {
    const res = await aiApi.generate({ type: 'OVERVIEW', range: 'MONTH' })
    aiReport.value = res.data
  } finally { aiLoading.value = false }
}

onMounted(async () => {
  try {
    const [s, t, w, tp] = await Promise.all([
      dashboardApi.summary(), dashboardApi.trend(),
      dashboardApi.warnings(), dashboardApi.topProducts()
    ])
    summary.value = s.data
    trend.value = t.data
    warnings.value = w.data
    topProducts.value = tp.data
  } catch {}
})
</script>

<style scoped>
.dashboard { max-width: 1200px; margin: 0 auto; }
.greeting-card { background: #fff; border-radius: 12px; padding: 24px; margin-bottom: 20px; }
.greeting-card h2 { margin: 0 0 8px; font-size: 20px; }
.warning-text { color: #faad14; margin: 0; }
.ok-text { color: #52c41a; margin: 0; }
.stat-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; margin-bottom: 20px; }
.stat-card { text-align: center; border-radius: 12px; }
.stat-value { font-size: 28px; font-weight: 700; color: #1677ff; }
.stat-label { font-size: 13px; color: #666; margin-top: 4px; }
.chart-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; margin-bottom: 20px; }
.chart-card { border-radius: 12px; }
.ai-preview h4 { margin: 0 0 8px; font-size: 15px; }
.ai-summary { color: #666; font-size: 13px; line-height: 1.6; margin: 0 0 8px; }
.ai-empty { text-align: center; padding: 40px 0; color: #999; }
</style>
